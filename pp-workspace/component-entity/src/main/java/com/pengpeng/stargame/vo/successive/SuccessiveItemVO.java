package com.pengpeng.stargame.vo.successive;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("奖品信息")
public class SuccessiveItemVO {
	@Desc("奖品数")
	private int num;
    @Desc("奖品名称")
    private String name;
    @Desc("奖品id")
    private String itemId;
    @Desc("奖品类型")
    private int type;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
