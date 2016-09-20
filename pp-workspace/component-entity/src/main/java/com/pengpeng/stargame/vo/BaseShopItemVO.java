package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Arrays;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:43
 */
@Desc("商店信息")
public class BaseShopItemVO {

	@Desc("道具编号")
	private String itemsId;
	@Desc("道具名称")
	private String name;
	@Desc("道具类型")
	private int type;
	@Desc("物品类型")
	private int itemtype;
	@Desc("绑定类型")
	private int bindingTypes;
	@Desc("回收价格")
	private int recyclingPrice;
	@Desc("游戏币购买价格")
	private int gamePrice;
	@Desc("达人币购买价格")
	private int goldPrice;
	@Desc("开放等级")
	private int farmLevel;
	@Desc("叠加数量")
	private int overlay;
	@Desc("道具描述")
	private  String desc;
	@Desc("道具图标")
	private  String icon;
	@Desc("生长时间/秒")
	private Integer growthTime;
	@Desc("收获时间/秒")
	private Integer[] gainTime;
	@Desc("总产量")
	private Integer production;
	@Desc("种子经验")
	private Integer seedsExp;
	@Desc("好友采摘作物奖励数量")
	private Integer cropsReward;
	@Desc("好友采摘经验奖励")
	private Integer expReward;
	@Desc("成长期图像")
	private String growthImage;
	@Desc("成熟期图像")
	private String matureImage;
    @Desc("是否在商店出售 0不在  1 在")
    private int shopSell;
    @Desc("有效期")
    private long  validityDate;

    @Desc("是否可赠送")
    private int  starGift;
    @Desc("粉丝值")
    private long  fansValues;

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

	public int getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(int goldPrice) {
		this.goldPrice = goldPrice;
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

	public Integer getGrowthTime() {
		return growthTime;
	}

	public void setGrowthTime(Integer growthTime) {
		this.growthTime = growthTime;
	}

	public Integer[] getGainTime() {
		return gainTime;
	}

	public void setGainTime(Integer[] gainTime) {
		this.gainTime = gainTime;
	}

	public Integer getProduction() {
		return production;
	}

	public void setProduction(Integer production) {
		this.production = production;
	}

	public Integer getSeedsExp() {
		return seedsExp;
	}

	public void setSeedsExp(Integer seedsExp) {
		this.seedsExp = seedsExp;
	}

	public Integer getCropsReward() {
		return cropsReward;
	}

	public void setCropsReward(Integer cropsReward) {
		this.cropsReward = cropsReward;
	}

	public Integer getExpReward() {
		return expReward;
	}

	public void setExpReward(Integer expReward) {
		this.expReward = expReward;
	}

	public String getGrowthImage() {
		return growthImage;
	}

	public void setGrowthImage(String growthImage) {
		this.growthImage = growthImage;
	}

	public String getMatureImage() {
		return matureImage;
	}

	public void setMatureImage(String matureImage) {
		this.matureImage = matureImage;
	}

    public long getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(long validityDate) {
        this.validityDate = validityDate;
    }

    public int getStarGift() {
        return starGift;
    }

    public void setStarGift(int starGift) {
        this.starGift = starGift;
    }

    public long getFansValues() {
        return fansValues;
    }

    public void setFansValues(long fansValues) {
        this.fansValues = fansValues;
    }

    @Override
	public String toString() {
		return "BaseShopItemVO{" +
				"itemsId='" + itemsId + '\'' +
				", name='" + name + '\'' +
				", type=" + type +
				", itemtype=" + itemtype +
				", bindingTypes=" + bindingTypes +
				", recyclingPrice=" + recyclingPrice +
				", gamePrice=" + gamePrice +
				", goldPrice=" + goldPrice +
				", farmLevel=" + farmLevel +
				", overlay=" + overlay +
				", desc='" + desc + '\'' +
				", icon='" + icon + '\'' +
				", growthTime=" + growthTime +
				", gainTime=" + (gainTime == null ? null : Arrays.asList(gainTime)) +
				", production=" + production +
				", seedsExp=" + seedsExp +
				", cropsReward=" + cropsReward +
				", expReward=" + expReward +
				", growthImage='" + growthImage + '\'' +
				", matureImage='" + matureImage + '\'' +
				'}';
	}
}
