package com.chitu.poker.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("com.chitu.poker.model.ExpType_poker.csv")
public class PokerExpTypes {

	@DataProperties("operation:0,name:未知类型")
	public static final int NULL = 3000;
	
	@DataProperties("operation:1,name:副本通关奖励")
	public static final int INSTANCE_REWARD = 3001;
	
}
