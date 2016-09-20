package com.chitu.poker.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gecko.player.msg.ListDto;

import com.chitu.poker.setting.msg.SettingDto;


public class SettingHolder {

	/**设置类型**/
	public enum SettingType {
		/**游戏音乐0**/
		GameMusic,
		/**游戏音效1**/
		GameSound,
		/**震动**/
		Shock;
		
		public static SettingType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
    private long playerId;
    
	private Map<SettingType,Integer> setting;
	
    private PersistSetting persist;
	
	private boolean update;

	private boolean needSave;
	
	
	public SettingHolder(long playerId) {
		this.playerId = playerId;
	}
	
	private void init() {
		if (persist != null) {
			return;
		}
		
		persist = PersistSetting.get(playerId);
		if (persist != null) {
			
			this.setting = PersistSetting.initSetting(this.persist.getSetting());
			
		}else{
			needSave = true;
			persist = new PersistSetting();
			persist.setId(playerId);
		}
	}
	
	/**
	 * 持久化
	 */
	public void persist() {
		if (persist == null) {
			return;
		}

		persist.setSetting(PersistSetting.settingData(this.setting));
		
		if (needSave) {
			persist.save();
			needSave = false;
		}
		else if (update) {
			persist.update();
			update = false;
		}
	}

	/**
	 * 角色下线
	 */
	public void destroy() {
		persist = null;
		playerId = 0;
	}
	
	public ListDto toDto(){
		init();
		
		List<SettingDto> dtos = new ArrayList<SettingDto>();
		for(Map.Entry<SettingType, Integer> entry : this.setting.entrySet()){
			SettingDto dto = new SettingDto(entry.getKey(),entry.getValue());
			dtos.add(dto);
		}
		return new ListDto(dtos);
	}
	
	public void setting(int gameMusic,int gameSound,int shock){
		init();
		
		this.setting.put(SettingType.GameMusic, gameMusic);
		this.setting.put(SettingType.GameSound, gameSound);
		this.setting.put(SettingType.Shock, shock);
		
		this.update = true;
	}
	
}
