package com.pengpeng.stargame.model.room;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 时装衣柜 ，包含所有分类
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午11:37
 */
public class FashionCupboard extends BaseEntity<String> {
	// 玩家ID
	private String pid;
	private Map<String,FashionPkg> fashionPkgMap = new HashMap<String, FashionPkg>();

	public Map<String, FashionPkg> getFashionPkgMap() {
		return fashionPkgMap;
	}

	private void initFashionPkg(String type){
		if(!fashionPkgMap.containsKey(type)){
			FashionPkg fashionPkg = new FashionPkg();
			fashionPkg.setPid(pid);
			fashionPkg.setLevel(1);
			fashionPkg.setSize(30);
			fashionPkgMap.put(type,fashionPkg);
		}
	}

	public FashionPkg getFashionPkg(String type){
		this.initFashionPkg(type);
		return fashionPkgMap.get(type);
	}

	@Override
	public String getId() {
		return pid;
	}

	@Override
	public void setId(String pid) {
		this.pid = pid;
	}

	@Override
	public String getKey() {
		return pid;
	}
}
