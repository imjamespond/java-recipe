package com.james.memcached;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.rubyeye.xmemcached.GetsResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.james.commons.data.DaoAnnotation;
import com.james.commons.utils.AnnotationUtils;

public abstract class XmemcachedDao<ItemType extends Indexable<String>>
		implements BaseDao<String, ItemType> {

	@Autowired
	private XmemcachedDB db;

	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
			.create();

	public abstract Class<ItemType> getClassType();

	protected String getKey(String index) {
		String prefix = AnnotationUtils
				.getAnno(getClass(), DaoAnnotation.class).prefix();
		String key = prefix + index;
		return key;
	}

	@Override
	public void insertBean(ItemType bean) {
		final String key = getKey(bean.getKey());
		db.set(key, gson.toJson(bean));
	}

	@Override
	public void saveBean(ItemType bean) {
		final String key = getKey(bean.getKey());
		db.set(key, gson.toJson(bean));
	}

	@Override
	public void deleteBean(String index) {
		final String key = getKey(index);
		db.delete(key);

	}

	@Override
	public ItemType getBean(String index) {
		final String key = getKey(index);
		String obj = db.get(key);
		return gson.fromJson(obj, getClassType());
	}

	@Override
	public void updateBean(String index, ItemType bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, ItemType> mGet(Set<String> keys) {
		Map<String, GetsResponse<String>> map = db.gets(keys);
		Map<String, ItemType> newMap = new HashMap<String, ItemType>();
		Iterator<Entry<String, GetsResponse<String>>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, GetsResponse<String>> entry = it.next();
			newMap.put(entry.getKey(), gson.fromJson(entry.getValue().getValue(),getClassType()));
		}
		return newMap;
	}

}