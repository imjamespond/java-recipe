package com.metasoft.flying.node.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.metasoft.flying.model.User;
import com.metasoft.flying.node.NodeProto.GenericResponse;
import com.metasoft.flying.node.NodeProto.IdentRequest;
import com.metasoft.flying.node.NodeProto.NodeRequest;
import com.metasoft.flying.node.NodeProto.NodeService.Interface;
import com.metasoft.flying.node.model.Node;
import com.metasoft.flying.node.net.RpcChannel;
import com.metasoft.flying.node.net.RpcUtils;
import com.metasoft.flying.node.service.RpcServerService;
import com.metasoft.flying.service.UserService;

@Controller
public class NodeServiceImpl implements Interface {
	//private static final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

	@Autowired
	private RpcServerService rpcService;
	@Autowired
	private UserService userService;
	@Override
	public void registNode(RpcController controller, NodeRequest request, RpcCallback<GenericResponse> done) {
		RpcChannel channel = RpcUtils.getRpcChannel();	
		Node node = new Node(channel);
		node.setId(request.getId());
		node.setName(request.getId());	
		node = rpcService.onSignIn(node);	
		done.run(GenericResponse.newBuilder().setOk(true).build());
	}

	@Override
	public void identifyConn(RpcController controller, IdentRequest request, RpcCallback<GenericResponse> done) {
		//logger.debug("identifyConn");
		
		try {
			User user = userService.getOnlineUserById(Long.valueOf(request.getUid()));
			if(user != null){
				done.run(GenericResponse.newBuilder().setOk(true).build());
			}
		} catch (NumberFormatException e) {
			
		}
		done.run(GenericResponse.newBuilder().setOk(false).build());
	}

/*	@Override
	public void streamStart(RpcController controller, IdentRequest request, RpcCallback<GenericResponse> done) {
		logger.debug("streamStart");
		User user = userService.getOnlineUserById(Long.valueOf(request.getUid()));
		if(user != null){
			//user.setMicophone(true);
		}
	}

	@Override
	public void streamStop(RpcController controller, IdentRequest request, RpcCallback<GenericResponse> done) {
		logger.debug("streamStop");
		User user = userService.getOnlineUserById(Long.valueOf(request.getUid()));
		if(user != null){
			//user.setMicophone(false);
		}
	}*/
}
