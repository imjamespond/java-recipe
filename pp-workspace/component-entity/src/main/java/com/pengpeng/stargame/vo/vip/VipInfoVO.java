package com.pengpeng.stargame.vo.vip;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-11-20
 * Time: 下午5:05
 */
@Desc("VIp信息VO")
public class VipInfoVO {
    @Desc("1表示是Vip 0表示不是")
    private int vip;
    @Desc("玩家头像")
    private String icon;
    @Desc("玩家名字")
    private String name;
    @Desc("当前地图的名字")
    private String mapName;
    @Desc("Vip特权描述")
    private String [] des;
    @Desc("剩余时间 毫秒数")
    private long surplusTime;
    @Desc("特权数值VO VipPrivilegeVO数组")
    private VipPrivilegeVO [] vipPrivilegeVOs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String[] getDes() {
        return des;
    }

    public void setDes(String[] des) {
        this.des = des;
    }

    public long getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(long surplusTime) {
        this.surplusTime = surplusTime;
    }

    public VipPrivilegeVO[] getVipPrivilegeVOs() {
        return vipPrivilegeVOs;
    }

    public void setVipPrivilegeVOs(VipPrivilegeVO[] vipPrivilegeVOs) {
        this.vipPrivilegeVOs = vipPrivilegeVOs;
    }
}
