package com.metasoft.model;

import com.keymobile.dataSharingMgr.interfaces.resource.TableModelColumn;

public class TableModelColumnWrapper {

	public String name;
	public int sqlDataType;
	public int size;
	public int decimalDigit;
	public int charOctetLength;
	public String nullable;
	public int ordinalPosition;
	public String remarks; 
	
	public String dbColumnId;
	public String nameInSource;
	
	public TableModelColumnWrapper(TableModelColumn tableModelColumn) {
		this.name = tableModelColumn.getName();
		this.sqlDataType = tableModelColumn.getSqlDataType();
		this.size = tableModelColumn.getSize();
		this.decimalDigit = tableModelColumn.getDecimalDigit();
		this.charOctetLength = tableModelColumn.getCharOctetLength();
		this.nullable = tableModelColumn.getNullable();
		this.ordinalPosition = tableModelColumn.getOrdinalPosition();
		this.remarks = tableModelColumn.getRemarks();
		
		dbColumnId = tableModelColumn.getDBColumnId();
		nameInSource = tableModelColumn.getNameInSource();
	}
	
	
}
