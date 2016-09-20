package com.metasoft.flying.node.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.metasoft.flying.node.NodeProto.GenericRequest;
import com.metasoft.flying.node.NodeProto.GenericResponse;
import com.metasoft.flying.node.NodeProto.Red5Request;
import com.metasoft.flying.node.NodeProto.Red5Response;
import com.metasoft.flying.node.NodeProto.Red5Service.Interface;
import com.metasoft.flying.node.service.Red5ApplicationAdapter;

@Controller
public class Red5ServiceImpl implements Interface {
	private static final Logger logger = LoggerFactory.getLogger(Red5ServiceImpl.class);
	static{
		logger.debug("Red5ServiceImpl");
	}
	
	private Red5ApplicationAdapter adapter;
	
	public void setAdapter(Red5ApplicationAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void assignRed5(RpcController controller, GenericRequest request, RpcCallback<Red5Response> done) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceClose(RpcController controller, Red5Request request, RpcCallback<GenericResponse> done) {
		adapter.forceClose(request.getGroup());
	}

	@Override
	public void cleanStream(RpcController controller, Red5Request request, RpcCallback<GenericResponse> done) {
		adapter.streamClose(request.getGroup());
	}
}
