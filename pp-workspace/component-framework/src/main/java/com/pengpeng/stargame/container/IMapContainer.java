package com.pengpeng.stargame.container;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.Indexable;

public interface IMapContainer<Index extends Serializable,Type extends Indexable<Index>>{
	
	public void init();
	public Type getElement(Index index);
	public void addElement(Type element);
	public void addElement(Collection<Type> colls);
	public void removeElement(Type element);
	public void removeElement(Index index);

	public List<Type> find(IFinder<Type> finder);

    public Collection<Type> values();

    int size();
}
