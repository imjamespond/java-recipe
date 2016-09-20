package com.chitu.poker.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.player.msg.ListDto;

import com.chitu.poker.data.StaticPet;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.PetTeam;
import com.chitu.poker.pet.msg.OperatePetDto;
import com.chitu.poker.pet.msg.PetColumnDto;
import com.chitu.poker.pet.msg.PetDto;
import com.chitu.poker.pet.msg.PetTeamDto;
import com.chitu.poker.service.PokerPlayerManager;

@Controller
public class PetController extends MultiGeneralController {

	@Autowired
	private PokerPlayerManager playerManager;
	
	/**
	 * 宠物栏
	 * @return
	 */
	public PetColumnDto listPet(){
		PokerPlayer player = playerManager.getRequestPlayer();
		PetColumnDto dto = new PetColumnDto();
		
		List<PetDto> petDtos = new ArrayList<PetDto>();
		for(Pet pet : player.petHolder.listPet()){
			petDtos.add(pet.toDto());
		}
		dto.setPets(petDtos);
		
		int petMaxCount = player.petHolder.getPetMaxCount();
		dto.setPetMaxCount(petMaxCount);
		
		return dto;
	}
	
	/**
	 * 宠物队伍
	 * @return
	 */
	public PetTeamDto petTeam(){
		PokerPlayer player = playerManager.getRequestPlayer();
		PetTeam petTeam = player.petHolder.getTeam();
		return petTeam.toDto();
	}
	
	/**
	 * 宠物离开队伍
	 * @param petId
	 */
	public void leaveTeam(String uniqueIdStr){
		long uniqueId = Long.valueOf(uniqueIdStr);
		PokerPlayer player = playerManager.getRequestPlayer();
		PetTeam petTeam = player.petHolder.getTeam();
		if(!petTeam.containsPet(uniqueId)){
			throw new GeneralException(PokerErrorCodes.TEAM_NOT_CONTAINS_PET);
		}
		if(petTeam.pet[0] == uniqueId){
			throw new GeneralException(PokerErrorCodes.TEAM_MAIN_PET_CANNOT_LEAVE);
		}
		player.petHolder.leaveTeam(uniqueId);
	}
	
	/**
	 * 宠物加入队伍
	 * @param petId
	 * @param index
	 */
	public void joinTeam(String uniqueIdStr,int index){
		if(index < 0 || index > 4){
			throw new GeneralException(PokerErrorCodes.PARAM_IS_NULL_OR_ERRO);
		}
		
		PokerPlayer player = playerManager.getRequestPlayer();
		player.petHolder.joinTeam(Long.valueOf(uniqueIdStr), index);
	}
	
	/**
	 * * 出售宠物,元素String
	 * @param uniqueIdStr多个ID则以逗号分隔
	 * @return
	 */
	public ListDto sell(String uniqueIdStr){
		String[] uniqueIds = uniqueIdStr.split(",");
		PokerPlayer player = playerManager.getRequestPlayer();
		
		List<String> petUniqueId = new ArrayList<String>();
		int sellMoney = 0;
		for(String uniqueId : uniqueIds){
			Pet pet = player.petHolder.getPet(Long.valueOf(uniqueId));
			if(pet == null){
				throw new GeneralException(PokerErrorCodes.PET_NOT_EXIST);
			}
			if(pet.inAction ||  pet.locked){
				throw new GeneralException(PokerErrorCodes.PET_CANNOT_SELL);
			}
			StaticPet staticPet = StaticPet.get(pet.staticId);
			sellMoney += staticPet.getSystemMoney();
			petUniqueId.add(uniqueId);
		}
		
		if(sellMoney > 0){
			player.wealthHolder.increaseMoney(sellMoney, BillType.get(PokerBillTypes.SELL_PET), uniqueIdStr);
		}
		for(String uniqueId : petUniqueId){
			player.petHolder.deletePet(Long.valueOf(uniqueId));
		}
		return new ListDto(petUniqueId);
	}
	
	/**
	 * 强化宠物
	 * @param basePetIdStr 基础宠物ID
	 * @param materialPetIds 材料宠物ID,多个ID则以逗号分隔
	 * @return
	 */
	public OperatePetDto forging(String baseUniqueIdStr,String materialUniqueIds){
		PokerPlayer player = playerManager.getRequestPlayer();
		long baseUniqueId = Long.valueOf(baseUniqueIdStr);
		Pet basePet = player.petHolder.getPet(baseUniqueId);
		
		StaticPet staticPet = StaticPet.get(basePet.staticId);
		if(staticPet.isMaxGrade()){
			throw new GeneralException(PokerErrorCodes.PET_GRADE_IS_MAX);
		}
		
		List<Pet> materialPet = new ArrayList<Pet>();
		String[] uniqueIds = materialUniqueIds.split(",");
		for(String uniqueId : uniqueIds){
			Pet pet = player.petHolder.getPet(Long.valueOf(uniqueId));
			if(pet == null){
				throw new GeneralException(PokerErrorCodes.PET_NOT_EXIST);
			}
			if(pet.locked){
				throw new GeneralException(PokerErrorCodes.PET_LOCKED);
			}
			if(pet.inAction){
				throw new GeneralException(PokerErrorCodes.PET_IN_ACTION);
			}
			materialPet.add(pet);
		}
		//强化
		player.petHolder.forging(basePet, materialPet);
		
		OperatePetDto dto = new OperatePetDto();
		dto.setTargetPet(basePet.toDto());
		dto.setUsePets(uniqueIds);
		return dto;
	}
	
	/**
	 * 进阶
	 * @param uniqueIdStr 基础宠物ID
	 * @param materialPetIds 材料宠物ID,多个ID则以逗号分隔
	 * @return
	 */
	public OperatePetDto tensei(String uniqueIdStr,String materialUniqueIds){
		PokerPlayer player = playerManager.getRequestPlayer();
		Pet basePet = player.petHolder.getPet(Long.valueOf(uniqueIdStr));
		
		List<Pet> materialPet = new ArrayList<Pet>();
		String[] uniqueIds = materialUniqueIds.split(",");
		for(String uniqueId : uniqueIds){
			Pet pet = player.petHolder.getPet(Long.valueOf(uniqueId));
			if(pet == null){
				throw new GeneralException(PokerErrorCodes.PET_NOT_EXIST);
			}
			if(pet.locked){
				throw new GeneralException(PokerErrorCodes.PET_LOCKED);
			}
			if(pet.inAction){
				throw new GeneralException(PokerErrorCodes.PET_IN_ACTION);
			}
			materialPet.add(pet);
		}
		//进阶
		player.petHolder.tensei(basePet,materialPet);
		
		OperatePetDto dto = new OperatePetDto();
		dto.setTargetPet(basePet.toDto());
		dto.setUsePets(uniqueIds);
		return dto;
	}
	
}
