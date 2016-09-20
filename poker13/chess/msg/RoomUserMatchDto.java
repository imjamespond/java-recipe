package com.chitu.chess.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.GeneralResponse;

public class RoomUserMatchDto extends GeneralResponse implements BroadcastMessage {

	public int id;
	public String name;//比赛名称
	public String date;//比赛时间
	public int num;//报名人数
	public int max;//最大报名人数
	public int state;//0报名,1进入,2停止
	public int cost;//费用
	public int prize;//奖品
	public boolean prizeAvail;
	public RoomUserMatchDto() {
		prizeAvail  = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**String 比赛名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**String 比赛时间
	 * @return
	 */
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	/**int 报名人数
	 * @return
	 */
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	/**int 最大报名人数
	 * @return
	 */
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	/**int 0报名,1进入,2停止
	 * @return
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**int 费用
	 * @return
	 */
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	/**int 奖品图标
	 * @return
	 */
	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	/**boolean 是否有奖
	 * @return
	 */
	public boolean isPrizeAvail() {
		return prizeAvail;
	}

	public void setPrizeAvail(boolean prizeAvail) {
		this.prizeAvail = prizeAvail;
	}



	
}
