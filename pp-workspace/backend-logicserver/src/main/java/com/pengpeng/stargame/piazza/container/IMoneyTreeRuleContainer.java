package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MoneyPick;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.piazza.rule.MoneyTreeRule;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27上午10:38
 */
public interface IMoneyTreeRuleContainer extends IMapContainer<Integer,MoneyTreeRule> {
    /**
     * 根据时间 获取一个摇钱树信息
     * @param moneyTree
     * @param date
     * @return
     */
    public boolean  getRunMoneyTree(MoneyTree moneyTree,Date date);

    /**
     * 根据规则  获取成熟时间
     * @return
     */
    public Date getRipeTime(String familyId,Date ripeTime);
    /**
     * 根据规则  获取成熟 结束 时间
     * @return
     */
    public Date getRipeEndTime(Date date);

    /**
     * 检测祝福
     *
     * @param moneyTree
     * @param level
     */
    public void checkBlessing(Player player,MoneyTree moneyTree,int level) throws AlertException;

    /**
     * 祝福摇钱树
     * @param moneyTree
     */
    public void blessing(FamilyMemberInfo familyMemberInfo,Player player,MoneyTree moneyTree,int level);

    /**
     * 检测摇钱
     * @param pid
     * @param moneyTree
     */
    public void checkRock(String pid,MoneyTree moneyTree,FamilyMemberInfo familyMemberInfo) throws AlertException;

    /**
     * 摇钱
     * @param moneyTree
     */
    public boolean rock(Player player,MoneyTree moneyTree,int level,FamilyMemberInfo familyMemberInfo);

    /**
     * 捡钱
     * @param player
     * @param moneyTree
     * @param id
     */
    public MoneyPick pickMoney(Player player,MoneyTree moneyTree,String id) throws  AlertException;

    /**
     * 设置 摇钱树 马上成熟
     * @param moneyTree
     */
    public void setRipe(MoneyTree moneyTree,int minute);

}
