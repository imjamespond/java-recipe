package com.pengpeng.stargame.container;

import java.io.Serializable;
import java.util.List;

import com.pengpeng.stargame.manager.IFinder;
import com.pengpeng.stargame.model.Indexable;

public interface IListContainer<Index extends Serializable, Type extends Indexable<Index>>{
	public Type getElement(Index index);
	public void addElement(Type element);
	public void removeElement(Index index);

	public List<Type> find(IFinder<Type> finder);	
}
