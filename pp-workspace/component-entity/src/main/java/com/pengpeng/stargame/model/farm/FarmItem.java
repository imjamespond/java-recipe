package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.model.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 仓库物品
 * User: 林佛权
 * Date: 12-12-25
 * Time: 上午11:32
 * To change this template use File | Settings | File Templates.
 */
public class FarmItem  {

	// 如果叠加达到上限 ,新增一个key做区分 uuid
	private String id;

	// 物品ID
	private String itemId;

	// 物品叠加数量
    private int num;
    //有效日期
    private Date validDete;


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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



	/**
	 * 数量是否足够
	 * @param num
	 * @return true :足够
	 */
	public boolean checkByNum(int num){
		if(this.num <= num){
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

    public Date getValidDete() {
        return validDete;
    }

    public void setValidDete(Date validDete) {
        this.validDete = validDete;
    }


}
