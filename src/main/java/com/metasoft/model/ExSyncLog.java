package com.metasoft.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.keymobile.common.persistence.metadata.Attribute;
import com.keymobile.dataSharingMgr.interfaces.resource.DBTable;
import com.metasoft.model.dao.SyncLogDao;
import com.metasoft.model.dao.SyncLogDetailColumnDao;
import com.metasoft.model.dao.SyncLogDetailTableDao;

public class ExSyncLog extends SyncLogDao {
	private String state_;
	private String policyName;
	private String dbAddressName;
	private Map<String,DBTable> dbTables;
	private List<SyncLogDetailTableDao> tables;
	private List<SyncLogDetailColumnDao> columns;
	
	public void setDbTables(Map<String, DBTable> dbTables) {
		this.dbTables = dbTables;
	}
	
	public Map<String, DBTable> getDbTables() {
		return dbTables;
	}

	public List<SyncLogDetailTableDao> getTables() {
		if (tables == null) {
			tables = new ArrayList<SyncLogDetailTableDao>();
		}
		return tables;
	}

	public void setTables(List<SyncLogDetailTableDao> tables) {
		this.tables = tables;
	}

	public List<SyncLogDetailColumnDao> getColumns() {
		if (columns == null) {
			columns = new ArrayList<SyncLogDetailColumnDao>();
		}
		return columns;
	}

	public void setColumns(List<SyncLogDetailColumnDao> columns) {
		this.columns = columns;
	}

	@Attribute(desc = "策略名称", prior = 2)
	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	@Attribute(desc = "数据资源名称", prior = 3)
	public String getDbAddressName() {
		return dbAddressName;
	}

	public void setDbAddressName(String dbAddressName) {
		this.dbAddressName = dbAddressName;
	}

	@Attribute(desc = "日志ID", prior = 1)
	@Override
	public String getLogid() {
		return super.getLogid();
	}

	@Attribute(desc = "更新时间", prior = 4)
	@Override
	public String getCreateTime() {
		return super.getCreateTime();
	}

	@Attribute(desc = "操作人", prior = 5)
	@Override
	public String getDealUser() {
		return super.getDealUser();
	}

	@Attribute(desc = "状态", prior = 6)
	public String getState_() {
		return state_;
	}

	@Override
	public void setState(String state) {
		super.setState(state);
		if (this.getState().equals(SyncLogDao.STATE_SYNCING))
			this.state_ = "对比中";
		else if (this.getState().equals(SyncLogDao.STATE_DEALING))
			this.state_ = "对比结束待处理";
		else if (this.getState().equals(SyncLogDao.STATE_CANCEL))
			this.state_ = "对比撤销";
		else if (this.getState().equals(SyncLogDao.STATE_DONE))
			this.state_ = "对比结束";
		else if (this.getState().equals(SyncLogDao.STATE_FAIL))
			this.state_ = "对比失败";
	}
}
