package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.Gift;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.rule.BaseGiftRule;
import com.pengpeng.stargame.vo.gift.ShopGiftVO;
import com.pengpeng.stargame.vo.role.GiftVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:27
 */
@Component
public class RspGiftFactory extends RspFactory {

    public GiftVO newGiftVO(Player player,Gift gift){
        GiftVO vo = new GiftVO();
        vo.setFid(gift.getFid());
        vo.setName(player.getNickName());
        vo.setItemId(gift.getItemId());
        vo.setNum(gift.getNum());
//        vo.setValidityTime(gift.getValidityDate());
        vo.setIcon(getUserPortrait(player.getUserId()));
        return vo;
    }

	public ShopGiftVO[] buildShopGiftVO(List<BaseGiftRule> list, int presentType) {
		if(list ==null || list.isEmpty()){
			return new ShopGiftVO[0];
		}
		List<ShopGiftVO> listVo = new ArrayList<ShopGiftVO>();
		for(BaseGiftRule rule : list){
			ShopGiftVO vo = new ShopGiftVO();
			BeanUtils.copyProperties(rule,vo);
			listVo.add(vo);
		}
		return listVo.toArray(new ShopGiftVO[0]);
	}
}
