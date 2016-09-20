package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.*;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.stall.container.IStallFriShelfContainer;
import com.pengpeng.stargame.stall.container.IStallGoldShelfContainer;
import com.pengpeng.stargame.stall.container.IStallPassengerContainer;
import com.pengpeng.stargame.stall.container.IStallVipShelfContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.stall.rule.StallPassengerFashionRule;
import com.pengpeng.stargame.stall.rule.StallPassengerPurchaseRule;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.fashion.FashionVO;
import com.pengpeng.stargame.vo.stall.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */


@Component
public class RspStallPassengerFactory extends RspFactory {
    @Autowired
    private IPlayerStallDao playerStallDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private IStallGoldShelfContainer stallGoldShelfContainer;
    @Autowired
    private IStallFriShelfContainer stallFriShelfContainer;
    @Autowired
    private IStallPassengerContainer stallPassengerContainer;
    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;

    public StallPassengerInfoVO stallPassengerInfoVO(PlayerStallPassengerInfo info,FarmPackage fp) throws AlertException {
        StallPassengerInfoVO vo = new StallPassengerInfoVO();
        PlayerStallPassenger[] passengers = info.getPassengers();
        List<StallPassengerVO> list = new ArrayList<StallPassengerVO>();
        for(int i=0;i<passengers.length;i++){
            PlayerStallPassenger p = passengers[i];
            if(p==null)
                continue;
            StallPassengerVO pVO = new StallPassengerVO();
            if(null!=p.getFashionId()){
                StallPassengerFashionRule rule = stallPassengerContainer.getFashionRules(p.getFashionId());
                FashionVO[] fashionVOs = new FashionVO[4];
                fashionVOs[0] = fashionVO(rule.getHair(), BaseItemConstant.FASHION_HAIR);
                fashionVOs[1] = fashionVO(rule.getFace(),BaseItemConstant.FASHION_FACE);
                fashionVOs[2] = fashionVO(rule.getCloth(),BaseItemConstant.FASHION_CLOTH);
                fashionVOs[3] = fashionVO(rule.getPants(),BaseItemConstant.FASHION_PANTS);
                pVO.setGender(rule.getGender());
                pVO.setFashionVO(fashionVOs);
            }
            if(null!=p.getPurchaseId()){
                StallPassengerPurchaseRule rule = stallPassengerContainer.getPurchaseRules(p.getPurchaseId());
                pVO.setItemId(rule.getItemId());
                pVO.setItemNum(rule.getItemNum());
                pVO.setInventory(fp.getSumByNum(rule.getItemId()));
                pVO.setGameCoin(rule.getGameCoin());
            }
            pVO.setAction(p.getAction());
            pVO.setCredit(p.getCredit());
            pVO.setPassengerId(i);
            list.add(pVO);
            p.setAction(0);//路人在等待
        }
        vo.setPassengerVOs(list.toArray(new StallPassengerVO[0]));
        vo.setRefreshTime(StallConstant.PASSENGER_REFRESH - System.currentTimeMillis() + info.getRefreshDate().getTime());
        return vo;
    }

    private FashionVO fashionVO(String itemId,int type){
        FashionVO vo = new FashionVO();
        FashionItemRule rule = fashionItemRuleContainer.getElement(itemId);
        vo.setIcon(rule.getIcon());
        vo.setImage(rule.getImage());
        vo.setItemId(rule.getItemsId());
        vo.setType(type);
        return vo;
    }
}
