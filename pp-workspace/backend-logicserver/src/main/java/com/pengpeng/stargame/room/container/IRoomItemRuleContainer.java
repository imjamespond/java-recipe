package com.pengpeng.stargame.room.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.room.rule.RoomItemRule;

import java.util.List;

/**
 * 个人房间道具规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-22下午4:03
 */
public interface IRoomItemRuleContainer extends IMapContainer<String,RoomItemRule>{

//    public void checkDown() throws RuleException;

//    public void down() throws RuleException;

    /**
     * 装饰房间
     * @param roomPlayer
     * @param pkg
     * @param itemId
     * @throws RuleException
     * @throws AlertException
     */
    public void decorate(RoomPlayer roomPlayer,RoomPackege pkg,String itemId,String position) throws RuleException,AlertException;

    /**
     * 拆下家具(物品或者道具等)
     * @param roomPlayer
     * @param pkg
     * @param itemId
     * @throws RuleException
     * @throws AlertException
     */
    public void removeDecorate(RoomPlayer roomPlayer,RoomPackege pkg,String itemId) throws RuleException,AlertException;
    /**
     * 检查是否可以买
     * @param player
     * @param itemId
     * @param num
     * @throws RuleException
     * @throws AlertException
     */
    public void checkBuy(Player player,String itemId,int num) throws RuleException, AlertException;

    /**
     * 检查是否可以卖
     * @param itemId
     * @throws RuleException
     * @throws AlertException
     */
    public void checkSale(String itemId) throws RuleException, AlertException;
    /**
     * 购买
     * @param player
     * @param roomPlayer
     * @param pkg
     * @param itemId
     * @param num
     * @throws RuleException
     * @throws AlertException
     */
    public void buy(Player player,RoomPlayer roomPlayer,RoomPackege pkg,String itemId,int num) throws RuleException, AlertException;

    /**
     * 卖出
     * @param player
     * @param roomPlayer
     * @param pkg
     * @param itemId
     * @param num
     * @throws RuleException
     */
    public void sale(Player player,RoomPlayer roomPlayer,RoomPackege pkg, String itemId, int num) throws RuleException;

    /**
     * 通过 房间内 编辑 卖出
      * @param player
     * @param roomPlayer
     * @param id
     */
    public void saleByRoom(Player player,RoomPlayer roomPlayer,String id) throws RuleException, AlertException;
    /**
     * 可否购买多个物品
     * @param itemIds
     * @param player
     */
    public void checkBuy(List<String> itemIds,Player player) throws RuleException, AlertException;

    /**
     * 购买多个物品
     * @param itemIds
     * @param player
     */
    public void consumerBuy(List<String> itemIds,Player player,RoomPackege roomPackege) throws RuleException, AlertException;


    /**
     * 获取房间豪华度
     * @param roomPlayer
     * @return
     */
    public int getLuxuryValue(RoomPlayer roomPlayer);

}
