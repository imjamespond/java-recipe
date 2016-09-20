package com.chitu.poker.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;

import com.chitu.poker.data.StaticPet;
import com.chitu.poker.data.StaticPlayerGrade;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.msg.PetDto;
import com.chitu.poker.service.PokerPlayerManager;
import com.chitu.poker.store.StoreHolder;
import com.chitu.poker.store.msg.BuyPetColumnDto;
import com.chitu.poker.store.msg.BuyPetTenTimesDto;
import com.chitu.poker.store.msg.BuyStrengthDto;

/**
 * 商店操作
 * @author open
 *
 */
@Controller
public class StoreController extends MultiGeneralController {

	@Autowired
	private PokerPlayerManager playerManager;
	
	/**
	 * 购买宠物栏上限
	 */
	public BuyPetColumnDto buyPetColumn(){
		PokerPlayer player = playerManager.getRequestPlayer();
		
		int needPoint = 10;
		if(!player.wealthHolder.hasEnoughPoint(needPoint)){
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		}
		
		int count = 5;
		player.wealthHolder.decreasePoint(needPoint, BillType.get(PokerBillTypes.BUY_STORE_PET_COLUMN),"");
		player.petHolder.incPetMaxCount(count);
		return new BuyPetColumnDto(player.petHolder.getPetMaxCount());
	}
	
	/**
	 * 购买体力
	 */
	public BuyStrengthDto buyStrength(){
		PokerPlayer player = playerManager.getRequestPlayer();
		if(!player.storeHolder.canBuyStrength()){
			throw new GeneralException(PokerErrorCodes.STORE_BUY_STENGTH_TIMES_OUT);
		}
		int needPoint = player.storeHolder.buyStrengthNeedPoint();
		if(!player.wealthHolder.hasEnoughPoint(needPoint)){
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		}
		
		player.wealthHolder.decreasePoint(needPoint, BillType.get(PokerBillTypes.BUY_STORE_STRENGTH),"");
		StaticPlayerGrade staticData = StaticPlayerGrade.get(player.grade);
		player.strength = staticData.getMaxStrength();
		int buyStrengthTimes = player.storeHolder.incBuyStrengthTimes();
		return new BuyStrengthDto(player.strength,buyStrengthTimes);
	}
	
	/**
	 * 单次抽奖,1-5星
	 */
	public PetDto buyPetOneTimes(){
		PokerPlayer player = playerManager.getRequestPlayer();
		if(player.petHolder.tryAddPet(1)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		int needPoint = StoreHolder.BUY_PET_ONE_TIMES_NEED_POINT;
		if(!player.wealthHolder.hasEnoughPoint(needPoint)){
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		}
		if(player.petHolder.tryAddPet(1)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		
		player.wealthHolder.decreasePoint(needPoint, BillType.get(PokerBillTypes.BUY_STORE_PET),"");
		player.storeHolder.incBuyPetTimes();
		
		StaticPet staticPet = player.storeHolder.randomPet(1, 5);
		Pet pet = player.petHolder.beOverlayOwned(staticPet.getId());
		return pet.toDto();
	}
	
	/**
	 * 抽奖10次,1-5星
	 */
	public BuyPetTenTimesDto buyPetTenTimes(){
		PokerPlayer player = playerManager.getRequestPlayer();
		if(player.petHolder.tryAddPet(10)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		
		int needPoint = StoreHolder.BUY_PET_TEN_TIMES_NEED_POINT;
		if(!player.wealthHolder.hasEnoughPoint(needPoint)){
			throw new GeneralException(PokerErrorCodes.POINT_NOT_ENOUGH);
		}
		if(player.petHolder.tryAddPet(10)){
			throw new GeneralException(PokerErrorCodes.PET_MAX_COUNT);
		}
		
		player.wealthHolder.decreasePoint(needPoint, BillType.get(PokerBillTypes.BUY_STORE_PET),"");
		
		List<PetDto> petDtos = new ArrayList<PetDto>();
		for(int i=0;i<10;i++){
			StaticPet staticPet = player.storeHolder.randomPet(1, 5);
			player.storeHolder.incBuyPetTimes();
			Pet pet = player.petHolder.beOverlayOwned(staticPet.getId());
			petDtos.add(pet.toDto());
		}
		return new BuyPetTenTimesDto(petDtos);
	}
	
	
	
}
