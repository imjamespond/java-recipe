package com.pengpeng.stargame.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.pengpeng.stargame.model.Indexable;

public abstract class HashMapManager<Index extends Serializable,Type extends Indexable<Index>> extends Observable implements IManager<Index,Type>{
	protected Map<Index,Type> items = new HashMap<Index,Type>();

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
	public void removeElement(Type element){
		items.remove(element.getKey());
	}
	public void removeElement(Index index){
		items.remove(index);
	}

    @Override
    public void addElement(Collection<Type> colls) {
        for(Type bean:colls){
            items.put(bean.getKey(),bean);
        }
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
