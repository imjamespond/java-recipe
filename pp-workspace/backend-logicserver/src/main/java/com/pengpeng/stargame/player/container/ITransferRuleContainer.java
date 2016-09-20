package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.player.rule.TransferRule;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-16下午12:24
 */
public interface ITransferRuleContainer  extends IMapContainer<String, TransferRule> {

    /**
     * 根据地图id,取得地图所有的传送点
     * @param mapId
     * @return
     */
    public List<TransferRule> getTransferByScene(String mapId);

    /**
     * 取得地图的入口
     * @param mapId
     * @return
     */
    public TransferRule getEnterTransfer(String mapId);
}
