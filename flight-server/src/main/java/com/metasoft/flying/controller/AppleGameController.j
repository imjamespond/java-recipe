package com.metasoft.flying.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserItem;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.PrizeData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.RankService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.vo.AppleGameRequest;
import com.metasoft.flying.vo.AppleInfoVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * @author james
 * 邀请controller
 */
@Controller
public class AppleGameController implements GeneralController {
	//private static final Logger logger =  LoggerFactory.getLogger(ChatController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private GameRoomService chessRoomService;
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private ItemController itemController;
	@Autowired
	private StaticDataService staticDataService;
	
	public static final int PRICE_HAMMER = 20;
	@HandlerAnno(name = "苹果开始", cmd = "apple.begin", req = AppleGameRequest.class)
	public void begin(GeneralRequest req) throws GeneralException {
		//User self = userService.getRequestUser();
		//self.setAppleBegin(System.currentTimeMillis());
	}

	@HandlerAnno(name = "苹果游戏", cmd = "apple.game", req = AppleGameRequest.class)
	public void game(AppleGameRequest req) throws GeneralException {
		User self = userService.getRequestUser();
	
		//时效检测
//		if((System.currentTimeMillis()-self.getAppleBegin())>6000l){
//			throw new GeneralException(0, "apple.time.out");
//		}
		
		//self.appleHit(req.getApple());
		UserItem item = self.addItem(ItemConstant.ITEMID_APPLE1, req.getApple(), "打苹果");
		// 物品通知
		if (null != self.getConn()) {
			self.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(item)));
		}
	}
	
	@HandlerAnno(name = "苹果信息", cmd = "apple.info", req = AppleGameRequest.class)
	public AppleInfoVO info(AppleGameRequest req) throws GeneralException {
		//User self = userService.getRequestUser();
		AppleInfoVO vo = new AppleInfoVO();
		vo.setDuration(30);
		vo.setTime(System.currentTimeMillis());
		//vo.setApple(self.getUserPersist().getApplehit());

		vo.setNext( AppleGameController.getNextTime(vo.getTime()) - vo.getTime());
		vo.setPrice(AppleGameController.PRICE_HAMMER);
		//vo.setHammer(self.getUserPersist().getHammer()>AppleGameController.getNextTime(vo.getTime())?1:0);
		
		vo.setStart(RankService.PRIZE_START);
		vo.setEnd(RankService.PRIZE_END);
		PrizeData pd = staticDataService.getPrizeData(RankService.PRIZE_ID);
		if(null != pd)
		vo.setName(pd.getName());
		return vo;
	}
	
	
	@HandlerAnno(name = "苹果锤子", cmd = "apple.hammer", req = AppleGameRequest.class)
	public void hammer(AppleGameRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		self.reduceGems(AppleGameController.PRICE_HAMMER, "魔力锤");
		//self.buyHammer(AppleGameController.getNextTime(System.currentTimeMillis()));

		// 财富通知
		if (null != self.getConn()) {
			self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
		}
	}
	
	private static long getNextTime(long now){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		//当晚21点
		if(hour >= GeneralConstant.HOUR12){
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), GeneralConstant.HOUR21, 0, 0);
		}
		//当天12点
		else{
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), GeneralConstant.HOUR12, 0, 0);
		}
		//明天12点
		if(hour >= GeneralConstant.HOUR21){
			c.setTimeInMillis(now + GeneralConstant.SINGLE_DAY);
			//设置时辰
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), GeneralConstant.HOUR12, 0, 0);
		}

		return c.getTimeInMillis();
	}
}