package com.metasoft.model;

import com.keymobile.dataSharingMgr.interfaces.resource.DBTable;

public class ExtDBTable extends DBTable {

	private boolean isRelatedWithTableModel = false;
	
	public ExtDBTable(DBTable dbTable, boolean isRelatedWithTableModel) {
		super(dbTable.getName());
		super.setDBTableId(dbTable.getDBTableId());
		super.setColumns(super.getColumns());
		this.isRelatedWithTableModel = isRelatedWithTableModel;
	}
	
	public boolean isRelatedWithTableModel() {
		return isRelatedWithTableModel;
	}
	
	public void setIsRelatedWithTableModel(boolean isRelatedWithTableModel) {
		this.isRelatedWithTableModel = isRelatedWithTableModel;
	}
	
}
