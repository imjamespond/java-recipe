package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.RewardVO;

/**
 * 用户在线礼物赠送
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-18上午11:10
 */
public interface IPlayerGiftContainer {

    public RewardVO give(Player player);
}
