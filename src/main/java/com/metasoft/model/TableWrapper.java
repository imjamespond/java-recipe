package com.metasoft.model;

import com.keymobile.metadataServices.interfaces.Table;

public class TableWrapper extends Table {
	
	protected boolean isAddable = true;
	
	public TableWrapper(Table table, boolean isAddable) {
		super(table.getName(), table.getRemarks(), table.getTypeName(), null, null, table.getSchema(), table.getSelfRefColName());
		this.isAddable = isAddable;
	}

}
