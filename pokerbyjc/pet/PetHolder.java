package com.chitu.poker.pet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.RandomUtils;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.mail.model.MailAttachment;

import com.chitu.poker.battle.HandEvaluator.ModelType;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.data.StaticPetSkill;
import com.chitu.poker.data.StaticSkill;
import com.chitu.poker.data.StaticTensei;
import com.chitu.poker.mail.PokerMail;
import com.chitu.poker.mail.PokerMailManager;
import com.chitu.poker.mail.msg.SystemNotifyMailDto.SystemNotifyType;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.service.PokerPlayerManager;

/**
 * 宠物操作类
 * @author open
 *
 */
public class PetHolder {
	
	/**宠物技能升级几率分母**/
	public static final int SKILL_UPDATE_RATE_LIMIT = 10000;

	private long playerId;
	
	private Map<Long,Pet> pets = new ConcurrentHashMap<Long,Pet>();
	
	private List<PetTeam> teams = new ArrayList<PetTeam>();
	
	/**宠物栏最大值**/
	private int petMaxCount = 30;
	
	private PersistPet persist;
	
	private boolean update;

	private boolean needSave;
	
	
	public PetHolder(long playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * 持久化
	 */
	public void persist() {
		if (persist == null) {
			return;
		}

		persist.setPets(PersistPet.petsData(this.pets));
		persist.setTeams(PersistPet.teamsData(this.teams));
		persist.setPetMaxCount(this.petMaxCount);

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
		if (pets != null) {
			pets.clear();
		}
		if(teams != null){
			teams.clear();
		}
	}
	
	private void init() {
		if (persist != null) {
			return;
		}
		
		persist = PersistPet.get(playerId);
		if (persist != null) {
			
			this.pets = PersistPet.initPets(persist.getPets());
			this.teams = PersistPet.initTeams(persist.getTeams());
			this.petMaxCount = persist.getPetMaxCount();
			
		} else {
			needSave = true;
			persist = new PersistPet();
			persist.setId(this.playerId);
			persist.setPetMaxCount(this.petMaxCount);
		}
	}
	
	/**
	 * 获取宠物
	 * @param uniqueId
	 * @return
	 */
	public Pet getPet(long uniqueId){
		init();
		return this.pets.get(uniqueId);
	}
	
	/**
	 * 宠物栏是否会溢出
	 * @param petCount
	 * @return
	 */
	public boolean tryAddPet(int petCount){
		init();
		if(this.pets.size()+petCount > this.petMaxCount){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加宠物,宠物栏空间不足则发邮件
	 * @param staticId
	 * @return
	 */
	public Pet addPet(int staticId){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		map.put(staticId, 1);
		List<Pet> pets = addPet(map);
		return pets.get(0);
	}
	
	/**
	 * 添加宠物,宠物栏空间不足则发邮件
	 * @param staticId
	 * @return
	 */
	public List<Pet> addPet(int[] staticId){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int id : staticId){
			map.put(id, 1);
		}
		return addPet(map);
	}
	
	/**
	 * 添加宠物,宠物栏空间不足则发邮件
	 * @param staticId
	 * @return
	 */
	public List<Pet> addPet(Map<Integer,Integer> staticId){
		init();
		
		List<Pet> pets = new ArrayList<Pet>();
		if(staticId.size() > this.petMaxCount){
			List<MailAttachment> packet = new ArrayList<MailAttachment>();
			for(Map.Entry<Integer, Integer> entry : staticId.entrySet()){
				MailAttachment attachment = new MailAttachment();
				attachment.itemId = entry.getKey();
				attachment.itemCount = entry.getValue();
				packet.add(attachment);
			}
			PokerMailManager mailManager = SpringUtils.getBeanOfType(PokerMailManager.class);
			PokerMail mail = mailManager.systemNotifyMail(SystemNotifyType.PetColumn, packet);
			mailManager.sendMail(mail, this.playerId);
			throw new GeneralException(PokerErrorCodes.PET_MAIL_SENDED);
			
		}else{
			for(Map.Entry<Integer, Integer> entry : staticId.entrySet()){
				int count = entry.getValue();
				for(int i=0;i<count;i++){
					pets.add(beOverlayOwned(entry.getKey()));
				}
			}
		}
		return pets;
	}
	
	/**
	 * 添加宠物,无视宠物栏上限
	 * @param staticId
	 * @return
	 */
	public Pet beOverlayOwned(int staticId){
		init();
		
		StaticPet staticPet = StaticPet.get(staticId);
		if(staticPet == null){
			throw new GeneralException(PokerErrorCodes.PET_NOT_EXIST);
		}
		
		Pet pet = Pet.getInstance(staticPet);
		this.pets.put(pet.uniqueId, pet);
		this.update = true;
		return pet;
	}
	
	/**
	 * 宠物列表
	 * @return
	 */
	public Collection<Pet> listPet(){
		init();
		return this.pets.values();
	}
	
	/**
	 * 移除宠物
	 * @param petId
	 * @return
	 */
	public Pet deletePet(long uniqueId){
		init();
		Pet pet = this.pets.remove(uniqueId);
		this.update = true;
		return pet;
	}
	
	/**
	 * 宠物栏上限
	 * @return
	 */
	public int getPetMaxCount(){
		init();
		return this.petMaxCount;
	}
	
	/**
	 * 扩充宠物栏
	 * @param count
	 */
	public void incPetMaxCount(int count){
		init();
		this.petMaxCount += count;
		this.update = true;
	}
	
	/**
	 * 获取队伍
	 * @return
	 */
	public PetTeam getTeam(){
		init();
		if(this.teams.size() == 0){
			PetTeam team = PetTeam.getInstance();
			this.teams.add(team);
			this.update = true;
		}
		return this.teams.get(0);
	}
	
	/**
	 * 当前队伍主宠
	 * @return
	 */
	public Pet getTeamMainPet(){
		init();
		PetTeam team = getTeam();
		return getPet(team.pet[0]);
	}
	
	
	/**
	 * 宠物离开队伍
	 * @param petId
	 */
	public void leaveTeam(long uniqueId){
		init();
		PetTeam team = getTeam();
		for(int i=0;i<team.pet.length;i++){
			if(team.pet[i] == uniqueId){
				team.pet[i] = 0;
			}
		}
		
		Pet pet = getPet(uniqueId);
		pet.inAction = false;
		this.update = true;
	}
	
	/**
	 * 宠物加入队伍
	 * @param petId
	 * @param index
	 */
	public void joinTeam(long uniqueId,int index){
		init();
		PetTeam team = getTeam();
		long oldUniqueId = team.pet[index];
		team.pet[index] = uniqueId;
		
		if(oldUniqueId > 0){
			Pet oldPet = getPet(oldUniqueId);
			oldPet.inAction = false;
		}
		Pet pet = getPet(uniqueId);
		pet.inAction = true;
		this.update = true;
	}
	
	/**
	 * 强化
	 * @param basePet
	 * @param materialPet
	 */
	public void forging(Pet basePet,List<Pet> materialPet){
		PokerPlayer player = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(playerId);
		StaticPet staticBase = StaticPet.get(basePet.staticId);
		
		int advanHp = basePet.advanHp;
		int advanHurt = basePet.advanHurt;
		int addExp = 0;
		for(Pet pet : materialPet){
			StaticPet staticData = StaticPet.get(pet.staticId);
			advanHp += pet.advanHp;
			advanHurt += pet.advanHurt;
			addExp += staticData.getForgingExp();
		}
		if(advanHp >= Pet.ADVAN_MAX_VALUE){
			advanHp = Pet.ADVAN_MAX_VALUE;
		}
		if(advanHurt >= Pet.ADVAN_MAX_VALUE){
			advanHurt = Pet.ADVAN_MAX_VALUE;
		}
		int needMoney = staticBase.getGrade() * 100 * materialPet.size() + (advanHp + advanHurt) * 1000;
		if(!player.wealthHolder.hasEnoughMoney(needMoney)){
			throw new GeneralException(PokerErrorCodes.MONEY_NOT_ENOUGH);
		}
		
		player.wealthHolder.decreaseMoney(needMoney, BillType.get(PokerBillTypes.FORGING_PET), String.valueOf(basePet.uniqueId));
		basePet.advanHp = advanHp;
		basePet.advanHurt = advanHurt;
		basePet.incExp(addExp);
		
		staticBase = StaticPet.get(basePet.staticId);
		int maxHp = staticBase.getHp() + basePet.advanHp * Pet.ADVAN_HP_VALUE;
		basePet.hp += maxHp - basePet.maxHp;
		basePet.maxHp = maxHp;
		basePet.modelAttack = staticBase.modelAttack(basePet.advanHurt * Pet.ADVAN_ATTACK_VALUE);
		
		skillUpdate(basePet,materialPet);
		for(Pet p : materialPet){
			deletePet(p.uniqueId);
		}
		this.update = true;
	}
	
	/**
	 * 强化,宠物技能升级
	 * @param basePet
	 * @param materialPet
	 */
	private void skillUpdate(Pet basePet, List<Pet> materialPet){
		Map<ModelType,Integer> map = new HashMap<ModelType,Integer>();
		for(Map.Entry<ModelType, Integer> entry : basePet.modelSkill.entrySet()){
			ModelType modelType = entry.getKey();
			int staticId = entry.getValue();
			StaticSkill staticSkill = StaticSkill.get(staticId);
			StaticSkill maxGradeSkill = StaticSkill.getMaxGrade(staticSkill.getSkillId());
			if(staticSkill.getGrade() == maxGradeSkill.getGrade()){
				continue;
			}
			
			int updateRate = curSkillUpdateRate(staticSkill.getSkillId(),materialPet);
			if(updateRate > 0){
				updateRate += staticSkill.getUpdateRate();
				boolean hit = RandomUtils.randomHit(SKILL_UPDATE_RATE_LIMIT, updateRate);
				if(hit){
					StaticSkill nextSkill = StaticSkill.get(staticSkill.getSkillId(), staticSkill.getGrade() + 1);
					map.put(modelType, nextSkill.getId());
				}
			}
		}
		
		for(Map.Entry<ModelType, Integer> entry : map.entrySet()){
			ModelType modelType = entry.getKey();
			int staticId = entry.getValue();
			basePet.modelSkill.put(modelType, staticId);
		}
	}
	
	/**
	 * 合计材料宠物提供的技能升级成功率
	 * @param skillId
	 * @param materialPet
	 * @return
	 */
	private int curSkillUpdateRate(int skillId,List<Pet> materialPet){
		int updateRate = 0;
		for(Pet pet : materialPet){
			for(int staticId : pet.modelSkill.values()){
				StaticSkill staticData = StaticSkill.get(staticId);
				if(staticData.getSkillId() == skillId){
					updateRate += staticData.getUpdateRate();
				}
			}
		}
		return updateRate;
	}
	
	/**
	 * 进阶
	 * @param pet
	 */
	public void tensei(Pet pet,List<Pet> materialPet){
		StaticPet staticPet = StaticPet.get(pet.staticId);
		if(!staticPet.isMaxGrade()){
			throw new GeneralException(PokerErrorCodes.PET_GRADE_NOT_MAX);
		}
		StaticTensei staticTensei = StaticTensei.get(staticPet.getPetId());
		if(staticTensei == null){
			throw new GeneralException(PokerErrorCodes.NOT_TENSEI_PET);
		}
		PokerPlayer player = SpringUtils.getBeanOfType(PokerPlayerManager.class).getAnyPlayerById(playerId);
		int needMoney = staticTensei.getNeedMoney();
		if(!player.wealthHolder.hasEnoughMoney(needMoney)){
			throw new GeneralException(PokerErrorCodes.MONEY_NOT_ENOUGH);
		}
		List<Integer> needPetIds = staticTensei.needPetId();
		if(needPetIds.size() != materialPet.size()){
			throw new GeneralException(PokerErrorCodes.TENSEI_MATERIAL_NOT_ENOUGH);
		}
		//检查材料宠物合法性
		for(int petId : needPetIds){
			boolean check = false;
			for(Pet p : materialPet){
				StaticPet data = StaticPet.get(p.staticId);
				if(petId == data.getPetId()){
					check = true;
				}
			}
			if(!check){
				throw new GeneralException(PokerErrorCodes.TENSEI_MATERIAL_NOT_ENOUGH);
			}
		}
		
		//消耗金币,宠物,变成新宠物
		player.wealthHolder.decreaseMoney(needMoney, BillType.get(PokerBillTypes.TENSEI_PET), String.valueOf(pet.uniqueId));
		for(Pet p : materialPet){
			deletePet(p.uniqueId);
		}
		
		StaticPet staticTarget = StaticPet.get(staticTensei.getTargetStaticId());
		StaticPetSkill staticPetSkill = StaticPetSkill.get(staticTarget.getPetId());
		
		pet.staticId = staticTarget.getId();
		pet.advanHp += staticPetSkill.getAdvanHp();
		if(pet.advanHp >= Pet.ADVAN_MAX_VALUE){
			pet.advanHp = Pet.ADVAN_MAX_VALUE;
		}
		pet.maxHp = staticTarget.getHp() + pet.advanHp * Pet.ADVAN_HP_VALUE;
		pet.hp = pet.maxHp;
		
		pet.advanHurt += staticPetSkill.getAdvanHurt();
		if(pet.advanHurt >= Pet.ADVAN_MAX_VALUE){
			pet.advanHurt = Pet.ADVAN_MAX_VALUE;
		}
		pet.modelAttack = staticTarget.modelAttack(pet.advanHurt * Pet.ADVAN_ATTACK_VALUE);
		pet.modelSkill = staticPetSkill.modelSkill();
		this.update = true;
	}
	
	
	
	
	
}
