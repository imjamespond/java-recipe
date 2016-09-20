package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.farm.GoodsVO;

/**
 * User: mql
 * Date: 13-5-13
 * Time: 下午8:24
 */
@Desc("奖励VO")
@EventAnnotation(name="event.reward",desc="奖励数据")
public class RewardVO {
    @Desc("类型   1表示收获后获取的东西 2表示农场升级 3表示祝福完成  4表示摇钱,5家族贡献 6表示购物东西  7表示卖出东西,8音乐榜礼包领取 " +
            "9 :订单完成 10 贩卖多个农场物品 11 领取加工完成的队列 掉落的物品 12圣诞礼包 13元旦礼包 14春节礼包奖励 15音乐投票活动奖励 " +
            "16、五一礼包")
    private int type;
    @Desc("收获田地的Id")
    private int fieldId;
    @Desc("抽象数量  ")
    private int num;
    @Desc("人民币 负数的时候表示 扣除的")
    private int rmb;
    @Desc("游戏币 负数的时候表示 扣除的")
    private int gold;
    @Desc("玩家经验")
    private int exp;
    @Desc("农场经验")
    private int farmExp;
    @Desc("网站积分")
    private int integral;
    @Desc("粉丝值")
    private int fanValue;
    @Desc("家族经费")
    private int funding;
    @Desc("家族贡献")
    private int contribution;
    @Desc("奖励的物品  奖励的数量 为 GoodsVO 中 myNum 属性")
    GoodsVO [] goodsVOs;
    @Desc("位置  拾取礼物时候的位置")
    private String position;
    public RewardVO(){

    }
    public RewardVO(int type){
        this.type=type;
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

    public GoodsVO[] getGoodsVOs() {
        return goodsVOs;
    }

    public void setGoodsVOs(GoodsVO[] goodsVOs) {
        this.goodsVOs = goodsVOs;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getFanValue() {
        return fanValue;
    }

    public void setFanValue(int fanValue) {
        this.fanValue = fanValue;
    }

    public int getFunding() {
        return funding;
    }

    public void setFunding(int funding) {
        this.funding = funding;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public void addGoodsVO(GoodsVO goodsVO){
        if(this.goodsVOs==null||this.goodsVOs.length==0){
            goodsVOs=new GoodsVO[1];
            goodsVOs[0]=goodsVO;
        }else{
            GoodsVO [] newGoodsVos=new GoodsVO[goodsVOs.length+1];
            for(int i=0;i<goodsVOs.length;i++){
                newGoodsVos[i]=goodsVOs[i];
            }
            newGoodsVos[newGoodsVos.length-1]=goodsVO;
            goodsVOs=newGoodsVos;
        }

    }
}
