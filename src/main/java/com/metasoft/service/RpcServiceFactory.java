package com.metasoft.service;

import com.keymobile.DBServices.interfaces.DBService;
import com.keymobile.WorkFlow.interfaces.ApplFlowService;
import com.keymobile.common.transport.client.RpcProxy;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.metadataServices.interfaces.MDService;

public class RpcServiceFactory {
	
    public static DataSharingMgrService GetDataSharingMgrService(String dataSharingMgrServiceAddress) {
		RpcProxy rpcProxy = new RpcProxy(dataSharingMgrServiceAddress);
		return rpcProxy.create(DataSharingMgrService.class);
    }

	public static MDService GetMdService(String metadataServiceAddress) {
		RpcProxy rpcProxy = new RpcProxy(metadataServiceAddress);
		return rpcProxy.create(MDService.class);
	}
	
	public static DBService GetDBService(String dbServiceAddress) {
		RpcProxy rpcProxy = new RpcProxy(dbServiceAddress);
		return rpcProxy.create(DBService.class);
	}
	
	public static ApplFlowService GetApplFlowService(String applFlowServiceAddress) {
		RpcProxy rpcProxy = new RpcProxy(applFlowServiceAddress);
		return rpcProxy.create(ApplFlowService.class);
	}
	
}
