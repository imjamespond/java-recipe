package com.chitu.poker.setting.msg;

import cn.gecko.broadcast.BroadcastMessage;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.poker.setting.SettingHolder.SettingType;

/**
 * 游戏设置
 * @author open
 *
 */
@IncludeEnums({ @IncludeEnum(SettingType.class) })
public class SettingDto implements BroadcastMessage {
	
	private int settingType;
	
    private int value;
    
    public SettingDto(SettingType type, int value){
    	this.settingType = type.ordinal();
    	this.value = value;
    }

    /**设置类型**/
	public int getSettingType() {
		return settingType;
	}

	public void setSettingType(int settingType) {
		this.settingType = settingType;
	}

	/**设置值**/
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
    
    
	
	
}
