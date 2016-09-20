package com.chitu.poker.pet.msg;

import cn.gecko.broadcast.GeneralResponse;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;

import com.chitu.poker.pet.PetTeam.PetTeamStatus;

/**
 * 编队DTO
 * @author open
 *
 */
@IncludeEnums({ @IncludeEnum(PetTeamStatus.class) })
public class PetTeamDto extends GeneralResponse {

	private String id;
	
	private String name;
	
	private int status;
	
	private String[] pet;

	/**宠物ID,元素string**/
	public String[] getPet() {
		return pet;
	}

	public void setPet(String[] pet) {
		this.pet = pet;
	}

	/**唯一ID**/
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**队名**/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**状态**/
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
	
	
	
	
}
