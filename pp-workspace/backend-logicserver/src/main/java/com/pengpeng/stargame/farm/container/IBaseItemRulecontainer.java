package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.*;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.vo.farm.FarmSpeedVO;

import java.util.List;


/**
 * User: mql
 * Date: 13-5-2
 * Time: 上午9:23
 */
public interface IBaseItemRulecontainer extends IMapContainer<String,BaseItemRule> {
    /**
     * 物品是否可买
     * @param itemId
     * @return
     */
    public void checkBuy(String itemId) throws AlertException;

    /**
     * 等级是否达到
     * @param itemId
     * @param level
     * @return
     */
    public void checkLevel(String itemId,int level) throws  AlertException;

    /**
     * 检测 仓库容量   是否达到上限
     * @param farmPackage
     * @param itemId
     * @param num
     */
    public void checkPackegeSize(FarmPackage farmPackage,String  itemId,int num) throws AlertException;


    /**
     *购买 检查 钱够不够
     * @param player
     * @param itemId
     * @param num
     * @return
     */

    public void checkMoney(Player player,String itemId,int num) throws AlertException;
    /**
     * 扣钱
     * @param obj
     * @param itemId
     * @param num
     * @return
     */

    public boolean deductMoney(Player obj,String itemId,int num);

    /**
     * 购买成功 添加物品
     * @param obj
     * @param itemId
     * @param num
     */
    public void addBuyItem(FarmPackage obj,String itemId,int num);

    /**
     * 物品是否可以卖
     * @param itemId
     * @throws AlertException
     */
    public void checkSales(FarmPackage farmPackage,String id,String itemId,int num) throws  AlertException;

    /**
     * 出售物品
     * @param player
     * @param farmPackage
     * @param itemId
     * @param num
     */
    public void salesFram(Player player,FarmPackage farmPackage,String itemId,String id,int num);

    /**
     * 种植之前 检测田地
     * @param farmField
     */

    public void checkField(FarmField farmField) throws AlertException;

    /**
     * 种植 之前 检测种子数量
     * @param farmPackage
     * @throws AlertException
     */
    public void chekPlantSeedNum(String itemId,FarmPackage farmPackage)throws AlertException;

    /**
     * 种植 规则
     * @param itemId
     * @param farmField
     * @param farmPackage
     */

    public void plant(String itemId,FarmField farmField,FarmPackage farmPackage);

    /**
     * 收获的 之前的 判断
     * @param farmField
     * @throws AlertException
     */
    public void checkMyHarvest(FarmField farmField,FarmPackage farmPackage) throws AlertException;

    /**
     * 一键收获 之前的 判断
     * @throws AlertException
     */
    public void checkMyHarvestAll(FarmPlayer farmPlayer,FarmPackage farmPackage) throws AlertException;

    /**
     * 帮助好友 收获 的判断
     * @param farmFriendHarvest
     * @param farmField
     * @throws AlertException
     */
    public void checkFriendHarvest(FarmFriendHarvest farmFriendHarvest,FarmField farmField,FarmPackage myFarmPackage,FarmPackage friendFarmPackage) throws AlertException;

    /**
     * 帮助好友 一键 收获
     * @param farmHarvest
     * @param friendFarmPlayer
     * @param myFarmPackage
     * @param friendFarmPackage
     * @param farmActionInfo
     */
    public void helpFriendHarvestAll(FarmFriendHarvest farmHarvest,FarmPlayer friendFarmPlayer,FarmPackage myFarmPackage,FarmPackage friendFarmPackage,FarmActionInfo farmActionInfo) throws AlertException;
    /**
     *
     * @param player
     * @param farmField
     * @param farmPackage
     * @throws GameException
     */
    public void checkSpeedUP(Player player,FarmField farmField,FarmPackage farmPackage) throws GameException;

    /**
     * 返回直接催熟需要的达人币
     * @param player
     * @param farmField
     */
    public int getSpeedupNeedGold(Player player,FarmField farmField) throws AlertException;

