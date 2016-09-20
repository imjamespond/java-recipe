package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * 农场id请求
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-26下午4:04
 */
@Desc("农场")
public class FarmIdReq extends BaseReq {
    @Desc("玩家id,好友id")
    private String pid;
    @Desc("好友id")
    private String fid;
    @Desc("订单id")
    private String oid;
    @Desc("农场id")
    private String farmId;
    @Desc("农田id")
    private String fieldId;
    @Desc("物品id")
    private String itemId;
    @Desc("数量")
    private int num;
    @Desc("种子的Id  种植的时候 用")
    private String seedId;
    @Desc("1 普通完成订单  2表示特快货运")
    private int type;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSeedId() {
        return seedId;
    }

    public void setSeedId(String seedId) {
        this.seedId = seedId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
