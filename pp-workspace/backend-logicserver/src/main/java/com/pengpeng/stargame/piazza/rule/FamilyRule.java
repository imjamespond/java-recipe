package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.piazza.container.FamilyConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-26下午4:45
 */
@Entity
@Table(name="sg_rule_family")
public class FamilyRule extends BaseEntity<String> {
    //等级
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String starName;
    //网站的明星id
    @Column
    private int starId;

    @Column
    private String starIcon;
    //家族人数上限
    @Column
    private int maxMember;
    //游戏币贡献兑换比例
    @Column
    private int gameCoinDevote;
    //达人币贡献兑换比例
    @Column
    private int goldCoinDevote;
    //游戏币经费兑换比例
    @Column
    private int gameCoinFunds;
    //达人币经费兑换比例
    @Column
    private int goldCoinFunds;
    //每日经费上限
    @Column
    private int fundsLimit;
    //道具贡献比例
    @Column
    private int propsDevote;
    //每日家族任务上限
    @Column
    private int tasksLimit;
    //游戏币欢迎
    @Column
    private int welcomeCoin;
    //家族变更需要游戏币
    @Column
    private int changeCoin;

    //默认赠言
    @Column
    private String  words;
    //摇钱树的成熟时间
    @Column
    private String treeRipeTime1;
    //摇钱树的成熟时间
    @Column
    private String treeRipeTime2;

    public int getChangeCoin() {
        return changeCoin;
    }

    public void setChangeCoin(int changeCoin) {
        this.changeCoin = changeCoin;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String star) {
        this.starName = star;
    }

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public String getStarIcon() {
        return starIcon;
    }

    public void setStarIcon(String starIcon) {
        this.starIcon = starIcon;
    }

    /**
     * 是否可以加入家族
     * @return
     */
    public boolean canAddFamily(int num){
        return maxMember>num;
    }
    /**
     * 是否可做任务
     * @return
     */
    public boolean canTask(int num){
        return tasksLimit<num;
    }
    /**
     * 是否可贡献
     * 每个玩家每天最多可为家族贡献的经费上限
     */
    public boolean canFundsLimit(int num){
        return num<=fundsLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public int getGameCoinDevote() {
        return gameCoinDevote;
    }

    public void setGameCoinDevote(int gameCoinDevote) {
        this.gameCoinDevote = gameCoinDevote;
    }

    public int getGoldCoinDevote() {
        return goldCoinDevote;
    }

    public void setGoldCoinDevote(int goldCoinDevote) {
        this.goldCoinDevote = goldCoinDevote;
    }

    public int getGameCoinFunds() {
        return gameCoinFunds;
    }

    public void setGameCoinFunds(int gameCoinFunds) {
        this.gameCoinFunds = gameCoinFunds;
    }

    public int getGoldCoinFunds() {
        return goldCoinFunds;
    }

    public void setGoldCoinFunds(int goldCoinFunds) {
        this.goldCoinFunds = goldCoinFunds;
    }

    public int getFundsLimit() {
        return fundsLimit;
    }

    public void setFundsLimit(int fundsLimit) {
        this.fundsLimit = fundsLimit;
    }

    public int getPropsDevote() {
        return propsDevote;
    }

    public void setPropsDevote(int propsDevote) {
        this.propsDevote = propsDevote;
    }

    public int getTasksLimit() {
        return tasksLimit;
    }

    public void setTasksLimit(int tasksLimit) {
        this.tasksLimit = tasksLimit;
    }

    public String getTreeRipeTime1() {
        return treeRipeTime1;
    }

    public void setTreeRipeTime1(String treeRipeTime1) {
        this.treeRipeTime1 = treeRipeTime1;
    }

    public String getTreeRipeTime2() {
        return treeRipeTime2;
    }

    public void setTreeRipeTime2(String treeRipeTime2) {
        this.treeRipeTime2 = treeRipeTime2;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
       this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public int getWelcomeCoin() {
        return welcomeCoin;
    }

    public void setWelcomeCoin(int welcomeCoin) {
        this.welcomeCoin = welcomeCoin;
    }

    public void accept(Player player,FamilyBuildInfo info){
        int level = info.getLevel(FamilyConstant.BUILD_CASTLE);
        switch (level){

        }

    }
}
