package com.metasoft.flying.vo;

import java.util.ArrayList;
import java.util.List;

import com.metasoft.flying.model.IPage;
import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("首页房间列表")
@EventAnno(desc = "", name = "event.roomlist")
public class RoomListVO implements IPage<RoomVO>{
	@DescAnno("总页数")
	private int amount;
	@DescAnno("当前页数")
	private int page;
	@DescAnno("排行列表")
	private List<RoomVO> list;

	public RoomListVO(int amount, int page, List<RoomVO> list) {
		this.amount = amount;
		this.page = page;
		this.list = list;
	}

	public RoomListVO() {
		this.amount = 0;
		this.page = 0;
		this.list = new ArrayList<RoomVO>();
	}

	public List<RoomVO> getList() {
		return list;
	}

	public void setList(List<RoomVO> list) {
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

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public void setPage(int amount, int offset) {
		this.amount = amount;
		this.page = offset;
	}

	@Override
	public void addItem(RoomVO item) {
		list.add(item);
	}



}
