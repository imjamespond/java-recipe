package com.metasoft.flying.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.controller.DressController;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.PveData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.PveFinishVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class FlightPve extends Flight{
	private static final Logger logger = LoggerFactory.getLogger(FlightPve.class);
	
	public int level;

	public FlightPve(GameRoom room) {
		super(room);
		type = GeneralConstant.GTYPE_PVE;
	}
	
	@Override
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
	
	/**
	 * 结算
	 */
	@Override
	public void finishNotify(int pos) {
		StaticDataService staticDataService = SpringService.getBean(StaticDataService.class);
		PveData data = staticDataService.pveDataMap.get(level);
		if(null==data){
			return;
		}
	
		PveFinishVO finishVO = new PveFinishVO();
		finishVO.setPos(pos);
		for (ChessPlayer cp : chessPlayers) {
			if (null != cp && cp.getUserId() != 0l && cp.getNpc()==0) {
				// 是否有玩家
				UserService userService = SpringService.getBean(UserService.class);

				try {
					User user = userService.getAnyUserById(cp.getUserId());
					
					//增加经验
					if(cp.getNpc()==0){
						user.addScore(data.getLevel());
					}
					
					// 财富通知
					BaseConnection conn = user.getConn();
					if (conn != null) {
						conn.deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
					}
					
					UserDataPersist ud = user.getUserDataPersist();
					if(pos == cp.getPos()){
						if(level>ud.getPve()){
							ud.setPve(level);
							//user.pveLevel = level;
							//ud.setPvetime(System.currentTimeMillis());
							if(level==12){
								user.addItem(300002, 1, "闯关");
								DressController dressController = SpringService.getBean(DressController.class);
								dressController.puton(user, 300002, ItemConstant.ITEMPOS_PLANE);
							}
							
							user.updateData = true;
						}
						ud.setPvetime(System.currentTimeMillis());
							
						finishVO.setExp(data.getLevel());
						finishVO.setUpgrade(level);
						finishVO.setRest(user.getPveNum());
					}else{
						finishVO.setExp(0);
						finishVO.setUpgrade(ud.getPve());
						finishVO.setRest(user.getPveNum());
					}

				} catch (GeneralException e) {
					e.printStackTrace();
				}
			}
		}
		
		broadcast(GeneralResponse.newObject(finishVO));
		GameRoom gr = room.get();
		if(null!=gr){
			gr.setFlightPve(null);
		}
		
		level = 0;
	}
}
