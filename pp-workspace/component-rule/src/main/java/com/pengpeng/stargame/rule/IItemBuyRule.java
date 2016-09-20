package com.pengpeng.stargame.rule;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;

public interface IItemBuyRule<Module,T extends Serializable> extends Indexable<T>{

    /**
     * 检查是否可以购买,不可购买则抛出异常
     * @param module
     * @throws GameException
     */
    public void checkBuy(Module module,int num) throws GameException;
    /**
     * 是否可以购买
     * @param bean
     * @return
     */
	public boolean canBuy(Module bean);

    /**
     * 是否买得到
     * @param bean
     * @param num
     * @return
     */
    public boolean canBuy(Module bean,int num);

    /**
     * 购买物品需要扣除
     * @param bean
     * @param num
     */
    public void consumerBuy(Module bean,int num);

    /**
     * 购买后得到的物品
     * @param bean
     * @param num
     */
	public void effectBuy(Module bean, int num);
}