    /**
     *
     * @param player
     * @param farmField
     * @param farmPackage
     */
    public FarmSpeedVO speedUp(Player player,FarmField farmField,FarmPackage farmPackage);
    /**
     * 收获  种子
     * @param farmField
     * @throws AlertException
     */
    public void harvestSeed(FarmField farmField,FarmPackage farmPackage,FarmPlayer farmPlayer,RoomPackege roomPackege,FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException;
    /**
     * 收获  种子
     * @throws AlertException
     */
    public void harvestSeedAll(FarmPackage farmPackage,FarmPlayer farmPlayer,RoomPackege roomPackege,FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException;

    public void eradicate(FarmPlayer farmPlayer,String fieldId);

    /**
     * 帮助好友收获
     * @param farmField
     * @param myPackage
     * @param friendPackage
     * @param farmFriendHarvest
     */
    public void helpFriendHarvest(FarmField farmField,FarmPackage myPackage,FarmPackage friendPackage,FarmFriendHarvest farmFriendHarvest,FarmFriendHarvest friendFarmFriendHarvest,FarmActionInfo farmActionInfo);

    /**
     * 返回 农场商店列表
     * @return
     */
    public List<BaseItemRule> getAll() ;

	/**
	 * 取得物品详细信息
	 * @param itemId
	 * @return
	 */
	public BaseItemRule getItemTips(String itemId);

    /**
     * 添加 物品
     * @param itemId
     * @param num
     */
    public void addGoods( Player player,String itemId,int num);

    /**
     * 获取物品数量
     * @param pid
     * @param itemId
     * @return
     */
    public int getGoodsNum(String pid,String itemId);

    /**
     * 使用物品
     * @param pid
     * @param itemId
     * @param num
     */
    public void useGoods(String pid,String itemId,int num);

    /**
     * 添加 物品 只是 运算，外层 保存
     * @param itemid
     * @param num
     * @param farmPackage
     * @param roomPackege
     * @param fashionCupboard
     * @param fashionCupboard 农场内的装饰品背包
     */
    public void addGoodsNoSave(String itemid,int num,FarmPackage farmPackage,RoomPackege roomPackege,FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg);

    /**
     * 获取容量
     * @param rewards
     * @return
     */
    public int  getCapacity(List<ItemData> rewards);
    /**
     * 根据物品判断 背包是否满足 ，如果 背包已经满了 给出提示  ,只判断格子
     * @return  ture表示超过背包容量
     */
    public boolean checkFarmPackgeByCell(String pid,List<ItemData> rewards);
    /**
     * 根据物品判断 背包是否满足 ，如果 背包已经满了 给出提示  ,农场 跟 时装需要分开判断
     * @return  ture表示超过背包容量
     */
    public boolean checkFarmPackge(String pid,List<ItemData> rewards);

    /**
     * 根据物品判断 背包是否满足 ，如果 背包已经满了 给出提示  ,农场 跟 时装需要分开判断
     * @return  ture表示超过背包容量
     */
    public boolean checkFashionPackge(String pid,List<ItemData> rewards);

    /**
     * 获取能帮助好友收获的最大次数
     * @param pid
     * @return
     */
    public int getMaxHelpFriendNum(String pid);

    /**
     *
     * @param baseItemRule
     * @param num
     * @param farmPackage          农场物品背包
     * @param roomPackege           房间物品背包
     * @param fashionCupboard      装饰品背包
     * @param farmDecoratePkg       农场内的装饰品背包
     * @return
     */
    boolean addItemAndCheckNoSaving(BaseItemRule baseItemRule, int num, FarmPackage farmPackage, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg);

    /**
     * 是否可以购买多个物品
     * @param itemIds
     * @param player
     * @throws GameException
     */
    public void checkBuy(List<String> itemIds, Player player) throws GameException;
}
