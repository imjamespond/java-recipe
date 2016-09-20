package com.pengpeng.stargame.model.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 仓库
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:59
 */
public class Item implements Serializable {

	// 如果叠加达到上限 ,新增一个key做区分,代表要新开一个格子放物品
	// 物品key/格子唯一编号
	private String grid;

	// 玩家ID
	private String pid;

	// 物品ID
	private String itemId;

	// 物品叠加数量
    private int num;

    //有效日期
    private Date  validDete;


    public Date getValidDete() {
        return validDete;
    }

    public void setValidDete(Date validDete) {
        this.validDete = validDete;
    }

    public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * 数量是否足够进行操作
	 * @param num
	 * @return true : 足够
	 */
	public boolean checkSatisfyByNum(int num){
		if(this.num >= num){
			return true;
		}
		return false;
	}

	/**
	 * 加数量
	 * @param num
	 */
	public void addNum(int num){
		this.num += num;
	}

	/**
	 * 减数量
	 * @param num
	 */
	public void subNum(int num){
		this.num -= num;
	}

	@Override
	public String toString() {
		return "Item{" +
				"grid='" + grid + '\'' +
				", pid='" + pid + '\'' +
				", itemId='" + itemId + '\'' +
				", num=" + num +
				'}';
	}
}
