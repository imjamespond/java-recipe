package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmOrderRuleContainer;
import com.pengpeng.stargame.farm.container.impl.FarmOrderRuleContainerImpl;
import com.pengpeng.stargame.farm.rule.FarmOrderRule;
import com.pengpeng.stargame.farm.rule.OrderGoods;
import com.pengpeng.stargame.model.farm.FarmOrder;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.OneOrder;
import com.pengpeng.stargame.vo.farm.FarmOrderInfoVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.farm.OneOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: mql
 * Date: 13-5-6
 * Time: 下午3:14
 */
@Component
public class RspFarmOrderFactory extends RspFactory{
    @Autowired
    private IFarmOrderRuleContainer farmOrderRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;


   public FarmOrderInfoVO transitionVO(FarmOrder farmOrder,FarmPackage farmPackage){
       FarmOrderInfoVO farmOrderInfoVo=new FarmOrderInfoVO();
       farmOrderInfoVo.setAllNum(farmOrderRuleContainer.getMaxOrderNum(farmOrder.getpId()));
       farmOrderInfoVo.setSurplusNum(farmOrderRuleContainer.getMaxOrderNum(farmOrder.getpId())- farmOrder.getFinishNum());
       farmOrderInfoVo.setNextTime(farmOrder.getNextTime().getTime());
       farmOrderInfoVo.setAddPercent(FarmOrderRuleContainerImpl.ADD_P);
       List<OneOrder> orders = farmOrder.getOrderList();
       OneOrderVO [] oneOrderVOs=new OneOrderVO[orders.size()];
       for(int i=0;i<orders.size();i++){
           OneOrder oneOrder = orders.get(i);
           FarmOrderRule rule=farmOrderRuleContainer.getElement(oneOrder.getOrderId());
           OneOrderVO oneOrderVO=new OneOrderVO(oneOrder.getId(),rule.getName());
           oneOrderVO.setExp(rule.getExpReward());
           oneOrderVO.setGameMoney(rule.getCurrencyReward());

           List<OrderGoods> goods = rule.getOrderGoodsList();
           GoodsVO[] goodsItems = new GoodsVO[goods.size()];
           for(int k=0;k<goods.size();k++){
               OrderGoods orderGoods = goods.get(k);
               GoodsVO goodsVO=new GoodsVO();
               goodsVO.setItemId(orderGoods.getItemId());
               goodsVO.setNeedNum(orderGoods.getNum());
               goodsVO.setMyNum(farmPackage.getSumByNum(orderGoods.getItemId()));
               goodsItems[k] = goodsVO;
           }
           oneOrderVO.setGoodsVOList(goodsItems);
           oneOrderVOs[i] = oneOrderVO;
       }
       farmOrderInfoVo.setOrderVOList(oneOrderVOs);

       return farmOrderInfoVo;
   }

}
