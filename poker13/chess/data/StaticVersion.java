package com.chitu.chess.data;

import java.util.Map;

import cn.gecko.commons.data.StaticDataManager;

public class StaticVersion {

	// 编号
	private int id;
	// 版本
	private String version;
	// 下载路径
	private String url;

	public static StaticVersion get(int id) {
		return StaticDataManager.getInstance().get(StaticVersion.class, id);
	}
	
	public static Map<Integer,StaticVersion> getAll() {
		return StaticDataManager.getInstance().getMap(StaticVersion.class);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
