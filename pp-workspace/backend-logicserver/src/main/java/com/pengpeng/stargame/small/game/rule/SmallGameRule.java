package com.pengpeng.stargame.small.game.rule;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */

@Entity
@Table(name="sg_rule_smallgame")
public class SmallGameRule extends BaseEntity<String> {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private int type;
    @Column
    private String priceStrategy;
    @Column
    private int timeLimit;//时间限制(秒)
    @Transient
    private Map<Integer,Integer> prices;//达人币->次数

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceStrategy() {
        return priceStrategy;
    }

    public void setPriceStrategy(String priceStrategy) {
        this.priceStrategy = priceStrategy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public final Map<Integer, Integer> getPrices() {
        return prices;
    }

    public void setPrices(Map<Integer, Integer> prices) {
        this.prices = prices;
    }

    //解析所有的奖励
    public void parseItem(){
        if(null != priceStrategy){
            prices = new HashMap<Integer, Integer>();
            String[] arrPrices = priceStrategy.split(";");
            for(String price:arrPrices){
                if(null != price){
                    String[] pricePair = price.split(",");
                    if(pricePair.length == 2){
                        prices.put(Integer.valueOf(pricePair[0]),Integer.valueOf(pricePair[1]));
                    }
                }
            }
        }
    }
}
