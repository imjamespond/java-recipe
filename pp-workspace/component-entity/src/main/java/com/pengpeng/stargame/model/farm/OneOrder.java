package com.pengpeng.stargame.model.farm;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 上午10:41
 */
public class OneOrder {
    private String  id;
    private String orderId;
    public OneOrder(){

    }

    public OneOrder(String id,String orderId){
        this.id=id;
        this.orderId=orderId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
