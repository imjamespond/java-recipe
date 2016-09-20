package com.pengpeng.stargame.rule;

import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 林佛权
 * Date: 12-12-26
 * Time: 上午11:55
 * To change this template use File | Settings | File Templates.
 */
public interface IItemSaleRule<SaleType,ConsumeType,EffectType,Index extends Serializable> extends Indexable<Index>{
    public void checkSale(SaleType bean,int num);
    public boolean canSale(SaleType bean,int num);
    public void consumerSale(ConsumeType bean,int num);
    public void effectSale(EffectType bean, int num);
}
