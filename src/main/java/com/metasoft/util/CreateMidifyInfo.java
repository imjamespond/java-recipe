package com.metasoft.util;

import com.keymobile.dataSharingMgr.interfaces.MiscInfo;

public class CreateMidifyInfo {

	public static void setCreateInfo(String userName, MiscInfo miscInfo) {
		miscInfo.setCreator(userName);
		miscInfo.setModifier(userName);
		miscInfo.setCreateDate(System.currentTimeMillis());
		miscInfo.setModifyDate(System.currentTimeMillis());
	}
	
	public static void setModifyInfo(String userName, MiscInfo miscInfo){
		miscInfo.setModifier(userName);
		miscInfo.setModifyDate(System.currentTimeMillis());
	}
	
}
