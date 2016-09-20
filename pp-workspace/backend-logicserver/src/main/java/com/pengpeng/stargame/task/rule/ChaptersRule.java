package com.pengpeng.stargame.task.rule;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: mql
 * Date: 13-8-2
 * Time: 下午12:06
 */
@Entity
@Table(name="sg_rule_task_chapters")
public class ChaptersRule extends BaseEntity<String> {
    @Id
    private String chaptersId;//章节编号
    @Column
    private String name; //章节名称
    @Column
   	private String taskId;  // 任务编号
    @Column
    private int 	gameCoin; //游戏币奖励
    @Column
    private int 	farmExp;//农场经验奖励
    @Column
    private int  	businessExp ;//商业经验奖励
    @Column
    private int 	familyFunds;//家族经费
    @Column
    private int  familyDevote;//家族贡献
    @Column
    private int 	bonusScore;  //积分奖励
    @Column
    private String items ;//道具奖励


    @Transient
    private List<ItemData> itemRewardList;

    public String getChaptersId() {
        return chaptersId;
    }

    public void setChaptersId(String chaptersId) {
        this.chaptersId = chaptersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getBusinessExp() {
        return businessExp;
    }

    public void setBusinessExp(int businessExp) {
        this.businessExp = businessExp;
    }

    public int getFamilyFunds() {
        return familyFunds;
    }

    public void setFamilyFunds(int familyFunds) {
        this.familyFunds = familyFunds;
    }

    public int getFamilyDevote() {
        return familyDevote;
    }

    public void setFamilyDevote(int familyDevote) {
        this.familyDevote = familyDevote;
    }

    public int getBonusScore() {
        return bonusScore;
    }

    public void setBonusScore(int bonusScore) {
        this.bonusScore = bonusScore;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void init() {
        List<ItemData> itemList = new ArrayList<ItemData>();
        //初始化物品
        StringTokenizer itemToken = new StringTokenizer(items,";");
        while(itemToken.hasMoreElements()){
            String item = itemToken.nextToken();
            String[] its = item.split(",");
            ItemData it = ItemData.newTaskItem(its);
            if (it!=null){
                itemList.add(it);
            }
        }
        itemRewardList=itemList;
    }

    @Override
    public String getKey() {
        return chaptersId;
    }

    @Override
    public String getId() {
        return chaptersId;
    }

    public List<ItemData> getItemRewardList() {
        return itemRewardList;
    }

    public void setItemRewardList(List<ItemData> itemRewardList) {
        this.itemRewardList = itemRewardList;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setKey(String key) {

    }
}
