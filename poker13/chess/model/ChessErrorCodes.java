package com.chitu.chess.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("cn.gecko.commons.data.ErrorCode_chess.csv")
public class ChessErrorCodes {

	@DataProperties("金额不能为负数或0")
	public static final int WEALTH_NEGATIVE = 3000;

	@DataProperties("金币不够")
	public static final int MONEY_NOT_ENOUGH = 3001;

	@DataProperties("钻石不够")
	public static final int POINT_NOT_ENOUGH = 3002;
	
	@DataProperties("元宝不够，请充值")
	public static final int RMB_NOT_ENOUGH = 30021;

	@DataProperties("金币总数超过上限")
	public static final int MONEY_LIMIT = 3003;

	@DataProperties("必须所有人准备好才能开始战斗")
	public static final int NOT_ALL_READY = 3004;

	@DataProperties("房间已经关闭，不允许进入")
	public static final int MAP_CLOSE = 3005;

	@DataProperties("非本房间的参战人员，不允许进入")
	public static final int NOT_IN_USER_LIST = 3006;

	@DataProperties("您已经加入了另外一个房间的战斗了")
	public static final int IN_ANOTHER_ROOM = 3007;

	@DataProperties("密码错误，不允许进入房间")
	public static final int ROOM_PASSWORD_WRONG = 3008;
	
	@DataProperties("密码错误")
	public static final int WRONG_PASSWORD = 30081;

	@DataProperties("房间人数已经达到上限")
	public static final int MAP_CAPABILITY_BEYOND = 3009;

	@DataProperties("英雄等级太低，不允许进入此关卡")
	public static final int GAME_HERO_LEVEL_LIMIT = 3010;

	@DataProperties("昵称已经被使用")
	public static final int NICKNAME_EXIST = 3011;

	@DataProperties("角色不存在")
	public static final int ROLE_NOT_EXIST = 3012;
	
	@DataProperties("该账号已经被使用")
	public static final int ROLE_EXIST = 30121;

	@DataProperties("威望不够")
	public static final int PRESTIGE_NOT_ENOUGH = 3013;
	
	@DataProperties("频道不存在")
	public static final int CHANNEL_NOT_EXIST = 3014;
	
	@DataProperties("已在频道内")
	public static final int CHANNEL_INSIDE = 3015;
	
	@DataProperties("已在比赛区")
	public static final int MATCH_INSIDE = 30151;
	
	@DataProperties("游戏区不存在")
	public static final int DISTRICT_NOT_EXIST = 30141;

	@DataProperties("房间不存在")
	public static final int ROOM_NOT_EXIST = 30142;
	
	@DataProperties("出牌未通过验证")
	public static final int INVALID_CARD = 3016;
	
	@DataProperties("暂无数据")
	public static final int NO_DATA = 3017;
	
	@DataProperties("报名人数已满")
	public static final int REACH_MAX = 3018;
	
	@DataProperties("比赛未开始")
	public static final int MATCH_UNAVAILABLE = 3019;
	
	@DataProperties("请先报名比赛")
	public static final int MATCH_NOTENROLL = 3020;
	
	@DataProperties("已经在比赛中淘汰")
	public static final int MATCH_KNOCKOUT = 3021;
	
	@DataProperties("比赛已经开始")
	public static final int MATCH_ALREADYSTART = 3022;
	
	
	@DataProperties("充值金额不正确")
	public static final int PAY_MZ_ERROR = 3100;
	
	@DataProperties("充值卡序列号不正确")
	public static final int CARD_XLH_ERROR = 3101;
	
	@DataProperties("充值卡密码不正确")
	public static final int CARD_MM_ERROR = 3102;
	
	@DataProperties("充值手机号不正确")
	public static final int PAY_MOD_ERROR = 3103;
	
	
	
	@DataProperties("接口关闭")
	public static final int VNETONE_ERROR_CODE_50002 = 50002;
	
	@DataProperties("面值不符合 不是 50 / 100的")
	public static final int VNETONE_ERROR_CODE_50003 = 50003;
	
	@DataProperties("商户代码问题")
	public static final int VNETONE_ERROR_CODE_50004 = 50004;
	
	@DataProperties("商户订单号问题")
	public static final int VNETONE_ERROR_CODE_50005 = 50005;
	
	@DataProperties("md5x 错误")
	public static final int VNETONE_ERROR_CODE_50006 = 50006;
	
	@DataProperties("网络问题未提交成功")
	public static final int VNETONE_ERROR_CODE_50007 = 50007;
	
	@DataProperties("卡密不是数字")
	public static final int VNETONE_ERROR_CODE_50008 = 50008;
	
	@DataProperties("序列号不是17位")
	public static final int VNETONE_ERROR_CODE_50009 = 50009;
	
	@DataProperties("刮开密码不是18位")
	public static final int VNETONE_ERROR_CODE_50010 = 50010;
	
	@DataProperties("订单号重复")
	public static final int VNETONE_ERROR_CODE_50011 = 50011;
	
	@DataProperties("序列号已经支付成功了,请换一张卡继续")
	public static final int VNETONE_ERROR_CODE_50012 = 50012;
	
	@DataProperties("该序列号已经提交3次了,请明天再次输入充值")
	public static final int VNETONE_ERROR_CODE_50013 = 50013;
	
	@DataProperties("订单异常,请返回商户网站重新生成订单")
	public static final int VNETONE_ERROR_CODE_50014 = 50014;
	
	@DataProperties("神州返回错误")
	public static final int VNETONE_ERROR_CODE_50015 = 50015;
	
	
}
