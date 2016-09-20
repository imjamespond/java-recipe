package com.pengpeng.stargame.container.role.impl;

import com.pengpeng.stargame.rule.role.NpcRule;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-10上午9:55
 */
public class NpcBean {
    private NpcRule rule;
    private int x;
    private int y;

    public NpcBean(NpcRule rule, int x, int y) {
        this.rule = rule;
        this.x = x;
        this.y = y;
    }

    public NpcRule getRule() {
        return rule;
    }

    public void setRule(NpcRule rule) {
        this.rule = rule;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
