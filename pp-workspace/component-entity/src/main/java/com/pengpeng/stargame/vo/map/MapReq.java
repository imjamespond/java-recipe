package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-16上午11:34
 */
@Desc("地图信息请求")
public class MapReq extends BaseReq {
    @Desc("地图id")
    private String mapId;

    @Desc("传送口id")
    private String transferId;

    @Desc("pid")
    private String pid;
    @Desc("指定坐标点x")
    private int x;
    @Desc("指定坐标点y")
    private int y;
    @Desc("家族Id")
    private String familyId;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
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

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
