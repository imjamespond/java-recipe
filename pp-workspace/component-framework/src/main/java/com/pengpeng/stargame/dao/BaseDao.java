package com.pengpeng.stargame.dao;

import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseDao<Index extends Serializable,ItemType extends Indexable<Index>> {
    public void saveBean(ItemType bean);
	public void deleteBean(Index index);
	public ItemType getBean(Index index);
    public Map<String,ItemType> mGet(final Set<String> keys);

}
