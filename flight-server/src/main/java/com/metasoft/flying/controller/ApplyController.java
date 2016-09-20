package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessApplication;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserItem;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.ItemData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.vo.ChessApplicationListVO;
import com.metasoft.flying.vo.ChessApplyRequest;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class ApplyController implements GeneralController {

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

	@HandlerAnno(name = "申请下棋", cmd = "chat.apply", req = ChessApplyRequest.class)
	public void apply(ChessApplyRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		ChessApplication app = room.getChessApp();
		long masterId = Long.valueOf(self.getGroup());
		User user = userService.getAnyUserById(masterId);
		// 0也能申请
		app.apply(self, 0, req.getType());
		if (req.getRose99() > 0) {
			try {
				UserItem item = self.addItem(ItemConstant.ITEMID_ROSE99, -req.getRose99(), "申请");
				int num = 99 * req.getRose99();
				app.apply(self, num, req.getType());
				ItemData itemData = staticDataService.getItemDataMap().get(ItemConstant.ITEMID_ROSE99);
				itemController.giveTo(itemData, req.getRose99(), user, self);
				// 物品通知
				self.getConn()
						.deliver(GeneralResponse.newObject(VoFactory.getItemVO(item)));
			} catch (GeneralException e) {
				// e.printStackTrace();
			}
		}
		if (req.getRose1() > 0) {
			try {
				self.addItem(ItemConstant.ITEMID_ROSE1, -req.getRose1(), "申请");
				app.apply(self, req.getRose1(), req.getType());
				ItemData itemData = staticDataService.getItemDataMap().get(ItemConstant.ITEMID_ROSE1);
				itemController.giveTo(itemData, req.getRose1(), user, self);
				// 物品通知
				self.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(ItemConstant.ITEMID_ROSE1, self)));
			} catch (GeneralException e) {
				// e.printStackTrace();
			}
		}

		// 广播申请列表
		room.broadcast(GeneralResponse.newObject(applyList(null)));
	}

	@HandlerAnno(name = "禁止申请", cmd = "chat.disable.apply", req = ChessApplyRequest.class)
	public void disableApply(ChessApplyRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		ChessApplication ChessApp = room.getChessApp();
		ChessApp.disable(req.getId());

		// 广播申请列表
		room.broadcast(GeneralResponse.newObject(applyList(null)));
	}

	@HandlerAnno(name = "取消申请", cmd = "chat.cancel.apply", req = GeneralRequest.class)
	public void cancelApply(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		ChessApplication ChessApp = room.getChessApp();
		ChessApp.cancel(self);

		// 广播申请列表
		room.broadcast(GeneralResponse.newObject(applyList(null)));
	}

	@HandlerAnno(name = "申请列表", cmd = "chat.apply.list", req = GeneralRequest.class)
	public ChessApplicationListVO applyList(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		ChessApplication ChessApp = room.getChessApp();
		ChessApplicationListVO vo = new ChessApplicationListVO();
		vo.setList(ChessApp.applictionList());
		vo.setNow(System.currentTimeMillis());
		return vo;
	}
}
