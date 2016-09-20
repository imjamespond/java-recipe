package com.pengpeng.stargame.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.pengpeng.stargame.manager.BreakException;
import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.Indexable;

import javax.annotation.PostConstruct;

public abstract class HashMapContainer<Index extends Serializable,Type extends Indexable<Index>> implements IMapContainer<Index,Type>,Map<Index,Type> {
	/**
	 * Logger for this class
	 */
	protected static final Logger logger = Logger.getLogger(HashMapContainer.class);

	protected Map<Index,Type> items = new ConcurrentHashMap<Index, Type>();

    @Override
    @PostConstruct
    public void init() {
        logger.debug("create "+this);
    }
    @Override
	public Collection<Type> values(){
		return items.values();
	}
	@Override
	public boolean containsKey(Object key) {
		return items.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return items.containsValue(value);
	}

	@Override
	public Set<Entry<Index, Type>> entrySet() {
		return items.entrySet();
	}

	@Override
	public Type get(Object key) {
		return items.get(key);
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public Set<Index> keySet() {
		return items.keySet();
	}

	@Override
	public Type put(Index key, Type value) {
		return items.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Index, ? extends Type> m) {
		items.putAll(m);
	}

	@Override
	public Type remove(Object key) {
		return items.remove(key);
	}	
	public void clear(){
		items.clear();
	}
	public int size(){
		return items.size();
	}
	public Type getElement(Index index){
		return items.get(index);
	}
	public void addElement(Type element){
		items.put(element.getKey(), element);
	}
	@Override
	public void addElement(Collection<Type> colls) {
		for(Type element:colls){
			items.put(element.getKey(), element);
		}
	}	

	public void removeElement(Type element){
		items.remove(element.getKey());
	}
	public void removeElement(Index index){
		items.remove(index);
	}
	@Override
	public List<Type> find(IFinder<Type> finder) {
		Collection<Type> collections = items.values();
		List<Type> list = new ArrayList<Type>();
		int index = 0;
		for(Type item:collections){
			try {
				if (finder.match(index,item))
					list.add(item);
			} catch (BreakException e) {
				list.add(item);
				break;
			}
			index++;
		}
		return list;
	}

}
