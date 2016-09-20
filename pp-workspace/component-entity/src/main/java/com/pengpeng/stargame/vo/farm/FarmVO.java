package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

import java.util.List;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 下午3:16
 */
@Desc("农场")
@EventAnnotation(name="event.farm.update",desc="农场数据更新")
public class FarmVO {
    @Desc("玩家id")
    private String pid;
    @Desc("玩家名称")
    private String name;
    @Desc("农场等级")
	private int level;
    @Desc("当前经验")
    private int exp;
    @Desc("下一个级别 需要的 经验")
    private int nextExp;
    @Desc("今日好评数量")
    private int goodReputation;
    @Desc("所有订单数量")
    private int allOrderNum;
    @Desc("剩余订单数量")
    private int surplusOrderNum;
    @Desc("好友帮忙收获的次数")
    private int friendHelpNum;
    @Desc("可以收获的田地数量")
    private int canHarvestNum;
    @Desc("所有有种子的田地数量")
    private int allseedNum;
    @Desc("农场访客记录数")
    private int farmActionNum;
    @Desc("田地列表")
    private FieldVO[] fieldVOs;

    public FarmVO() {
    }

    public FarmVO(String pid, String name) {
        this.pid = pid;
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldVO[] getFieldVOs() {
        return fieldVOs;
    }

    public void setFieldVOs(FieldVO[] fieldVOs) {
        this.fieldVOs = fieldVOs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNextExp() {
        return nextExp;
    }

    public void setNextExp(int nextExp) {
        this.nextExp = nextExp;
    }

    public int getGoodReputation() {
        return goodReputation;
    }

    public void setGoodReputation(int goodReputation) {
        this.goodReputation = goodReputation;
    }

    public int getAllOrderNum() {
        return allOrderNum;
    }

    public void setAllOrderNum(int allOrderNum) {
        this.allOrderNum = allOrderNum;
    }

    public int getSurplusOrderNum() {
        return surplusOrderNum;
    }

    public void setSurplusOrderNum(int surplusOrderNum) {
        this.surplusOrderNum = surplusOrderNum;
    }

    public int getFriendHelpNum() {
        return friendHelpNum;
    }

    public void setFriendHelpNum(int friendHelpNum) {
        this.friendHelpNum = friendHelpNum;
    }

    public int getCanHarvestNum() {
        return canHarvestNum;
    }

    public void setCanHarvestNum(int canHarvestNum) {
        this.canHarvestNum = canHarvestNum;
    }

    public int getAllseedNum() {
        return allseedNum;
    }

    public void setAllseedNum(int allseedNum) {
        this.allseedNum = allseedNum;
    }

    public int getFarmActionNum() {
        return farmActionNum;
    }

    public void setFarmActionNum(int farmActionNum) {
        this.farmActionNum = farmActionNum;
    }
}
