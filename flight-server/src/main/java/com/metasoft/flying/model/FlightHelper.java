package com.metasoft.flying.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.metasoft.flying.controller.DressController;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.ItemData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.util.RandomUtils;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.ChessDispelVO;
import com.metasoft.flying.vo.ChessFinishVO;
import com.metasoft.flying.vo.ChessFogVO;
import com.metasoft.flying.vo.ChessGoVO;
import com.metasoft.flying.vo.ChessItemVO;
import com.metasoft.flying.vo.ChessReady2VO;
import com.metasoft.flying.vo.ChessRefuelVO;
import com.metasoft.flying.vo.ChessScoreVO;
import com.metasoft.flying.vo.ChessThornsVO;
import com.metasoft.flying.vo.ItemVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class FlightHelper {
	//private static final Logger logger = LoggerFactory.getLogger(FlightHelper.class);

	public static void randomCoord(Flight flight) {
		// 初始化
		int randomSize = 0;		
		for (int i = 0; i < flight.randomTmp.length; i++) {
			boolean avail = true;
			//排除有棋子的坐标
			for(ChessPlayer cp:flight.chessPlayers){
				if(cp!=null && cp.getUserId()>0){
					for(Chess chess :cp.chesses){
						if(chess.getCurCoord()==i)
							avail=false;
					}
				}
			}
			if(avail)
				//记录有效坐标
				flight.randomTmp[randomSize++] = i;
		}
		// 随机分布
		int counterNum = randomSize-1;
		while (counterNum-- > 0) {
			int index = (int) Math.floor(counterNum * Math.random());
			flight.random[counterNum] = flight.randomTmp[index];
			flight.randomTmp[index] = flight.randomTmp[counterNum];
		}
		int i = 0;
		for (; i < flight.items.length; i++) {
			flight.items[i].setCoord(flight.random[i]);//
			// FIXME some tricks!
			// if(chessRoom.items[i].getItemId() ==
			// ChessConstant.INCIDENT_INVUL){
			// chessRoom.items[i].setCoord(47);
			// }
		}
	}

	/**
	 * 
	 */
	public static void initItem(Flight chessRoom) {
		StaticDataService service = SpringService.getBean(StaticDataService.class);
		ChessItemVO[] items = new ChessItemVO[service.getChessList().size()];

		for (int i = 0; i < items.length; i++) {
			ChessItemVO item = new ChessItemVO();
			// item.setCoord(chessRoom.random[i]);
			item.setItemId(service.getChessList().get(i).getId());
			items[i] = item;
		}
		chessRoom.items = items;
	}
	
	public static int useDispel(Flight flight) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return -1;
		}

		int num = cp.reduce(ChessConstant.ITEM_DISPEL);
			
		for (ChessItemVO item : flight.items) {
				// 龙卷风
			if (item.getItemId() == ChessConstant.INCIDENT_TORNADO) {
				item.setCoord(-1);
				flight.broadcast(GeneralResponse.newObject(new ChessDispelVO()));
			}
		}//消除
		return num;
	}
	public static int useTakeoff(Flight flight, long userid, int chessPos) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return -1;
		}
		
		int num = cp.reduce(ChessConstant.ITEM_TAKEOFF);
			
		Chess chess = flight.getChessByChessPos(userid, chessPos);
		if (chess != null) {
			if (chess.getState() == 0) {
					chess.restart();

					// 广播出发
					ChessReady2VO vo = VoFactory.getChessReady2VO(chess);
					flight.broadcast(GeneralResponse.newObject(vo));
			}
		}
		return num;
	}
	
	public static int useThorns(Flight flight, long userid) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return -1;
		}
		
		int num = cp.reduce(ChessConstant.ITEM_THORNS);
		
		int currentPos = flight.getCurrentPos();
		cp.setThorns(currentPos);

		ChessThornsVO vo = new ChessThornsVO();
		vo.setBuff(FlightHelper.getThornBuff(flight.getChessPlayers(), currentPos));
		flight.broadcast(GeneralResponse.newObject(vo));			
		return num;
	}
	
	public static int useFog(Flight flight, long userid) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return -1;
		}
		
		int num = cp.reduce(ChessConstant.ITEM_FOG);
		
		int currentPos = flight.getCurrentPos();
		cp.setFog(currentPos);

		ChessFogVO vo = new ChessFogVO();
		vo.setBuff(FlightHelper.getFogBuff(flight.getChessPlayers(), currentPos));
		flight.broadcast(GeneralResponse.newObject(vo));			
		return num;
	}
	
	
	public static int useRefuel(Flight flight) {
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return -1;
		}
		
		int num = cp.reduce(ChessConstant.ITEM_REFUEL);	
		cp.addRefuelBy1();
		flight.broadcast(GeneralResponse.newObject(new ChessRefuelVO()));		
		return num;
	}
	/**
	 * @param step 
	 * @param fightVoList 
	 * @param self自己
	 * @param coodinate
	 *            战场坐标
	 */
	public static boolean rebound(Flight chessRoom, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList) {
		boolean rebound = false;
		Chess chess = chessList.getFirst();
		int chessBits = FlightHelper.getChessBits(chessList);
		int pos = chessRoom.getPos();
		//List<Chess> enemyList = new ArrayList<Chess>(chessRoom.chessPlayers.length);
		int enemyBits = 0;
		int enemySize = 0;
		for (int i = 0; i < chessRoom.chessPlayers.length; ++i) {
			if (null != chessRoom.chessPlayers[i]) {
				if (pos == i) {
					continue;// self
				}
				Chess[] enemyChesses = chessRoom.chessPlayers[i].getChesses();
				for (int j = 0; j < enemyChesses.length; ++j) {
					Chess enemyChess = enemyChesses[j];
					// 敌机不在外面飞行
					if (!enemyChess.isFlyingOutside()) {
						continue;
					}
					// 坐标相同
					if (enemyChess.getCurCoord() == chess.getCurCoord()) {
						enemyBits |= 1<<(enemyChess.getChessPos() + enemyChess.getPos()*ChessConstant.CHESS_SIZE);
						enemySize++;
					}
				}
				if (enemySize > 0) {
					// 如果自己的飞机数大于或等于敌机数
					// 如果自己的飞机数小于敌机数
					if (chessList.size() < enemySize) {
						ChessGoVO vo = new ChessGoVO( chess.getJourney(), -1, 0, 0, enemyBits|chessBits, ChessConstant.ANIM_REBOUND);
						fightVoList.add(vo);
						rebound = true;
					}
				}
			}
		}
		return rebound;
	}

	/**
	 * 穿越战斗
	 * 
	 * @param flight
	 * @param fightVoList 
	 * @param self自己
	 * @param enemy对角的敌人
	 * @return 
	 */
	public static void fightAcross(Flight flight, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList) {
		ChessGoVO vo = new ChessGoVO();
		
		vo.setJourney(-2);		
		// 吃棋
		vo.setCrash(FlightHelper.acrossCrash(flight, chessList));

		fightVoList.add(vo);
	}

	/**
	 * @param fightVoList 
	 * @param self自己
	 * @param coodinate
	 *            战场坐标
	 * @return 
	 */
	public static void fight(Flight flight, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList, int step) {
		Chess chess = chessList.getFirst();
		ChessGoVO vo = new ChessGoVO();
		int chessBits = FlightHelper.getChessBits(chessList);
		vo.setChess(chessBits);
		vo.setJourney(chess.getJourney());
		// 带上
		vo.setChess(FlightHelper.takeAlong(flight, chessList));
		vo.setStep(step);
		fightVoList.add(vo);
		// 未进入跑道
		if (chess.getJourney() < ChessConstant.JOURNEY_OUT) {
			// 吃棋
			FlightHelper.crash(flight, chessList, vo);
		}

		FlightHelper.fortune(flight, chessList, fightVoList, vo);
	}

	/**
	 * @param chessRoom
	 * @param chessList
	 * @param goList
	 * @param goVO此格的goVO
	 */
	public static void fortune(Flight chessRoom, Deque<Chess> chessList, Deque<ChessGoVO> goList, ChessGoVO goVO) {
		Chess chess = chessList.getFirst();
		// 龙卷风
		if(chess.isFlyingOutside()
				&& (chess.getState() & ChessConstant.CHESS_REBOUND) == 0){	
			if(FlightHelper.fortuneTornado(chessRoom, chessList, goList, goVO)){
				return;
			}else if(FlightHelper.fortuneInvulnr(chessRoom, chessList, goList, goVO)){
				return;
			}
			// 如果没触发龙卷风..触发事件
			else {
				for (ChessItemVO item : chessRoom.items) {
					// 随机事件, 坐标匹配
					if (item.getCoord() < 0) {
						continue;
					} else if (item.getCoord() == chess.getCurCoord()) {//prevent from chess been reset to origin coord
						FlightHelper.getChessFortune(chessRoom, item, chessList, goList, goVO);
						return;
					}
				}
			}
		}
	}
	
	public static boolean passby(Flight chessRoom, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList, int step) {
		// 龙卷风
		return FlightHelper.fortuneTornado(chessRoom, chessList, fightVoList, null);	
	}

	public static void crash(Flight flight, Deque<Chess> chessList, ChessGoVO vo) {		
		Chess chess = chessList.getFirst();
		int chessBits = FlightHelper.getChessBits(chessList);
		int crashBits = 0;
		int pos = flight.getPos();
		for (int i = 0; i < flight.chessPlayers.length; ++i) {
			if (null != flight.chessPlayers[i] && flight.chessPlayers[i].getUserId() > 0l) {
				
				//判断自己,路过带走自己棋子
				if (pos == i) {
					continue;
				}
				
				//荆棘装甲
				if (flight.chessPlayers[i].isThorns(flight.getCurrentPos())){
					//找到当前坐标上的敌机
					boolean hasEnemy = false;
					int enemyChessBits = 0;
					for (Chess enemyChess : flight.chessPlayers[i].getChesses()) {
						// 敌机在飞行
						// 敌机在跑道外
						// 坐标相同
						if (enemyChess.getCurCoord() == chess.getCurCoord()
								&& enemyChess.isFlyingOutside() 
								&& chess.isFlyingOutside() ) {
							hasEnemy = true;
							enemyChessBits |= getChessBit(enemyChess);
						}
					}
					if (hasEnemy){
						for (Chess crash : chessList) {
							crash.reset();
							crashBits |= getChessBit(crash);
						}
						vo.setChess(enemyChessBits);
						vo.setCrash(crashBits);
						vo.setType(ChessConstant.ITEM_THORNS);
						return;//荆棘装甲计算返回
					}
				}
				
				//找到当前坐标上的敌机
				int enemyBits = 0;
				int enemySize = 0;
				for (Chess enemyChess : flight.chessPlayers[i].getChesses()) {
					// 敌机在飞行
					// 敌机在跑道外
					// 坐标相同
					//System.out.println("enemyChess:"+enemyChess.getCurCoord()+"-chess:"+chess.getCurCoord());
					if (enemyChess.getCurCoord() == chess.getCurCoord()
							&& enemyChess.isFlyingOutside()
							&& chess.isFlyingOutside() ) {
						enemySize++;
						enemyBits |= getChessBit(enemyChess);
					}
				}
				
				// 如果自己的飞机数大于或等于敌机数
				// 如果自己的飞机数小于敌机数
				if (enemySize > 0) {
					
					if (chessList.size() < enemySize) {
						for (Chess crash : chessList) {
							crash.reset();
							crashBits |= getChessBit(crash);
						}
					}

					for (Chess enemyChess : flight.chessPlayers[i].getChesses()) {
						if((enemyBits&getChessBit(enemyChess))>0){
							enemyChess.reset();
						}
					}
					
					crashBits |= enemyBits;
				}
			}
		}
		vo.setCrash(crashBits);
		vo.setChess((crashBits ^ chessBits)&chessBits);//TODO exclude my plane;	
	}
	
	public static int takeAlong(Flight flight, Deque<Chess> chessList) {
		int chessBits = FlightHelper.getChessBits(chessList);
		Chess chess = chessList.getFirst();
		int pos = flight.getPos();
		for (int i = 0; i < flight.chessPlayers.length; ++i) {
			if (null != flight.chessPlayers[i] && flight.chessPlayers[i].getUserId() != 0l) {
				
				//判断自己,路过带走自己棋子
				if (pos == i) {
					Chess[] myChesses = flight.chessPlayers[i].getChesses();
					
					for (int j = 0; j < myChesses.length; ++j) {
						Chess myChess = myChesses[j];
						int token = getChessBit(myChess);
						// 不在飞行
						if ((myChess.getState() & ChessConstant.CHESS_FLIGHT)==0) {
							continue;
						}
						// 一个在跑道中,一个在外面坐标相同..
						if (myChess.getJourney() != chess.getJourney()) {
							continue;
						}
						// 坐标相同
						if (myChess.getCurCoord() == chess.getCurCoord() 
								&& (chessBits & token) == 0) {//exclude the chess already in queue
							chessBits |= token;
							chessList.add(myChess);
						}
					}
					continue;
				}
			}
		}
		return chessBits;
	}

	public static int acrossCrash(Flight flight, Deque<Chess> chessList) {
		// 对角的玩家
		int enemyPos = (flight.getCurrentPos() + 2) % 4;
		ChessPlayer enemy = flight.chessPlayers[enemyPos];
		if (null == enemy) {
			return 0;
		} else if (enemy.getUserId() == 0) {
			return 0;
		}
		Chess[] enemyChesses = flight.chessPlayers[enemyPos].getChesses();
		Deque<Chess> enemyList = new ArrayDeque<Chess>();
		for (int i = 0; i < enemyChesses.length; ++i) {
			Chess enemyChess = enemyChesses[i];
			if (enemyChess.getJourney() == 52) {
				enemyList.add(enemyChess);
			}
		}
		if (enemyList.size() > 0) {
			int bit = 0;
			// 如果自己的飞机数大于或等于敌机数
			// 如果自己的飞机数小于敌机数
			if (chessList.size() < enemyList.size()) {
				for (Chess crash : chessList) {
					crash.reset();
					bit |= 1<<(crash.getChessPos() + crash.getPos() * ChessConstant.CHESS_SIZE);
				}
			}

			for (Chess enemyChess : enemyList) {
				enemyChess.reset();
				bit |= 1<<(enemyChess.getChessPos() + enemyChess.getPos() * ChessConstant.CHESS_SIZE);
			}
			return bit;
		}
		return 0;
	}

	public static boolean fortuneInvulnr(Flight chessRoom, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList, ChessGoVO start) {
		boolean engage = false;
		int chessBits = FlightHelper.getChessBits(chessList);
		Chess chess = chessList.getFirst();
		for (ChessItemVO item : chessRoom.items) {
			//无敌
			if (item.getItemId() == ChessConstant.INCIDENT_INVUL) {
				if (item.getCoord() == chess.getCurCoord()
						&& item.getCoord() >= 0
						&& chess.isFlyingOutside()) {
					
					ChessPlayer cplayer = chessRoom.getCurrentPlayer();
					if(null == cplayer){
						return false;
					}
					
					chess.addState(ChessConstant.CHESS_NO_JUMP);
					engage = true;
					if(start == null){
						start = new ChessGoVO(chess.getJourney(), 1, 0, 0, chessBits, ChessConstant.INCIDENT_INVUL);
						fightVoList.add(start);
					}else{
						start.setType(ChessConstant.INCIDENT_INVUL);
					}
					
					//step forth 10
					for (int m = 0; m < 10; ++m){ 
						Chess.go(chessList, 1);
						//每个玩家
						for (ChessPlayer cp : chessRoom.getChessPlayers()) {
							//玩家ok
							if (null != cp && cp.getUserId() > 0l) {
								//make sure not self
								if ( cplayer.getPos() != cp.getPos()) {
									int bit = 0;
									Chess[] chesses = cp.getChesses();
									for (int j = 0; j < chesses.length; ++j) {
			
										//自己和敌机都要在跑道外
										if (chesses[j].getCurCoord() == chess.getCurCoord() 
												&& chess.isFlyingOutside()
												&& chesses[j].isFlyingOutside()){
											chesses[j].restart();
											bit |= 1<<(chesses[j].getChessPos() + chesses[j].getPos()*ChessConstant.CHESS_SIZE);
										}
									}
									if(bit>0){
										ChessGoVO attack = new ChessGoVO(chess.getJourney(), 1, 0, bit, chessBits, ChessConstant.INCIDENT_INVUL);
										fightVoList.add(attack);
									}
								}
							}
						}
					}//step forth 10
					ChessGoVO attack = new ChessGoVO(chess.getJourney(), 1, 0, 0, chessBits, ChessConstant.INCIDENT_INVUL);
					fightVoList.add(attack);
				}
			}
		}
		return engage;
	}

	public static boolean fortuneTornado(Flight flight, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList, ChessGoVO start) {
		Chess chess = chessList.getFirst();
		for (ChessItemVO item : flight.items) {
			// 龙卷风
			if (item.getItemId() == ChessConstant.INCIDENT_TORNADO) {
				// step by step
				if (item.getCoord() == chess.getCurCoord()
						&& item.getCoord() >= 0
						&& chess.isFlyingOutside()) {
					
					chess.addState(ChessConstant.CHESS_NO_JUMP);				
					int chessBits = FlightHelper.getChessBits(chessList);
					if(start == null){
						start = new ChessGoVO(chess.getJourney(), 1, 0, 0, chessBits, ChessConstant.INCIDENT_TORNADO);
						fightVoList.add(start);
					}else{
						start.setType(ChessConstant.INCIDENT_TORNADO);
					}
					
					//in case of fall on the same location
					int random = RandomUtils.nextInt(ChessConstant.JOURNEY_JUMP);		
					if(ChessConstant.TORNADO_FALL < 0){
						random = (random + chess.getJourney() + 1)%ChessConstant.JOURNEY_OUT;//max 49
					}
					//FIXME TORNADO trick
					else{
						random = ChessConstant.TORNADO_FALL;
					}
					Chess.setJourney(chessList, random);
					//吃棋
					ChessGoVO fallVo = new ChessGoVO();
					fallVo.setJourney(chess.getJourney());//吹落
					fallVo.setType(ChessConstant.INCIDENT_TORNADO);
					FlightHelper.crash(flight, chessList, fallVo);
					
					fightVoList.add(fallVo);
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean detectEnemy(Chess chess,int range, Flight flight) {
		ChessPlayer[] chessPlayers = flight.getChessPlayers();
		int pos = flight.getPos();
		// step by step
		for(int i=-range; i<0; i++){
			int detect = Chess.getCoord(chess.getJourney()+i, chess.getPos());
			for (int j = 0; j < chessPlayers.length; ++j) {
				ChessPlayer cplayer = chessPlayers[j];
				if(cplayer!=null && cplayer.getUserId()>0l && cplayer.getPos()!=pos && cplayer.getNpc()==0){
					for(Chess enemy:cplayer.getChesses()){
						if(enemy.getCurCoord()==detect && enemy.isFlyingOutside()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean detectChess(Chess chess, Flight flight) {
		ChessPlayer[] chessPlayers = flight.getChessPlayers();
		int detect = Chess.getCoord(chess.getJourney()+flight.getDice(), chess.getPos());
		for (int j = 0; j < chessPlayers.length; ++j) {
			ChessPlayer cplayer = chessPlayers[j];
			if(cplayer!=null && cplayer.getUserId()>0l){
				for(Chess c:cplayer.getChesses()){
					if(chess.getPos()==c.getPos() && chess.getChessPos()==c.getChessPos()){//同一棋了
						continue;
					}
					if(c.isFlying()){//检测在飞行的棋子
						//System.out.printf("detectChess:%d,(%d,%d,%d),(%d,%d,%d)\n",detect,c.getCurCoord(),c.getPos(),c.getChessPos(),chess.getCurCoord(),chess.getPos(),chess.getChessPos());
						if(c.getCurCoord()==detect ){
							return true;
						}else if (detect % 4 == c.getPos()) {//检测跳格
							if(c.getCurCoord()==(detect+4)% ChessConstant.DISTANCE){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean detectTornadoTeleport(Chess chess,int range, int tornado, int teleport, Flight flight) {		
			// step by step
			for(int i=range; i>0; i--){
				if (tornado == Chess.getCoord(chess.getJourney()+i, chess.getPos()) && tornado >= 0) {
					return true;
				}
			}
			
			int detect = Chess.getCoord(chess.getJourney()+range, chess.getPos());
			if(detect==teleport){
				return true;
			}

			if (detect % 4 == chess.getPos()) {//检测跳格
				for(int i=4; i>0; i--){
					if (tornado == (detect+i)% ChessConstant.DISTANCE && tornado >= 0) {
						return true;
					}
				}
				if(teleport==(detect+4)% ChessConstant.DISTANCE){
					return true;
				}
			}
		return false;
	}

	public static boolean fogCheck(ChessPlayer[] chessPlayers, int pos) {
		int currentPos = pos%4;
		for (int j = 0; j < chessPlayers.length; ++j) {
			ChessPlayer cplayer = chessPlayers[j];
			if(cplayer!=null && cplayer.getUserId()>0l && cplayer.getPos()!=currentPos){
				if(cplayer.isFog(pos)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getFogBuff(ChessPlayer[] chessPlayers, int pos) {
		int buff = 0;
		for (ChessPlayer cp :  chessPlayers) {
			if (null != cp && cp.getUserId() > 0l ) {
				//有迷雾
				if(cp.isFog(pos)){
					//影响其他人的buff
					for (ChessPlayer cp_ :  chessPlayers) {
						if (null != cp_ && cp_.getUserId() > 0l && cp_.getPos() != cp.getPos()) {
							for(Chess chess : cp_.chesses){
								//有buff的棋子
								if(chess.isFlyingOutside()){
									buff |= 1<<(chess.getChessPos()+chess.getPos()*ChessConstant.CHESS_SIZE);
								}
							}
						}
					}
				}
			}
		}
		return buff;
	}
	
	public static int getThornBuff(ChessPlayer[] chessPlayers, int pos) {
		int buff = 0;
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && chessPlayers[i].getUserId() > 0l ) {
				ChessPlayer cp = chessPlayers[i];
				//有迷雾
				if(cp!=null && cp.getUserId()>0l ){
					if(cp.isThorns( pos)){
						for(int j=0; j<cp.chesses.length; j++){
							//有buff的棋子
							if(cp.chesses[j].isFlyingOutside()){
								buff |= 1<<(cp.chesses[j].getChessPos()+cp.chesses[j].getPos()*ChessConstant.CHESS_SIZE);
							}
						}
					}
				}
			}
		}
		return buff;
	}
	
	public static ChessFinishVO getChessFinishVO(ChessPlayer[] chessPlayers, long victoryId) {
		ChessFinishVO finishVO = new ChessFinishVO();
		List<ChessScoreVO> listVO = new ArrayList<ChessScoreVO>(4);
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && chessPlayers[i].getUserId() != 0l) {
				// 是否有玩家
				UserService userService = SpringService.getBean(UserService.class);

				int score = 1;
				int gold = 0;
				try {
					User user = userService.getAnyUserById(chessPlayers[i].getUserId());
					// 查询飞机
					DressController ic = SpringService.getBean(DressController.class);
					score += ic.checkPlaneIsPutOn(user);

					// 胜负
					if (chessPlayers[i].getUserId() == victoryId) {
						score *= ChessConstant.SCORE_WIN;
						gold = ChessConstant.GOLD_WIN;

						finishVO.setPos(chessPlayers[i].getPos());
						finishVO.setUserId(chessPlayers[i].getUserId());

						LocalizationService localService = SpringService.getBean(LocalizationService.class);
						String msg = localService.getLocalString("chess.victory", new String[] { String.valueOf(score) });
						BaseConnection conn = user.getConn();
						if (conn != null) {
							conn.deliver(GeneralResponse.newObject(new ChatVO(msg)));
						}
					} else {
						score *= ChessConstant.SCORE_LOSE;

						LocalizationService localService = SpringService.getBean(LocalizationService.class);
						String msg = localService.getLocalString("chess.lose", new String[] { String.valueOf(score) });
						BaseConnection conn = user.getConn();
						if (conn != null) {
							conn.deliver(GeneralResponse.newObject(new ChatVO(msg)));
						}
					}
					//增加经验
					if(chessPlayers[i].getNpc()==0){
						user.addScore(score);
						user.addGold(gold);
					}
					
					// 财富通知
					BaseConnection conn = user.getConn();
					if (conn != null) {
						conn.deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
					}

					ChessScoreVO vo = new ChessScoreVO(chessPlayers[i].getUserId(), i, score);
					vo.setNickname(user.getUserPersist().getNickname());
					listVO.add(vo);
				} catch (GeneralException e) {
					e.printStackTrace();
				}
			}
		}
		finishVO.setScoreList(listVO);
		return finishVO;
	}
	
	/**给金币
	 * @param chessPlayers
	 * @param victoryId
	 * @return
	 */
	public static ChessFinishVO getChessFinishVO2(ChessPlayer[] chessPlayers, long victoryId, int gold) {
		ChessFinishVO finishVO = new ChessFinishVO();
		List<ChessScoreVO> listVO = new ArrayList<ChessScoreVO>(4);
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && chessPlayers[i].getUserId() != 0l) {
				// 是否有玩家
				UserService userService = SpringService.getBean(UserService.class);

				int score = 1;
				int goldGain = 0;
				try {
					User user = userService.getAnyUserById(chessPlayers[i].getUserId());
					// 查询飞机
					DressController ic = SpringService.getBean(DressController.class);
					score += ic.checkPlaneIsPutOn(user);

					// 胜负
					if (chessPlayers[i].getUserId() == victoryId) {
						goldGain = gold==0?200:gold;
						score *= ChessConstant.SCORE_WIN;

						finishVO.setPos(chessPlayers[i].getPos());
						finishVO.setUserId(chessPlayers[i].getUserId());

						LocalizationService localService = SpringService.getBean(LocalizationService.class);
						String msg = localService.getLocalString("chess.victory", new String[] { String.valueOf(score) });
						BaseConnection conn = user.getConn();
						if (conn != null) {
							conn.deliver(GeneralResponse.newObject(new ChatVO(msg)));
						}
					} else {
						score *= ChessConstant.SCORE_LOSE;

						LocalizationService localService = SpringService.getBean(LocalizationService.class);
						String msg = localService.getLocalString("chess.lose", new String[] { String.valueOf(score) });
						BaseConnection conn = user.getConn();
						if (conn != null) {
							conn.deliver(GeneralResponse.newObject(new ChatVO(msg)));
						}
					}
					//增加经验
					if(chessPlayers[i].getNpc()==0){
						user.addScore(score);
						user.addGold(goldGain);
					}
					
					// 财富通知
					BaseConnection conn = user.getConn();
					if (conn != null) {
						conn.deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
					}

					ChessScoreVO vo = new ChessScoreVO(chessPlayers[i].getUserId(), i, score);
					vo.setNickname(user.getUserPersist().getNickname());
					vo.setGold(goldGain);
					listVO.add(vo);
				} catch (GeneralException e) {
					e.printStackTrace();
				}
			}
		}
		finishVO.setScoreList(listVO);
		return finishVO;
	}
	
	/**
	 * @param flight
	 * @param chessItem
	 * @param chessList
	 * @param goVo走到此格的vo
	 */
	public static void getChessFortune(Flight flight, ChessItemVO chessItem, Deque<Chess> chessList, Deque<ChessGoVO> fightVoList, ChessGoVO goVo) {
		//事件
		StaticDataService sds = SpringService.getBean(StaticDataService.class);
		ItemData fortuneItemData = sds.getItemDataMap().get(chessItem.getItemId());
		if(fortuneItemData != null){
			if((fortuneItemData.getEffect() & ItemConstant.ITEM_CHESS)>0){
				ChessPlayer cp = flight.getCurrentPlayer();
				if (null != cp) {
					cp.add(fortuneItemData.getNum());
					//清除
					chessItem.setCoord(-1);
					goVo.setType(fortuneItemData.getId());
				}
			}
		}else{
			return ;
		}
		
		if (chessItem.getItemId() == ChessConstant.INCIDENT_TELEPORT) {
			int random = RandomUtils.nextInt(100);
			for (Chess chess : chessList) {							
				if(random>50){
					chess.complete();
				}else{
					chess.restart();
				}
				goVo.setType(ChessConstant.INCIDENT_TELEPORT);
			}
			int chessBits = FlightHelper.getChessBits(chessList);
			goVo.setChess(chessBits);
			ChessGoVO endVo = new ChessGoVO(0, 1, 0, 0, chessBits, ChessConstant.INCIDENT_TELEPORT);
			fightVoList.add(endVo);
			if(random>50){
				endVo.setJourney(55);
			}else{
				endVo.setJourney(-1);
			}
			
			
		} else if (chessItem.getItemId() == ChessConstant.INCIDENT_PRIZE) {
			UserService us = SpringService.getBean(UserService.class);
			try {
				ChessPlayer cplayer = flight.getCurrentPlayer();
				User user = us.getAnyUserById(cplayer.getUserId());
				ItemData randomItemData = sds.getRandomPrize();
				if(null != randomItemData){
					UserItem uItem = user.addItem(randomItemData.getId(), 1, "奖");
					ItemVO ivo = VoFactory.getItemVO(uItem);
					if (user.getConn() != null) {
						user.getConn().deliver(GeneralResponse.newObject(ivo));
					}
					goVo.setType(uItem.getItemId());
				}
			} catch (GeneralException e) {
				//log.debug("getChessFortuneVO user is null");
			}
		}
	}
	
	/**返回16位有效字节表示棋子
	 * @param chessList
	 * @return
	 */
	public static int getChessBits(Deque<Chess> chessList) {
		int bit = 0;
		for(Chess chess : chessList){
			bit |= 1<<(chess.getChessPos()+chess.getPos()*ChessConstant.CHESS_SIZE);
		}
		return bit;
	}
	public static int getChessBit(Chess chess) {
		return 1<<(chess.getChessPos()+chess.getPos()*ChessConstant.CHESS_SIZE);
	}
}
