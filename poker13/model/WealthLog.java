/**
 * 
 */
package com.chitu.poker.model;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.logs.GeneralLog;
import cn.gecko.commons.logs.LogUtils;

/**
 * 财富追踪日志
 * 
 * @author ivan
 * 
 */
public class WealthLog extends GeneralLog {

	private long playerId;
	private int money;
	private int before;
	private int after;
	private BillType billType;
	private String description;

	public WealthLog(long playerId, int money, int before, int after, BillType billType,
			String description) {
		this.playerId = playerId;
		this.money = money;
		this.before = before;
		this.after = after;
		this.billType = billType;
		this.description = description;
	}

	@Override
	public CharSequence format() {
		StringBuilder sb = new StringBuilder();
		sb.append(LogUtils.getLogTransactionalIdentity()).append(SEPARATOR);
		sb.append(playerId).append(SEPARATOR);
		sb.append(billType.getOperation()).append(SEPARATOR);
		sb.append(billType.getId()).append(SEPARATOR);
		sb.append(money).append(SEPARATOR);
		sb.append(before).append(SEPARATOR);
		sb.append(after).append(SEPARATOR);
		sb.append(description).append(SEPARATOR);
		return sb;
	}

}
