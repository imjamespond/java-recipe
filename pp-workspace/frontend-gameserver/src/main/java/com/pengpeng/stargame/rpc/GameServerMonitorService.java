package com.pengpeng.stargame.rpc;

import com.pengpeng.stargame.managed.NodeInfo;
import com.pengpeng.stargame.managed.SimpleClientService;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-10-24下午3:18
 */
public class GameServerMonitorService extends SimpleClientService {
    @Override
    public void notifyNodeStop(NodeInfo stoppedServer) {
        //TODO:在这里判断什么服务器被停止,需要做出什么响应
        super.notifyNodeStop(stoppedServer);
    }
}
