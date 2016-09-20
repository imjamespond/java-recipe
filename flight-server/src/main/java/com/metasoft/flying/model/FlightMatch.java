package com.metasoft.flying.model;

import java.util.List;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.ChessScoreVO;
import com.metasoft.flying.vo.MatchFinishVO;
import com.metasoft.flying.vo.MatchInfoVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class FlightMatch extends Flight{
	//private static final Logger logger = LoggerFactory.getLogger(FlightMatch.class);
	public static final long MATCH_LENGTH = 3000 * 1000l;
	//private List<WeakReference<GameRoom> > roomList = new ArrayList<>(4);
	private MatchPlayer[] matchPlayers = new MatchPlayer[4];
	private long commence;//计时50分钟

	public FlightMatch(GameRoom room) {
		super(room);
		type = GeneralConstant.GTYPE_MATCH;
	}

//	public void addGameRoom(GameRoom room){
//		roomList.add(new WeakReference<GameRoom>(room));
//	}
	
	@Override
	public void beginNotify() {
		commence();
		// 广播棋子初始位置
		MatchInfoVO vo = FlightMatchHelper.getMatchInfoVO(this);
		//ChessInfoVO vo = VoFactory.getChessInfoVO(this);
		broadcast(GeneralResponse.newObject(vo));	
	}
	
//	@Override
//	public void broadcast(GeneralResponse resp){
//		//logger.debug("broadcast");
//		for(WeakReference<GameRoom> room:roomList){
//			GameRoom gr = room.get();
//			if(null!=gr){
//				gr.broadcast(resp);
//			}
//		}
//	}
	
	@Override
	public void end(){
		reset();
	}
	
	/**
	 * 结算
	 */
	@Override
	public void finishNotify(int pos) {
		// 结束vo
		long duration = System.currentTimeMillis()-commence;
		MatchFinishVO vo = FlightMatchHelper.getMatchFinishVO(this, duration);
		//比赛结果记录
		List<ChessScoreVO> list = vo.getFinish().getScoreList();
		UserMatch um = new UserMatch();
		um.setScores(vo.getFinish().getScoreList());
		um.setMtime(System.currentTimeMillis());
		um.setDuration(duration);
		um.setWin(pos);

		UserService us = SpringService.getBean(UserService.class);
		for(ChessScoreVO csvo : list){
			try {
				User user = us.getAnyUserById(csvo.getUserId());
				user.addMatch(um);
			} catch (GeneralException e) {
				e.printStackTrace();
			}
		}
		
		broadcast(GeneralResponse.newObject(vo));
		//broadcast(GeneralResponse.newObject(FlightHelper.getChessFinishVO(chessPlayers,chessPlayers[pos].getUserId())));
//		for(WeakReference<GameRoom> room:roomList){
//			GameRoom gr = room.get();
//			if(null!=gr){
//				gr.setFlightMatch(null);
//			}
//		}
	}
	
	@Override
	public boolean check(){
		//最大时长
		long now = System.currentTimeMillis();
		if(commence + MATCH_LENGTH < now){		
			finishNotify(-1);
			end();
			return true;
		}
		return super.check();
	}

//	public int getRoomSize() {
//		return roomList.size();
//	}

	public long getCommence() {
		return commence;
	}

	public void commence() {
		this.commence = System.currentTimeMillis();
	}

	public void putMatchPlayer(long uid, int pos) {
		MatchPlayer mp = new MatchPlayer(uid);
		matchPlayers[pos] = mp;
	}

	public MatchPlayer getMatchPlayer(int pos) {
		return matchPlayers[pos];
	}
}
