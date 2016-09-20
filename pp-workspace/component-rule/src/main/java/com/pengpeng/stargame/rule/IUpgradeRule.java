package com.pengpeng.stargame.rule;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.Indexable;

import java.io.Serializable;

public interface IUpgradeRule<CheckType,ConsumeType,EffectType,T extends Serializable> extends Indexable<T> {

     public void checkUpgrade(CheckType bean) throws GameException;
	/**
	 * 检查资源是否满足条件
	 * 
	 * @param bean
	 * @return
	 */
	public boolean canUpgrade(CheckType bean);

	/**
	 * 扣除资源
	 * 
	 * @param bean
	 */
	public void consumeUpgrade(ConsumeType bean);

	/**
	 * 升级后奖励
	 * 
	 * @param bean
	 */
	public void effectUpgrade(EffectType bean);

}
