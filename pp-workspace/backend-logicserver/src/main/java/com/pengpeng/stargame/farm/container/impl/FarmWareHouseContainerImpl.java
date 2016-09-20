package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmWareHouseContainer;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-4-27
 * Time: 下午7:19
 */
@Component
public class FarmWareHouseContainerImpl extends HashMapContainer<Integer,FarmWareHouseRule> implements IFarmWareHouseContainer {

	@Autowired
	private IExceptionFactory exceptionFactory;

	@Override
	public boolean checkMaxLevel(int level) {
		FarmWareHouseRule farmWareHouseRule = super.getElement(level+1);
		if(farmWareHouseRule == null){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkNextLevel(FarmPackage farmPackage) {
		final int nextLevel = farmPackage.getLevel() + 1;
		FarmWareHouseRule farmWareHouseRule = super.getElement(nextLevel);
		if(farmWareHouseRule == null){
			return false;
		}

		return farmWareHouseRule.check(farmPackage);
	}

	@Override
	public void deduct(FarmPackage farmPackage) {
		final int nextLevel = farmPackage.getLevel() + 1;
		FarmWareHouseRule farmWareHouseRule = super.getElement(nextLevel);

		farmWareHouseRule.deduct(farmPackage);
	}

	@Override
	public void upgrade(FarmPackage farmPackage) {
		final int nextLevel = farmPackage.getLevel() + 1;
		FarmWareHouseRule farmWareHouseRule = super.getElement(nextLevel);

		farmPackage.setLevel(farmWareHouseRule.getWarehouseLevel());
		farmPackage.setSize(farmWareHouseRule.getCapacity());
	}

	@Override
	public List<FarmWareHouseRule.Items> getNextLevelByItems(FarmPackage farmPackage) {
		final int nextLevel = farmPackage.getLevel() + 1;
		FarmWareHouseRule farmWareHouseRule = super.getElement(nextLevel);

		List<FarmWareHouseRule.Items> list = farmWareHouseRule.getItems();
		return list;
	}
}
