package com.james.memcached;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface BaseDao<Index extends Serializable, ItemType extends Indexable<Index>> {
	public void insertBean(ItemType bean);

	public void saveBean(ItemType bean);

	public void deleteBean(Index index);

	public ItemType getBean(Index index);

	public void updateBean(Index index, ItemType bean);

	public Map<String, ItemType> mGet(final Set<String> keys);

}
