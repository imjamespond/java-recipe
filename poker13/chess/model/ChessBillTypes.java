package com.chitu.chess.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("cn.gecko.commons.data.BillType_chess.csv")
public class ChessBillTypes {

	@DataProperties("operation:0,name:未知类型")
	public static final int NULL = 5000;
	

	
	@DataProperties("operation:0,name:每局变动")
	public static final int GAME = 5002;	
	
	@DataProperties("operation:0,name:每局抽取")
	public static final int TAX = 5003;
	
	@DataProperties("operation:0,name:任务")
	public static final int MISSION = 5004;

	@DataProperties("operation:0,name:GM")
	public static final int GM = 5005;
	
	@DataProperties("operation:0,name:购买")
	public static final int BUY = 5001;
	@DataProperties("operation:0,name:付钱")
	public static final int PAY = 5006;	
	
	@DataProperties("operation:0,name:比赛奖励")
	public static final int MATCH = 5007;
	
	
	
	@DataProperties("operation:1,name:神州行充值卡充值")
	public static final int PAY_CARD_INC_RMB = 5100;
	
	@DataProperties("operation:1,name:短信支付充值")
	public static final int PAY_MESSAGE_INC_RMB = 5101;
}
