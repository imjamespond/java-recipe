package com.pengpeng.stargame.dao;

import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-12下午3:35
 */
public interface ISetDao<Index extends Serializable,ItemType extends Indexable<Index>> {
    public long size(String key);
    public void insertBean(String key,ItemType bean);
    public void deleteBean(String key,ItemType bean);
    public ItemType getBean(String key,ItemType bean);
    public void updateBean(String key,ItemType bean);
    public boolean isMember(String id, ItemType bean);
}
