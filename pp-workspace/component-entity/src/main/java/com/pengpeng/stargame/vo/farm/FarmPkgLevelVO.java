package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-7 上午10:44
 */
@Desc("仓库升级材料")
public class FarmPkgLevelVO {

	@Desc("玩家ID")
	private String pid;

	@Desc("物品ID")
	private String itemId;

	@Desc("玩家物品数量")
	private int num;

	@Desc("升级所需数量")
	private int levelNum;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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

	public int getLevelNum() {
		return levelNum;
	}

	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}
}
