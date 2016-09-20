package com.chitu.chess.msg;

import java.util.List;

import cn.gecko.broadcast.GeneralResponse;

public class ChessDistrictsInfoDto extends GeneralResponse {

	
	/**
	 * 各区的信息数组
	 */
	private ChessDistrictDto[] districts;
	
	public ChessDistrictsInfoDto(List<ChessDistrictDto> districts){
		this.districts = new ChessDistrictDto[districts.size()];
		districts.toArray(this.districts);
	}

	/**
	 * 各区的信息数组
	 */
	public ChessDistrictDto[] getDistricts() {
		return districts;
	}

	public void setDistricts(ChessDistrictDto[] districts) {
		this.districts = districts;
	}
}
