package com.chitu.poker.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("cn.gecko.commons.data.BillType_poker.csv")
public class PokerBillTypes {

	@DataProperties("operation:0,name:未知类型")
	public static final int NULL = 3000;

	@DataProperties("operation:1,name:出售宠物所得")
	public static final int SELL_PET = 3001;

	@DataProperties("operation:0,name:强化宠物消耗")
	public static final int FORGING_PET = 3002;

	@DataProperties("operation:0,name:进阶宠物消耗")
	public static final int TENSEI_PET = 3003;

	@DataProperties("operation:0,name:购买商品宠物栏上限消耗")
	public static final int BUY_STORE_PET_COLUMN = 3004;

	@DataProperties("operation:0,name:购买体力消耗")
	public static final int BUY_STORE_STRENGTH = 3005;

	@DataProperties("operation:0,name:购买宠物消耗")
	public static final int BUY_STORE_PET = 3006;

	@DataProperties("operation:0,name:修改呢称消耗")
	public static final int CHANGE_NICK_NAME = 3007;

	@DataProperties("operation:0,name:战斗复活")
	public static final int BATTLE_RELIVE = 3008;

	@DataProperties("operation:1,name:副本通关奖励")
	public static final int INSTANCE_REWARD = 3009;

	@DataProperties("operation:1,name:邮件附件获得")
	public static final int MAIL_ATTACHMENT = 3010;

	@DataProperties("operation:1,name:竞技场连胜获得")
	public static final int ARENA_REPEAT_REWARD = 3011;

	@DataProperties("operation:1,name:竞技场排名奖励获得")
	public static final int ARENA_RANK_REWARD = 3012;

	@DataProperties("operation:1,name:竞技场金矿奖励获得")
	public static final int ARENA_MINE_REWARD = 3013;

	@DataProperties("operation:1,name:复仇胜利获得")
	public static final int REVENGE_REWARD = 3014;

	// /////////////////// 友情点相关
	@DataProperties("operation:1,name:战斗胜利获得")
	public static final int BATTLE_WIN_GET = 3100;

	// /////////////////// 体力相关
	@DataProperties("operation:0,name:战斗胜利扣除")
	public static final int BATTLE_WIN_PAY = 3200;
	@DataProperties("operation:0,name:pvp挑战扣除")
	public static final int PVP_PAY = 3201;

}
