package com.metasoft.flying.service.net;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.metasoft.flying.net.BaseGroup;


public class GroupService<T extends BaseGroup> {
	protected ConcurrentMap<String, T> groupMap = new ConcurrentHashMap<String, T>();

	public void resetGroup() {
		groupMap = new ConcurrentHashMap<String, T>();
	}
	
	public T getGroup(String name) {
		T group = (T) this.groupMap.get(name);
		return group;
	}

	public void registerGroup(T group) {
		this.groupMap.put(group.getName(), group);
	}

	public void unregisterGroup(String name) {
		this.groupMap.remove(name);
	}
	public int getGroupSize() {
		return groupMap.size();
	}
	public Set<Entry<String, T>> getEntrySet(){
		return groupMap.entrySet();
	}
}