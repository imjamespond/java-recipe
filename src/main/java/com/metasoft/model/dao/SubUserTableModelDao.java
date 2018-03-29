package com.metasoft.model.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;
//


import org.springframework.jdbc.core.RowMapper;

import com.metasoft.model.GenericDao;

@Table("SUB_USER_TABLEMODEL")
public class SubUserTableModelDao extends GenericDao implements RowMapper<SubUserTableModelDao>, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int SELECT_FLAG = 1;
	
	public static final int UNSELECT_FLAG = 0;
	
	@Column("USER_ID")
	private String userId;
	
	@Column("TMODEL_ID")
	private String tmodelId;
	
	@Column("TMODEL_NAME")
	private String tmodelName; //指标模型名称
	
	@Column("DATA_CYCLE")
	private String dataCycle; //收据周期 1:日  2:月 3:周 4:季度 5:半年 6:年
	
	@Column("SELECT_FLAG")
	private Integer selectFlag; //精选标志 0:未选 1:选中
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTmodelId() {
		return tmodelId;
	}

	public void setTmodelId(String tmodelId) {
		this.tmodelId = tmodelId;
	}

	public String getTmodelName() {
		return tmodelName;
	}

	public void setTmodelName(String tmodelName) {
		this.tmodelName = tmodelName;
	}

	public String getDataCycle() {
		return dataCycle;
	}

	public void setDataCycle(String dataCycle) {
		this.dataCycle = dataCycle;
	}

	public void setSelectFlag(Integer selectFlag) {
		this.selectFlag = selectFlag;
	}
	
	public Integer getSelectFlag() {
		return selectFlag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static int getUnselectFlag() {
		return UNSELECT_FLAG;
	}

	@Override
	public SubUserTableModelDao mapRow(ResultSet rs, int rowNum) throws SQLException {
		SubUserTableModelDao modelDao = new SubUserTableModelDao();
		modelDao.setDataCycle(rs.getString("DATA_CYCLE"));
		modelDao.setSelectFlag(rs.getInt("SELECT_FLAG"));
		modelDao.setTmodelId(rs.getString("TMODEL_ID"));
		modelDao.setTmodelName(rs.getString("TMODEL_NAME"));
		modelDao.setUserId(rs.getString("USER_ID"));
		return modelDao;
	}

	
}
