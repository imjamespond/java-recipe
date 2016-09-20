package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.AppointRequest;
import com.metasoft.flying.vo.ChatBulletinVO;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

/**
 * @author james
 * 邀请controller
 */
@Controller
public class InviteController implements GeneralController {

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
	private LocalizationService localizationService;
	@Autowired
	private StaticDataService staticDataService;
	private static final int kMsgSize = 32;//must be power of 2	
	private static final ChatBulletinVO[] kMsgQueue = new ChatBulletinVO[kMsgSize];
	private static int kMsgIndex = 0;
	//private static CircularFifoQueue<ChatBulletinVO> msgQueue = new CircularFifoQueue<ChatBulletinVO>(30);

	@HandlerAnno(name = "邀请", cmd = "chat.invite", req = GeneralRequest.class)
	public void invite(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		ChatBulletinVO msg = VoFactory.getChatBulletinVO(self);
		connectionService.broadcast(GeneralResponse.newObject(msg));
		
		synchronized(kMsgQueue){
			kMsgQueue[kMsgIndex++]=msg;
			kMsgIndex&=(kMsgSize-1);
		}
	}
	
	@HandlerAnno(name = "公告列表", cmd = "chat.bulletin.list", req = GeneralRequest.class)
	public ChatBulletinVO[] list(GeneralRequest req) throws GeneralException {
		
		BaseConnection conn = RequestUtils.getCurrentConn();
		//广播一些
		for(ChatBulletinVO vo:kMsgQueue){
			if(null!=vo){
				conn.deliver(GeneralResponse.newObject(vo));
			}
		}
		ChatVO chat = new ChatVO(localizationService.getLocalString("system.first" ));	
		chat.setId(-3);
		chat.setName("");
		conn.deliver(GeneralResponse.newObject(chat));
		chat.setId(-2);
		chat.setMsg(localizationService.getLocalString("system.second" ));
		conn.deliver(GeneralResponse.newObject(chat));
		
		return null;
	}
	
	@HandlerAnno(name = "预约", cmd = "chat.appoint", req = GeneralRequest.class)
	public void appoint(AppointRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		//self.setAppoint(req.getAppoint());
	}
}