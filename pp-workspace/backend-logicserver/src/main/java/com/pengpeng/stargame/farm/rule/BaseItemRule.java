package com.pengpeng.stargame.farm.rule;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-4-28
 * Time: 下午5:38
 */
@Entity
@Table(name = "sg_rule_base_item")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseItemRule extends BaseEntity<String> {
    @Id            //道具编号
    private String itemsId;
    @Column     //道具名称
    private String name;
    @Column   //道具类型
    private int type;
    @Column       //物品类型
    private int itemtype;
    @Column        //绑定类型
    private int bindingTypes;
    @Column       //回收价格
    private int recyclingPrice;
    @Column           //游戏币购买
    private int gamePrice;
    @Column            //游戏币购买
    private int goldPrice;
    @Column           //农场等级
    private int farmLevel;
    @Column         //物品堆叠数量
    private int overlay;
    @Column(name = "des")     //道具描述
    private String desc;
    @Column         //图标文件名称
    private String icon;
    @Column         //是否在商店出售 0不再  1 在
    private int shopSell;
    @Column //可否赠送给明星  0不能  1 可以
    private int starGift;
    @Column  //送给明星时候获得的粉丝值
    private int fansValues;
    @Column   //过期时间
    protected String itemValidDate;
    protected int  validDay;//有效期 天数
    @Column  //送给明星礼物时候 获取明星回馈礼物 编辑
    private String rewardEditor;
    @Column         //上架价格
    private int shelfPrice;
    @Transient
    //给明星送礼 掉宝
    List<DropItem> dropItemList;


    public void init() {
        /**
         * 初始化 掉落 信息
         */
        if(rewardEditor==null){
            return;
        }
        String[] line1 = this.rewardEditor.split(";");

        List<DropItem> list1 = new ArrayList<DropItem>();
        for (int i = 0; i < line1.length; i++) {
            DropItem dropItem = new DropItem();
            String[] items = line1[i].split(",");
            if (items.length != 3) {
                continue;
            }
            dropItem.setItemId(items[0]);
            dropItem.setNum(Integer.parseInt(items[1]));
            dropItem.setProbability(Integer.parseInt(items[2]));
            list1.add(dropItem);
        }
        dropItemList = list1;

    }

    public int getValidDay() {
        return validDay;
    }

    public void setValidDay(int validDay) {
        this.validDay = validDay;
    }

    public List<DropItem> getDropItemList() {
        return dropItemList;
    }

    public void setDropItemList(List<DropItem> dropItemList) {
        this.dropItemList = dropItemList;
    }

    public String getItemValidDate() {
        return itemValidDate;
    }

    public void setItemValidDate(String itemValidDate) {
        this.itemValidDate = itemValidDate;
    }

    public int getFansValues() {
        return fansValues;
    }

    public void setFansValues(int fansValues) {
        this.fansValues = fansValues;
    }

    public int getStarGift() {
        return starGift;
    }

    public void setStarGift(int starGift) {
        this.starGift = starGift;
    }

    public int getShopSell() {
        return shopSell;
    }

    public void setShopSell(int shopSell) {
        this.shopSell = shopSell;
    }

    public String getItemsId() {
        return itemsId;
    }

    public void setItemsId(String itemsId) {
        this.itemsId = itemsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getItemtype() {
        return itemtype;
    }

    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }

    public int getBindingTypes() {
        return bindingTypes;
    }

    public void setBindingTypes(int bindingTypes) {
        this.bindingTypes = bindingTypes;
    }

    public int getRecyclingPrice() {
        return recyclingPrice;
    }

    public void setRecyclingPrice(int recyclingPrice) {
        this.recyclingPrice = recyclingPrice;
    }

    public int getGamePrice() {
        return gamePrice;
    }

    public void setGamePrice(int gamePrice) {
        this.gamePrice = gamePrice;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getOverlay() {
        return overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getShelfPrice() {
        return shelfPrice;
    }

    public void setShelfPrice(int shelfPrice) {
        this.shelfPrice = shelfPrice;
    }

    @Override
    public String getId() {
        return this.itemsId;
    }

    @Override
    public void setId(String id) {
        this.itemsId = id;
    }

    @Override
    public String getKey() {
        return this.itemsId;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getRewardEditor() {
        return rewardEditor;
    }

    public void setRewardEditor(String rewardEditor) {
        this.rewardEditor = rewardEditor;
    }

    /**
     * 等级是否可种
     *
     * @param level
     * @return
     */
    public void checkLevel(int level) throws AlertException {
        if (farmLevel != 0) {
            if (farmLevel < level) {
                throw new AlertException("等级未达到");
            }
        }
    }

    /**
     * 购买检测 钱够不够
     *
     * @param player
     * @param num
     * @throws AlertException
     */
    public void checkMoney(Player player, int num) throws AlertException {
        if (gamePrice != 0) {
            if (player.getGameCoin() < gamePrice * num) {
                throw new AlertException("游戏币不足！");
            }
        }
        if (goldPrice != 0) {
            if (player.getGoldCoin() < goldPrice * num) {
                throw new AlertException("货币不足！");
            }
        }
    }

    public void checkSales() throws AlertException {
        if (recyclingPrice == 0) {
            throw new AlertException("物品不能出售");
        }
    }

//    /**
//     * 购买扣钱
//     *
//     * @param player
//     * @param num
//     */
//    public void deductMoney(Player player, int num) {
//        if (gamePrice != 0) {
//            player.decGameCoin(gamePrice * num);
//        }
//        if (goldPrice != 0) {
//            player.decGoldCoin(goldPrice * num);
//        }
//    }
//
//    /**
//     * 回收 加游戏币
//     *
//     * @param player
//     * @param num
//     */
//    public void addGameMoney(Player player, int num) {
//        player.incGameCoin(recyclingPrice * num);
//    }

    public void checkBuy() throws AlertException {
//        if (shopSell==0) {
//            throw new AlertException("此物品不能购买");
//        }
        if (gamePrice == 0 && goldPrice == 0) {
            throw new AlertException("此物品不能购买");
        }
    }

    public boolean checkPackageSize(String itemsId, FarmPackage farmPackage, int num) {
        return farmPackage.isMaxBySize(itemsId, num, overlay);
    }

    /**
     * 获取物品的 过期时间
     *
     * @return
     */
    public Date getValidDete() {
        if (this.itemValidDate != null &&! this.itemValidDate.equals("") &&! this.itemValidDate.equals("0")) {
            return DateUtil.convertStringToDate(null, this.getItemValidDate());
        }
        if(validDay>0){
           return DateUtil.addMinute(new Date(),validDay*24*60);
        }
        return null;
    }
}
