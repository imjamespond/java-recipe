package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Arrays;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:46
 */
@Desc("仓库信息")
public class BasePkgVO {

	@Desc("玩家ID")
	private String pid;

	@Desc("仓库等级")
	private int level;

	@Desc("仓库容量")
	private int size;



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




}
