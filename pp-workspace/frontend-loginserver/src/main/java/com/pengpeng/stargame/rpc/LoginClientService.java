package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.SimpleClientService;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-10-24下午3:25
 */
public class LoginClientService extends SimpleClientService {
    @Override
    public void notifyNodeStop(NodeInfo stoppedServer) {
        super.notifyNodeStop(stoppedServer);
    }
}
