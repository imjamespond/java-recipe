package com.metasoft.empire.controller;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.empire.common.GeneralController;
import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.annotation.HandlerAnno;
import com.metasoft.empire.model.GameRoom;
import com.metasoft.empire.model.User;
import com.metasoft.empire.service.GameService;
import com.metasoft.empire.service.UserService;
import com.metasoft.empire.service.common.LocalizationService;
import com.metasoft.empire.vo.ChatReqInvite;
import com.metasoft.empire.vo.GameRequest;
import com.metasoft.empire.vo.GameSwapRequest;
import com.metasoft.empire.vo.VoidRequest;

@Controller
public class GameController implements GeneralController {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	@Autowired
	private LocalizationService localizationService;
	@Autowired
	private UserService userService;
	@Autowired
	private GameService gameService;
	@Autowired
	private UserController userController;
	static{log.debug(GameController.class.toString());}

	@HandlerAnno(name = "对战分配", cmd = "GameAllocate", req = GameRequest.class)
	public boolean allocate(GameRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		user.map = req.getMap();
		if(null!=req.getRoles())
		System.arraycopy(req.getRoles(), 0, user.roles, 0,
                Math.min(req.getRoles().length, user.roles.length));
		
		if(gameService.allocate(user)){
		
			String msg = localizationService.getLocalString("game.invite", new String[]{user.getUserPersist().getUsername()});
			
			ChatReqInvite chat = new ChatReqInvite();
			chat.setType(2);
			chat.setName("aaa");
			chat.setMsg(msg);
			chat.setInvite(1);
			String key = new BigInteger(130, userController.random).toString(32);
			chat.setUid(user.getId());
			chat.setKey(key);
			chat.setMap(req.getMap());
			chat.setRoles(req.getRoles());
			userController.bufuMap.put(key, chat);
			userController.addChat(chat);
			
			return true;
		}
		return false;
	}
	
	@HandlerAnno(name = "pve对战分配", cmd = "GamePveAlloc", req = GameRequest.class)
	public void pveAlloc(GameRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		user.map = req.getMap();
		if(null!=req.getRoles())
		System.arraycopy(req.getRoles(), 0, user.roles, 0,
                Math.min(req.getRoles().length, user.roles.length));
		
		gameService.pveAlloc(user);
	}
	
	@HandlerAnno(name = "邀请", cmd = "GameInvite", req = ChatReqInvite.class)
	public void invite(ChatReqInvite req) throws GeneralException {
		
	}
	
	@HandlerAnno(name = "接受邀请", cmd = "GameAccept", req = GameRequest.class)
	public void accept(GameRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		
		if(null!=req.getRoles())
		System.arraycopy(req.getRoles(), 0, user.roles, 0,
                Math.min(req.getRoles().length, user.roles.length));
		
		gameService.accept(user, req.getUid());
	}
	
	@HandlerAnno(name = "回合换位", cmd = "GameSwap", req = GameSwapRequest.class)
	public void swap(GameSwapRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		GameRoom gr = user.getGameRoom();
		if(null!=gr){
			gr.swap(user.getId(), req.getType());
		}
	}
	@HandlerAnno(name = "退出", cmd = "GameExit", req = VoidRequest.class)
	public void exit(VoidRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		GameRoom gr = user.getGameRoom();
		gr.getPlayer(user.getId()).vo.setHp(0);
		gr.end();
	}
}
