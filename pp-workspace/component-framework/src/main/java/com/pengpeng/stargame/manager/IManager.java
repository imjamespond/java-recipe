package com.pengpeng.stargame.manager;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.pengpeng.stargame.model.Indexable;

public interface IManager<Index extends Serializable,Type extends Indexable<Index>> {
    public void addElement(Collection<Type> colls);
	public Type getElement(Index index);
	public void addElement(Type element);
	public void removeElement(Type element);
	public void removeElement(Index index);
	
	public void store();//保存数据
	public List<Type> find(IFinder<Type> finder);

    public void load();
}
