package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("排行信息")
public class RankUserListVO {
	@DescAnno("总页数")
	private int amount;
	@DescAnno("当前页数")
	private int page;
	@DescAnno("我的排名")
	private int myRank;
	@DescAnno("排行列表")
	private List<RankUserVO> list;

	public RankUserListVO(int amount, int page, List<RankUserVO> list) {
		super();
		this.amount = amount;
		this.page = page;
		this.list = list;
	}

	public RankUserListVO() {
		super();
	}

	public List<RankUserVO> getList() {
		return list;
	}

	public void setList(List<RankUserVO> list) {
		this.list = list;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getMyRank() {
		return myRank;
	}

	public void setMyRank(int myRank) {
		this.myRank = myRank;
	}

}
