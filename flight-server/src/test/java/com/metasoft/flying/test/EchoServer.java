package com.metasoft.flying.test;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.metasoft.flying.node.EchoProto;
import com.metasoft.flying.node.EchoProto.EchoResponse;
import com.metasoft.flying.node.EchoProto.EchoRequest;
import com.metasoft.flying.node.EchoProto.EchoService.Interface;
import com.metasoft.flying.node.service.RpcServerService;

public class EchoServer {

    public static void main(String[] args) {
        RpcServerService server = new RpcServerService();
        server.registerService(EchoProto.EchoService.newReflectiveService(new Interface() {
            @Override
            public void echo(RpcController controller, EchoRequest request, RpcCallback<EchoResponse> done) {
                done.run(EchoResponse.newBuilder().setPayload(request.getPayload()).build());
            }
        }));
        server.start(8888);
    }
}
