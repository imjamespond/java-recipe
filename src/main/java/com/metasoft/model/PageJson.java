package com.metasoft.model;

import java.util.List;

import org.copycat.framework.Page;

import com.metasoft.util.Attr;

public class PageJson<T> {

	private Page page;
	private List<Attr> attrs;
	private List<T> list;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Attr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<Attr> attrs) {
		this.attrs = attrs;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
