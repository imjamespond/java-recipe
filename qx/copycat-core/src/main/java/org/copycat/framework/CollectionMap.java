package org.copycat.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollectionMap {
	private String collectionName;
	private Map<String, String> map = new HashMap<String, String>();

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void addElement(String key, String elment) {
		map.put(key, elment);
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public String get(String key) {
		return map.get(key);
	}

	public String toString() {
		return collectionName + " " + map;
	}
}
