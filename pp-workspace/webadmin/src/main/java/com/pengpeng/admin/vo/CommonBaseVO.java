package com.pengpeng.admin.vo;


import com.pengpeng.framework.PageSelector;

/**
 * 公用的基本VO.
 * 
 * @author: xinhui.shen@m-time.com
 * @since: 2010-7-12 下午19:09:31
 * @version 1.0
 */
public class CommonBaseVO {
	
	private PageSelector selector = new PageSelector();//分页组件
	
	 /**
     * 开始取记录行号
     */
	private Integer startRow;

    /**
     * 获取记录数目
     */
    private Integer fetchSize;
    
    /**
     * 传多个ID 不同的ID之间用逗号','隔开  比如100,101
     */
    private String strId;
    
	public Integer getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(Integer fetchSize) {
		this.fetchSize = fetchSize;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	
	public PageSelector getSelector() {
		return selector;
	}

	public void setSelector(PageSelector selector) {
		this.selector = selector;
	}

	public String getStrId() {
		return strId;
	}
	
	public void setStrId(String strId) {
		this.strId = strId;
	}
	
	@Override
	public String toString() {
		 return "CommonBaseVO{ startRow="+ startRow + ",fetchSize="+ fetchSize + ",selector=" + selector + ",strId=" + strId + "}";
	}
}
