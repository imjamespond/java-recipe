package com.pengpeng.stargame.successive.rule;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */

@Entity
@Table(name="sg_rule_successive")
public class SuccessiveRule extends BaseEntity<String> {

    @Id
    private String id;
    @Column
    private int day;
    @Column
    private String items;
    @Column
    private int gameCoin;
    @Column
    private int goldCoin;

    @Transient
    private List<ItemData> itemNumList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    @Override
    public String getKey() {
        return String.valueOf(day);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public final List<ItemData> getItemNumList() {
        return itemNumList;
    }

    //解析所有的奖励
    public void parseItem(){
        if(null != items){
            itemNumList = new ArrayList<ItemData>();
            String[] arrItem = items.split(";");
            for(String item:arrItem){
                if(null != item){
                    String[] itemNum = item.split(",");
                    if(itemNum.length == 2){
                        ItemData sin = new ItemData();
                        sin.setItemId( itemNum[0]);
                        sin.setNum(Integer.valueOf(itemNum[1]));
                        itemNumList.add(sin);
                    }
                }
            }
        }


    }
}

