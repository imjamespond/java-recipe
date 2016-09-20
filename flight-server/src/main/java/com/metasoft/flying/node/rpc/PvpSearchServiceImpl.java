package com.metasoft.flying.node.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.metasoft.flying.controller.PvpController;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.node.FlightProto.PvpBeginRequest;
import com.metasoft.flying.node.FlightProto.PvpResponse;
import com.metasoft.flying.node.FlightProto.PvpSearchRequest;
import com.metasoft.flying.node.FlightProto.PvpSearchResponse;
import com.metasoft.flying.node.FlightProto.PvpSearchService.Interface;

@Controller
public class PvpSearchServiceImpl implements Interface {
	@Value("${server.name}")
	private String serverName;
	@Autowired
	private PvpController pvpController;

	@Override
	public void search(RpcController controller, PvpSearchRequest request, RpcCallback<PvpSearchResponse> done) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin(RpcController controller, PvpBeginRequest req, RpcCallback<PvpResponse> done) {
		//System.out.println("PvpSearchServiceImpl begin");
		try {
			pvpController.begin2(req.getUidSeqList(), req.getGold());
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		done.run(PvpResponse.newBuilder().setOk(true).setMsg("handled by "+serverName).build());
	}
}
