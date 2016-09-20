package com.pengpeng.stargame.util;

import java.util.List;

/**
 * 分页器
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-25 下午1:59
 */
public class Page<T> {

	protected int begin = 1; // 起始页
	protected int size = 10; // 每页记录数
	protected int maxPage = 0; // 最大页数
	protected List<T> elements;

	public Page() {
	}

	public Page(int begin, int size, int maxPage, List<T> elements) {
		this.begin = begin;
		this.size = size;
		this.maxPage = maxPage;
		this.elements = elements;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<T> getElements() {
		return elements;
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}
}
