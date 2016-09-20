/**
 * 
 */
package com.chitu.chess.model;

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
	private int point;
    private int prestige;
	private String before;
	private String after;
	private BillType billType;
	private String description;

	public WealthLog(long playerId, int money, int point, int prestige, String before, String after, BillType billType,
			String description) {
		this.playerId = playerId;
		this.money = money;
		this.point = point;
		this.prestige = prestige;
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
		sb.append(point).append(SEPARATOR);
		sb.append(prestige).append(SEPARATOR);
		sb.append(before).append(SEPARATOR);
		sb.append(after).append(SEPARATOR);
		sb.append(description).append(SEPARATOR);
		return sb;
	}

}
