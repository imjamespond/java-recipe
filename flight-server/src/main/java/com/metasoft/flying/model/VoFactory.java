package com.metasoft.flying.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.data.ArenaData;
import com.metasoft.flying.model.data.MatchData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.ArenaVO;
import com.metasoft.flying.vo.ChatBulletinVO;
import com.metasoft.flying.vo.ChessCastVO;
import com.metasoft.flying.vo.ChessDiceVO;
import com.metasoft.flying.vo.ChessInfoVO;
import com.metasoft.flying.vo.ChessItemListVO;
import com.metasoft.flying.vo.ChessPlayerVO;
import com.metasoft.flying.vo.ChessReady2VO;
import com.metasoft.flying.vo.ChessReadyVO;
import com.metasoft.flying.vo.ChessRoomUserVO;
import com.metasoft.flying.vo.ChessRoomVO;
import com.metasoft.flying.vo.ChessScoreVO;
import com.metasoft.flying.vo.ChessVO;
import com.metasoft.flying.vo.ItemVO;
import com.metasoft.flying.vo.MatchPlayerVO;
import com.metasoft.flying.vo.MatchResultVO;
import com.metasoft.flying.vo.MatchVO;
import com.metasoft.flying.vo.PvpRoomVO;
import com.metasoft.flying.vo.RoomUserVO;
import com.metasoft.flying.vo.UserVO;
import com.metasoft.flying.vo.WealthVO;

public class VoFactory {
	//private static final Logger log = LoggerFactory.getLogger(VoFactory.class);
	
	public static UserVO getUserVO(User user) {
		UserVO vo = new UserVO();
		UserPersist up = user.getUserPersist();
		vo.setUserId(user.getId());
		vo.setGroup(user.getGroup());
		vo.setGender(up.getGender());
		vo.setName(up.getNickname());
 
		UserWealthPersist userWealth = user.getUserWealthPersist();
		UserDataPersist userData = user.getUserDataPersist();
		vo.setRose(userWealth.getRose());
		vo.setGold(userData.getGold());		
		vo.setContribute(userData.getContribute());
		vo.setCharm(userData.getCharm());
		vo.setScore(userData.getScore());
		vo.setGuide(userData.getGuide());
		vo.setAvatar(up.getAvatar());
		vo.setPveUpgrade(user.getNInitUpgrade());
		vo.setPve(userData.getPve());
		vo.setPveNum(user.getPveNum());
		vo.setMatchNum(userData.getMatchNumber());
		
		//周苹果数
		//vo.setApple(up.getAppleprize());
		return vo;
	}

	public static ChessVO getChessVO(Chess chess) {
		ChessVO vo = new ChessVO();
		vo.setCurCoord(chess.getCurCoord());
		vo.setJourney(chess.getJourney());
		vo.setState(chess.getState());
		vo.setChessPos(chess.getChessPos());
		vo.setPos(chess.getPos());
		return vo;
	}

	public static ChessReadyVO getChessReadyVO(Chess chess) {
		ChessReadyVO vo = new ChessReadyVO();
		vo.setPos(chess.getPos());
		vo.setChessPos(chess.getChessPos());
		return vo;
	}
	public static ChessReady2VO getChessReady2VO(Chess chess) {
		ChessReady2VO vo = new ChessReady2VO();
		vo.setPos(chess.getPos());
		vo.setChessPos(chess.getChessPos());
		return vo;
	}

	public static Deque<ChessVO> getChessVOList(Deque<Chess> chessList) {
		Deque<ChessVO> voList = new ArrayDeque<ChessVO>();
		for (Chess chess : chessList) {
			ChessVO vo = getChessVO(chess);
			voList.add(vo);
		}
		return voList;
	}

