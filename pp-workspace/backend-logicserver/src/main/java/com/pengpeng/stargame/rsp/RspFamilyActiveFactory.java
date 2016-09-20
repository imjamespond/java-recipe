package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import com.pengpeng.stargame.vo.piazza.ActivityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 上午9:51
 */
@Component()
public class RspFamilyActiveFactory extends RspFactory {
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    public ActivityVO[] getActivetyVos(List<ActiveControlRule> activeControlRuleList){
        List<ActivityVO> activityVOList=new ArrayList<ActivityVO>();
        for(ActiveControlRule activeControlRule:activeControlRuleList){
            ActivityVO activityVO=new ActivityVO();
            BeanUtils.copyProperties(activeControlRule,activityVO);
            activityVO.setOpenLevel(activeControlRule.getLevel());
            activityVO.setId(activeControlRule.getId());
            activityVO.setDesc(activeControlRule.getContentDesc());
            List<GoodsVO> list=new ArrayList<GoodsVO>();
            if(activeControlRule.getItemsDisplay()!=null&&!activeControlRule.getItemsDisplay().equals("")&&!activeControlRule.getItemsDisplay().equals("0")){
                String [] items=activeControlRule.getItemsDisplay().split(";");
                for(String itemId: items){
                     GoodsVO goodsVO=new GoodsVO();
                    goodsVO.setItemId(itemId);
                    goodsVO.setIcon(baseItemRulecontainer.getElement(itemId).getIcon());
                    list.add(goodsVO)  ;
                }
            }
            activityVO.setGoodsVOs(list.toArray(new GoodsVO[0]));
            activityVOList.add(activityVO);
        }
        return activityVOList.toArray(new ActivityVO[0]);
    }
}
