package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.farm.box.PlayerFarmBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.box.FarmBoxVO;

/**
 * User: mql
 * Date: 14-4-17
 * Time: 下午3:38
 */
public interface IFarmBoxRuleContainer {

    void refreshFarmBox(PlayerFarmBox playerFarmBox);

    RewardVO openBox(PlayerFarmBox playerFarmBox,Player player) throws RuleException;
}
