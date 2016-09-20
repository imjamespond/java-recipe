package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.rpc.Session;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午4:14
 */
@Component()
public interface IGiftBoxRuleContainer {
    /**
     * 获取 精美物品列表
     * @return
     */
    List<BaseItemRule> getExquisitetGift();
    /**
     * 获取普通物品列表
     * @return
     */
    List<BaseItemRule> getPtGift();

    /**
     *
     * @param player
     * @param type
     * @throws AlertException
     */
    public void  checkSend(Player player,int type,String id,int num,FarmPlayer farmPlayer,FamilyMemberInfo familyMemberInfo) throws AlertException;

    /**
     *
     * @param player
     * @param type
     * @throws AlertException
     */

   public DropItem send(Session session,Player player,int type,String id,int num ,String words,FamilyMemberInfo familyMemberInfo)throws AlertException;

}
