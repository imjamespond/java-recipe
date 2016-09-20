package com.pengpeng.stargame.managed;

/**
 * 定义服务器节点（实例）的运行状态。
 * <p>状态说明：</p>
 * <ul>
 *     <li>正常 - 正常提供服务</li>
 *     <li>停止 - 服务器实例失去响应：1、实例自已关闭，2、管理器发现该实例太久没发状态过来，3、管理器关闭该实例</li>
 *     <li>暂停 - 由管理器执行，必须只能由服务器管理器恢复为正常状态</li>
 * </ul>
 *
 * @author ChenHonghong@gmail.com
 * @since 13-4-16 下午4:07
 */
public enum NodeStatus {

    ONSERVICE("onservice", "正常"),
    PAUSED("paused", "暂停"),
    STOPPED("stopped", "停止"),
    SHUTDOWN("shutdown", "关机"),
    EXCEPTION("exception", "异常"),
    BUSY("busy", "繁忙"),
    STOPPING("stopping", "正在停止"),
    STARTING("starting", "正在启动");

    private String code;

    private String name;

    // 构造方法
    private NodeStatus(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + "";
    }


}
