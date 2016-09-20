package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-27 上午11:04
 */
@Desc("玩家仓库信息")
@EventAnnotation(name="event.farm.pkg.update",desc="农场仓库数据更新")
public class FarmPkgVO {

	@Desc("玩家ID")
	private String pid;

	@Desc("仓库等级")
	private int level;

	@Desc("仓库容量")
	private int size;

	@Desc("仓库物品集合")
	private FarmItemVO [] farmItemVO;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public FarmItemVO[] getFarmItemVO() {
		return farmItemVO;
	}

	public void setFarmItemVO(FarmItemVO[] farmItemVO) {
		this.farmItemVO = farmItemVO;
	}
}
