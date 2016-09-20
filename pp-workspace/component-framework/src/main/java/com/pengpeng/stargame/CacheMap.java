package com.pengpeng.stargame;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheMap implements ICache{

    private ConcurrentMap<String, Serializable>  cache=new ConcurrentHashMap<String,Serializable>();
    @Override
    public boolean set(String key, Serializable value) {
        Serializable o= cache.put(key,value);
        return null!=o;
    }

    @Override
    public boolean set(String key, Serializable value, Date expriedDate) {
        Serializable o= cache.put(key,value);
        return null!=o;  
    }

    @Override
    public boolean set(String key, Serializable value, int expirySec) {
        Serializable o= cache.put(key,value);
        return null!=o;  
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return  (T)cache.get(key);
    }
    @Override
    public <T extends Serializable> Collection<T> getValues(Collection<String> keys) {
        List result=new ArrayList();
        for(String key:keys){
            result.add(cache.get(key));
        }
        return result;
    }
    @Override
    public <T extends Serializable> Map<String, T> getBatch(Collection<String> keys) {
       Map result=new HashMap();
        for(String key:keys){
            result.put(key,cache.get(key));
        }
        return result; 
    }
    @Override
    public boolean delete(String key) {
        cache.remove(key);
        return true;
    }

	@Override
	public boolean add(String key, Serializable value) {
		Serializable o= cache.put(key,value);
        return null!=o;
	}

	@Override
	public boolean add(String key, Serializable value, Date expriedDate) {
		Serializable o= cache.put(key,value);
        return null!=o;
	}

	@Override
	public boolean add(String key, Serializable value, int expirySec) {
		Serializable o= cache.put(key,value);
        return null!=o;
	}
}
