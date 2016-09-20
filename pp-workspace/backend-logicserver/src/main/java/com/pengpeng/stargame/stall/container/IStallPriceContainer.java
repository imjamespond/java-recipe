package com.pengpeng.stargame.stall.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.PlayerShelf;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.stall.PlayerStallAdvertisement;
import com.pengpeng.stargame.model.stall.StallAssistant;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface IStallPriceContainer {


    /**
     * 检测开启条件
     * @param fp
     * @return
     */
    public void checkEnable(FarmPlayer fp, Player p) throws AlertException;
    /**
     * 检测价格
     * @param
     * @return
     */
    public void checkHitShelf(String itemId, int price) throws AlertException;
    /**
     * 物品上架
     * @param shelfOrder 货架序号
     * @return
     */
    public PlayerShelf hitShelf(FarmPackage fp,PlayerStall ps,String itemId,int itemNum,int price,int shelfOrder, int shelfType) throws AlertException;

    /**
     * 下架
     * @param
     * @return
     */
    public String offShelf(FarmPackage fp, Player player, PlayerStall ps, int shelfOrder, int shelfType) throws AlertException;
    /**
     * 购买
     * @param
     * @return
     */
    public String buy(Player player,FarmPackage myPackage, PlayerStall sellerStall, int shelfOrder, int shelfType) throws AlertException;
    /**
     * 拿钱
     * @param
     * @return
     */
    public void getMoney(Player player,PlayerStall sellerStall, int shelfOrder, int shelfType) throws AlertException;


    /**
     * 广告价格
     * @param
     * @return
     */
    public void setAdvPrice(int key, int val);

    /**
     * 随机物品
     * @param
     * @return
     */
    public void addRandomItem(BaseItemRule rule);

    /**
     * 广告
     * @param
     * @return
     */
    public void advertise(Player player, PlayerStall sellerStall, int shelfOrder, int shelfType, int advType) throws AlertException;

    /**
     * 获取广告
     * @param
     * @return
     */
    public PlayerStallAdvertisement[] getAdvertisement(int page, int size) throws AlertException;

    /**
     * 系统购买,如亲妈购买
     * @param
     * @return
     */
    public void sysBuy(PlayerStall sellerStall);

    /**
     * 生成亲妈货架
     * @param
     * @return
     */
    public List<PlayerShelf> generateMomShelf(PlayerStall playerStall);
    /**
     * 购买前检测
     * @param
     * @return
     */
    void checkBuy(PlayerStall playerStall) throws AlertException;
    /**
     * 购买次数增加
     * @param
     * @return
     */
    void adddBuyingTime(PlayerStall playerStall) throws AlertException;

    /**
     * 是否有货卖,头像buff用
     * @param
     * @return
     */
    boolean isAvailable(PlayerStall playerStall);

    void setAssistantPrices(int key, int val);

    void removeAssistantInfo(StallAssistant sa, String pid) throws AlertException;

    void addAssistantInfo(StallAssistant sa, String pid, int order, int type, PlayerShelf shelf) throws AlertException;

    void enableAssistant(Player player, PlayerStall playerStall, int type) throws AlertException;

    //助手虚拟购买
    void assistantBuy(Player player, FarmPackage myPackage, String itemId, int itemNum, int needMoney) throws AlertException;
}
