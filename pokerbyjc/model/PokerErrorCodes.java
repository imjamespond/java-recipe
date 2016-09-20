package com.chitu.poker.model;

import cn.gecko.commons.data.DataFile;
import cn.gecko.commons.data.DataProperties;

@DataFile("cn.gecko.commons.data.ErrorCode_poker.csv")
public class PokerErrorCodes {

	@DataProperties("金额不能为负数或0")
	public static final int WEALTH_NEGATIVE = 3000;

	@DataProperties("金币不够")
	public static final int MONEY_NOT_ENOUGH = 3001;

	@DataProperties("魔石不够")
	public static final int POINT_NOT_ENOUGH = 3002;

	@DataProperties("昵称已经被使用")
	public static final int NICKNAME_EXIST = 3003;

	@DataProperties("角色不存在")
	public static final int PLAYER_NOT_EXIST = 3004;

	@DataProperties("宠物不存在")
	public static final int PET_NOT_EXIST = 3005;

	@DataProperties("昵称不能为空")
	public static final int NICKNAME_IS_NULL = 3006;

	@DataProperties("参数有误")
	public static final int PARAM_IS_NULL_OR_ERRO = 3007;

	@DataProperties("帐号不存在")
	public static final int ACCOUNT_NOT_EXIST = 3008;

	@DataProperties("帐号已存在")
	public static final int ACCOUNT_EXIST = 3009;

	@DataProperties("密码与确认密码不相同")
	public static final int PASSWORD_NOT_SAME = 3010;

	@DataProperties("密码不正确")
	public static final int PASSWORD_ERROR = 3011;

	@DataProperties("不能小于等于0")
	public static final int NEGATIVE_ERROR = 3012;

	@DataProperties("友情点不足")
	public static final int FRIEND_POINT_NOT_ENOUGH = 3013;

	@DataProperties("体力不足")
	public static final int STRENGTH_NOT_ENOUGH = 3014;

	/** 宠物***************************3100 - 3199 *********************************/

	@DataProperties("参数不能为空")
	public static final int ARGUMENT_CANNOT_BE_NULL = 3100;

	@DataProperties("队伍不包含此宠物")
	public static final int TEAM_NOT_CONTAINS_PET = 3101;

	@DataProperties("锁定或处理编队中的宠物不能卖出")
	public static final int PET_CANNOT_SELL = 3102;

	@DataProperties("宠物栏已满")
	public static final int PET_MAX_COUNT = 3103;

	@DataProperties("主将不能离开队伍")
	public static final int TEAM_MAIN_PET_CANNOT_LEAVE = 3104;

	@DataProperties("宠物等级已达上限")
	public static final int PET_GRADE_IS_MAX = 3105;

	@DataProperties("进阶失败,宠物未达到等级上限")
	public static final int PET_GRADE_NOT_MAX = 3106;

	@DataProperties("进阶失败,宠物没有进阶特性")
	public static final int NOT_TENSEI_PET = 3107;

	@DataProperties("进阶失败,没有足够的材料宠物")
	public static final int TENSEI_MATERIAL_NOT_ENOUGH = 3108;

	@DataProperties("宠物被锁定")
	public static final int PET_LOCKED = 3109;

	@DataProperties("宠物处于编队状态")
	public static final int PET_IN_ACTION = 3110;

	@DataProperties("宠物栏已满,奖励以邮件形式发放")
	public static final int PET_MAIL_SENDED = 3111;

	/** 商店***************************3200 - 3299 *********************************/

	@DataProperties("今日购买体力次数已使用完")
	public static final int STORE_BUY_STENGTH_TIMES_OUT = 3200;

	/** 好友***************************3300 - 3399 *********************************/

	@DataProperties("角色ID不存在")
	public static final int FRIENDID_NOT_EXIST = 3300;

	@DataProperties("您好友数量已经达到上限")
	public static final int FRIENDNUM_REACH_MAX = 3301;

	/** 战斗***************************3400 - 3499 *********************************/
	@DataProperties("关卡不存在")
	public static final int STATIC_BATTLE_NOT_EXIST = 3400;
	@DataProperties("前置关卡没有通关")
	public static final int PRE_BATTLE_NOT_PASS = 3401;
	@DataProperties("放牌点必须配对")
	public static final int POKER_POINT_NOT_PAIR = 3403;
	@DataProperties("放牌点上已经有牌")
	public static final int POKER_POINT_NOT_EMPTY = 3404;
	@DataProperties("手上并没有要出的牌")
	public static final int NOT_OWN_POKER = 3405;
	@DataProperties("放牌后没有形成牌型")
	public static final int POKER_MODEL_NONE = 3406;
	@DataProperties("副本战斗不存在")
	public static final int BATTLE_NOT_EXIST = 3407;
	@DataProperties("关卡已经通关")
	public static final int BATTLE_HAS_PASS = 3408;
	@DataProperties("关卡已经通关")
	public static final int RESUME_HP_NO_COUNT = 3409;
	@DataProperties("最终关卡还没通过")
	public static final int FINAL_BATTLE_NOT_PASS = 3410;
	@DataProperties("能量值不够")
	public static final int POWER_NOT_ENOUGH = 3411;
	@DataProperties("台面上已经有两个王，不能再转换")
	public static final int HAS_TWO_JOKER = 3412;
	@DataProperties("关卡战斗已经关闭")
	public static final int BATTLE_CLOSE = 3413;
	@DataProperties("已经死亡")
	public static final int HAS_DIE = 3414;
	@DataProperties("没有可邀请的人")
	public static final int NO_INVITER = 3415;

	/** 竞技场***************************3500 - 3599 *********************************/
	
	@DataProperties("没有合适的战场")
	public static final int ARENA_IS_NULL = 3500;
	
	@DataProperties("已领过此连胜奖励")
	public static final int HAS_REPEAT_REWARD = 3501;
	
	@DataProperties("未达到领取此连胜奖励要求")
	public static final int REPEAT_REWARD_LIMIT = 3502;
	
	@DataProperties("未达到领取此排名奖励要求")
	public static final int RANK_REWARD_LIMIT = 3503;
	
	@DataProperties("已领过排名奖励")
	public static final int HAS_RANK_REWARD = 3504;
	
	@DataProperties("金矿不存在")
	public static final int MINE_IS_NULL = 3505;
	
	@DataProperties("战报不存在")
	public static final int ARENA_LOG_IS_NULL = 3506;
	
	@DataProperties("此战报不是防守战报")
	public static final int ARENA_LOG_NOT_DEFEND = 3507;
	
}
