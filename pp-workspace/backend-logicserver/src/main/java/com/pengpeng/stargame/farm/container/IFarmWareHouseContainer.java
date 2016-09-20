package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
import com.pengpeng.stargame.model.farm.FarmPackage;

import java.util.List;

/**
 * User: mql
 * Date: 13-4-27
 * Time: 下午7:16
 */
public interface IFarmWareHouseContainer extends IMapContainer<Integer,FarmWareHouseRule> {

	/**
	 * 检查仓库是否达到最大等级
	 * @param level
	 * @return true : 已达到最高等级
	 */
	public boolean checkMaxLevel(int level);

	/**
	 * 检查仓库是否可以升级
	 * @param farmPackage
	 * @return true : 可以升级
	 */
	public boolean checkNextLevel(FarmPackage farmPackage);

	/**
	 * 扣除物品 - 升级农场
	 * @param farmPackage
	 */
	public void deduct(FarmPackage farmPackage);

	/**
	 * 升级农场
	 * @param farmPackage
	 */
	public void upgrade(FarmPackage farmPackage);

	/**
	 * 升级仓库下一级物品
	 * @param farmPackage
	 * @return
	 */
	public List<FarmWareHouseRule.Items> getNextLevelByItems(FarmPackage farmPackage);
}