	public static ChessRoomUserVO getChessRoomUserVO(ChessPlayer player) {
		UserService userService = SpringService.getBean(UserService.class);

		ChessRoomUserVO vo = new ChessRoomUserVO();
		vo.setPos(player.getPos());
		vo.setUserId(player.getUserId());
		vo.setRose(player.getRose());
		vo.setShow(player.getShow());
		vo.setAutoState(player.isAutoState() ? 1 : 0);
		//vo.setMagicDice(player.getMagic());
		try {
			User user = userService.getAnyUserById(player.getUserId());
			vo.setName(user.getUserPersist().getNickname());
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		return vo;
	}

	public static ChessRoomVO getChessRoomVO(GameRoom room) {
		ChessRoomVO roomVO = new ChessRoomVO();
		Flight flight = room.getFlight();
		//roomVO.setId(room.getMasterId());
		roomVO.setName(room.getName());
		roomVO.setState(flight.isFlightBegin()?1:0);
		roomVO.setMagic(flight.getMagicDiceNum());
		roomVO.setShow(ChessConstant.SHOW_ITEM_NUM);
		roomVO.setType(flight.getType());
		//是否在对战
		PkArena arena = room.getArena();
		if(arena!=null){
			roomVO.setType(1);
		}
		return roomVO;
	}
	
	public static PvpRoomVO getPvpRoomVO(GameRoom room) {
		PvpRoomVO roomVO = new PvpRoomVO();
		Flight flight = room.getFlight();
		roomVO.setName(room.getName());
		roomVO.setState(flight.isFlightBegin()?1:0);
		roomVO.setType(flight.getType());

		return roomVO;
	}
	
	public static ChessPlayerVO getChessPlayerVO(ChessPlayer cPlayer) {
		ChessPlayerVO playerVO = new ChessPlayerVO();
		Chess[] chesses = cPlayer.getChesses();
		ChessVO[] voList = new ChessVO[chesses.length];
		for (int i=0; i<chesses.length; i++) {
			ChessVO vo = getChessVO(chesses[i]);
			voList[i]=vo;
		}
		playerVO.setChesses(voList);
		playerVO.setItems(cPlayer.getItems());
		playerVO.setUserId(cPlayer.getUserId());
		playerVO.setNpc(cPlayer.getNpc());
		
		playerVO.setPos(cPlayer.getPos());
		playerVO.setRose(cPlayer.getRose());
		playerVO.setShow(cPlayer.getShow());
		playerVO.setAutoState(cPlayer.isAutoState() ? 1 : 0);
		//playerVO.setMagicDice(cPlayer.getMagic());
		playerVO.setPlane(cPlayer.getPlane());
		playerVO.setRefuel(cPlayer.getRefuel());
		playerVO.setThorns(cPlayer.getThorns());
		playerVO.setFog(cPlayer.getFog());
		UserService userService = SpringService.getBean(UserService.class);
		try {
			User user = userService.getAnyUserById(cPlayer.getUserId());
			playerVO.setName(user.getUserPersist().getNickname());
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		return playerVO;
	}

	public static ChessInfoVO getChessInfoVO(Flight flight) {
		ChessInfoVO infoVO = new ChessInfoVO();

		infoVO.setSign(flight.getSign());
		infoVO.setItems(flight.items);
		infoVO.setRound(flight.getRound());

		List<ChessPlayerVO> voList = new ArrayList<ChessPlayerVO>(4);
		ChessPlayer[] chessPlayers = flight.getChessPlayers();
		//是否有buff
		int currentPos = flight.getCurrentPos();
		int fogBuff = FlightHelper.getFogBuff(chessPlayers, currentPos);
		int thornBuff = FlightHelper.getThornBuff(chessPlayers, currentPos);
		for (int i = 0; i < chessPlayers.length; ++i) {
			if (null != chessPlayers[i] && chessPlayers[i].getUserId() > 0l ) {
				ChessPlayer cp = chessPlayers[i];
				ChessPlayerVO chessPlayerVO = VoFactory.getChessPlayerVO(cp);
				chessPlayerVO.setFog(15 & (fogBuff>>i*4));//15 represent for 1,2,4,8
				chessPlayerVO.setThorns(15 & (thornBuff>>i*4));
				voList.add(chessPlayerVO);
				//log.debug("chessPlayerVO fog:{}, thorn:{}",chessPlayerVO.getFog(), chessPlayerVO.getThorns());
			}
		}
		infoVO.setPlayers(voList);
		return infoVO;
	}
	
	public static ChessCastVO getChessCastVO(Flight flight, int type){
		ChessCastVO vo = new ChessCastVO();
		vo.setPos(flight.getPos());
		vo.setRound(flight.getRound());
		ChessPlayer[] chessPlayers = flight.getChessPlayers();
		vo.setFog(FlightHelper.getFogBuff(chessPlayers, flight.getCurrentPos()));
		vo.setThorns(FlightHelper.getThornBuff(chessPlayers, flight.getCurrentPos()));
		vo.setType(type);
		vo.setTurn(flight.getCurrentPos());//FIXME using dice count consistency
		vo.setCount(flight.getDiceCount());
		//System.out.printf("ChessCastVO fog:%d, thorn:%d\n",vo.getFog(), vo.getThorns());
		return vo;
	}

	public static ChessDiceVO getChessDiceVO(Flight flight) {
		ChessDiceVO vo = new ChessDiceVO();
		ChessPlayer cp = flight.getCurrentPlayer();
		if (null == cp) {
			return vo;
		}
		vo.setItem(cp.getItemPos());
		vo.setDice(flight.getDice());
		vo.setDice2(flight.getDice2());
		vo.setPos(flight.getCurrentPos());
		return vo;
	}

	public static RoomUserVO getRoomUserVO(User user, int state) {
		RoomUserVO vo = new RoomUserVO();
		vo.setUserId(user.getId());
		vo.setUserName(user.getUserPersist().getNickname());
		vo.setState(state);
		UserDataPersist userData = user.getUserDataPersist();
		vo.setContribute(userData.getContribute());
		vo.setScore(userData.getScore());
		return vo;
	}

	public static WealthVO getWealthVO(User user) {
		WealthVO vo = new WealthVO();
		UserWealthPersist userWealth = user.getUserWealthPersist();
		UserDataPersist userData = user.getUserDataPersist();
		vo.setRose(userWealth.getRose());
		vo.setGold(userData.getGold());		
		vo.setContribute(userData.getContribute());
		vo.setCharm(userData.getCharm());
		vo.setScore(userData.getScore());
		return vo;
	}

	public static ItemVO getItemVO(int itemId, User user) {
		ItemVO vo = new ItemVO();
		Map<Integer, UserItem> itemMap = user.getItemMap();
		UserItem ui = itemMap.get(itemId);
		vo.setItemId(itemId);
		if (null != ui) {
			vo.setNum(ui.getItemNum());
			vo.setDeadline(ui.getItemTime());
		}
		return vo;
	}
	
	
	public static ItemVO getItemVO(UserItem item){
		return new ItemVO(0l, item.getItemId(), item.getItemNum());
	}

	public static ChatBulletinVO getChatBulletinVO(User user) {
		ChatBulletinVO vo = new ChatBulletinVO();
		vo.setGender(user.getUserPersist().getGender());
		vo.setId(user.getId());
		vo.setName(user.getUserPersist().getNickname());
		return vo;
	}

	public static ChessItemListVO getChessItemListVO(Flight chessRoom) {
		ChessItemListVO vo = new ChessItemListVO();
		vo.setItemList(chessRoom.items);
		return vo;
	}
	
	public static List<MatchVO> getMatchVOList(long uid, Map<Integer, List<MatchPlayerVO>> matchRankMap,Map<Integer, MatchData> matchMap, Deque<UserMatch> matchDeq){
		List<MatchVO> list = new ArrayList<MatchVO>();
		Calendar cld = Calendar.getInstance();
		for(Entry<Integer, MatchData> entry:matchMap.entrySet()){
			MatchData md = entry.getValue();
			MatchVO vo = new MatchVO();
			vo.setCost(md.getCost());
			vo.setId(md.getId());
			vo.setName(md.getName());
			vo.setTime(md.getTime());
			//开始比赛
			if(md.getTime()==cld.get(Calendar.HOUR_OF_DAY)){
				MatchService ms = SpringService.getBean(MatchService.class);
				if(ms.enrollConfine(uid)){
					vo.setEnd(2);
				}else{
					vo.setEnd(1);
				}
			//比赛结束
			}else if(md.getTime()<cld.get(Calendar.HOUR_OF_DAY)){
				vo.setEnd(2);
			}
			//比赛结束有排行
			if(null!=matchRankMap.get(md.getId())){
				vo.setEnd(3);
			}

			list.add(vo);
		}
		return list;
	}
	
	public static MatchResultVO getMatchResultVO(UserMatch um){
		MatchResultVO mrvo = new MatchResultVO();
		UserService us = SpringService.getBean(UserService.class);
		for(ChessScoreVO vo:um.getScores()){
			if(null==vo.getNickname()&&vo.getUserId()>0){
				User user;
				try {
					user = us.getAnyUserById(vo.getUserId());
					vo.setNickname(user.getUserPersist().getNickname());
				} catch (GeneralException e) {
					e.printStackTrace();
				}
			}
		}
		mrvo.setFinishtime(um.getMtime());
		mrvo.setDuration(um.getDuration());
		mrvo.setPlayers(um.getScores());
		return mrvo;
	}
	
	public static List<ArenaVO> getArenaVOList(Map<Integer, ArenaData> arenaMap){
		List<ArenaVO> list = new ArrayList<ArenaVO>();
		for(Entry<Integer, ArenaData> entry:arenaMap.entrySet()){
			ArenaData ad = entry.getValue();
			ArenaVO vo = new ArenaVO();
			vo.setId(ad.getId());
			vo.setGold(ad.getGold());
			vo.setCost(ad.getCost());
			list.add(vo);
		}
		return list;
	}
}
