package com.metasoft.empire.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.metasoft.empire.model.User;
import com.metasoft.empire.model.UserUpgrade;

public class LoginVO {
	private long uid;
	private String name;
	private int level;
	private int score;
	private int redeem;
	private int recieve;
	private Map<Integer, UpgradeVO> upgrade;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getRedeem() {
		return redeem;
	}
	public void setRedeem(int redeem) {
		this.redeem = redeem;
	}

	public int getRecieve() {
		return recieve;
	}
	public void setRecieve(int l) {
		this.recieve = l;
	}

	public Map<Integer, UpgradeVO> getUpgrade() {
		return upgrade;
	}
	public void setUpgrade(Map<Integer, UserUpgrade> map) {
		upgrade = new HashMap<Integer, UpgradeVO>();
		for(Entry<Integer, UserUpgrade> entry: map.entrySet()){
			UserUpgrade uu = entry.getValue();
			upgrade.put(uu.getRoleid(), new UpgradeVO(uu.getRoleid(), uu.getNumber(), uu.getUpgrade(), uu.getLevel(),
					uu.getAttack(), uu.getHp()));
		}
	}
	public static LoginVO getLoginVO(User user){
		LoginVO vo = new LoginVO();
		vo.setUid(user.getId());
		vo.setName(user.getUserPersist().getUsername());
		vo.setLevel(user.getLevel());
		vo.setRedeem(user.getUserDataPersist().getRedeem());
		vo.setScore(user.getUserDataPersist().getScore());
		vo.setUpgrade(user.getUpgradeMap());
		int pass = (int) ((System.currentTimeMillis()-user.recieve)/1000l);
		vo.setRecieve(pass>3600?0:3600-pass);
		return vo;
	}
}
