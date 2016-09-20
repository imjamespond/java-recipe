package com.pengpeng.stargame.antiaddiction.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.antiaddiction.container.IAntiAddictionContainer;
import com.pengpeng.stargame.antiaddiction.dao.IPlayerAntiAddictionDao;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.antiaddiction.PlayerAntiAddiction;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.antiaddiction.AntiAddictionReq;
import com.pengpeng.stargame.vo.antiaddiction.AntiAddictionVO;
import com.pengpeng.stargame.vo.lottery.LotteryInfoVO;
import com.pengpeng.stargame.vo.lottery.LotteryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */
@Component
public class AntiAddictionRpc extends RpcHandler {
    @Autowired
    public IPlayerAntiAddictionDao antiAddictionDao;
    @Autowired
    public IOtherPlayerDao otherPlayerDao;
    @Autowired
    private IAntiAddictionContainer antiAddictionContainer;



    @RpcAnnotation(cmd="antiaddiction.certificate",vo=AntiAddictionVO.class,req=AntiAddictionReq.class,name="实名认证")
    public AntiAddictionVO certificate(Session session ,AntiAddictionReq req) throws GameException {

        PlayerAntiAddiction player = antiAddictionDao.getPlayerAntiAddiction(req.getPid());

        long birth = antiAddictionContainer.check(req.getIdentity());

        player.setIndentity(req.getIdentity());
        player.setName(req.getName());
        player.setBirth(birth);
        antiAddictionDao.saveBean(player);

        OtherPlayer op = otherPlayerDao.getBean(req.getPid());
        AntiAddictionVO vo = new AntiAddictionVO();
        vo.setCertificate(player.getBirth()!=0l);
        vo.setEightteen(antiAddictionContainer.isEighteen(player.getBirth()));
        vo.setOnlineTime(op.getAccumulateOnlineTime(new Date()));
        return vo;
    }

    @RpcAnnotation(cmd="antiaddiction.check",vo=AntiAddictionVO.class,req=AntiAddictionReq.class,name="是否已实名认证")
    public AntiAddictionVO check(Session session ,AntiAddictionReq req) throws GameException {
        OtherPlayer op = otherPlayerDao.getBean(req.getPid());
        PlayerAntiAddiction player = antiAddictionDao.getPlayerAntiAddiction(req.getPid());
        AntiAddictionVO vo = new AntiAddictionVO();
        vo.setCertificate(player.getBirth()!=0l);
        vo.setEightteen(antiAddictionContainer.isEighteen(player.getBirth()));
        vo.setOnlineTime(op.getAccumulateOnlineTime(new Date()));
        return vo;
    }

}
