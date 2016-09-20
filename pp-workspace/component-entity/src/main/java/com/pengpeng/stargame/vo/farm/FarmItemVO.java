package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * 物品
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-27 下午4:20
 */
@Desc("物品")
public class FarmItemVO {

	@Desc("玩家ID")
	private String pid;

	// 如果叠加达到上限 ,新增一个key做区分,代表要新开一个格子放物品
	@Desc("物品key")
	private String id;

	@Desc("物品ID")
	private String itemId;

	@Desc("物品叠加数量")
	private int num;

	@Desc("物品类型")
	private int subType;

    @Desc("物品上架价格")
    private int shelfPrice;

    @Desc("有效期")
    private long  validityDate;

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

    public long getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(long validityDate) {
        this.validityDate = validityDate;
    }

    public int getShelfPrice() {
        return shelfPrice;
    }

    public void setShelfPrice(int shelfPrice) {
        this.shelfPrice = shelfPrice;
    }
}
