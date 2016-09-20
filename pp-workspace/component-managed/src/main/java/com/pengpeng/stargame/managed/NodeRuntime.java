package com.pengpeng.stargame.managed;

import java.io.Serializable;

/**
 * 封装节点运行时信息，比如内存信息、物理服务器负载、CPU负载信息等等。
 * @author ChenHonghong@gmail.com
 * @since 13-5-16 下午3:57
 */
public class NodeRuntime implements Serializable {

    private static final long serialVersionUID = 4225775144254386690L;

    /**
     * 已用内存（单位：M）。
     */
    private int memoryUsed;

    /**
     * 总内存（单位：M）。
     */
    private int memoryTotal;

    public int getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(int memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public int getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(int memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    @Override
    public String toString() {
        return "NodeRuntime{" +
                "memoryUsed=" + memoryUsed +
                ", memoryTotal=" + memoryTotal +
                '}';
    }
}
