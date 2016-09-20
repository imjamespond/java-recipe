package com.chitu.poker.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.broadcast.RequestUtils;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.IdUtils;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.commons.utils.ValidatorUtils;
import cn.gecko.persist.GenericDao;

import com.chitu.poker.battle.BattleManager;
import com.chitu.poker.battle.PveBattle;
import com.chitu.poker.battle.msg.BattleLineResult;
import com.chitu.poker.battle.msg.BattleResult;
import com.chitu.poker.battle.msg.PetAttackResult;
import com.chitu.poker.battle.msg.PveRoundResult;
import com.chitu.poker.data.StaticPet;
import com.chitu.poker.model.PersistPokerAccount;
import com.chitu.poker.model.PersistPokerPlayer;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.msg.PokerPlayerDto;
import com.chitu.poker.msg.RoleDto;
import com.chitu.poker.pet.Pet;
import com.chitu.poker.pet.PetTeam;
import com.chitu.poker.service.PokerPlayerManager;

@Controller
public class LoginController extends MultiGeneralController {

	@Autowired
	private PokerPlayerManager playerManager;

	/**
	 * 本地版本,注册
	 * 
	 * @param accountId
	 * @param password
	 * @param confirmPassword
	 */
	public void register(String accountId, String password, String confirmPassword) {
		PersistPokerAccount persistAccount = SpringUtils.getBeanOfType(GenericDao.class).get(PersistPokerAccount.class,
				"where accountId=?", accountId);
		if (persistAccount != null) {
			throw new GeneralException(PokerErrorCodes.ACCOUNT_EXIST);
		}
		if (!password.equals(confirmPassword)) {
			throw new GeneralException(PokerErrorCodes.PASSWORD_NOT_SAME);
		}

		persistAccount = new PersistPokerAccount();
		persistAccount.setId(IdUtils.generateLongId());
		persistAccount.setAccountId(accountId);
		persistAccount.setPassword(password);
		persistAccount.save();
	}

	/**
	 * 本地版本,密码登陆
	 * 
	 * @param accountId
	 * @param password
	 * @param ip
	 * @return
	 */
	public RoleDto passwordLogin(String accountId, String password, String ip) {
		PersistPokerAccount account = SpringUtils.getBeanOfType(GenericDao.class).get(PersistPokerAccount.class,
				"where accountId=?", accountId);
		if (account == null) {
			throw new GeneralException(PokerErrorCodes.ACCOUNT_NOT_EXIST);
		}
		if (!account.getPassword().equals(password)) {
			throw new GeneralException(PokerErrorCodes.PASSWORD_ERROR);
		}

		List<PersistPokerPlayer> persistPlayer = SpringUtils.getBeanOfType(GenericDao.class).getAll(
				PersistPokerPlayer.class, 1, "where accountId=?", accountId);
		if (persistPlayer.size() > 0) {
			return new RoleDto(persistPlayer.get(0));
		}
		return null;
	}

	/**
	 * 获取此帐号的角色
	 * 
	 * @param accountId
	 * @return
	 */
	public RoleDto getPlayer(String accountId) {
		PersistPokerPlayer player = SpringUtils.getBeanOfType(GenericDao.class).get(PersistPokerPlayer.class,
				"where accountId=?", accountId);
		if (player != null) {
			return new RoleDto(player);
		}
		return null;
	}

	/**
	 * 检查玩家呢称
	 * 
	 * @param nickName
	 */
	public void checkNickName(String nickName) {
		if (StringUtils.isBlank(nickName)) {
			throw new GeneralException(PokerErrorCodes.NICKNAME_IS_NULL);
		}

		ValidatorUtils.checkLength(nickName, 4, 30);
		ValidatorUtils.checkRegKeyword(nickName);

		PersistPokerPlayer persistPlayer = PersistPokerPlayer.getByNickname(nickName);
		if (persistPlayer != null) {
			throw new GeneralException(PokerErrorCodes.NICKNAME_EXIST);
		}
	}

	/**
	 * 创建角色
	 * 
	 * @param accountId
	 * @param nickName
	 * @param defaultPetId
	 *            宠物ID
	 * @param ip
	 * @return
	 */
	public PokerPlayerDto createPlayer(String accountId, String nickName, int defaultPetId, String ip) {
		ValidatorUtils.checkLength(nickName, 4, 30);
		ValidatorUtils.checkRegKeyword(nickName);

		PersistPokerPlayer persistPlayer = new PersistPokerPlayer();
		persistPlayer.setId(IdUtils.generateLongId());
		persistPlayer.setAccountId(accountId);
		persistPlayer.setNickname(nickName);
		persistPlayer.setGrade(1);
		persistPlayer.setStrength(100);
		persistPlayer.setLoginTime(new Timestamp(System.currentTimeMillis()));
		persistPlayer.setCreateTime(new Timestamp(System.currentTimeMillis()));
		persistPlayer.firstLogin = true;
		persistPlayer.save();

		PokerPlayer player = playerManager.registerPlayer(RequestUtils.getCurrentSid(), persistPlayer.getId(),
				persistPlayer.getAccountId());
		player.init(persistPlayer);
		player.ip = ip;
		player.login();

		//Pet pet = player.petHolder.addPet(defaultPetId, 1);
		StaticPet staticPet = StaticPet.get(defaultPetId,1);
		Pet pet = player.petHolder.addPet(staticPet.getId());
		PetTeam petTeam = player.petHolder.getTeam();
		petTeam.pet[0] = pet.uniqueId;
		pet.inAction = true;

		PokerPlayerDto playerDto = new PokerPlayerDto(player, true);
		return playerDto;
	}

