package com.chitu.poker.setting;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

import com.chitu.poker.setting.SettingHolder.SettingType;

@Entity
@Table(name = "poker_setting")
@PersistEntity(cache = false)
public class PersistSetting extends PersistObject {

	public static final byte SETTING_VERSION_1 = 1;

	public static final byte SETTING_VERSION = SETTING_VERSION_1;
	
	private long id;
	
	private byte[] setting;
	
	public static PersistSetting get(long id) {
		return PersistObject.get(PersistSetting.class, id);
	}
	
	public static Map<SettingType,Integer> initSetting(byte[] byteData){
		Map<SettingType,Integer> map = new HashMap<SettingType,Integer>();
		if (byteData == null || byteData.length == 0) {
			return map;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		byte version = buffer.get();
		if (version == SETTING_VERSION_1) {
			int size = buffer.getInt();
			for(int i=0;i<size;i++){
				SettingType type = SettingType.from(buffer.getInt());
				int value = buffer.getInt();
				map.put(type, value);
			}
		}
		return map;
	}
	
	public static byte[] settingData(Map<SettingType,Integer> setting) {
		int length = 1 + setting.size() + setting.size() * (4 + 4);
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(SETTING_VERSION);
		buffer.putInt(setting.size());
		for(Map.Entry<SettingType, Integer> entry : setting.entrySet()){
			buffer.putInt(entry.getKey().ordinal());
			buffer.putInt(entry.getValue());
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}
	
	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getSetting() {
		return setting;
	}

	public void setSetting(byte[] setting) {
		this.setting = setting;
	}

	
	
	

}
