package com.metasoft.model;
import java.util.List;

import com.keymobile.common.persistence.metadata.Attribute;
import com.metasoft.model.dao.SyncPolicyDao;

public class ExSyncPolicy extends SyncPolicyDao {

	public String categoryName;
	public String dbaddressName;
	public String state_;
	public String period_;
	public String displayTime;//04:10
	public List<Pattern> patterns;
	
	@Override
	public void setState(String state){
		super.setState(state);
		if(this.getState().equals(SyncPolicyDao.STATE_START))
			this.state_ = "启动";
		else if(this.getState().equals(SyncPolicyDao.STATE_STOP))
			this.state_ = "停止";
		else if(this.getState().equals(SyncPolicyDao.STATE_CANCEL))
			this.state_ = "撤销";
	}
	@Override
	public void setPeriod(String period) {
		super.setPeriod(period);
		if(this.getPeriod().equals(SyncPolicyDao.PERIOD_ONCE))
			this.period_ = "一次";
		else if(this.getPeriod().equals(SyncPolicyDao.PERIOD_DAY))
			this.period_ = "每日";
		else if(this.getPeriod().equals(SyncPolicyDao.PERIOD_WEEK))
			this.period_ = "每周";
		else if(this.getPeriod().equals(SyncPolicyDao.PERIOD_MONTH))
			this.period_ = "每月";
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Attribute(desc = "数据资源库", prior = 3)
	public String getDbaddressName() {
		return dbaddressName;
	}

	public void setDbaddressName(String dbaddressName) {
		this.dbaddressName = dbaddressName;
	}
	
	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}
	
	public List<Pattern> getPatterns() {
		return patterns;
	}
	
	public void setDisplayTime(int startHour ,int startMinute) {
		String hour_ = String.valueOf(startHour);
		String minute_ = String.valueOf(startMinute);
		if (startHour<10) {
			hour_ = "0"+String.valueOf(startHour);
		}
		if (startMinute<10) {
			minute_ = "0"+String.valueOf(startMinute);
		}
		this.displayTime = hour_+":"+minute_;
	}
	
	@Attribute(desc = "时间点", prior = 8)
	public String getDisplayTime() {
		return 	displayTime;
	}

	@Attribute(desc = "策略ID", prior = 1)
	@Override
	public String getId() {
		return super.getId();
	}

	@Attribute(desc = "策略名称", prior = 2)
	@Override
	public String getName() {
		return super.getName();
	}

	@Attribute(desc = "更新频率", prior = 4)
	public String getPeriod_() {
		return period_;
	}

	@Attribute(desc = "schema同步范围", prior = 5)
	
	@Override
	public String getInclude() {
		return super.getInclude();
	}

	@Override
	public String getmPatterns() {
		return super.getmPatterns();
	}

	@Attribute(desc = "状态", prior = 7)
	public String getState_() {
		return this.state_;
	}
	
	public static void main(String[] args) {
		int xx = 1;
		String x = "";
		x = "0"+String.valueOf(xx);
		System.out.println(x);
		
	}
	
}
