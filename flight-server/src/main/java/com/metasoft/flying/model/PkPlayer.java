package com.metasoft.flying.model;

import java.lang.ref.WeakReference;

import com.metasoft.flying.vo.PkPlayerVO;

/**
 * @author james
 * player killing
 */
public class PkPlayer extends PkPlayerVO{
	
	public static int STATE_DEAD = 0;
	public static int STATE_ALIVE = 1;
	
	private WeakReference<User> user;
	private int planeType;
	
	public int getPlaneType() {
		return planeType;
	}

	public void setPlaneType(int planeType) {
		this.planeType = planeType;
	}

	public User getUser() {
		if(null == user){
			return null;
		}
		return user.get();
	}

	public void setUser(User user) {
		this.user = new WeakReference<User>(user);
	}
	
	public int getHurt(float f){
		return (int) ((attack + planeType)*(1+f));
	}

	public int decreaseHp(int attack) {
		hp = hp - attack;
		return hp=hp<0?0:hp;
	}

	public void reset() {
		user = null;
		name = null;
		userId = 0;
		team = 0;
		pos = 0;
		x = 0;
		y = 0;
		hp = 100;
		hpMax = 100;
		attack = 10;
		plane = 0;
		state = 1;
		score = 0;
		
		planeType=0;
	}
	public void rebirth() {
		x = 0;
		y = 0;
		hp = hpMax;
		plane = 0;
		state = 1;
	}
}
