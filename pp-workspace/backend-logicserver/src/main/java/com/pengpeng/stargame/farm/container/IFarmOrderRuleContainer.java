package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.rule.FarmOrderRule;
import com.pengpeng.stargame.model.farm.FarmOrder;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.OneOrder;
import com.pengpeng.stargame.model.player.Player;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-7下午6:25
 */
public interface IFarmOrderRuleContainer extends IMapContainer<String,FarmOrderRule> {

    /**
     * 获取农场订单信息 ，如果到了 刷新时间 那么根据农场等级 刷新订单
     * @param farmOrder
     * @param time
     * @param farmLevel
     * @return
     */
    public boolean getFarmOrder(FarmOrder farmOrder,long time,int farmLevel);

    /**
     * 检测刷新
     * @param player
     */
    public void checkFresh(Player player,int farmLevel) throws AlertException;

    /**
     * 根据玩家农场等级 获取 刷新后的 订单
     * @param farmLevel
     * @return
     */
    public List<OneOrder> getOneOrders(int farmLevel);

    /**
     * 根据玩家农场等级 添加一张  订单
     * @param farmLevel
     * @return
     */
    public void getOneOrder(int farmLevel,FarmOrder farmOrder);

    /**
     * 刷新订单
     * @param player
     * @param farmOrder
     */
    public void refresh(Player player,FarmOrder farmOrder,int farmLevel);

    /**
     * 订单可否完成
     * @param farmPackage
     * @param orderId
     */
    public void checkFinishOrder(Player player,FarmPackage farmPackage,String orderId,int type) throws AlertException;

    /**
     * 完成订单
     * @param farmPackage
     * @param farmOrder
     * @param orderUId
     */
    public void finishOrder(FarmPlayer farmPlayer,Player player,FarmPackage farmPackage,FarmOrder farmOrder,String orderUId,int type);

    /**
     * 获取玩家 当日可以 完成的最大的订单 的数量
     * @param pid
     * @return
     */
    public int getMaxOrderNum(String pid);
}
