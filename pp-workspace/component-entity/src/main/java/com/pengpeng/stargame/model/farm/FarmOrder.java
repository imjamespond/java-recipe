package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 上午10:24
 */
public class FarmOrder extends BaseEntity<String> {
    private String pId;
    private List<OneOrder> orderList;
    private int finishNum;
    private Date nextTime; //下次订单的刷新时间
    private Date nextRefreshNumTime;//订单天 刷新时间

    public FarmOrder(){
        orderList=new ArrayList<OneOrder>();
    }
    public FarmOrder(String pId,Date time,Date nextRefreshNumTime){
        nextTime=time;
        this.nextRefreshNumTime=nextRefreshNumTime;
        this.pId=pId;
        orderList=new ArrayList<OneOrder>();
    }

    public String getpId() {
        return pId;
    }



    public List<OneOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OneOrder> orderList) {
        this.orderList = orderList;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public Date getNextRefreshNumTime() {
        return nextRefreshNumTime;
    }

    public void setNextRefreshNumTime(Date nextRefreshNumTime) {
        this.nextRefreshNumTime = nextRefreshNumTime;
    }

    @Override
    public String getId() {
        return pId;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pId;
    }

    /**
     * 刷新订单
     * @param orders
     */
    public void refresh(List<OneOrder> orders){
        orderList.clear();
        orderList.addAll(orders);
    }

    /**
     *  获取一个订单
     * @param id
     */
    public OneOrder getOneOrder(String id){
        for(int i=0;i<orderList.size();i++){
            OneOrder oneOrder=orderList.get(i);
            if(oneOrder.getId().equals(id)){
                return oneOrder;
            }
        }
        return null;
    }

    /**
     * 删除一个 订单
     * @param oneOrder
     */
    public void deleteOneOrder(OneOrder oneOrder){
        orderList.remove(oneOrder);
    }
}
