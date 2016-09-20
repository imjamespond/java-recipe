package com.metasoft.flying.model;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.controller.DressController;
import com.metasoft.flying.model.action.ActionNode;
import com.metasoft.flying.model.action.CheatNode;
import com.metasoft.flying.model.action.TakeOffNode;
import com.metasoft.flying.model.action.DispelsNode;
import com.metasoft.flying.model.action.ItemNode;
import com.metasoft.flying.model.action.MoveNode;
import com.metasoft.flying.model.action.ItemTakeOffNode;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.constant.ErrorCodes;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.util.RandomUtils;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.ChessAutoVO;
import com.metasoft.flying.vo.ChessFinishVO;
import com.metasoft.flying.vo.ChessFlightVO;
import com.metasoft.flying.vo.ChessGoVO;
import com.metasoft.flying.vo.ChessInfoVO;
import com.metasoft.flying.vo.ChessItemVO;
import com.metasoft.flying.vo.ChessReadyVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class Flight extends IFlight{
	private static final Logger logger = LoggerFactory.getLogger(Flight.class);
	// private long id;//System.currentMillis;棋局时间戳,标记过期定时器用
	protected WeakReference<GameRoom> room;
	// 配置
	private int magicDiceNum = 0;// 魔力色子使用次数
	protected int type = GeneralConstant.GTYPE_NORMAL;//2为比赛
	// 状态
	private int round = 0;
	private int dice = 0;// 扔色子
	private int dice2;//动力装置
	private int dice3;//空中接力
	private int state = 0;// 状态
	private int currentPos = 0;// 当前位置0~infinity	
	private int sign = 0;//1为第一架飞机
	private int diceCount = 0;
	private Integer operation = new Integer(0);// 自动操作
	private long deadline = 0;// 自动操作时间

	//private Integer animateState = new Integer(0);// 动画完成状态0-3号玩家对应1,2,4,8状态,全部播完是15
	private static ActionNode chessAction;
	private static ActionNode itemAction;

	public ChessPlayer[] chessPlayers;
	public ChessItemVO[] items;// 棋局道具
	public int[] random;
	public int[] randomTmp;
	public int randomItem = 0;// 随机位置
	public int degree = 0;//npc AI 分级

	public Flight(GameRoom gameRoom) {
		token = System.currentTimeMillis();
		room = new WeakReference<GameRoom>(gameRoom);
		chessPlayers = new ChessPlayer[4];
		random = new int[ChessConstant.DISTANCE];
		randomTmp = new int[ChessConstant.DISTANCE];
		FlightHelper.initItem(this);
	}

	public int join(long userId) {
		// FIXME find the duplicate user id
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null == chessPlayers[i]) {
				chessPlayers[i] = new ChessPlayer(i);
			} else if (chessPlayers[i].getUserId() > 0) {
				//if (chessPlayers[i].getUserId() == userId) {
					//break;
				//}
				continue;
			}
			chessPlayers[i].reset();
			chessPlayers[i].setUserId(userId);
			chessPlayers[i].setPos(i);
			return i;
		}
		return -1;
	}

	public void join(long userId, int i) {
		if (null == chessPlayers[i]) {
			chessPlayers[i] = new ChessPlayer(i);
		}

		DressController dressController = SpringService.getBean(DressController.class);
		UserService userService = SpringService.getBean(UserService.class);

		User user;
		try {
			user = userService.getAnyUserById(userId);
			chessPlayers[i].setPlane(dressController.checkPlaneIsPutOn2(user));
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		chessPlayers[i].reset();	
		chessPlayers[i].setUserId(userId);
		chessPlayers[i].setPos(i);
	}

	public void begin() {
		if (getPlayerNum() == 0) {
			logger.error("begin getPlayerNum is 0");
			return;
		}
		// 随机事件位置
		FlightHelper.randomCoord(this);

		beginNotify();
		nextTurn(0);
	}
	
	public void beginNotify() {
		// 广播棋子初始位置
		ChessInfoVO vo = VoFactory.getChessInfoVO(this);
		broadcast(GeneralResponse.newObject(vo));	
	}

	/**
	 * 回合开始
	 */
	protected void nextTurn(int type) {

		// 刷新道具位置
		//logger.debug("nextTurn currentPos:{}, randomItem:{}",currentPos, randomItem);
		if ((currentPos - randomItem) > 19) {
			FlightHelper.randomCoord(this);
			broadcast(GeneralResponse.newObject(VoFactory.getChessItemListVO(this)));
			randomItem = currentPos;
		}
		
		// 改变房间状态
		state = ChessConstant.ROOM_BEGIN;

		// 广播开始
		broadcast(GeneralResponse.newObject(VoFactory.getChessCastVO(this, type)));

		// 注册一个定时器 扔色子
		setWaitDice();	
	}

	public void reset() {
		// 玩家归零
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i]) {
				chessPlayers[i].reset2();
			}
		}
		state = ChessConstant.ROOM_IDLE;
		operation = 0;
		round = 0;
		currentPos = 0;
		randomItem = 0;
		diceCount = 0;
		deadline = 0;
	}

	public void end() {	
		reset();
	}

	public boolean endCheck() {
		// 全部棋子完成的玩家
		int playerNum = 0;
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i]) {
				if (chessPlayers[i].getUserId() == 0l) {
					continue;
				}
				playerNum++;
				if (chessPlayers[i].endCheck()) {
					// 结算
					finishNotify(i);
					return true;
				}
			}
		}
		if (playerNum > 1) {
			return false;
		} else {
			interrupt();
			return true;
		}
	}

	public void interrupt() {
		// 广播结束
		ChessFinishVO vo = new ChessFinishVO();
		vo.setPos(-1);
		broadcast(GeneralResponse.newObject(vo));
		// 流局
		ChatVO chatVO = new ChatVO(SpringService.getBean(LocalizationService.class).getLocalString("chess.stop"));
		broadcast(GeneralResponse.newObject(chatVO));
	}

	/**
	 * 结算
	 */
	public void finishNotify(int pos) {
		settleGuess(pos);
		// 结束vo
		broadcast(GeneralResponse.newObject(FlightHelper.getChessFinishVO(chessPlayers, chessPlayers[pos].getUserId())));
	}

	private void settleGuess(int winnerPos) {
		// 竞猜
		GameRoom gr = room.get();
		if(null!=gr){
		ChessGuess chessGuess = gr.getGuess();
		if (chessGuess != null) {
			chessGuess.settle(winnerPos);
			chessGuess.clear();
		}
		}
	}
	
	public void diceCaculate(int num) {
		if (!isFlightBegin()) {
			return;
		}
		setState(ChessConstant.ROOM_DICE);
		diceCount++;

		// 计算色子点数
		if (num > 0) {
			dice = num;
		} else {
			dice = RandomUtils.nextInt(6) + 1;// pseudorandom
		}

		// 广播扔色子
		broadcast(GeneralResponse.newObject(VoFactory.getChessDiceVO(this)));

		int checkGo = checkGo();
		if (checkGo>1) {
			if (isAutoState()) {
				autoGo();
			} else {
				operation = ChessConstant.ROOM_GO;//wait for player going
				deadline = System.currentTimeMillis() + ChessConstant.GO_SEC;
			}
			
		} else if(checkGo==1){
			// 若有一个棋子可走
			autoGo();
		} else {
			// 若没有棋子可走,下个人走
			next();
		}
	}

	public void dice(int num, int itemPos) {
		if(isFlightDiced()){
			return;
		}
		
		ChessPlayer cp = getCurrentPlayer();
		if (null == cp) {
			return;
		}
		//重置道具
		dice2 = 0;
		dice3 = 1;
		if (cp.getItemPos() != ChessConstant.ITEM_REFUEL ) {
			cp.setItemPos(itemPos);
		}
		//npc 使用的道具
		if(cp.getNpc()>0){
			if(null!=itemAction&&itemAction.act(this)){
				return ;
			}
		}else if(itemPos>0){
			diceTrick(itemPos);
		}
		
		diceCaculate(num);
	}
	
	public void diceTrick(int itemPos){
		ChessPlayer cp = getCurrentPlayer();
		if (null == cp) {
			return;
		}

		if (itemPos == ChessConstant.ITEM_ENHANCE) {
			cp.reduce(itemPos);
			dice2 = RandomUtils.nextInt(6) + 1;
		}else if(itemPos == ChessConstant.ITEM_RELAY){
			cp.reduce(itemPos);
			dice3 = 3;
		}		
	}

	public void autoDice() {
		if(isFlightDiced()){
			return;
		}
		ChessPlayer cp = getCurrentPlayer();
		if (null == cp) {
			return;
		}	
		//重置道具
		dice2 = 0;
		dice3 = 1;
		cp.setItemPos(-1);
		//npc 使用的道具
		if(cp.getNpc()>0){
			if(null!=itemAction&&itemAction.act(this)){
				return;
			}
		}

		// 托管
		setAutoState(cp.getUserId());

		diceCaculate(0);
	}

	public void ready(long userId, int chessPos) {
		if (!isFlightBegin()) {
			return;
		}
		// 注册一个定时器 播动画
		setWaitAnim();

		Chess chess = getChessByChessPos(userId, chessPos);
		if (chess != null) {
			if (chess.getState() == 0) {
				if (dice >= 5) {
					chess.restart();

					// 广播自动走棋计算
					ChessReadyVO vo = VoFactory.getChessReadyVO(chess);
					broadcast(GeneralResponse.newObject(vo));
				}else{
					logger.error("ready dice<5 currentPos:{},uid:{},chess:{}",currentPos, userId,chessPos);
				}
			}else{
				logger.error("ready state>0 currentPos:{},uid:{},chess:{}",currentPos, userId,chessPos);
			}
		}else{
			logger.error("ready null currentPos:{},uid:{},chess:{}",currentPos, userId,chessPos);
		}
	}

	
	/**
	 * 通过玩家找棋
	 * 
	 * @param userId
	 * @param bits
	 * @throws GeneralException
	 */
	public void go(long userId, int bits) throws GeneralException {
		if (!isFlightBegin()) {
			return;
		}

		// 取消托管
		// quitAutoState(userId);

		// 注册一个定时器 播动画
		setWaitAnim();

		// 走棋
		ArrayDeque<Chess> chessGoList = getChessByChessBits(userId, bits);
		if (0 == chessGoList.size()) {
			throw new GeneralException(ErrorCodes.CHESS_NOT_FOUND, "chess.not.found");
		}

		// 走棋计算
		ChessFlightVO vo = new ChessFlightVO();
		//ArrayDeque<Chess> chessList = chessGoList.clone();
		vo.setGoList(goCalculate( chessGoList));
		vo.setPos(getPos());

		// 广播走棋计算
		broadcast(GeneralResponse.newObject(vo));
		
		sign|=1;//标记
	}

	/**
	 * 自动走棋
	 */
	private void autoGo() {
		if (!isFlightBegin()) {
			return;
		}
		// 自动走棋
		ChessPlayer cp = getCurrentPlayer();
		if (null == cp) {
			return;
		}

		if(null!=chessAction&&!chessAction.act(this)){
			//autoGo2();
		}

		setWaitAnim();
	}

	/**
	 * 检测是否自动
	 */
	private boolean isAutoState() {
		int pos = getPos();
		return chessPlayers[pos].isAutoState();
	}

	/**
	 * 检测走棋
	 */
	private int checkGo() {
		ChessPlayer cp = getCurrentPlayer();
		if (null == cp ) {
			return 0;
		}
		
		//是否有人用了迷雾
		boolean isFog = FlightHelper.fogCheck(chessPlayers, getCurrentPos());
		
		int count = 0;
		// 找出可走的棋
		Chess[] chesses = cp.getChesses();
		for (Chess chess : chesses) {
			if (chess.isReady()) {
				count++;
			}		
			//待飞区,跑道,机场内
			if(chess.isFlying()){
				if(isFog && !chess.isOutside()){
					count++;
				}else if(!isFog){//没有迷雾
					count++;
				}
			}
			//可以出场
			if(chess.getState() == 0 && dice >= 5 && dice2 == 0){
				count++;
			}
		}
		//dice2不算数
		return count;
	}

	public void animateDone(long userId, int turn, int count) {
		synchronized (operation) {
			//第一个播完动画
			if(operation == ChessConstant.ROOM_ANIM && turn == currentPos && count==diceCount){//FIXME
				next();
			}
		}
	}

	/**
	 * 下一步
	 */
	private void next() {
		clearState(ChessConstant.ROOM_DICE);

		// 判断棋局是否结束
		if (endCheck()) {
			end();
			return;
		}

		ChessPlayer cp = getCurrentPlayer();
		if (null == cp) {
			return;
		}

		// 色子是6继续
		if (6 == dice) {
			if (cp.getItemPos() == ChessConstant.ITEM_MAGIC) {
			} else if (cp.getItemPos() == ChessConstant.ITEM_ENHANCE) {
			} else if (cp.getItemPos() == ChessConstant.ITEM_RELAY) {
			} else {
				nextTurn(1);
				plant();
				return;
			}
		}
			
		//先用6连续效果,再用空中加油
		//logger.debug("next refuel:{}", cp.getRefuel());
		if (cp.getRefuel() > 0) {
			cp.reduceRefuelBy1();
			nextTurn(2);
			plant();
			return;
		}

		nextPos();
		nextTurn(0);// round+1
		
		plant();
	}
	
	/**
	 * 托自动下棋-_-#
	 */
	private void plant(){
		ChessPlayer nextcp = getCurrentPlayer();
		if (null != nextcp) {
			if(nextcp.getNpc() > 0){
				this.dice(0,-1);
			}
		}
	}

	public void leave(int leavePos) {

		// 判断棋局是否结束
		if (endCheck()) {
			end();
			return;
		}

		if (getPos() == leavePos) {
			nextPos();
			nextTurn(0);
		}

		// 清除玩家棋子
		//ChessInfoVO vo = VoFactory.getChessInfoVO(this);
		//broadcast(GeneralResponse.newObject(vo));
	}

	/**
	 * 下个玩家
	 */
	private void nextPos() {
		// 下个玩家
		int pos = 0;
		for (int i = 0; i < chessPlayers.length; ++i) {
			pos = (++currentPos) % 4;

			// 是否下个回合
			if (pos == 0) {
				round++;
			}

			if (null == chessPlayers[pos]) {
				continue;
			} else if (0l == chessPlayers[pos].getUserId()) {
				continue;
			}
			break;// 找到了
		}
	}

	/**
	 * 走棋计算
	 * @return 
	 */
	public Deque<ChessGoVO> goCalculate(Deque<Chess> chessList) {
		Chess chess = chessList.getFirst();
		Deque<ChessGoVO> goList = new ArrayDeque<ChessGoVO>();
		ChessGoVO start = new ChessGoVO(chess.getJourney(), 1, 0, 0, FlightHelper.getChessBits(chessList), 0);
		goList.add(start);
		// 住前走
		int amountDice =  dice * dice3 + dice2;
		int step = 1;
		//步进
		for(int i=1;i<=amountDice;i++){
			Chess.go(chessList,step);
			
			//终点
			if(i==amountDice){		
				FlightHelper.fight(this, chessList, goList, step);		
				
				// 未进入跑道
				if (chess.isOutside()) {
					// 判断是否穿越
					if (chess.getCurCoord() == ChessConstant.GO_ACROSS[chess.getPos()]
							&& (chess.getState() & ChessConstant.CHESS_NO_JUMP) == 0) {
											
						FlightHelper.fightAcross(this, chessList, goList);
						//判断是否坠落
						if( chess.getState() > 0){
							Chess.go(chessList, 12);
							FlightHelper.fight(this, chessList, goList, step);
						}
						
						// 判断是否同色
						if (chess.canJump()
								&& (chess.getState() & ChessConstant.CHESS_NO_JUMP) == 0 ) {
							if (chess.getCurCoord() % 4 == chess.getPos()) {
								
								//跳4格
								for(int j=1;j<=4;j++){
									Chess.go(chessList,step);
									//终点
									if(j==4){
										
										FlightHelper.fight(this, chessList, goList, step);
									}else{
										
										if(FlightHelper.passby(this, chessList, goList, step)){
											break;
										}
										if(FlightHelper.rebound(this, chessList, goList)){
											step = -1;//*=-1 go back and forth?
										}								
									}
								}
								//跳4格
							}
						}

					}
					// 判断是否同色
					else if (chess.getCurCoord() % 4 == chess.getPos() 
							&& (chess.getState() & ChessConstant.CHESS_NO_JUMP) == 0 
							&& chess.canJump()) {
							
						//跳4格
						for(int j=1;j<=4;j++){
							Chess.go(chessList,step);
							//终点
							if(j==4){
								
								FlightHelper.fight(this, chessList, goList, step);
							}else{
								
								if(FlightHelper.passby(this, chessList, goList, step)){
									break;
								}
								if(FlightHelper.rebound(this, chessList, goList)){
									step = -1;//*=-1 go back and forth?
								}									
							}
						}
						//跳4格
						
						// 判断是否穿越
						if (chess.getCurCoord() == ChessConstant.GO_ACROSS[chess.getPos()]
								&& (chess.getState() & ChessConstant.CHESS_NO_JUMP) == 0 ) {
							FlightHelper.fightAcross(this, chessList, goList);
							//判断是否坠落
							if( chess.getState() > 0){
								Chess.go(chessList, 12);
								FlightHelper.fight(this, chessList, goList, step);
							}
						}
					}
				}
			}
			//路过
			else{
				// 未进入跑道
				if (chess.isOutside()) {
					if(FlightHelper.passby(this, chessList, goList, step)){
						break;
					}
					if(FlightHelper.rebound(this, chessList, goList)){
						chess.addState(ChessConstant.CHESS_NO_JUMP);
						chess.addState(ChessConstant.CHESS_REBOUND);						
						step = -1;//*=-1 go back and forth?
					}
				}
			}
		}
		
		chess.baseState();

		//logger.debug(ChessGoVO.debug(goList));
		//logger.debug("coordinate:{}, journey:{}, pos:{}", chess.getCurCoord(), chess.getJourney(), chess.getPos());
		return goList;
	}

	/**
	 * 获得玩家数
	 * 
	 * @return
	 */
	public int getPlayerNum() {
		int playerNum = 0;
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && chessPlayers[i].getUserId() > 0) {
				++playerNum;
			}
		}
		return playerNum;
	}
	
	public ArrayDeque<Chess> getChessByChessBits(long userId, int bits) {
		ArrayDeque<Chess> list = new ArrayDeque<Chess>();
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && userId == chessPlayers[i].getUserId()) {
				Chess[] chesses = chessPlayers[i].getChesses();
				for(Chess chess:chesses){
					int token = 1<<(chess.getChessPos()+chess.getPos()*ChessConstant.CHESS_SIZE);
					//logger.debug("getChessByChessBits{},{}",bits,token);
					if((token&bits)>0){
						list.add(chess);
					}
				}
			}
		}
		return list;
	}

	/**通过棋子位置
	 * @param userId
	 * @param chessPos
	 * @return
	 */
	public Chess getChessByChessPos(long userId, int chessPos) {
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && userId == chessPlayers[i].getUserId()) {
				Chess[] chesses = chessPlayers[i].getChesses();
				if (chessPos < chesses.length && chessPos >= 0)
					return chesses[chessPos];
			}
		}
		return null;
	}

	public ArrayDeque<Chess> getChessByJourney(Chess[] chesses, int journey) {
		ArrayDeque<Chess> list = new ArrayDeque<Chess>();
		for(Chess chess:chesses){
			if (chess.isFlying() && chess.getJourney() == journey) {
				list.add(chess);
			}
		}
		return list;
	}
	
	public ChessPlayer getChessPlayerByPos(int pos) {
		if(null != chessPlayers[pos] && chessPlayers[pos].getUserId()>0){
			return chessPlayers[pos];
		}
		return null;
	}

	public ChessPlayer getChessPlayer(long userId) {
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i]) {
				if (userId == chessPlayers[i].getUserId()) {
					return chessPlayers[i];
				}
			}
		}
		return null;
	}

	public void quitAutoState(long userId) {
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i]) {
				if (chessPlayers[i].getUserId() == userId) {
					chessPlayers[i].setAutoDisable();
					broadcast(GeneralResponse.newObject(new ChessAutoVO(0, i)));
				}
			}
		}
	}

	public void setAutoState(long userId) {
		ChessPlayer cp = getChessPlayer(userId);
		if(null != cp){
			if(!cp.isAutoState()){
				broadcast(GeneralResponse.newObject(new ChessAutoVO(1, cp.getPos())));
			}
			cp.setAutoState(ChessConstant.AUTO_ENABLE);
			
		}
	}
	
	public void setWaitDice() {
		operation = ChessConstant.ROOM_DICE;
		if (isAutoState()) {
			deadline = System.currentTimeMillis() + ChessConstant.AUTODICE_SEC;
		} else {
			deadline = System.currentTimeMillis() + ChessConstant.DICE_SEC;
		}	
	}
	
	public void setWaitAnim() {
		operation = ChessConstant.ROOM_ANIM;
		deadline = System.currentTimeMillis() + ChessConstant.ANIMATE_SEC;
	}

	public void resetDeadline() {
		if(operation == ChessConstant.ROOM_DICE){
			deadline = System.currentTimeMillis() + ChessConstant.GO_SEC;
		}
	}

	public int getPos() {
		return currentPos % 4;
	}
	public ChessPlayer[] getChessPlayers() {
		return chessPlayers;
	}
	public int getRound() {
		return round;
	}
	public int get2Dices() {
		return dice + dice2;
	}
	public int getDice() {
		return dice;
	}
	public int getDice2() {
		return dice2;
	}

	public int getDiceCount() {
		return diceCount;
	}

	public boolean isFlightBegin(){
		return (state & ChessConstant.ROOM_BEGIN) > 0;
	}
	public boolean isFlightDiced(){
		return (state & ChessConstant.ROOM_DICE) > 0;
	}
	public void setState(int s){
		state|=s;
	}
	public void clearState(int s){
		if((state&s)>0){
			state^=s;
		}
	}
	
	public int getCurrentPos() {
		return currentPos;
	}

	public ChessPlayer getCurrentPlayer() {
		int pos = getPos();
		if(null != chessPlayers[pos] && chessPlayers[pos].getUserId()>0){
			return chessPlayers[pos];
		}
		return null;
	}
	public int getMagicDiceNum() {
		return magicDiceNum;
	}
	public void setMagicDiceNum(int magicDiceNum) {
		this.magicDiceNum = magicDiceNum;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSign() {
		return sign;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}
	
	public void setNpc(int pos) {
		chessPlayers[pos].setNpc(1);
		chessPlayers[pos].setAutoState(ChessConstant.AUTO_ENABLE);
	}
	
	public void offline(long userId) {
		ChessPlayer mcp = getChessPlayer(userId);
		if (null != mcp && mcp.getUserId() == userId) {
			mcp.setAutoState(ChessConstant.AUTO_OFFLINE);
		}
		ChessPlayer cp = getCurrentPlayer();
		if (null != cp && cp.getUserId() == userId) {

			synchronized (operation) {
			if (operation == ChessConstant.ROOM_DICE) {
				autoDice();
			} else if (operation == ChessConstant.ROOM_GO) {
				autoGo();
			} //else if(operation == ChessConstant.ROOM_ANIM){
				//next();
			//}			
			}
			
		}
	}
	
	public void broadcast(GeneralResponse resp){
		GameRoom gr = room.get();
		if(null!=gr){
		gr.broadcast(resp);
		}
	}

	public boolean check() {
		long now = System.currentTimeMillis();

		GameRoom gr = room.get();
		if(null!=gr){//一个房间只能有一局棋
			if(gr.getFlight().getToken()!=getToken())
				return true;
		}
		
		//从轮询队列删除
		if(state == ChessConstant.ROOM_IDLE || deadline == 0){
			return true;
		}
		//时间没到
		else if (deadline > now) {
			return false;
		}

		if (operation == ChessConstant.ROOM_DICE) {
			autoDice();
		} else if (operation == ChessConstant.ROOM_GO) {
			autoGo();
		} else if(operation == ChessConstant.ROOM_ANIM){
			next();
		} 
		return false;
	}
	
	static{
		itemAction = new CheatNode();
		ActionNode node = itemAction;
		node.next = new ItemTakeOffNode();
		node = node.next;
		node.next = new DispelsNode();	
		node = node.next;
		node.next = new ItemNode();
		
		chessAction = new TakeOffNode(); 
		node = chessAction;
		node.next = new MoveNode();
	}
}
