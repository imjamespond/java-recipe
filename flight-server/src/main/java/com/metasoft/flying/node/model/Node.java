package com.metasoft.flying.node.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.RpcCallback;
import com.metasoft.flying.controller.PvpController;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.node.EchoProto;
import com.metasoft.flying.node.EchoProto.EchoRequest;
import com.metasoft.flying.node.EchoProto.EchoResponse;
import com.metasoft.flying.node.EchoProto.EchoService;
import com.metasoft.flying.node.FlightProto.PvpSearchRequest;
import com.metasoft.flying.node.FlightProto.PvpSearchResponse;
import com.metasoft.flying.node.FlightProto.PvpSearchService;
import com.metasoft.flying.node.NodeProto.GenericResponse;
import com.metasoft.flying.node.NodeProto.Red5Request;
import com.metasoft.flying.node.NodeProto.Red5Service;
import com.metasoft.flying.node.net.RpcChannel;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.PvpInviteVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class Node {

	private static final Logger logger = LoggerFactory.getLogger(Node.class);
	static {
		logger.trace("Node");
	}
	private String id;
	private String name;

	private String ip;
	private String group;
	private long createTime;
	private long loginTime;
	private int onlineTime;

	private RpcChannel channel;
	private Red5Service.Stub red5Stub;
	private EchoService.Stub echoStub;
	private PvpSearchService.Stub pvpSearchStub;

	private boolean needUpdate;

	/**
	 * Created by UserService::getAnyPlayerById()
	 * 
	 */
	public Node() {
		needUpdate = false;
	}
	public Node(RpcChannel channel) {
		needUpdate = false;
		this.channel = channel;
		red5Stub = Red5Service.newStub(channel);
		echoStub = EchoService.newStub(channel);
		pvpSearchStub = PvpSearchService.newStub(channel);
	}
	
	/**
	 * done最后由RpcChannel的outstandings记录并完成回调
	 */
//	public void registNode() {
//        RpcCallback<NodeProto.GenericResponse> done = new RpcCallback<NodeProto.GenericResponse>() {
//            @Override
//            public void run(GenericResponse response) {
//            	System.out.println("registNode请求完成");
//            }
//        };
//        
//        NodeRequest request = NodeRequest.newBuilder().setId("hehehe client...").setType(0).build();
//        remoteNodeService.registNode(null, request, done);
//    }
	
	public void echo() {
        RpcCallback<EchoResponse> done = new RpcCallback<EchoProto.EchoResponse>() {
            @Override
            public void run(EchoResponse response) {
            	System.out.println("echo done");
            }
        };
        
        EchoRequest request = EchoRequest.newBuilder().setPayload(".......").build();
        echoStub.echo(null, request, done);
    }

	public void forceClose(String group) {
		logger.debug("rpc-> forceClose");
        RpcCallback<GenericResponse> done = new RpcCallback<GenericResponse>() {
            @Override
            public void run(GenericResponse response) {
            	logger.debug("forceClose done");
            }
        };
        
        Red5Request request = Red5Request.newBuilder().setGroup(group).build();
        red5Stub.forceClose(null, request, done);
    }
	public void cleanStream(String group) {
		logger.debug("rpc-> cleanStream");
        RpcCallback<GenericResponse> done = new RpcCallback<GenericResponse>() {
            @Override
            public void run(GenericResponse response) {
            	logger.debug("cleanStream done");
            }
        };
        
        Red5Request request = Red5Request.newBuilder().setGroup(group).build();
        red5Stub.cleanStream(null, request, done);
    }
		
	public void pvpSearch(final long uid, final int gold, final int serial) {
		logger.debug("rpc-> pvpSearch");
		final BaseConnection conn = RequestUtils.getCurrentConn();
        RpcCallback<PvpSearchResponse> done = new RpcCallback<PvpSearchResponse>() {
            @Override
            public void run(PvpSearchResponse response) {
            	GeneralResponse resp = GeneralResponse.newObject(new Long(response.getUid()));
            	resp.setSerial(serial);
            	if(uid==response.getUid()&&gold==0){
					PvpController ctrl = SpringService.getBean(PvpController.class);
            		ctrl.sendInvitation(uid );
            	}
            	conn.deliver(resp);
            	logger.debug("rpc-> pvpSearch done uid:{}",response.getUid());
            }
        };
        
        PvpSearchRequest req = PvpSearchRequest.newBuilder().setUid(uid).setGold(gold).build();
		pvpSearchStub.search(null, req, done);
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public RpcChannel getChannel() {
		return channel;
	}

	public void setChannel(RpcChannel channel) {
		this.channel = channel;
	}
}
/* auto generated code */
/* end generated */
