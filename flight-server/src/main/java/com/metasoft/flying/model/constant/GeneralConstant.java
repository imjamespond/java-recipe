package com.metasoft.flying.model.constant;

public class GeneralConstant {
	public static final int TYPE_ROSE = 1;// 玫瑰
	public static final int TYPE_GEM = 2;// 钻石
	public static final int TYPE_CREDIT = 3;//积分
	/**
	 * 性别
	 */
	public static final int MALE = 1;
	public static final int FEMALE = 2;
	/**
	 * 关系类型
	 */
	public static final int RELATION_FOLLOW = 0;
	public static final int RELATION_BLACK = 1;

	/**
	 * 踢出房间
	 * RoomUserVO::state
	 */
	public static final int KICK_OUT = 2;
	
	/**
	 * 每天5点时间偏差
	 */
	public static final long EXPIRE_TIME_OFFSET = 5*3600*1000l;
	
	/**
	 * 检索房间类型
	 */
	public static final int ROOM_INSIDE = 1;
	public static final int ROOM_GIRL = 2;
	
	public static final int MATCH_LIMIT = 3;
	
	/**
	 * 队列限制
	 */
	public static final int ONLINE_LIMIT = 10000;
	public static final int RELATION_LIMIT = 200;
	public static final int APPLY_LIMIT = 50;
	public static final int GIFT_LIMIT = 50;
	
	public static final long SINGLE_DAY = 24*3600*1000l;
	public static final int HOUR12 = 12;
	public static final int HOUR20 = 20;
	public static final int HOUR21 = 21;
	public static final long HOUR9_MILLIS = 12*3600*1000l;
	public static final long HOUR15_MILLIS = 21*3600*1000l;
	
	public static final int ROOM_LIST_SIZE = 256;
	
	public static final int GTYPE_NORMAL = 0;
	public static final int GTYPE_RP = 4;
	public static final int GTYPE_NPC = 3;
	public static final int GTYPE_MATCH = 2;
	public static final int GTYPE_PVE = 5;
	public static final int GTYPE_PVP = 6;
}
