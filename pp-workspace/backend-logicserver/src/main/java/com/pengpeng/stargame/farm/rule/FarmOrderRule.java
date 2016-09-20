package com.pengpeng.stargame.farm.rule;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.*;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-7下午6:26
 */
@Entity
@Table(name="sg_rule_farm_order")
public class FarmOrderRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private int farmLevel;
    @Column
    private int currencyReward;
    @Column
    private int expReward;
    @Column
    private String orderRequest ;

    @Transient
    private List<OrderGoods>  orderGoodsList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getCurrencyReward() {
        return currencyReward;
    }

    public void setCurrencyReward(int currencyReward) {
        this.currencyReward = currencyReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public String getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public List<OrderGoods> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void initOrderItem(){
        orderGoodsList=new ArrayList<OrderGoods>();
        String [] line=this.orderRequest.split(";");
        for (int i=0;i<line.length;i++){
            OrderGoods orderGoods=new OrderGoods();
            String[] items=line[i].split(",");
            orderGoods.setItemId(items[0]);
            orderGoods.setNum(Integer.parseInt(items[1]));
            orderGoodsList.add(orderGoods);
        }
    }

    public  void  checkFinishOrder(FarmPackage farmPackage) throws AlertException{
        for(OrderGoods orderGoods:orderGoodsList){
            if(orderGoods.getNum()>farmPackage.getSumByNum(orderGoods.getItemId())){
                throw new AlertException("物品数量未达到");
            }
        }
    }

//    public  void  finishOrder(Player player,FarmPackage farmPackage){
//        player.incGameCoin(currencyReward);
//        for(OrderGoods orderGoods:orderGoodsList){
//            farmPackage.deduct(orderGoods.getItemId(),orderGoods.getNum());
//        }
//    }

}
