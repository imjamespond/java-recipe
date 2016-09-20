package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.vo.RewardVO;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27上午10:40
 */
public interface IFamilyRuleContainer extends IMapContainer<String,FamilyRule> {

    /**
     * 取得默认的家族明星id
     * @return
     */
    public int getDefaultStarId();
    /**
     * 是否可增加成员
     */
    public void canAddFamily(Family family,FamilyMemberInfo memberInfo) throws AlertException, RuleException;

    /**
     * 是否可移除家族成员
     * @param family
     * @param memberInfo
     * @return
     * @throws AlertException
     * @throws RuleException
     */
    public void canRemoveMember(Family family,FamilyMemberInfo memberInfo) throws AlertException, RuleException;

    /**
     * 是否可做任务
     * @return
     */
    public boolean canTask(int num);
    /**
     * 检查最大家族经费
     */
    public void checkMaxFunds(Family family, FamilyBuildInfo bi) throws RuleException;

    public void changeFamilyInfo(Family family, String content, int alterType);

    public void checkModify(FamilyMemberInfo memberInfo, int alterType) throws RuleException;

    public List<FamilyRule> getAll();

    public FamilyRule getRuleByStarId(int starId);

    /**
     * 检查是否家族成员
     * @param family
     * @param info
     */
    void checkMember(Family family, FamilyMemberInfo info) throws RuleException;

    /**
     * 检查是否家族成员
     * @param family
     * @param info
     * @return
     */
    public boolean isMember(Family family,FamilyMemberInfo info);

    int welcome(Family family, Player myPlayer, Player toPlayer) throws RuleException;

    int getWelcomeCoin(Family family);

    /**
     * 家族捐献
     * @param family
     * @param info
     * @param player
     * @param money
     * @param alterType
     */
    public RewardVO endow(Family family, FamilyMemberInfo info, Player player, int money, int alterType, FamilyBuildInfo bi) throws RuleException;

    /**
     * 检查是否可捐献
     * @param family
     * @param info
     * @param bi
     * @param player
     * @param money
     * @param alterType
     */
    void checkEndow(Family family, FamilyMemberInfo info, FamilyBuildInfo bi, Player player, int money, int alterType) throws RuleException;

    void checkChangeFamily(Family oldFamily, Family newFamily, FamilyMemberInfo memberInfo,Player player) throws RuleException, AlertException;
    public void changeFamily(Family oldFamily, Family newFamily, FamilyMemberInfo memberInfo,Player player,FamilyBank familyBank) throws RuleException, AlertException;

    int receiveBoon(FamilyBuildInfo bi, FamilyMemberInfo info, Player player);

    void checkBoon(Family family, FamilyBuildInfo bi, FamilyMemberInfo info, Player player) throws RuleException, AlertException;

    int getBoon(FamilyBuildInfo bi, FamilyMemberInfo info);


    /**
     * 加入新成员
     * @param info
     * @param player
     */
    public void joinMember(Family family, FamilyMemberInfo info, Player player);

    /**
     * 取钱
      * @param player
     * @param familyBank
     * @param familyBuildInfo
     */
    public void familyBankGet(Player player,FamilyBank familyBank,FamilyBuildInfo familyBuildInfo,int gameMoney) throws RuleException;

    /**
     * 存钱
     * @param player
     * @param familyBank
     * @param familyBuildInfo
     */
    public void familyBankSave(Player player,FamilyBank familyBank,FamilyBuildInfo familyBuildInfo,int gameMoney) throws RuleException;

    /**
     * 添加 家族经费 调用
     * @param family
     * @param value
     */
    public void addFailyFunds(Family family,int value);

    /**
     * 添加 家族贡献 调用
     * @param familyMemberInfo
     * @param value
     */
    public void addFamilyDevote(FamilyMemberInfo familyMemberInfo,int value);
}
