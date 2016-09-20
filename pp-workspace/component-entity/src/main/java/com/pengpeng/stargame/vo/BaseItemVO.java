package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:38
 */
@Desc("物品")
public class BaseItemVO {
	@Desc("玩家ID")
	private String pid;

	// 如果叠加达到上限 ,新增一个key做区分,代表要新开一个格子放物品
	@Desc("物品key/格子唯一编号")
	private String grid;

	@Desc("物品ID")
	private String itemId;

	@Desc("物品叠加数量")
	private int num;

	@Desc("物品类型")
	private String itemtype;

    @Desc("物品名字")
    private String name;

    @Desc("有效期")
    private long  validityDate;


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
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

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(long validityDate) {
        this.validityDate = validityDate;
    }
}
