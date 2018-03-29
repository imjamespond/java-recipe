package com.metasoft.model;
public class Pattern {
	
	private String tablePattern;
	private String modelCatalogId;
	private String categoryName;
	private Object[] categoryItems;
	public String getTablePattern() {
		return tablePattern;
	}
	public void setTablePattern(String tablePattern) {
		this.tablePattern = tablePattern;
	}
	public String getModelCatalogId() {
		return modelCatalogId;
	}
	public void setModelCatalogId(String modelCatalogId) {
		this.modelCatalogId = modelCatalogId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Object[] getCategoryItems() {
		return categoryItems;
	}
	public void setCategoryItems(Object[] categoryItems) {
		this.categoryItems = categoryItems;
	}
	
	
	

}
