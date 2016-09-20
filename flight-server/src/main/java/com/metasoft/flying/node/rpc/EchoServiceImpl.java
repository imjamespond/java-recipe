package com.metasoft.flying.node.rpc;

import org.springframework.stereotype.Controller;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.metasoft.flying.node.EchoProto.EchoRequest;
import com.metasoft.flying.node.EchoProto.EchoResponse;
import com.metasoft.flying.node.EchoProto.EchoService.Interface;

@Controller
public class EchoServiceImpl implements Interface {

	@Override
	public void echo(RpcController controller, EchoRequest request, RpcCallback<EchoResponse> done) {
		System.out.println(request.getPayload());
		done.run(EchoResponse.newBuilder().setPayload(request.getPayload()).build());
	}
}
