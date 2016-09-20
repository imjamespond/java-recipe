package com.chitu.poker.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("cn.gecko.commons.data.StaticConfig_poker.csv")
public class PokerStaticConfigs {

	@DataProperties("value:500,name:背包初始容量")
	public static final int BACKPACK_INIT_CAPABILITY = 3000;

	@DataProperties("value:20,name:最多接任务数")
	public static final int ACCEPT_QUEST_MAX_COUNT = 3001;

	@DataProperties("value:100,name:最大好友数")
	public static final int MAX_FRIEND_COUNT = 3002;

	@DataProperties("value:50,name:最大黑名单数")
	public static final int MAX_BLACK_COUNT = 3003;

	@DataProperties("value:10,name:最大好友分组数")
	public static final int MAX_GROUP_COUNT = 3004;
}
