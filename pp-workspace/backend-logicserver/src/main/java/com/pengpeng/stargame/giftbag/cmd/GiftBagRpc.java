package com.pengpeng.stargame.giftbag.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.cmd.FarmPkgRpc;
import com.pengpeng.stargame.giftbag.GiftBagConstant;
import com.pengpeng.stargame.giftbag.container.IGiftBagRuleContainer;
import com.pengpeng.stargame.giftbag.dao.IGiftBagDao;
import com.pengpeng.stargame.model.giftBag.PlayerGiftGag;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.giftBag.GiftBagReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-12-12
 * Time: 下午4:46
 */
@Component
public class GiftBagRpc extends RpcHandler {
    @Autowired
    private IGiftBagDao giftBagDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private IGiftBagRuleContainer giftBagRuleContainer;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private FarmPkgRpc farmPkgRpc;
    @RpcAnnotation(cmd = "giftbag.open", lock = true, vo = RewardVO.class, req = GiftBagReq.class, name = "打开礼包")
    public RewardVO getGiftBag(Session session, GiftBagReq req) throws RuleException, AlertException {
        Player player=playerDao.getBean(session.getPid());
        PlayerGiftGag playerGiftGag=giftBagDao.getPlayerGiftBag(session.getPid());

        RewardVO rewardVO=null;
        if(req.getType()==1){ //圣诞礼包
            if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.M_C_GIFTID)){
                rewardVO=giftBagRuleContainer.openGiftBag(player,playerGiftGag,GiftBagConstant.M_C_GIFTID);
                rewardVO.setType(12);
            }
       }
        if(req.getType()==2){ //元旦礼包
            if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.Y_D_GIFTID)){
                rewardVO=giftBagRuleContainer.openGiftBag(player,playerGiftGag,GiftBagConstant.Y_D_GIFTID);
                rewardVO.setType(13);
            }
        }
        if(req.getType()==3){ //春节礼包
            if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.C_J_GIFTID)){
                rewardVO=giftBagRuleContainer.openGiftBag(player,playerGiftGag,GiftBagConstant.C_J_GIFTID);
                rewardVO.setType(14);
            }
        }
        if(req.getType()==4){ //音乐活动礼物，每人领取一次
            if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.MUSIC)){
                rewardVO=giftBagRuleContainer.openGiftBag(player,playerGiftGag,GiftBagConstant.MUSIC);
                rewardVO.setType(15);
            }
        }
        if(req.getType()==5){ //五一活动礼物，每人领取一次
            if(!playerGiftGag.getHistoryGet().containsKey(GiftBagConstant.W_1)){
                rewardVO=giftBagRuleContainer.openGiftBag(player,playerGiftGag,GiftBagConstant.W_1);
                rewardVO.setType(16);
            }
        }
        if(req.getGiftId()!=null&&!req.getGiftId().equals("")){
            if(!playerGiftGag.getHistoryGet().containsKey(req.getGiftId())){
                rewardVO=giftBagRuleContainer.openGiftBag(player,playerGiftGag,req.getGiftId());
            }
        }
        playerDao.saveBean(player);
        giftBagDao.saveBean(playerGiftGag);

        Session[] mysessions={session};
        frontendService.broadcast(mysessions,roleFactory.newPlayerVO(player));

        /**
         * 广播农场背包
         */
//        frontendService.broadcast(session,farmPkgRpc.getItemByPid(session.getPid()));
        return rewardVO;
    }
}
