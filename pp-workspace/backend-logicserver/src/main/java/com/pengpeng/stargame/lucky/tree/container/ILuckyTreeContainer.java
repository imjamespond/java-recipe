package com.pengpeng.stargame.lucky.tree.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.lucky.tree.rule.LuckyTreeRule;
import com.pengpeng.stargame.model.lucky.tree.LuckyTreeCall;
import com.pengpeng.stargame.model.lucky.tree.PlayerLuckyTree;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeRuleVO;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface ILuckyTreeContainer {


    void addLuckyTreeRuleMap(LuckyTreeRule rule);

    Map<Integer,LuckyTreeRule> getLuckyTreeRuleMap();

    void water(PlayerLuckyTree playerLuckyTree) throws AlertException;

    void waterFri(PlayerLuckyTree playerLuckyTree) throws AlertException;

    LuckyTreeCall call(Player player,PlayerLuckyTree playerLuckyTree) throws AlertException;

    void add(Player player, PlayerLuckyTree playerLuckyTree) throws AlertException;

    int getGoldCallNum(PlayerLuckyTree playerLuckyTree, LuckyTreeRule rule);

    void setLuckyTreeRuleVOs(LuckyTreeRuleVO[] luckyTreeRuleVOs);

    LuckyTreeRuleVO[] getLuckyTreeRuleVOs();
}
