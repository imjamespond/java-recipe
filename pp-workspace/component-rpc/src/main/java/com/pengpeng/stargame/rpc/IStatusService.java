package com.pengpeng.stargame.rpc;

/**
 * 状态服务器使用的rpc接口,和IBackendService接口一样
 * 为了区别各服务器的接口,避免概念上的混乱,做两套一样的接口,不同服务器使用不同名称的Service接口
 * 比如:后台服务器使用IBackendService 接口和实现,状态服务器使用 IStatusService接口和实现
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午4:33
 */
public interface IStatusService {
    /**
     * 后台发布的服务,一般是被前台服务器调用
     * JSON result:
     * <ul>
     *     <li>serverid : </li>
     * </ul>
     * @param code
     * @param session
     * @param json
     * @return
     */
    public RpcResult process(String code, Session session, String json) throws Exception;

}
