package com.metasoft.flying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.net.annotation.HandlerType;
import com.metasoft.flying.node.model.Node;
import com.metasoft.flying.node.service.RpcService;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.vo.StreamRequest;

@Controller
public class StreamController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserPersistService userPersistService;
	@Autowired
	private GameRoomService chatService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private StaticDataController staticDataCtr;

	@Autowired
	private ConnectionService connectionService;
	
	@Autowired
	private RpcService rpcService;

	@HandlerAnno(name = "清理频道", cmd = "stream.clean", req = StreamRequest.class, type = HandlerType.FORWARD)
	public void clean(StreamRequest req) throws GeneralException {
		//测试red5
		Node node = rpcService.getOnlineNodeById("media-1");
		if(null != node){
			node.cleanStream(req.getGroup());
		}
	}

	@HandlerAnno(name = "关闭连接", cmd = "stream.close", req = StreamRequest.class, type = HandlerType.FORWARD)
	public void close(StreamRequest req) throws GeneralException {
		
		Node node = rpcService.getOnlineNodeById("media-1");
		if(null != node){
			node.forceClose(String.valueOf(req.getGroup()));
		}
	}
}
