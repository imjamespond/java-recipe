package com.pengpeng.stargame.rpc;

/**
 * 后台服务,接收和处理所有的请求
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午4:33
 */
public interface IBackendService {

    public String testConnecting();
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
    public RpcResult process(String code,Session session,String json) throws Exception;
}
