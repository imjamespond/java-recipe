package com.pengpeng.stargame.rule;

/**
 * 商品或建筑卖出规则
 * @author Administrator
 *
 */
public interface ISaleRule<SaleType,EffectType> {
	public boolean canSale(SaleType bean);
	public void effectSale(EffectType bean);
}