	/**
	 * 角色登录
	 * 
	 * @param id
	 *            角色ID
	 * @param accountId
	 *            账号ID
	 * @param ip
	 *            ip地址
	 * @return
	 */
	public PokerPlayerDto login(String id, String accountId, String ip) {
		PersistPokerPlayer persistPlayer = PersistPokerPlayer.get(Long.valueOf(id));
		if (persistPlayer == null) {
			throw new GeneralException(PokerErrorCodes.PLAYER_NOT_EXIST);
		}
		persistPlayer.firstLogin = false;

		PokerPlayer player = playerManager.registerPlayer(RequestUtils.getCurrentSid(), persistPlayer.getId(),
				persistPlayer.getAccountId());
		player.init(persistPlayer);
		player.ip = ip;
		player.login();
		PokerPlayerDto playerDto = new PokerPlayerDto(player, true);
		return playerDto;
	}

	 void testBattle() {
		PveBattleController pve = SpringUtils.getBeanOfType(PveBattleController.class);
		BattleManager battleManager = SpringUtils.getBeanOfType(BattleManager.class);
		pve.startPveBattle(1);
		PokerPlayer player = playerManager.getRequestPlayer();
		PveBattle battle = (PveBattle) battleManager.getBattle(player.battleHolder.currentBattleId);
		System.out.println(battle.myMember.modelAttack);
		System.out.println(battle.antiMember.modelAttack);
		int roundIndex = 1;
		while (true) {
			PveRoundResult roundResult = null;
			System.out.println("###################################################");
			int[] ai = battle.ai(battle.myMember);
			if (ai == null) {
				roundResult = pve.pass();
				System.out.println(roundIndex + " my pass");
			} else {
				roundResult = pve.putPoker(ai[0], ai[1], ai[2], ai[3]);
				System.out.println(roundIndex + " put poker " + Arrays.toString(ai));
			}
			printRoundResult(battle, roundResult);
			roundIndex = roundResult.getNextRoundIndex();
		}
	}

	private void printRoundResult(PveBattle battle, PveRoundResult roundResult) {
		System.out.println("#########################");
		if (roundResult.getMyResult() == null) {
			System.out.println("my result null");
		} else {
			System.out.println("my result:");
			BattleResult result = roundResult.getMyResult();
			System.out.println("\t win:" + result.isWin());
			System.out.println("\t rewardMoney:" + result.getRewardMoney());
			System.out.println("\t rewardPets:" + Arrays.toString(result.getRewardPets()));
			System.out.println("\t lineResults:");
			for (BattleLineResult lineResult : result.getLineResults()) {
				System.out.println("\t\t lineIndex:" + lineResult.getLineIndex());
				System.out.println("\t\t pokerType:" + lineResult.getPokersType());
				System.out.println("\t\t addPower:" + lineResult.getAddPower());
				System.out.println("\t\t dead:" + lineResult.isDead());
				System.out.println("\t\t petAttacks:");
				for (PetAttackResult petAttackResult : lineResult.getPetAttacks()) {
					System.out.println("\t\t\t " + petAttackResult.getPetId() + ":" + petAttackResult.getAttackLife());
				}

			}
		}
		//
		System.out.println("#########################");
		if (roundResult.getMyReset() == null) {
			System.out.println("my reset null");
		} else {
			System.out.println("my reset");
		}
		//
		System.out.println("#########################");
		if (roundResult.getAntiSendPoker() == null) {
			System.out.println("anti send poker null");
		} else {
			System.out.println("anti send poker:");
			System.out.println("\t sends:" + Arrays.toString(roundResult.getAntiSendPoker().getPokers()));
		}
		//
		System.out.println("#########################");
		if (roundResult.getAntiPutPoker() == null) {
			System.out.println("anti pass");
		} else {
			System.out.println("anti put poker:");
			System.out.println("\t " + roundResult.getAntiPutPoker().getTableIndex1() + ":"
					+ roundResult.getAntiPutPoker().getTableIndex1());
			System.out.println("\t " + roundResult.getAntiPutPoker().getTableIndex2() + ":"
					+ roundResult.getAntiPutPoker().getTableIndex2());
		}
		//
		System.out.println("#########################");
		if (roundResult.getAntiResult() == null) {
			System.out.println("anti result null");
		} else {
			System.out.println("anti result:");
			BattleResult result = roundResult.getAntiResult();
			System.out.println("\t win:" + result.isWin());
			System.out.println("\t rewardMoney:" + result.getRewardMoney());
			System.out.println("\t rewardPets:" + Arrays.toString(result.getRewardPets()));
			System.out.println("\t lineResults:");
			for (BattleLineResult lineResult : result.getLineResults()) {
				System.out.println("\t\t lineIndex:" + lineResult.getLineIndex());
				System.out.println("\t\t pokerType:" + lineResult.getPokersType());
				System.out.println("\t\t addPower:" + lineResult.getAddPower());
				System.out.println("\t\t dead:" + lineResult.isDead());
				System.out.println("\t\t petAttacks:");
				for (PetAttackResult petAttackResult : lineResult.getPetAttacks()) {
					System.out.println("\t\t\t " + petAttackResult.getPetId() + ":" + petAttackResult.getAttackLife());
				}

			}
		}
		//
		System.out.println("#########################");
		if (roundResult.getAntiReset() == null) {
			System.out.println("anti reset null");
		} else {
			System.out.println("anti reset:");
		}
		//
		System.out.println("#########################");
		if (roundResult.getMySendPoker() == null) {
			System.out.println("my send poker null");
		} else {
			System.out.println("my send poker:");
			System.out.println("\t sends:" + Arrays.toString(roundResult.getMySendPoker().getPokers()));
		}
	}

}
