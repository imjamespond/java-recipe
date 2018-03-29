package com.metasoft.model.dao;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

/**
 * 表修改详情
 */
@Table("SYNC_LOG_DETAIL_TABLE")
public class SyncLogDetailTableDao extends GenericDao {

	final static public String DESC_TO_ADD = "待新增";
	final static public String DESC_TO_DELETE = "待删除";
	
	final static public String DESC_HAS_ADD = "已新增";
	final static public String DESC_HAS_DELETE = "已删除";
	
	final static public String TYPE_TO_ADD = "to_create";
	final static public String TYPE_HAS_ADD = "has_create";
	final static public String TYPE_TO_DELETE = "to_delete";
	final static public String TYPE_HAS_DELETE = "has_delete";
	

	@AutoId("ID")
	private String detailId;
	@Column("LOGID")
	private String logId;
	@Column("TYPE")
	private String type;
	@Column("TSCHEMA")
	private String tschema;
	@Column("TNAME")
	private String tname;
	
	private String type_;
	
	private String remarks;//说明

	/**
	 * 新表名
	 */
	@Column("NTNAME")
	private String ntname;
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getRemarks() {
		return remarks;
	}

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
		else if (type.equals(TYPE_HAS_DELETE))
			type_ = DESC_HAS_DELETE;
	}

	public String getTschema() {
		return tschema;
	}

	public void setTschema(String tschema) {
		this.tschema = tschema;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		// 判断表名有没有带Schema，如果有，则把schema分割开
		int idx = tname.indexOf(".");

		if (idx != -1) {
			setTschema(tname.substring(0, idx));
			tname = tname.substring(idx + 1);
		}
		this.tname = tname;
	}

	public String getNtname() {
		return ntname;
	}

	public void setNtname(String ntname) {
		this.ntname = ntname;
	}

	public String getType_() {
		return type_;
	}

	public void setType_(String type_) {
		this.type_ = type_;
	}
}
