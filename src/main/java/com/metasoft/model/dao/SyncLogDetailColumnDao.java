package com.metasoft.model.dao;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

/**
 * 字段修改详情
 */
@Table("SYNC_LOG_DETAIL_COLUMN")
public class SyncLogDetailColumnDao extends GenericDao {

	final static public String DESC_TO_ADD = "待新增";
	final static public String DESC_HAS_ADD = "已新增";
	final static public String DESC_TO_DELETE = "待删除";
	final static public String DESC_HAS_DELETE = "已删除";
	final static public String DESC_HAS_UPDATE = "已更新";
	final static public String DESC_TO_UPDATE = "待更新";
	
	final static public String TYPE_TO_ADD = "to_create";
	final static public String TYPE_HAS_ADD = "has_create";
	final static public String TYPE_TO_DELETE = "to_delete";
	final static public String TYPE_HAS_DELETE = "has_delete";
	final static public String TYPE_TO_UPDATE = "to_update";
	final static public String TYPE_HAS_UPDATE = "has_update";
	

	@AutoId("ID")
	private String detailId;
	@Column("LOG_ID")
	private String logId;

	@Column("TYPE")
	private String type;

	/**
	 * 表名
	 */
	@Column("TNAME")
	private String tName;
	/**
	 * Schema名
	 */
	@Column("SNAME")
	private String sName;

	/**
	 * 字段名
	 */
	@Column("NAME")
	private String name;
	@Column("SQL_DATA_TYPE")
	private Integer sqlDataType;
	@Column("SIZE")
	private Integer size;
	@Column("DECIMAL_DIGIT")
	private Integer decimalDigit;
	@Column("ORDINALPOSITION")
	private Integer ordinalposition;

	@Column("NNAME")
	private String nName;
	@Column("NSQL_DATA_TYPE")
	private Integer nSqlDataType;
	@Column("NSIZE")
	private Integer nSize;
	@Column("NDECIMAL_DIGIT")
	private Integer nDecimalDigit;
	
	//中文描述
	private String type_;

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		if(type.equals(TYPE_TO_ADD))
			type_ = DESC_TO_ADD;
		else if(type.equals(TYPE_HAS_ADD))
			type_ = DESC_HAS_ADD;
		else if(type.equals(TYPE_TO_DELETE))
			type_ = DESC_TO_DELETE;
		else if(type.equals(TYPE_HAS_DELETE))
			type_ = DESC_HAS_DELETE;
		else if(type.equals(TYPE_TO_UPDATE))
			type_ = DESC_TO_UPDATE;
		else if(type.equals(TYPE_HAS_UPDATE))
			type_ = DESC_HAS_UPDATE;
			
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		// 判断表名有没有带Schema，如果有，则把schema分割开
		int idx = tName.indexOf(".");

		if (idx != -1) {
			setsName(tName.substring(0, idx));
			tName = tName.substring(idx + 1);
		}
		this.tName = tName;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSqlDataType() {
		return sqlDataType;
	}

	public void setSqlDataType(Integer sqlDataType) {
		this.sqlDataType = sqlDataType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getDecimalDigit() {
		return decimalDigit;
	}

	public void setDecimalDigit(Integer decimalDigit) {
		this.decimalDigit = decimalDigit;
	}

	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}

	public Integer getnSqlDataType() {
		return nSqlDataType;
	}

	public void setnSqlDataType(Integer nSqlDataType) {
		this.nSqlDataType = nSqlDataType;
	}

	public Integer getnSize() {
		return nSize;
	}

	public void setnSize(Integer nSize) {
		this.nSize = nSize;
	}

	public Integer getnDecimalDigit() {
		return nDecimalDigit;
	}

	public void setnDecimalDigit(Integer nDecimalDigit) {
		this.nDecimalDigit = nDecimalDigit;
	}

	public String getType_() {
		return type_;
	}

	public void setType_(String type_) {
		this.type_ = type_;
	}
	
	public void setOrdinalposition(Integer ordinalposition) {
		this.ordinalposition = ordinalposition;
	}
	
	public Integer getOrdinalposition() {
		return ordinalposition;
	}

}
