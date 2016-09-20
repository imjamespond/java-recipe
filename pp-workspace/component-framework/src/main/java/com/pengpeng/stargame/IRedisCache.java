package com.pengpeng.stargame;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface IRedisCache {

    boolean set(String key, Serializable value);

    boolean set(String key, Serializable value, Date expriedDate) ;

    boolean set(String key, Serializable value, int expirySec) ;

    <T extends Serializable> T get(String key);

    <T extends Serializable> Collection<T> getValues(String keys);

    <T extends Serializable> Map<String, T> getBatch(String keys);

    public void addList(String key,int index,Serializable value);
    public void addSet(String key,Serializable value);
    public void addMap(String key,String k,Serializable value);

    public <T extends Serializable> T get(String key,String k);
    public <T extends Serializable> T get(String key,int idx);

    boolean delete(String key);
    
    public boolean add(String key, Serializable value);
    public boolean add(String key, Serializable value, Date expriedDate);
    public boolean add(String key, Serializable value, int expirySec);
}
