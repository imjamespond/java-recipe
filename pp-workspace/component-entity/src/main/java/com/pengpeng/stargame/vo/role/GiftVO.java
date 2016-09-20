package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:23
 */
@Desc("领取到的礼物")
public class GiftVO {
    @Desc("赠送人")
    private String fid;
    @Desc("赠送人名称")
    private String name;
    @Desc("赠送人头像")
    private String icon;
    @Desc("物品id")
    private String itemId;
    @Desc("数量")
    private int num;
    @Desc("有效时间")
    private String validityTime;
    @Desc("留言")
    private String message;
    @Desc("类型0未领取删除,1未领取不删除,2已领取")
    private int type;
    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

	public String getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(String validityTime) {
		this.validityTime = validityTime;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
