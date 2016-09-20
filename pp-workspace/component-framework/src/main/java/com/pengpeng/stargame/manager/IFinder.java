package com.pengpeng.stargame.manager;

import com.pengpeng.stargame.model.Indexable;

public interface IFinder<Type extends Indexable<?>> {
	public boolean match(int index, Type item) throws BreakException;
}
