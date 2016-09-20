package com.pengpeng.stargame.rule;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;

public interface IItemUseRule<SaleType,EffectType,Index extends Serializable> extends Indexable<Index> {
    public void checkUse(SaleType bea,int num) throws GameException;
    public boolean canUse(SaleType bean,int num);
    public void consumerUse(SaleType bean,int num);
    public void effectUse(EffectType bean, int num);
}
