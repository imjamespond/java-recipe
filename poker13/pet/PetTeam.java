package com.chitu.poker.pet;

import java.nio.ByteBuffer;

import cn.gecko.commons.utils.IdUtils;
import cn.gecko.persist.ByteBufferUtils;

import com.chitu.poker.pet.msg.PetTeamDto;

/**
 * 宠物编队
 * @author open
 *
 */
public class PetTeam {

	public static final byte PET_TEAM_VERSION_1 = 1;
	public static final byte PET_TEAM_VERSION = PET_TEAM_VERSION_1;
	
	/**队伍状态**/
	public enum PetTeamStatus {
		/**应战0**/
		Action,
		/**备战1**/
		Prepare;
		
		public static PetTeamStatus from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	public long id;
	
	public String name;
	
	/**队伍状态**/
	public PetTeamStatus status = PetTeamStatus.Prepare;
	
	/**宠物**/
	public long[] pet = new long[5];
	
	
	public void initBuffer(ByteBuffer buffer){
		this.id = buffer.getLong();
		this.name = ByteBufferUtils.readString(buffer);
		this.status = PetTeamStatus.from(buffer.getInt());
		int size = buffer.getInt();
		for(int i=0;i<size;i++){
			this.pet[i] =buffer.getLong();
		}
	}
	
	public void toBuffer(ByteBuffer buffer){
		buffer.putLong(this.id);
		ByteBufferUtils.putString(buffer, this.name);
		buffer.putInt(this.status.ordinal());
		buffer.putInt(this.pet.length);
		for(long petId : this.pet){
			buffer.putLong(petId);
		}
	}
	
	public int byteLength(){
		return  8 + ByteBufferUtils.stringLength(this.name) + 4 + 4 + this.pet.length * 8;
	}
	
	public PetTeamDto toDto(){
		PetTeamDto dto = new PetTeamDto();
		dto.setId(String.valueOf(this.id));
		dto.setName(this.name);
		dto.setStatus(this.status.ordinal());
		
		String[] uniqueId = new String[this.pet.length];
		for(int i=0;i<this.pet.length;i++){
			uniqueId[i] = String.valueOf(this.pet[i]);
		}
		dto.setPet(uniqueId);
		return dto;
	}
	
	public static PetTeam getInstance(){
		PetTeam team = new PetTeam();
		team.id = IdUtils.generateLongId();
		team.name = String.valueOf(team.id);
		return team;
	}
	
	/**
	 * 队伍是包含此宠物
	 * @param petId
	 * @return
	 */
	public boolean containsPet(long uniqueId){
		for(long id : this.pet){
			if(id == uniqueId){
				return true;
			}
		}
		return false;
	}
	
	
}
