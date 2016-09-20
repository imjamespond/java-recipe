package com.metasoft.flying.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeanUtils;

import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.ChessApplicationVO;

public class ChessApplication {

	private String name;

	private ConcurrentMap<Long, ChessApplicationVO> appMap = new ConcurrentHashMap<Long, ChessApplicationVO>();

	public ChessApplication(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChessApplicationVO> applictionList() {
		List<ChessApplicationVO> list = new ArrayList<ChessApplicationVO>();
		for (Entry<Long, ChessApplicationVO> entry : appMap.entrySet()) {
			ChessApplicationVO vo = new ChessApplicationVO();
			BeanUtils.copyProperties(entry.getValue(), vo);
			UserService us = SpringService.getBean(UserService.class);
			User user = us.getOnlineUserById(vo.userId);
			if (null == user) {
				vo.online = 0;
			} else {
				vo.online = 1;
			}
			list.add(vo);
		}
		/*
		 * list.addAll(appMap.values()); Collections.sort(list, new
		 * Comparator<ChessApplicationVO>() {
		 * 
		 * @Override public int compare(ChessApplicationVO o1,
		 * ChessApplicationVO o2) { int diff = o2.rose - o1.rose;
		 * if(diff>0){//order by descend return 1; }else if(diff==0){
		 * if(o2.appTime - o1.appTime>0){ return 1; } } return -1; } });
		 */
		return list;
	}

	public void apply(User user, int rose, int type) {
		long now = System.currentTimeMillis();
		ChessApplicationVO app = appMap.get(user.getId());
		if (null == app) {
			app = new ChessApplicationVO();
			app.userId = user.getId();
			app.appTime = now;
			app.rose = rose;
			app.userName = user.getUserPersist().getNickname();
			appMap.put(user.getId(), app);
		} else {
			app.rose += rose;
		}
		app.type = type;
	}

	public void disable(long userId) {
		synchronized (appMap) {
			ChessApplicationVO app = appMap.get(userId);
			if (null != app) {
				app.type = 2;
				app.setRose(0);
			}
		}
	}

	public void cancel(User user) {
		synchronized (appMap) {
			ChessApplicationVO app = appMap.get(user.getId());
			if (null != app) {
				app.type = 1;
				app.setRose(0);
			}
		}
	}

	public void remove(User user) {
		synchronized (appMap) {
			appMap.remove(user.getId());
		}
	}

	public void remove(long id) {
		synchronized (appMap) {
			appMap.remove(id);
		}
	}

	public int getSize() {
		return appMap.size();
	}

}
