package com.pengpeng.stargame.dao;

import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-12下午3:26
 */
public interface IMapDao<Index extends Serializable,ItemType extends Indexable<Index>> {
    public long size(String key);
	public long maxPage(String key,int size);
    public void insertBean(String key,ItemType bean);
    public void saveBean(String key,ItemType bean);
    public void deleteBean(String key,Index index);
    public ItemType getBean(String key,Index index);
    public void updateBean(String key,ItemType bean);
	public HashMap<Index,ItemType> findAll(String key);
	public HashMap<Index,ItemType> findPage(String key,int begin,int size);

    public List<ItemType> find(String key,IFinder<ItemType> finder);
}
