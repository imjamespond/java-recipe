package com.metasoft.flying.model;

import com.metasoft.flying.model.constant.ChessConstant;

public class ChessPlayer {
	//private static final Logger logger = LoggerFactory.getLogger(ChessPlayer.class);

	public static final int ITEM_LEN = 8;
	// id
	protected long userId = 0;
	// 位置
	protected int pos = 0;//
	// 魔力色子次数
	//protected int magic = 0;//
	// 展示飞机次数
	protected int show = 0;//
	// 赠送玫瑰次数
	protected int rose = 0;//
	// 空中加油次数
	protected int refuel = 0;//
	
	// 荆棘装甲buff回合
	protected int thorns = 0;//
	// 使用迷雾buff回合
	protected int fog = 0;//
	
	// 0默认1设置托管2取消托管4托管中
	protected int autoState = 0;
	//当前使用的道具
	protected int itemPos;
	// 棋子们
	protected Chess[] chesses;
	protected int[] items;

	private int plane;
	private int npc;
	
	public ChessPlayer(int pos) {
		this.pos = pos;
		chesses = new Chess[4];
		for (int i = 0; i < chesses.length; i++) {
			chesses[i] = new Chess(pos, i);
		}
		items = new int[ITEM_LEN];
	}

	public void reset() {
		npc = 0;
		autoState = 0;
		//magic = 0;
		show = 0;
		rose = 0;
		
		thorns = -8;//2回合
		fog = -4;//1回合
		refuel = 0;
		
		itemPos = 0;
		int i = 0;
		for (i = 0; i < items.length; i++) {
			items[i] = 0;
		}
		for (i = 0; i < chesses.length; i++) {
			chesses[i].reset();
		}
	}

	public void reset2() {
		reset();
		userId = 0;
	}

	public boolean endCheck() {
		for (int i = 0; i < chesses.length; i++) {
			//logger.debug("chess i:{}, state:{}", i, chesses[i].getState());
			if (chesses[i].getState() != ChessConstant.CHESS_FINISH) {
				return false;
			}
		}
		return true;
	}
	
	public int finishCount() {
		int count = 0;
		for (int i = 0; i < chesses.length; i++) {
			//logger.debug("chess i:{}, state:{}", i, chesses[i].getState());
			if (chesses[i].getState() == ChessConstant.CHESS_FINISH) {
				count++;
			}
		}
		return count;
	}
	
	public int standbyCount() {
		int count = 0;
		for (int i = 0; i < chesses.length; i++) {
			//logger.debug("chess i:{}, state:{}", i, chesses[i].getState());
			if (chesses[i].isFlying()||chesses[i].isReady()) {
				count++;
			}
		}
		return count;
	}
	
	public int jouneyMax() {
		int count = 0;
		for (int i = 0; i < chesses.length; i++) {
			//logger.debug("chess i:{}, state:{}", i, chesses[i].getState());
			if (chesses[i].isFlying()) {
				count = chesses[i].getJourney() > count ? chesses[i].getJourney(): count;
			}
		}
		return count;
	}	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Chess[] getChesses() {
		return chesses;
	}

	public void setChesses(Chess[] chesses) {
		this.chesses = chesses;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}

	public boolean isOffline() {
		if ((ChessConstant.AUTO_OFFLINE & autoState) > 0) {
			return true;
		}
		return false;
	}
	public boolean isAutoState() {
		//logger.debug("isAutoState, {} {}",pos, autoState);
		if ((ChessConstant.AUTO_ENABLE_OFFLINE & autoState) > 0) {
			return true;
		}
		return false;
	}
	public void setAutoDisable() {
		this.autoState = 0;
	}
	public void setAutoState(int autoState) {
		this.autoState |= autoState;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

	public int[] getItems() {
		return items;
	}

	public void setItems(int[] items) {
		this.items = items;
	}

	public int getAutoState() {
		return autoState;
	}

	public void add(int itemPos){
		items[itemPos]++;
	}
	public int reduce(int itemPos){
		if(items[itemPos]>0){
			return --items[itemPos];
		}else{
			return -1;
		}
	}


	public int getItemPos() {
		return itemPos;
	}

	public void setItemPos(int itemPos) {
		this.itemPos = itemPos;
	}

	public int getPlane() {
		return plane;
	}

	public void setPlane(int plane) {
		this.plane = plane;
	}

	public boolean isThorns(int turn) {
		return thorns + 8 > turn;
	}

	public void setThorns(int thorns) {
		this.thorns = thorns;
	}

	public boolean isFog(int turn) {
		//FIXME
		//System.out.printf("isfog pos:%d, buff:%d, turn:%d\n",pos, fog, turn);
		return fog + 4 > turn;
	}

	public void setFog(int fog) {
		this.fog = fog;
	}

	public int getRefuel() {
		return refuel;
	}

	public void addRefuelBy1() {
		this.refuel++;
	}
	public void reduceRefuelBy1() {
		this.refuel--;
	}

	public int getThorns() {
		return thorns;
	}

	public int getFog() {
		return fog;
	}

	public void setRefuel(int refuel) {
		this.refuel = refuel;
	}

	public int getNpc() {
		return npc;
	}

	public void setNpc(int npc) {
		this.npc = npc;
	}
	
}
