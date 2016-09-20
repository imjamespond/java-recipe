package com.pengpeng.stargame.managed;

/**
 * 定义各个服务器类型.
 * @author ChenHonghong@gmail.com
 * @since 13-4-18 下午12:19
 */
public final class ServerType {

    /**
     * 服务器管理器.
     */
    public static final String MANAGER = "server-manager";

    /**
     * 状态服务器。
     */
    public static final String STATUS = "backend-statusserver";

    /**
     * 逻辑服务器。
     */
    public static final String LOGIC = "backend-logicserver";

    /**
     * 登录服务器。
     */
    public static final String LOGIN = "frontend-loginserver";

    /**
     * API服务器。
     */
    public static final String API = "apiserver";

    /**
     * 前端服务器，与 flash 交互。
     */
    public static final String GAMESERVER = "frontend-gameserver";

    /**
     * 后台业务服务器。
     */
    public static final String WEBADMIN = "webadmin";
}
