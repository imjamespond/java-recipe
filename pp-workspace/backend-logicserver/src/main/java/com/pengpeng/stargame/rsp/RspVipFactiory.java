package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vip.rule.PayMemberRule;
import com.pengpeng.stargame.vo.vip.VipInfoVO;
import com.pengpeng.stargame.vo.vip.VipPrivilegeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: mql
 * Date: 13-11-21
 * Time: 上午11:28
 */
@Component
public class RspVipFactiory extends RspFactory {

    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private ISceneRuleContainer sceneRuleContainer;

    public VipInfoVO getVipInfoVO(Session session,PlayerVip vipInfo,Player player){
        VipInfoVO vipInfoVO=new VipInfoVO();
        vipInfoVO.setVip(vipInfo.getViP());
        vipInfoVO.setIcon(getUserPortrait(player.getUserId()));
        vipInfoVO.setName(player.getNickName());

        List<PayMemberRule> payMemberRules=payMemberRuleContainer.getListByLevel(vipInfo.getLevel());
        Collections.sort(payMemberRules,new Comparator<PayMemberRule>() {
            @Override
            public int compare(PayMemberRule o1, PayMemberRule o2) {
                return o1.getType()-o2.getType();
            }
        });
        List<String> desList=new ArrayList<String>();
        List<VipPrivilegeVO> vipPrivilegeVOList=new ArrayList<VipPrivilegeVO>();
        for(PayMemberRule payMemberRule:payMemberRules){
            desList.add(payMemberRule.getDes());
            VipPrivilegeVO vipPrivilegeVO=new VipPrivilegeVO();
            vipPrivilegeVOList.add(vipPrivilegeVO);
        }
        vipInfoVO.setDes(desList.toArray(new String[0]));
        vipInfoVO.setVipPrivilegeVOs(vipPrivilegeVOList.toArray(new VipPrivilegeVO[0]));

        if(vipInfo.getViP()==1&&vipInfo.getEndTime()!=null){
            Date now=new Date();
            vipInfoVO.setSurplusTime(vipInfo.getEndTime().getTime()-now.getTime());
        }

        if(session.getScene()!=null){
            if(sceneRuleContainer.getElement(session.getScene())!=null){
                vipInfoVO.setMapName(sceneRuleContainer.getElement(session.getScene()).getName());
            }
        }

        return vipInfoVO;
    }
}
