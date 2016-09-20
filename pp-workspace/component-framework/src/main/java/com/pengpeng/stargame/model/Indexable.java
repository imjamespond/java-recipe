package com.pengpeng.stargame.model;

import java.io.Serializable;

public interface Indexable<Type extends Serializable> extends Serializable {
	public Type getKey();
    public void setKey(Type key);
}
