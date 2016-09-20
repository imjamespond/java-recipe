package com.pengpeng.stargame;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface ICache {

    boolean set(String key, Serializable value);

    boolean set(String key, Serializable value, Date expriedDate) ;

    boolean set(String key, Serializable value, int expirySec) ;

    <T extends Serializable> T get(String key);

    <T extends Serializable> Collection<T> getValues(Collection<String> keys);

    <T extends Serializable> Map<String, T> getBatch(Collection<String> keys);

    boolean delete(String key);
    
    public boolean add(String key, Serializable value);
    public boolean add(String key, Serializable value, Date expriedDate);
    public boolean add(String key, Serializable value, int expirySec);
}
