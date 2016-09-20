package com.chitu.poker.model;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.logs.GeneralLog;
import cn.gecko.commons.logs.LogUtils;

/**
 * 金币追踪日志
 * @author open
 *
 */
public class PointLog extends GeneralLog {

	private long playerId;
	private int point;
	private int before;
	private int after;
	private BillType billType;
	private String description;

	public PointLog(long playerId, int point, int before, int after, BillType billType,String description) {
		this.playerId = playerId;
		this.point = point;
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
		sb.append(point).append(SEPARATOR);
		sb.append(before).append(SEPARATOR);
		sb.append(after).append(SEPARATOR);
		sb.append(description).append(SEPARATOR);
		return sb;
	}

}
