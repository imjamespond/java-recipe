package com.chitu.poker.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import cn.gecko.commons.model.GeneralException;

import com.chitu.poker.battle.Card.Rank;
import com.chitu.poker.battle.Card.Suit;
import com.chitu.poker.battle.HandEvaluator.ModelType;
import com.chitu.poker.battle.msg.BattleLineResult;
import com.chitu.poker.battle.msg.BattleResult;
import com.chitu.poker.battle.msg.PetAttackResult;
import com.chitu.poker.battle.msg.PokerResetResult;
import com.chitu.poker.battle.msg.PutPokerResult;
import com.chitu.poker.battle.msg.PveRoundResult;
import com.chitu.poker.battle.msg.SendPokerResult;
import com.chitu.poker.model.PokerErrorCodes;
import com.chitu.poker.model.PokerPlayer;
import com.chitu.poker.pet.Pet;

/**
 * 一次副本战斗的内存数据
 * 
 * @author ivan
 * 
 */
public class Battle {
	public static int RELIVE_NEED_POINT = 5;

	public static int MEMBER_POKER_COUNT = 4;

	public static int MEMBER_PET_COUNT = 6;

	public static int MEMBER_MAX_POWER = 100;

	// 8组配对点
	public final static int[][] PAIR_POINT = new int[][] { { 0, 24 }, { 1, 21 }, { 2, 22 }, { 3, 23 }, { 4, 20 },
			{ 5, 9 }, { 10, 14 }, { 15, 19 } };

	// 8组配对点对应的牌型线
	public final static int[] PAIR_POINT_LINE = new int[] { 10, 6, 7, 8, 11, 1, 2, 3 };

	// 12组牌型线
	public final static int[][] POKERS_LINE = new int[][] {
			//
			{ 0, 1, 2, 3, 4 }, { 5, 6, 7, 8, 9 }, { 10, 11, 12, 13, 14 }, { 15, 16, 17, 18, 19 },
			{ 20, 21, 22, 23, 24 },
			//
			{ 0, 5, 10, 15, 20 }, { 1, 6, 11, 16, 21 }, { 2, 7, 12, 17, 22 }, { 3, 8, 13, 18, 23 },
			{ 4, 9, 14, 19, 24 },
			//
			{ 0, 6, 12, 18, 24 }, { 4, 8, 12, 16, 20 } };

	// 9个初始牌点
	public final static int[] INIT_POINT = new int[] { 6, 7, 8, 11, 12, 13, 16, 17, 18 };

