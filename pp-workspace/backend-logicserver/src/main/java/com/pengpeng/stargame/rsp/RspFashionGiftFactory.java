package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.Gift;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.role.GiftVO;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-4上午11:27
 */
@Component
public class RspFashionGiftFactory extends RspFactory {

    public GiftVO newGiftVO(Player player,Gift gift){
        GiftVO vo = new GiftVO();
        vo.setFid(gift.getFid());
        vo.setName(player.getNickName());
        vo.setItemId(gift.getItemId());
        vo.setNum(gift.getNum());
        vo.setMessage(gift.getMessage());
//        vo.setValidityTime(gift.getValidityDate());
        vo.setIcon(getUserPortrait(player.getUserId()));
        return vo;
    }
}
