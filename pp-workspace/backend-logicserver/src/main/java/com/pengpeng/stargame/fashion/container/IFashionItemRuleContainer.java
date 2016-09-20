package com.pengpeng.stargame.fashion.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.model.player.Player;

import java.util.List;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:25
 */
public interface IFashionItemRuleContainer  extends IMapContainer<String,FashionItemRule> {
    /**
     * 获取所有的 时装列表
     * @return
     */
    public List<FashionItemRule> getAll() ;

    /**
     * 换装检查
     * @param fashionCupboard
     * @param id
     * @throws AlertException
     */
    public void checkChange(FashionPlayer fashionPlaye,FashionCupboard fashionCupboard,String itemId,String  id) throws AlertException;

    /**
     * 换装操作
     * @param fashionPlayer
     * @param fashionCupboard
     * @param itemId
     * @param id
     */
    public void change(FashionPlayer fashionPlayer,FashionCupboard fashionCupboard,String  itemId,String id) throws AlertException;

    /**
     * 脱下一个 服装
     * @param fashionPlayer
     * @param fashionCupboard
     * @param id
     * @param type
     */
    public void takeOff(FashionPlayer fashionPlayer,FashionCupboard fashionCupboard,String id,String type) throws AlertException;

    /**
     * 脱掉 所有 服装
     * @param fashionPlayer
     * @param fashionCupboard
     */
    public  void takeOffAll(FashionPlayer fashionPlayer,FashionCupboard fashionCupboard);

    /**
     * 检测 是否可以随机从 背包里穿戴
     * @param fashionPlayer
     * @param fashionCupboard
     * @throws AlertException
     */
    public void checkRandomFromPkg(FashionPlayer fashionPlayer,FashionCupboard fashionCupboard) throws  AlertException;

    /**
     * 随机穿戴
      * @param fashionPlayer
     * @param fashionCupboard
     */
    public void randomFromPkg (FashionPlayer fashionPlayer,FashionCupboard fashionCupboard);

    /**
     * 时装的 购买 检测
     * @param fashionCupboard
     * @param player
     * @param itemIds
     * @throws AlertException
     */
    public void checkBuy (FashionCupboard fashionCupboard,Player player,String [] itemIds) throws AlertException;



    /**
     * 检查 卖出
     * @param fashionCupboard
     * @param id
     */
    public void checkSale(FashionCupboard fashionCupboard, String itemId,int num,String id) throws AlertException;

    /**
     * 购买
     * @param fashionCupboard
     * @param player
     * @param itemIds
     */
    public void buy(FashionCupboard fashionCupboard,Player player, String[] itemIds);

    /**
     * 卖出
     * @param fashionCupboard
     * @param player
     * @param itemId
     * @param num
     */
    public void sale(FashionCupboard fashionCupboard,Player player,String itemId,int num,String id);

    /**
     * 获取玩家 时尚值
     * @return
     */
    public int getFasionValue(FashionPlayer fashionPlayer,FashionCupboard fashionCupboard);
}