	// 4张牌中两张的所有组合
	public final static int[][] FOUR_POKER_COMPOSE = new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 },
			{ 2, 3 } };

	// key:放牌点位置，value:牌型线数组位置，斜横竖的顺序
	@SuppressWarnings("serial")
	public final static Map<Integer, int[]> POINT2LINE_MAP = new HashMap<Integer, int[]>(PAIR_POINT.length * 2) {
		{
			put(0, new int[] { 10, 0, 5 });
			put(1, new int[] { 0, 6 });
			put(2, new int[] { 0, 7 });
			put(3, new int[] { 0, 8 });
			put(4, new int[] { 11, 0, 9 });
			put(5, new int[] { 1, 5 });
			put(9, new int[] { 1, 9 });
			put(10, new int[] { 2, 5 });
			put(14, new int[] { 2, 9 });
			put(15, new int[] { 3, 5 });
			put(19, new int[] { 3, 9 });
			put(20, new int[] { 11, 4, 5 });
			put(21, new int[] { 4, 6 });
			put(22, new int[] { 4, 7 });
			put(23, new int[] { 4, 8 });
			put(24, new int[] { 10, 4, 9 });

		}
	};

	protected static final AtomicInteger BATTLE_ID_GENERATOR = new AtomicInteger(1);

	public int id;

	/**
	 * 0---1---2---3---4 <br>
	 * 5---6---7---8---9 <br>
	 * 10--11--12--13--14 <br>
	 * 15--16--17--18--19 <br>
	 * 20--21--22--23--24 <br>
	 */
	public Card[] battleTable = new Card[25];

	public int roundIndex = 1;

	protected List<Card> battlePokers;

	public BattleMember myMember;// 我方

	public BattleMember antiMember;// 对方

	protected PokerPlayer player;

	public Battle() {
		id = BATTLE_ID_GENERATOR.getAndIncrement();
	}

	/**
	 * 初始化台面的牌
	 */
	protected void initTablePokers() {
		for (int index : INIT_POINT) {
			battleTable[index] = battlePokers.remove(0);
		}
	}

	/**
	 * 重置台面的牌，清除+初始化
	 */
	public void resetTablePokers() {
		clearTable();
		initTablePokers();
	}

	/**
	 * 清除台面的牌
	 */
	protected void clearTable() {
		for (int i = 0; i < battleTable.length; i++) {
			battleTable[i] = null;
		}
	}

	/**
	 * 是否为配对点
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean isPairPoint(int p1, int p2) {
		if (p1 == p2)
			return false;
		for (int i = 0; i < PAIR_POINT.length; i++) {
			int[] pair = PAIR_POINT[i];
			if ((p1 == pair[0] && p2 == pair[1]) || (p1 == pair[1] && p2 == pair[0]))
				return true;
		}
		return false;
	}

	/**
	 * 台面的牌是否已满
	 * 
	 * @return
	 */
	public boolean isTableFull() {
		for (int i = 0; i < battleTable.length; i++) {
			if (battleTable[i] == null)
				return false;
		}
		return true;
	}

	/**
	 * 是否允许放置牌
	 * 
	 * @param member
	 * @param tableIndex1
	 * @param pokerIndex1
	 * @param tableIndex2
	 * @param pokerIndex2
	 */
	protected void checkPut(BattleMember member, int tableIndex1, int pokerIndex1, int tableIndex2, int pokerIndex2) {
		if (battleTable[tableIndex1] != null || battleTable[tableIndex2] != null)
			throw new GeneralException(PokerErrorCodes.POKER_POINT_NOT_EMPTY);
		if (pokerIndex1 < 0 || pokerIndex1 >= Battle.MEMBER_POKER_COUNT || pokerIndex2 < 0
				|| pokerIndex2 >= Battle.MEMBER_POKER_COUNT || pokerIndex1 == pokerIndex2)
			throw new GeneralException(PokerErrorCodes.NOT_OWN_POKER);

		if (!isPairLineOk(member, tableIndex1, pokerIndex1, tableIndex2, pokerIndex2))
			throw new GeneralException(PokerErrorCodes.POKER_MODEL_NONE);
	}

	/**
	 * 放牌的那条牌型线是否能凑成牌型
	 * 
	 * @param member
	 * @param tableIndex1
	 * @param pokerIndex1
	 * @param tableIndex2
	 * @param pokerIndex2
	 * @return
	 */
	protected boolean isPairLineOk(BattleMember member, int tableIndex1, int pokerIndex1, int tableIndex2,
			int pokerIndex2) {
		int lineIndex = findPairPointLine(tableIndex1, tableIndex2);
		if (lineIndex < 0)
			return false;
		int[] oneLine = POKERS_LINE[lineIndex];
		if (battleTable[oneLine[1]] == null || battleTable[oneLine[2]] == null || battleTable[oneLine[3]] == null)
			return false;
		Card[] cards = new Card[] { member.pokers[pokerIndex1], battleTable[oneLine[1]], battleTable[oneLine[2]],
				battleTable[oneLine[3]], member.pokers[pokerIndex2] };
		HandEvaluator evaluator = HandEvaluator.createHandEvaluator(cards);
		return evaluator.getModelType() != ModelType.NONE;
	}

	/**
	 * 根据配对点找出对应的牌型线
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	protected int findPairPointLine(int p1, int p2) {
		if (p1 == p2)
			return -1;
		for (int i = 0; i < PAIR_POINT.length; i++) {
			int[] pair = PAIR_POINT[i];
			if ((p1 == pair[0] && p2 == pair[1]) || (p1 == pair[1] && p2 == pair[0]))
				return PAIR_POINT_LINE[i];
		}
		return -1;
	}

	/**
	 * 找出还没有放牌的配对点
	 * 
	 * @return 配对点数组索引的列表
	 */
	protected List<Integer> findEmptyPairPoint() {
		List<Integer> indexs = new ArrayList<Integer>(PAIR_POINT.length);
		for (int i = 0; i < PAIR_POINT.length; i++) {
			int[] pair = PAIR_POINT[i];
			if (battleTable[pair[0]] == null && battleTable[pair[1]] == null)
				indexs.add(i);
		}
		return indexs;
	}

	/**
	 * 获得所有有牌型的牌
	 * 
	 * @param tableIndex1
	 *            台面位置1
	 * @param tableIndex2
	 *            台面位置2
	 * @return Map key:牌型线，value:牌型
	 */
	protected Map<Integer, HandEvaluator> getEvaluators(int tableIndex1, int tableIndex2) {
		Map<Integer, HandEvaluator> evaluators = new HashMap<Integer, HandEvaluator>(5);
		int[] lineIndexs1 = POINT2LINE_MAP.get(tableIndex1);
		int[] lineIndexs2 = POINT2LINE_MAP.get(tableIndex2);
		Set<Integer> doneSet = new HashSet<Integer>(5);
		//
		for (int i = 0; i < lineIndexs1.length; i++) {
			int[] oneLine = POKERS_LINE[lineIndexs1[i]];
			if (battleTable[oneLine[0]] == null || battleTable[oneLine[1]] == null || battleTable[oneLine[2]] == null
					|| battleTable[oneLine[3]] == null || battleTable[oneLine[4]] == null)
				continue;
			Card[] cards = new Card[] { battleTable[oneLine[0]], battleTable[oneLine[1]], battleTable[oneLine[2]],
					battleTable[oneLine[3]], battleTable[oneLine[4]] };
			doneSet.add(lineIndexs1[i]);
			HandEvaluator evaluator = HandEvaluator.createHandEvaluator(cards);
			if (evaluator.getModelType() == ModelType.NONE)
				continue;
			evaluators.put(lineIndexs1[i], evaluator);

		}
		//
		for (int i = 0; i < lineIndexs2.length; i++) {
			if (doneSet.contains(lineIndexs2[i]))
				continue;
			int[] oneLine = POKERS_LINE[lineIndexs2[i]];
			if (battleTable[oneLine[0]] == null || battleTable[oneLine[1]] == null || battleTable[oneLine[2]] == null
					|| battleTable[oneLine[3]] == null || battleTable[oneLine[4]] == null)
				continue;
			Card[] cards = new Card[] { battleTable[oneLine[0]], battleTable[oneLine[1]], battleTable[oneLine[2]],
					battleTable[oneLine[3]], battleTable[oneLine[4]] };
			HandEvaluator evaluator = HandEvaluator.createHandEvaluator(cards);
			if (evaluator.getModelType() == ModelType.NONE)
				continue;
			evaluators.put(lineIndexs2[i], evaluator);
		}
		return evaluators;
	}

	public void destroy() {
		clearTable();
	}

	/**
	 * 从未派的牌中移除指定的牌
	 */
	protected void removePokerFromRestPokers(Rank rank, Suit suit) {
		int index = -1;
		for (int i = 0; i < battlePokers.size(); i++) {
			Card card = battlePokers.get(i);
			if (card.rank == rank && card.suit == suit) {
				index = i;
				break;
			}
		}
		if (index >= 0)
			battlePokers.remove(index);
	}

	/**
	 * 创建一副新牌，要去掉双方手上持有的牌
	 */
	protected synchronized void createNewSetPoker() {
		battlePokers = new ArrayList<Card>(54);
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				if (!inMemberPokers(i, j))
					battlePokers.add(new Card(Rank.from(i), Suit.from(j)));
			}
		}
		if (!inMemberPokers(Rank.JOKER.ordinal(), Suit.BLACK.ordinal()))
			battlePokers.add(new Card(Rank.JOKER, Suit.BLACK));
		if (!inMemberPokers(Rank.JOKER.ordinal(), Suit.RED.ordinal()))
			battlePokers.add(new Card(Rank.JOKER, Suit.RED));
		// 打乱
		Collections.shuffle(battlePokers);
	}

	/**
	 * 是否为双方手上持有的牌
	 * 
	 * @param rank
	 * @param suit
	 * @return
	 */
	protected boolean inMemberPokers(int rank, int suit) {
		for (Card card : myMember.pokers) {
			if (card == null)
				continue;
			if (rank == card.rank.ordinal() && suit == card.suit.ordinal())
				return true;
		}
		for (Card card : antiMember.pokers) {
			if (card == null)
				continue;
			if (rank == card.rank.ordinal() && suit == card.suit.ordinal())
				return true;
		}
		return false;
	}

	/**
	 * 剩余的牌是否足够发下一次的牌
	 * 
	 * @return
	 */
	protected boolean haveEnoughRest(BattleMember member) {
		int needPokerCount = 0;
		for (Card poker : member.pokers) {
			if (poker == null)
				needPokerCount++;
		}
		return battlePokers.size() >= needPokerCount;
	}

	protected BattleMember getAnotherMember(BattleMember member) {
		if (member == antiMember)
			return myMember;
		return antiMember;
	}

	protected PokerPlayer getPlayer(BattleMember member) {
		return player;
	}

	/**
	 * 补满牌给用户
	 * 
	 * @param member
	 */
	protected void givePokerToMember(BattleMember member) {
		for (int i = 0; i < member.pokers.length; i++) {
			if (member.pokers[i] == null)
				member.pokers[i] = battlePokers.remove(0);
		}
	}

	protected void printBattle(String desc) {
		// System.out.println("********************* " + desc);
		// System.out.println("****");
		// System.out.println(Arrays.toString(antiMember.pokers));
		// for (int i = 0; i < battleTable.length; i++) {
		// Card card = battleTable[i];
		// System.out.print(card + ",");
		// if ((i + 1) % 5 == 0)
		// System.out.print("\n");
		// }
		// System.out.println(Arrays.toString(myMember.pokers));
		// System.out.println("****");
		// System.out.println("*********************");

	}

	// ////////AI 相关

	/**
	 * ai计算
	 * 
	 * @return int[0]:第一张牌在桌面的位置，int[1]:第一张牌在手上牌的位置，int[2]:第二张牌在桌面的位置，int[3]:
	 *         第二张牌在手上牌的位置
	 */
	public synchronized int[] ai(BattleMember member) {
		List<Integer> validPair = findEmptyPairPoint();
		Map<int[], Integer> allAttacks = new HashMap<int[], Integer>();
		for (Integer index : validPair) {
			int[] pairPoint = PAIR_POINT[index];
			for (int i = 0; i < FOUR_POKER_COMPOSE.length; i++) {
				int[] twoPokerIndex = FOUR_POKER_COMPOSE[i];
				// 不能放牌的情况
				if (!isPairLineOk(member, pairPoint[0], twoPokerIndex[0], pairPoint[1], twoPokerIndex[1]))
					continue;
				calAttack(allAttacks, member, pairPoint, twoPokerIndex);
			}
		}
		return findMaxAttack(allAttacks);
	}

	/**
	 * 计算本组合的攻击效果
	 * 
	 * @param allAttacks
	 *            存放所有攻击组合的容器
	 * @param pairPoint
	 *            配对点
	 * @param twoPokerIndex
	 *            任意两张牌的索引
	 */
	private void calAttack(Map<int[], Integer> allAttacks, BattleMember member, int[] pairPoint, int[] twoPokerIndex) {
		battleTable[pairPoint[0]] = member.pokers[twoPokerIndex[0]];
		battleTable[pairPoint[1]] = member.pokers[twoPokerIndex[1]];
		Map<Integer, HandEvaluator> evaluators = getEvaluators(pairPoint[0], pairPoint[1]);
		int allAttackLife = 0;
		for (Entry<Integer, HandEvaluator> entry : evaluators.entrySet()) {
			ModelType modelType = entry.getValue().getModelType();
			Integer attackLife = member.modelAttack.get(modelType);
			if (attackLife != null)
				allAttackLife += attackLife;
		}
		allAttacks.put(new int[] { pairPoint[0], twoPokerIndex[0], pairPoint[1], twoPokerIndex[1] }, allAttackLife);

		// 交换两个牌再试
		battleTable[pairPoint[0]] = member.pokers[twoPokerIndex[1]];
		battleTable[pairPoint[1]] = member.pokers[twoPokerIndex[0]];
		evaluators = getEvaluators(pairPoint[0], pairPoint[1]);
		allAttackLife = 0;
		for (Entry<Integer, HandEvaluator> entry : evaluators.entrySet()) {
			ModelType modelType = entry.getValue().getModelType();
			Integer attackLife = member.modelAttack.get(modelType);
			if (attackLife != null)
				allAttackLife += attackLife;
		}
		allAttacks.put(new int[] { pairPoint[0], twoPokerIndex[1], pairPoint[1], twoPokerIndex[0] }, allAttackLife);
		// 还原
		battleTable[pairPoint[0]] = null;
		battleTable[pairPoint[1]] = null;
	}

	/**
	 * 找出最高攻击的组合
	 * 
	 * @param allAttacks
	 * 
	 * @return int[0]:第一张牌在桌面的位置，int[1]:第一张牌在手上牌的位置，int[2]:第二张牌在桌面的位置，int[3]:
	 *         第二张牌在手上牌的位置
	 */
	private int[] findMaxAttack(Map<int[], Integer> allAttacks) {
		int[] max = null;
		int maxAttack = -1;
		for (Entry<int[], Integer> entry : allAttacks.entrySet()) {
			if (entry.getValue() > maxAttack) {
				maxAttack = entry.getValue();
				max = entry.getKey();
			}
		}
		return max;
	}

	// ////////玩法相关

	protected boolean isBattleEnd() {
		return true;
	}

	protected int calAddPower(BattleMember member, ModelType modelType) {
		if (member.power >= MEMBER_MAX_POWER) {
			member.power = MEMBER_MAX_POWER;
			return 0;
		}
		int addPower = 0;
		switch (modelType) {
		case NONE:
			return 0;
		case ONE_PAIR:
			addPower = 10;
			break;
		case TWO_PAIR:
			addPower = 12;
			break;
		case THREE_OF_A_KIND:
			addPower = 15;
			break;
		case STRAIGHT:
			addPower = 20;
			break;
		case FLUSH:
			addPower = 25;
			break;
		case FULL_HOUSE:
			addPower = 30;
			break;
		case FOUR_OF_A_KIND:
			addPower = 40;
			break;
		case STRAIGHT_FLUSH:
			addPower = 50;
			break;
		case ROYAL_FLUSH:
			addPower = 60;
			break;
		case JOKER:
			addPower = 100;
			break;
		default:
			return 0;
		}
		if (member.power + addPower > MEMBER_MAX_POWER)
			return MEMBER_MAX_POWER - member.power;
		return addPower;
	}

	protected void myWin(BattleMember member, BattleResult myResult) {

	}

	protected void antiWin() {

	}

	protected int battleResultId() {
		return 0;
	}

	/**
	 * 放弃本回合
	 * 
	 * @param member
	 * @return
	 */
	public PveRoundResult pass(BattleMember member) {
		if (isBattleEnd())
			throw new GeneralException(PokerErrorCodes.BATTLE_CLOSE);
		PveRoundResult roundResult = new PveRoundResult();
		myMember.clearPokers();
		givePokerToMember(antiMember);
		roundResult.setAntiSendPoker(new SendPokerResult(antiMember));
		antiPutPoker(roundResult);
		roundResult.setNextRoundIndex(++roundIndex);
		givePokerToMember(myMember);
		roundResult.setMySendPoker(new SendPokerResult(myMember));
		return roundResult;
	}

	/**
	 * 我方放置两张牌
	 * 
	 * @param member
	 * @param tableIndex1
	 * @param pokerIndex1
	 * @param tableIndex2
	 * @param pokerIndex2
	 * @return
	 */
	public PveRoundResult putPoker(BattleMember member, int tableIndex1, int pokerIndex1, int tableIndex2,
			int pokerIndex2) {
		if (isBattleEnd())
			throw new GeneralException(PokerErrorCodes.BATTLE_CLOSE);
		checkPut(member, tableIndex1, pokerIndex1, tableIndex2, pokerIndex2);
		printBattle("before my put");
		battleTable[tableIndex1] = member.pokers[pokerIndex1];
		battleTable[tableIndex2] = member.pokers[pokerIndex2];
		member.pokers[pokerIndex1] = null;
		member.pokers[pokerIndex2] = null;
		printBattle("after my put");
		PveRoundResult roundResult = new PveRoundResult();
		BattleResult myResult = checkBattleResult(member, tableIndex1, tableIndex2);
		roundResult.setMyResult(myResult);
		// 胜利奖励
		if (myResult.isWin()) {
			myWin(member, myResult);
			return roundResult;
		}
		//

		PokerResetResult myReset = checkReset(antiMember);
		if (myReset != null) {
			printBattle("my reset");
			roundResult.setMyReset(myReset);
		}
		givePokerToMember(antiMember);
		roundResult.setAntiSendPoker(new SendPokerResult(antiMember));
		antiPutPoker(roundResult);
		if (roundResult.getAntiResult() != null && roundResult.getAntiResult().isWin())
			return roundResult;
		roundResult.setNextRoundIndex(++roundIndex);
		givePokerToMember(myMember);
		roundResult.setMySendPoker(new SendPokerResult(myMember));
		return roundResult;
	}

	/**
	 * 放牌后战斗效果
	 * 
	 * @param member
	 * @param tableIndex1
	 * @param tableIndex2
	 * @return
	 */
	private BattleResult checkBattleResult(BattleMember member, int tableIndex1, int tableIndex2) {
		Map<Integer, HandEvaluator> evaluators = getEvaluators(tableIndex1, tableIndex2);
		List<BattleLineResult> lineResults = new ArrayList<BattleLineResult>();
		BattleMember anotherMember = getAnotherMember(member);
		boolean dead = false;
		for (Entry<Integer, HandEvaluator> entry : evaluators.entrySet()) {
			ModelType modelType = entry.getValue().getModelType();
			List<PetAttackResult> petAttacks = new ArrayList<PetAttackResult>(member.pets.size());
			for (Pet pet : member.pets) {
				Integer attackLife = pet.modelAttack.get(modelType);
				if (attackLife == null || attackLife == 0)
					continue;
				anotherMember.currentHp -= attackLife;
				if (anotherMember.currentHp < 0)
					anotherMember.currentHp = 0;
				PetAttackResult petAttack = new PetAttackResult(pet.uniqueId, attackLife);
				petAttacks.add(petAttack);
			}
			int addPower = calAddPower(member, modelType);
			member.power += addPower;
			dead = anotherMember.currentHp <= 0;
			BattleLineResult lineResult = new BattleLineResult();
			lineResult.setLineIndex(entry.getKey());
			lineResult.setPokersType(entry.getValue().getModelType().ordinal());
			lineResult.setPetAttacks(petAttacks);
			lineResult.setAddPower(addPower);
			lineResult.setDead(dead);
			lineResults.add(lineResult);
		}

		return new BattleResult(battleResultId(), lineResults, dead);
	}

	/**
	 * 重置效果
	 * 
	 * @return
	 */
	private PokerResetResult checkReset(BattleMember member) {
		if (isTableFull() || !haveEnoughRest(member)) {
			createNewSetPoker();
			resetTablePokers();
			return new PokerResetResult(battleTable);
		}
		return null;
	}

	/**
	 * 对方放牌
	 * 
	 * @param roundResult
	 */
	private void antiPutPoker(PveRoundResult roundResult) {
		printBattle("before anti put");
		int[] aiResult = ai(antiMember);
		// pass 的情况
		if (aiResult == null) {
			antiMember.clearPokers();
			return;
		}

		int tableIndex1 = aiResult[0];
		Card card1 = antiMember.pokers[aiResult[1]];
		antiMember.pokers[aiResult[1]] = null;
		int tableIndex2 = aiResult[2];
		Card card2 = antiMember.pokers[aiResult[3]];
		antiMember.pokers[aiResult[3]] = null;
		battleTable[tableIndex1] = card1;
		battleTable[tableIndex2] = card2;
		printBattle("after anti put");
		PutPokerResult putResult = new PutPokerResult(tableIndex1, aiResult[1], tableIndex2, aiResult[3]);
		roundResult.setAntiPutPoker(putResult);
		BattleResult antiResult = checkBattleResult(antiMember, tableIndex1, tableIndex2);
		roundResult.setAntiResult(antiResult);
		if (antiResult.isWin()) {
			antiWin();
			return;
		}

		PokerResetResult antiReset = checkReset(myMember);
		if (antiReset != null) {
			printBattle("anti reset");
			roundResult.setAntiReset(antiReset);
		}
	}

}
