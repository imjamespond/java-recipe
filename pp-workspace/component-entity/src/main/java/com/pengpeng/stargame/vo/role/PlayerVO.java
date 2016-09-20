package com.pengpeng.stargame.vo.role;


import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Desc("角色信息")
@EventAnnotation(name="event.player.update",desc="玩家数据更新")
public class PlayerVO {
    @Desc("id")
    public String id;
    @Desc("角色名")
    private String nickName;// 角色名
    @Desc("角色类型")
    private int type;//角色类型
    @Desc("魅力值")
    private int charm;//魅力值
    @Desc("积分(来源于网站)")
    private int integral;//积分
    @Desc("礼金(游戏赠送等同于rmb)")
    private int gift;//礼金
    @Desc("人民币")
    private int rmb;//(人民币)
    @Desc("游戏币")
    private int gold;//碰币(游戏币)
    @Desc("身份(vip类型)")
    private int vip;//0普通,1vip
    @Desc("性别")
    private int sex;//性别

	@Desc("用户主站头像")
	private String portrait;// 用户主站头像

	@Desc("用户主站ID")
	private int userId;

    private String sceneId;
    // ////////////////////////////附加数据//////////////////////
////////////////////////////////附加模块属性////////////////////////////
    @Desc("家族Id ，如果是 空字符串 表示目前没有加入家族")
    private String familyId;
    @Desc("玩家达人币的上限")
    private int maxGold;
    @Desc("明星头像")
    private String starIcon;

    @Desc("是否领取达人币")
    private boolean claim;
    @Desc("自己的农场等级")
    private int farmLevel;
    @Desc("家族名字")
    private String fName;

    public boolean isClaim() {
        return claim;
    }

    public void setClaim(boolean claim) {
        this.claim = claim;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCharm() {
        return charm;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    public int getRmb() {
        return rmb;
    }

    public void setRmb(int rmb) {
        this.rmb = rmb;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getMaxGold() {
        return maxGold;
    }

    public void setMaxGold(int maxGold) {
        this.maxGold = maxGold;
    }

    public String getStarIcon() {
        return starIcon;
    }

    public void setStarIcon(String starIcon) {
        this.starIcon = starIcon;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String toString(){
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
    }
}
