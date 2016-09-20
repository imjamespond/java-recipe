package com.chitu.poker.model;

import cn.gecko.commons.logs.GeneralLog;
import cn.gecko.commons.logs.LogUtils;

public class ExpLog extends GeneralLog {

	private long playerId;
    private int exp;
    
    private int beforeGrade;
    private int beforeExp;
    
	private int afterGrade;
	private int afterExp;
	
	private ExpType expType;
	private String description;
	
	public ExpLog(long playerId, int exp, ExpType expType,String description) {
		this.playerId = playerId;
		this.exp = exp;
		this.expType = expType;
		this.description = description;
	}
	
	@Override
	public CharSequence format() {
		StringBuilder sb = new StringBuilder();
		sb.append(LogUtils.getLogTransactionalIdentity()).append(SEPARATOR);
		sb.append(playerId).append(SEPARATOR);
		sb.append(expType.getOperation()).append(SEPARATOR);
		sb.append(expType.getId()).append(SEPARATOR);
		sb.append(exp).append(SEPARATOR);
		sb.append(beforeGrade).append(SEPARATOR);
		sb.append(beforeExp).append(SEPARATOR);
		sb.append(afterGrade).append(SEPARATOR);
		sb.append(afterExp).append(SEPARATOR);
		sb.append(description).append(SEPARATOR);
		return sb;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getBeforeGrade() {
		return beforeGrade;
	}

	public void setBeforeGrade(int beforeGrade) {
		this.beforeGrade = beforeGrade;
	}

	public int getBeforeExp() {
		return beforeExp;
	}

	public void setBeforeExp(int beforeExp) {
		this.beforeExp = beforeExp;
	}

	public int getAfterGrade() {
		return afterGrade;
	}

	public void setAfterGrade(int afterGrade) {
		this.afterGrade = afterGrade;
	}

	public int getAfterExp() {
		return afterExp;
	}

	public void setAfterExp(int afterExp) {
		this.afterExp = afterExp;
	}

	public ExpType getExpType() {
		return expType;
	}

	public void setExpType(ExpType expType) {
		this.expType = expType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
	
}
