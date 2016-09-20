package com.pengpeng.stargame.player.container.impl;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-10上午11:16
 */
public class TransferBean {
    private String id;
    private String mapId;
    private int type;//0入口,1出口
    private int x;
    private int y;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
}
