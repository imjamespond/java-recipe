package com.pengpeng.stargame.dao;

import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;
import java.util.List;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-12下午3:02
 */
public interface IListDao<Index extends Serializable,ItemType extends Indexable<Index>> {
    public long size(String id);
    public void insertBean(String id,ItemType bean);
    public void saveBean(String id,ItemType bean);
    public void lPush(String id,ItemType bean);
    public void rPop(String id) ;
    public void insertBean(String id,long idx,ItemType bean);
    public void saveBean(String id,long idx,ItemType bean);
    public void deleteBean(String id,long idx);
    public ItemType getBean(String id,long idx);
    public void updateBean(String id,long idx,ItemType bean);
	public List<ItemType> getList(String id);
	public List<ItemType> getList(String id,int begin,int size);
}
