package com.pengpeng.stargame.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 */
public abstract class BaseEntity<Index extends Serializable> implements Indexable<Index>,Serializable {

	//protected boolean modifyed = false;

	public abstract Index getId();

	public abstract void setId(Index id);

    public void setKey(Index key){

    }
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
	}
}
