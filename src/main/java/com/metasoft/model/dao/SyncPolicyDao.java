package com.metasoft.model.dao;

import org.copycat.framework.annotation.AutoId;
import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Table;

import com.metasoft.model.GenericDao;

/**
 * 同步策略
 */
@Table("SYNC_POLICY")
public class SyncPolicyDao extends GenericDao {

	final static public String PERIOD_ONCE = "once";
	final static public String PERIOD_DAY = "daily";
	final static public String PERIOD_WEEK = "weekly";
	final static public String PERIOD_MONTH = "monthly";

	final static public String STATE_START = "running";
	final static public String STATE_STOP = "stopped";
	final static public String STATE_CANCEL = "canceled";

	final static public String DEAL_TYPE__MANUAL = "manual";
	final static public String DEAL_TYPE__AUTO = "auto";
	
	final static public String MATCH_TYPE__MANUAL = "manual";
	final static public String MATCH_TYPE__AUTO = "auto";
	
	

	@AutoId("ID")
	private String id;
	@Column("DBID")
	private String dbadressid;
	@Column("PNAME")
	private String name;
	@Column("PERIOD")
	private String period = PERIOD_ONCE;
	@Column("S_INCLUDE")
	private String include;
	@Column("MUNUAL_PATTERNS")
	private String mPatterns;//手动匹配的正则表达式
	@Column("AUTO_PATTERN")
	private String aPattern;//自动匹配的正则表达式
	@Column("DEAL_TYPE")
	private String dealType;
	@Column("STATE")
	private String state = STATE_STOP;
	@Column("CATEGORY")
	private String category;
	@Column("DESCB")
	private String desc;
	@Column("CREATOR")
	private String creator;
	@Column("DOMAIN_ID")
	private String domainid;
	@Column("CREATE_TIME")
	private long createTime;
	@Column("START_HOUR")
	private int startHour;
	@Column("START_MINUTE")
	private int startMinute;
	@Column("LATEST_EXECUTE_TIME")
	private long latestExecuteTime;//记录最近的执行时间
	@Column("MATCH_TYPE")
	private String matchType = MATCH_TYPE__MANUAL;//默认手动匹配
	
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	
	public String getMatchType() {
		return matchType;
	}
	
	public long getLatestExecuteTime() {
		return latestExecuteTime;
	}

	public void setLatestExecuteTime(long latestExecuteTime) {
		this.latestExecuteTime = latestExecuteTime;
	}

	public void setStartHour(int startHout) {
		this.startHour = startHout;
	}
	
	public int getStartHour() {
		return startHour;
	}
	
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
	
	public int getStartMinute() {
		return startMinute;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDbadressid() {
		return dbadressid;
	}

	public void setDbadressid(String dbadressid) {
		this.dbadressid = dbadressid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	

	public String getmPatterns() {
		return mPatterns;
	}

	public void setmPatterns(String mPatterns) {
		this.mPatterns = mPatterns;
	}

	public String getaPattern() {
		return aPattern;
	}

	public void setaPattern(String aPattern) {
		this.aPattern = aPattern;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getDomainid() {
		return domainid;
	}
	
	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
