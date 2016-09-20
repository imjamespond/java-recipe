package com.tongyi.action;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int page;
	private int total;
	private List<T> rows;
	
	public Page() {
		
	}
	public Page(int page, List<T> items) {
		this.page = page;
		this.rows = items;
	}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
