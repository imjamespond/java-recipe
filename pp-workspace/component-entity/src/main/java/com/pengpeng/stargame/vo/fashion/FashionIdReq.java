package com.pengpeng.stargame.vo.fashion;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:37
 */
@Desc("装扮的请求")
public class FashionIdReq extends BaseReq {
    @Desc("玩家的Id")
    private String pid;
    @Desc("装扮的唯一id")
    private String fashionId;
    @Desc("装扮id")
    private String itemId;
    @Desc("装扮的Id 购物车用")
    private String [] itemIds;
    @Desc("装扮出售的数量")
    private int num;
    @Desc("服装的类型")
    private String type;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFashionId() {
        return fashionId;
    }

    public void setFashionId(String fashionId) {
        this.fashionId = fashionId;
    }

    public String[] getItemIds() {
        return itemIds;
    }

    public void setItemIds(String[] itemIds) {
        this.itemIds = itemIds;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
