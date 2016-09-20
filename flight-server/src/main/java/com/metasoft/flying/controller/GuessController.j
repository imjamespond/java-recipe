package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessGuess;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserItem;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.vo.GuessRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class GuessController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private GameRoomService chessRoomService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private ItemController itemController;
	@Autowired
	private StaticDataService staticDataService;

	@HandlerAnno(name = "竞猜", cmd = "chat.guess", req = GuessRequest.class)
	public void guess(GuessRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		long masterId = Long.valueOf(self.getGroup());
		User master = userService.getAnyUserById(masterId);
		UserItem item = self.addItem(ItemConstant.ITEMID_ROSE1, -1, "竞猜");
		self.addContribution(1);
		master.addRose(1, "猜赠");
		master.addCharm(1);
		// 物品通知
		self.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(item)));		
		self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
		// 财富通知
		if (null != master.getConn()) {
			master.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(master)));
		}
		
		GameRoom room = chessRoomService.getGroup(self.getGroup());	
		ChessGuess guess = room.getGuess();
		guess.guess(self.getId(), req.getPos());

	}
	@HandlerAnno(name = "购买竞猜", cmd = "chat.buy.guess", req = GuessRequest.class)
	public void buyGuess(GuessRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		long masterId = Long.valueOf(self.getGroup());
		User master = userService.getAnyUserById(masterId);
		self.reduceGems(10, "购猜");
		self.addContribution(1);
		master.addRose(1, "猜赠");
		master.addCharm(1);
		// 物品通知
		self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));		
		// 财富通知
		if (null != master.getConn()) {
			master.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(master)));
		}
			
		GameRoom room = chessRoomService.getGroup(self.getGroup());	
		ChessGuess guess = room.getGuess();
		guess.guess(self.getId(), req.getPos());

	}
}
