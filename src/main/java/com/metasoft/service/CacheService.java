package com.metasoft.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.metasoft.util.CacheData;

@Service
public class CacheService {

	private static final Logger log = LoggerFactory.getLogger(CacheService.class);
	private static ConcurrentMap<String, CacheData> cacheMap = new  ConcurrentHashMap<String, CacheData>();
	//private static Map cacheMap; 
	//private Hashtable cacheMap;//存放缓存对象
	
	private static byte[] lock = new byte[0];
	
	/**
	 * 添加缓存对象进map
	 * @param key
	 * @param data
	 */
	public void addCacheData(String key,Object data){
		synchronized(lock){
			addCacheData(key,data,true);
		}
	}
	
	
	public void removeByList(List<String> list){
		for(String s:list){
			removeCacheData(s);
		}
		
	}
	
	/**
	 * 更新缓存
	 */
	public void updateOrAddCacheData(String key,Object data){
		if(cacheMap.containsKey(key)){
			removeCacheData(key);			
		}
		addCacheData(key,data);
		
	}
	
	/**
	 * 获取缓存对象
	 * @param key
	 */
	public Object getCacheData(String key){
		synchronized(lock){
			if(key!=null){
				CacheData data = (CacheData) cacheMap.get(key);
				if(null!=data){
					return data.getData();
				}
			}
			return null;
		}
	}
	
	private void addCacheData(String key,Object data,boolean check){
		//System.out.println("内存剩余："+Runtime.getRuntime().freeMemory()/(1024L*1024));
		//虚拟内存不足的时候
		if(Runtime.getRuntime().freeMemory()<5L*1024L){
			log.warn("web缓存:内存不足,开始清理缓存");
			removeAllCacheData();
			return ;
		}else if(check&&cacheMap.containsKey(key)){
			log.warn("web缓存:key值"+key+"在缓存中重复了本次不缓存");
		}
		cacheMap.put(key, new CacheData(data));	
		log.warn("存缓存key="+key);
	}
	
	
	public void removeCacheData(String key){
		if(cacheMap!=null){
			if(cacheMap.containsKey(key)){
				cacheMap.remove(key);
			}
		}
	}
	
	public void removeAllCacheData(){
		cacheMap.clear();
	}

	
}
