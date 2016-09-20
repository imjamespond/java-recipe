package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("登录VO")
public class LoginVO {
	@DescAnno("玩家vo")
	private UserVO user;
	@DescAnno("道具配置ItemDataVO")
	private List<ItemDataVO> itemDatas;
	
	@DescAnno("道具数据ItemVO")
	private List<ItemVO> items;
	@DescAnno("好友信息")
	private List<UserLiteVO> relation;
	@DescAnno("飞行场列表")
	private List<ArenaVO> arenaList;
	
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	public List<ItemDataVO> getItemDatas() {
		return itemDatas;
	}
	public void setItemDatas(List<ItemDataVO> itemDatas) {
		this.itemDatas = itemDatas;
	}
	public List<ItemVO> getItems() {
		return items;
	}
	public void setItems(List<ItemVO> items) {
		this.items = items;
	}
	public List<UserLiteVO> getRelation() {
		return relation;
	}
	public void setRelation(List<UserLiteVO> relation) {
		this.relation = relation;
	}
	public List<ArenaVO> getArenaList() {
		return arenaList;
	}
	public void setArenaList(List<ArenaVO> arenaList) {
		this.arenaList = arenaList;
	}
}
