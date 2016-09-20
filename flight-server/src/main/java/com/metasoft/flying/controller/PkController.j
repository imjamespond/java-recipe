package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessApplication;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.PkArena;
import com.metasoft.flying.model.PkPlayer;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.constant.ArenaConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.PkArenaService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.vo.PkArenaInfoVO;
import com.metasoft.flying.vo.PkFireRequest;
import com.metasoft.flying.vo.PkGoRequest;
import com.metasoft.flying.vo.PkHurtRequest;
import com.metasoft.flying.vo.PkJoinRequest;
import com.metasoft.flying.vo.PkRebirthRequest;
import com.metasoft.flying.vo.PkVoFactory;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class PkController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private PkArenaService pkArenaService;
	@Autowired
	private GameRoomService chessRoomService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private RelationController relationController;
	@Autowired
	private ApplyController applyController;
	
	@HandlerAnno(name = "准备或开始", cmd = "arena.start", req = PkJoinRequest.class)
	public void start(PkJoinRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		pkArenaService.readyOrCommence(room, req);
		
		// 删除申请
		ChessApplication app = room.getChessApp();
		PkArena arena = room.getArena();
		if(null != arena){
			PkPlayer[] players = arena.getPkPlayers();
			for (int i = 0; i < players.length; i++) {
				PkPlayer cp = players[i];
				if (null != cp) {
					if (cp.getUserId() > 0) {
						app.remove(cp.getUserId());
					}
				}
			}
		}
		// 广播更新申请列表
		room.broadcast(GeneralResponse.newObject(applyController.applyList(null)));
	}
	
	@HandlerAnno(name = "混战", cmd = "arena.pk", req = PkJoinRequest.class)
	public void pk(PkJoinRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		PkArena arena = room.getArena();
		if(null != arena){
			if(arena.getState() == ArenaConstant.COMMENCE){
				arena.cease();
			}
			arena.pk();
		}
	}
	
	@HandlerAnno(name = "三国杀模式", cmd = "arena.emperor", req = PkJoinRequest.class)
	public void emperor(PkJoinRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		pkArenaService.commence(room, req);
		
		// 删除申请
		ChessApplication app = room.getChessApp();
		PkArena arena = room.getArena();
		if(null != arena){
			PkPlayer[] players = arena.getPkPlayers();
			for (int i = 0; i < players.length; i++) {
				PkPlayer cp = players[i];
				if (null != cp) {
					if (cp.getUserId() > 0) {
						app.remove(cp.getUserId());
					}
				}
			}
		}
		// 广播更新申请列表
		room.broadcast(GeneralResponse.newObject(applyController.applyList(null)));
	}

	@HandlerAnno(name = "同步信息", cmd = "arena.info", req = PkFireRequest.class)
	public PkArenaInfoVO info(PkFireRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		PkArena arena = room.getArena();
		if(null != arena){
			return PkVoFactory.getPkArenaInfoVO(arena);
		}
		return null;
	}
	
	@HandlerAnno(name = "开火", cmd = "arena.fire", req = PkFireRequest.class)
	public void fire(PkFireRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		PkArena arena = room.getArena();
		if(null != arena){
			arena.fire(req);
		}
	}
	@HandlerAnno(name = "伤害", cmd = "arena.hurt", req = PkHurtRequest.class)
	public void hurt(PkHurtRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		PkArena arena = room.getArena();
		if(null != arena){
			arena.hurt(req);
		}
	}
	@HandlerAnno(name = "重生", cmd = "arena.rebirth", req = PkRebirthRequest.class)
	public void rebirth(PkRebirthRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		PkArena arena = room.getArena();
		if(null != arena){
			arena.rebirth(req);
		}
	}
	@HandlerAnno(name = "行走", cmd = "arena.go", req = PkGoRequest.class)
	public void act(PkGoRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		PkArena arena = room.getArena();
		if(null != arena){
			arena.go(req);
		}
	}
}
