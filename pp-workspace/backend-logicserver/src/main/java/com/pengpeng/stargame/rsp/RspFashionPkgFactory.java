package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.model.room.FashionItem;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.vo.fashion.FashionItemVO;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.fashion.FashionShopItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-29
 * Time: 上午11:56
 */
@Component
public class RspFashionPkgFactory extends RspFactory {
    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;

    public FashionPkgVO getFashionPkg(FashionPkg fashionPkg, String type) {
        FashionPkgVO vo = new FashionPkgVO();
        BeanUtils.copyProperties(fashionPkg, vo);
        vo.setItemType(type);

        List<FashionItem> listItem = fashionPkg.getItemAll();
        vo.setBaseItemVO(this.buildFashionItem(listItem));
        return vo;
    }

    public FashionItemVO[] buildFashionItem(List<FashionItem> list) {
        if (list == null || list.isEmpty()) {
            return new FashionItemVO[0];
        }
        List<FashionItemVO> fashionItemVOList = new ArrayList<FashionItemVO>();

        for (FashionItem farmItem : list) {
            FashionItemVO vo = new FashionItemVO();
            BeanUtils.copyProperties(farmItem, vo);
            if (farmItem.getValidDete() != null) {
                vo.setValidityDate(farmItem.getValidDete().getTime());
            }
            fashionItemVOList.add(vo);
        }
        return fashionItemVOList.toArray(new FashionItemVO[0]);
    }

    public FashionShopItemVO[] getFashionShopList(List<FashionItemRule> list) {
        if (list == null || list.isEmpty()) {
            return new FashionShopItemVO[0];
        }

        List<FashionShopItemVO> listVo = new ArrayList<FashionShopItemVO>();
        for (FashionItemRule rule : list) {
            listVo.add(this.getFashionShopItem(rule));
        }
        return listVo.toArray(new FashionShopItemVO[0]);
    }

    public FashionShopItemVO getFashionShopItem(FashionItemRule fashionItemRule) {
        FashionShopItemVO vo = new FashionShopItemVO();
        BeanUtils.copyProperties(fashionItemRule, vo);
        if (fashionItemRule.getValidDete() != null) {
            vo.setValidityDate(fashionItemRule.getValidDete().getTime());
        }
        return vo;
    }

    public FashionItemVO buildFashionItemVO(FashionItem fashionItem, String type) {
        FashionItemVO vo = new FashionItemVO();
        BeanUtils.copyProperties(fashionItem, vo);
        return vo;
    }
}
