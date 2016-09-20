package com.metasoft.flying.model.constant;

public class ChessConstant {
	public static final int DISTANCE = 52;//棋局坐标数
	public static final int DISTANCE_QUATER = 13;//四分一路程总长
	public static final int JOURNEY_OUT = 50;//跑道外行程
	public static final int JOURNEY_JUMP = 49;//跑道外行程能同色跳
	public static final int JOURNEY = 55;//行程
	
	public static final int RED = 39;
	public static final int BLUE = 0;
	public static final int YELLOW = 13;
	public static final int GREEN = 26;
	public static final int[] START = {RED, BLUE, YELLOW, GREEN};//起始坐标
	public static final int[] GO_ACROSS = {4, 17, 30, 43};//飞越坐标
	public static final int[] STATES = {1, 2, 4, 8};//玩家各类状态
	
	public static final int CHESS_SIZE = 4;//棋子数
	
	public static final int CHESS_READY = 1;//准备
	public static final int CHESS_FLIGHT = 2;//飞行
	public static final int CHESS_FINISH = 4;//结束
	public static final int CHESS_BASE = 7;//clear后面状态用
	public static final int CHESS_NO_JUMP = 8;//不再弹跳
	public static final int CHESS_CAN_GO = CHESS_READY | CHESS_FLIGHT;//可走
	public static final int CHESS_REBOUND = 16;//反弹后
	
	public static final int DICE_SEC = 15000;//扔色子时间
	public static final int AUTODICE_SEC = 5000;//自动扔色子时间
	public static final int GO_SEC = 15000;//走棋时间
	public static final int AUTOGO_SEC = 5000;//自动走棋时间
	public static final int ANIMATE_SEC = 20000;//动画时间
	
	public static final int ROOM_IDLE = 0;//未下棋
	public static final int ROOM_BEGIN = 1;//开始下棋
	public static final int ROOM_DICE = 2;//已扔色子
	public static final int ROOM_GO = 4;//已走棋
	public static final int ROOM_ANIM = 8;//播动画
	//public static final int ROOM_DICE_ANIM = 12;//播色子动画
	
	public static final int MAGIC_DICE_NUM = 30;//魔力色子次数
	public static final int SHOW_ITEM_NUM = 3;//展示次数
	
	public static final int SCORE_WIN = 3;//胜利分得
	public static final int SCORE_LOSE = 1;//失败得分
	public static final int GOLD_WIN = 100;//胜利金币
	
	public static final int AUTO_DISABLE = 2;
	public static final int AUTO_ENABLE = 1;
	public static final int AUTO_ENABLED = 4;
	public static final int AUTO_OFFLINE = 8;//掉线
	public static final int AUTO_ENABLE_OFFLINE = 9;
	
	public static final int INCIDENT_TELEPORT = 500008;//传送门
	public static final int INCIDENT_PRIZE = 500007;//奖励
	public static final int INCIDENT_INVUL = 500004;//无敌
	public static final int INCIDENT_TORNADO = 500005;//龙头风
	public static final int INCIDENT_POWER = 500002;//动力装置
	public static final int INCIDENT_REFUEL = 500003;//空中加油
	public static final int INCIDENT_MAGIC = 500006;//魔力控制
	
	public static final int ITEM_MAGIC = 0;//魔力色子
	public static final int ITEM_ENHANCE = 1;//动力装置
	public static final int ITEM_REFUEL = 2;//空中加油
	public static final int ITEM_RELAY = 3;//接力
	public static final int ITEM_TAKEOFF = 5;//魔力起飞
	public static final int ITEM_DISPEL = 6;//驱散风卷风
	public static final int ITEM_THORNS = 4;//荆棘装甲
	public static final int ITEM_FOG = 7;//迷雾
	
	public static final int ANIM_REBOUND = 1;//反弹动画
	//public static final int ANIM_ITEM = 2;//道具
	
	public static int TORNADO_FALL = -1;//龙卷风掉下坐标
}
