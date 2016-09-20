package com.chitu.chess.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("cn.gecko.commons.data.StaticConfig_chess.csv")
public class ChessStaticConfigs {

	@DataProperties("value:500,name:背包初始容量")
	public static final int BACKPACK_INIT_CAPABILITY = 3000;

	@DataProperties("value:20,name:最多接任务数")
	public static final int ACCEPT_QUEST_MAX_COUNT = 3001;

}
