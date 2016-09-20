package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("赠送信息")
@EventAnno(desc = "", name = "event.gift")
public class UserGiftVO {
	@DescAnno("道具id")
	private int itemId;
	@DescAnno("道具数量")
	private int itemNum;
	@DescAnno("玩家id")
	private long userId;
	@DescAnno("玩家名字")
	private String userName;
	@DescAnno("时间")
	private long date;
	@DescAnno("0未表示感谢1已经感谢")
	private int thank;

	public UserGiftVO() {

	}

	public UserGiftVO(int itemId, int itemNum, long userId, long date) {
		super();
		this.itemId = itemId;
		this.itemNum = itemNum;
		this.userId = userId;
		this.date = date;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getThank() {
		return thank;
	}

	public void setThank(int thank) {
		this.thank = thank;
	}

}
