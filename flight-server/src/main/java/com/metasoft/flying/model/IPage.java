package com.metasoft.flying.model;

public interface IPage<T> {

	public abstract int getSize();

	public abstract void addItem(T t);

	public abstract void setPage(int amount,int offset);
}
