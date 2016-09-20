package com.pengpeng.stargame.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pengpeng.stargame.manager.BreakException;
import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.Indexable;

public abstract class ListContainer<Index extends Serializable, Type extends Indexable<Index>> implements IListContainer<Index, Type> {
	protected List<Type> items = new ArrayList<Type>();
	
	public int size(){
		return items.size();
	}
	public void addElement(Type element){
		items.add(element);
	}
	public void removeElement(Type element){
		items.remove(element);
	}
	public void removeElement(Index index){
		items.remove((Integer)index);
	}
	@Override
	public Type getElement(Index index) {
		return items.get((Integer)index);
	}
	@Override
	public List<Type> find(IFinder<Type> finder) {
		Collection<Type> collections = items;
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
