package com.metasoft.flying.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.FlightMatch;
import com.metasoft.flying.model.FlightMatchHelper;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserDataPersist;
import com.metasoft.flying.model.UserMatch;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.ChessRequest;
import com.metasoft.flying.vo.EnrollInfoVO;
import com.metasoft.flying.vo.MatchInfoVO;
import com.metasoft.flying.vo.MatchResultVO;
import com.metasoft.flying.vo.MatchVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * @author james
 * 邀请controller
 */
@Controller
public class EnrollController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private GameRoomService gameRoomService;
	@Autowired
	private StaticDataService staticDataService;
	@Value("${enroll.time}")
	private String enrollTime;
	private ArrayList<EnrollHour> enrollHours = new ArrayList<EnrollHour>();
	
	@PostConstruct
	public void init(){
		String[] times = enrollTime.split(";");//次数
		for(String time:times){
			String[] hour = time.split(":");
			enrollHours.add(new EnrollHour(Integer.valueOf(hour[0]), Integer.valueOf(hour[1])) );
		}
	}
	
	@HandlerAnno(name = "比赛列表", cmd = "match.list", req = GeneralRequest.class)
	public List<MatchVO> matchList(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return VoFactory.getMatchVOList(self.getId(), matchService.matchRankMap, staticDataService.matchMap, self.getmatchDeq());
	}
	
	@HandlerAnno(name = "比赛结果List<MatchResultVO>", cmd = "match.result", req = GeneralRequest.class)
	public List<MatchResultVO> matchResult(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		List<MatchResultVO> list = new ArrayList<MatchResultVO>();
		Deque<UserMatch> deq = self.getmatchDeq();
		for(UserMatch um:deq){
			list.add(VoFactory.getMatchResultVO(um));
		}
		return list;
	}
	
	@HandlerAnno(name = "报名比赛", cmd = "enroll.match", req = GeneralRequest.class)
	public Integer match(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if(null==matchService.getCurMatch()){
			throw new GeneralException(0, "invalid.match");
		}
		self.reduceGold(matchService.getCurMatch().getCost());
		// 财富更新
		RequestUtils.getCurrentConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
		
		long now = System.currentTimeMillis();
		UserDataPersist userData = self.getUserDataPersist();
		userData.setMatchepoch(now);
		userData.setMatchnum(userData.getMatchnum()+1);
		self.updateData = true;
		matchService.enroll(self.getId());
		return userData.getMatchNumber();
	}
	
	@HandlerAnno(name = "报名信息", cmd = "enroll.info", req = GeneralRequest.class)
	public EnrollInfoVO erollInfo(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		
		EnrollInfoVO vo = new EnrollInfoVO();
		vo.setTime(System.currentTimeMillis());
		//vo.setNext( getNextTime(vo.getTime()) - vo.getTime());
		vo.setNext(getEnrollTime());//8~23可报名
		vo.setEnroll(matchService.enrollCheck(self.getId()));
		return vo;
	}
	
	@HandlerAnno(name = "比赛信息", cmd = "match.info", req = ChessRequest.class)
	public MatchInfoVO info(ChessRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (null == user.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = gameRoomService.getGroup(user.getGroup());
		FlightMatch flight = (FlightMatch) room.getFlight();
		//取消自动
		//ChessPlayer cp = flight.getChessPlayer(user.getId());
		//if(null != cp){
			//cp.setAutoDisable();
		//}
		
		MatchInfoVO vo = FlightMatchHelper.getMatchInfoVO(flight);
		return vo;
	}
	
	/**是否明天比赛
	 * @return
	 */
	public boolean isTomorrow(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minu = c.get(Calendar.MINUTE);
		
		for(EnrollHour enrollHour:enrollHours){
			if(hour < enrollHour.hour){
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), enrollHour.hour, enrollHour.minute, 0);
				return false;
			}else if(hour == enrollHour.hour && minu < enrollHour.minute){
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), enrollHour.hour, enrollHour.minute, 0);
				return false;
			}
		}
		return true;
	}
	
	/**下次比赛时间
	 * @param now
	 * @return
	 
	private long getNextTime(long now){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minu = c.get(Calendar.MINUTE);
		
		boolean tomorrow = true;
		for(EnrollHour enrollHour:enrollHours){
			if(hour < enrollHour.hour){
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), enrollHour.hour, enrollHour.minute, 0);
				tomorrow = false;
				break;
			}else if(hour == enrollHour.hour && minu < enrollHour.minute){
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), enrollHour.hour, enrollHour.minute, 0);
				tomorrow = false;
				break;
			}
		}

		//明天12点
		if(tomorrow) {
			c.setTimeInMillis(now + GeneralConstant.SINGLE_DAY);
			//设置时辰
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), enrollHours.get(0).hour, enrollHours.get(0).minute, 0);
		}

		return c.getTimeInMillis();
	}*/
	
	/**
	 * 报名倒计时
	 * @return
	 */
	private long getEnrollTime(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		//int minu = c.get(Calendar.MINUTE);
		long now = c.getTimeInMillis();
		
		/*if(hour >= 23){
			c.setTimeInMillis(now + GeneralConstant.SINGLE_DAY);
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
			return c.getTimeInMillis()-now;
		}else*/ if(hour <= 8){
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
			return c.getTimeInMillis()-now;
		}
		return 0;
	}
	
	public int getOrder(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minu = c.get(Calendar.MINUTE);
		
		int order = 0;
		for(EnrollHour enrollHour:enrollHours){			
			if(hour < enrollHour.hour){
				return order;
			}else if(hour == enrollHour.hour && minu < enrollHour.minute){
				return order;
			}
			order++;
		}

		return -1;
	}
	
	private class EnrollHour{
		public int hour;
		public int minute;
		public EnrollHour(int hour, int minute) {
			super();
			this.hour = hour;
			this.minute = minute;
		}
		
	}
}