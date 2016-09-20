package com.pengpeng.stargame.rsp;


import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.farm.rule.*;
import com.pengpeng.stargame.model.farm.FarmItem;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.vo.BaseShopItemVO;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.farm.FarmPkgLevelVO;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.farm.FarmShopItemVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecoratePkgVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 仓库  物品
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:20
 */
@Component()
public class RspFarmItemFactory extends RspFactory {

	// 农场规则
	@Autowired
	private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmDecorateRuleContainer farmDecorateRuleContainer;

	/**
	 * 构建物品
	 * @param listFarmItem 物品集合
	 * @return FarmItemVO[]
	 */
	private FarmItemVO[] buildFarmItemVO(List<FarmItem> listFarmItem){
		List<FarmItemVO> list = new ArrayList<FarmItemVO>();

		for (FarmItem farmItem : listFarmItem){
			FarmItemVO vo =new FarmItemVO();
			BeanUtils.copyProperties(farmItem,vo);

			BaseItemRule baseItemRule =  baseItemRulecontainer.getElement(farmItem.getItemId());
			vo.setSubType(baseItemRule.getItemtype());
            vo.setShelfPrice(baseItemRule.getShelfPrice());
            if (farmItem.getValidDete() != null) {
                vo.setValidityDate(farmItem.getValidDete().getTime());
            }
			list.add(vo);
		}
		return list.toArray(new FarmItemVO[0]);
	}


	public FarmShopItemVO [] getFarmShopItemVOArr(List<BaseItemRule> listItem){
		List<FarmShopItemVO> list = new ArrayList<FarmShopItemVO>();
		if(listItem == null || listItem.isEmpty()){
			return list.toArray(new FarmShopItemVO[0]);
		}

		for(BaseItemRule baseItemRule : listItem){
			list.add(this.getFarmShopItemVO(baseItemRule));
		}
		return list.toArray(new FarmShopItemVO[0]);
	}
    public BaseShopItemVO getGoodsVO(BaseItemRule baseItemRule){
        BaseShopItemVO vo = new BaseShopItemVO();
        BeanUtils.copyProperties(baseItemRule,vo);

        // 种子
        if(baseItemRule instanceof FarmSeedRule){
            FarmSeedRule farmSeedRule = (FarmSeedRule) baseItemRule;
            Integer[] gainTime = new Integer[farmSeedRule.getProductList().size()];
            int i=0;
            for(Product p:farmSeedRule.getProductList()){
                gainTime[i]=p.getTime();
                i++;
            }
            vo.setGainTime(gainTime);
        }
        return vo;
    }
	public FarmShopItemVO getFarmShopItemVO(BaseItemRule baseItemRule){
		FarmShopItemVO vo = new FarmShopItemVO();
		BeanUtils.copyProperties(baseItemRule,vo);

		// 种子
		if(baseItemRule instanceof FarmSeedRule){
			FarmSeedRule farmSeedRule = (FarmSeedRule) baseItemRule;
			Integer[] gainTime = new Integer[farmSeedRule.getProductList().size()];
			int i=0;
			for(Product p:farmSeedRule.getProductList()){
				gainTime[i]=p.getTime();
				i++;
			}
			vo.setGainTime(gainTime);
		}
		return vo;
	}

    public FarmDecoratePkgVO getFarmDecoratePkg(FarmDecoratePkg farmPackage) {
        if(farmPackage == null){
            return null;
        }

        //仓库
        FarmDecoratePkgVO vo = new FarmDecoratePkgVO();
        BeanUtils.copyProperties(farmPackage, vo);

        if(!farmPackage.existItems()){
            return vo;
        }

        //物品
        List<FarmItem> list = farmPackage.getItemAll();
        FarmItemVO [] farmItemVOs=this.buildFarmItemVO(list);
        //农场装饰品的子类型 要特殊处理
        for(FarmItemVO farmItemVO:farmItemVOs){
            FarmDecorateRule farmDecorateRule=farmDecorateRuleContainer.getElement(farmItemVO.getItemId());
            farmItemVO.setSubType(farmDecorateRule.getDecoratetype());
        }
        vo.setFarmItemVO(farmItemVOs);
        return vo;
    }

	public FarmPkgVO getFarmPackageByPid(FarmPackage farmPackage) {
		if(farmPackage == null){
			return null;
		}

		//仓库
		FarmPkgVO vo = new FarmPkgVO();
		BeanUtils.copyProperties(farmPackage, vo);

		if(!farmPackage.existItems()){
			return vo;
		}

		//物品
		List<FarmItem> list = farmPackage.getItemAll();
		vo.setFarmItemVO(this.buildFarmItemVO(list));
		return vo;
	}

	public FarmItemVO getFarmItemByItemId(FarmPackage farmPackage,String itemId) {
		if(farmPackage == null){
			return null;
		}

		if(!farmPackage.existItems()){
			return null;
		}

		FarmItemVO [] farmItemVOs = this.buildFarmItemVO(farmPackage.getItemByItemId(itemId));
		return farmItemVOs[0];
	}

	public FarmItemVO[] getFarmItem(List<FarmItem> list) {
		if(list == null || list.isEmpty()){
			return new FarmItemVO[0];
		}
		return this.buildFarmItemVO(list);
	}

	public FarmPkgLevelVO [] getLevelItem(FarmPackage farmPackage, List<FarmWareHouseRule.Items> listItems) {
		List<FarmPkgLevelVO> list = new ArrayList<FarmPkgLevelVO>();

		for(FarmWareHouseRule.Items items : listItems){
			final String itemId = items.getItemId();
			final int levelNum = items.getNum();

			FarmPkgLevelVO vo = new FarmPkgLevelVO();
			vo.setPid(farmPackage.getPid());
			vo.setItemId(itemId);
			vo.setLevelNum(levelNum);
			vo.setNum(farmPackage.getSumByNum(itemId));
			list.add(vo);
		}

		return list.toArray(new FarmPkgLevelVO[0]);
	}
}
