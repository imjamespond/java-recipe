package com.chitu.poker.data;

import java.util.ArrayList;
import java.util.List;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.commons.data.StaticDataManager;

/**
 * 进阶数据表
 * @author open
 *
 */
public class StaticTensei implements BroadcastMessage {

	private int id;
	
	private int targetStaticId;
	
	private int needMoney;
	
	private int needPet1;
	
	private int needPet2;
	
	private int needPet3;
	
	private int needPet4;
	
	private int needPet5;
	
	public static StaticTensei get(int id) {
		return StaticDataManager.getInstance().get(StaticTensei.class, id);
	}
	
	/**
	 * 进阶所需材料
	 * @return
	 */
	public List<Integer> needPetId(){
		List<Integer> list = new ArrayList<Integer>();
		if(this.needPet1 > 0){
			list.add(this.needPet1);
		}
		if(this.needPet2 > 0){
			list.add(this.needPet2);
		}
		if(this.needPet3 > 0){
			list.add(this.needPet3);
		}
		if(this.needPet4 > 0){
			list.add(this.needPet4);
		}
		if(this.needPet5 > 0){
			list.add(this.needPet5);
		}
		return list;
	}

	/**基础宠物petId**/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**进阶所需金币**/
	public int getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	/**进阶所需材料宠物petId**/
	public int getNeedPet1() {
		return needPet1;
	}

	public void setNeedPet1(int needPet1) {
		this.needPet1 = needPet1;
	}

	/**进阶所需材料宠物petId**/
	public int getNeedPet2() {
		return needPet2;
	}

	public void setNeedPet2(int needPet2) {
		this.needPet2 = needPet2;
	}

	/**进阶所需材料宠物petId**/
	public int getNeedPet3() {
		return needPet3;
	}

	public void setNeedPet3(int needPet3) {
		this.needPet3 = needPet3;
	}

	/**进阶所需材料宠物petId**/
	public int getNeedPet4() {
		return needPet4;
	}

	public void setNeedPet4(int needPet4) {
		this.needPet4 = needPet4;
	}

	/**进阶所需材料宠物petId**/
	public int getNeedPet5() {
		return needPet5;
	}

	public void setNeedPet5(int needPet5) {
		this.needPet5 = needPet5;
	}

	/**进阶宠物staticId**/
	public int getTargetStaticId() {
		return targetStaticId;
	}

	public void setTargetStaticId(int targetStaticId) {
		this.targetStaticId = targetStaticId;
	}
	
	
}
