package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.dao.IActivePlayerDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspActiveFactory;
import com.pengpeng.stargame.vo.role.ActiveReq;
import com.pengpeng.stargame.vo.role.ActiveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-12下午12:13
 */
@Component
public class ActiveRpc extends RpcHandler {

    @Autowired
    private IActivePlayerDao activePlayerDao;

    @Autowired
    private RspActiveFactory rspActiveFactory;

    @Autowired
    private IActivePlayerContainer activePlayerContainer;

    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @RpcAnnotation(cmd="active.list",lock=false,req=ActiveReq.class,name="取得活跃度信息")
    public ActiveVO getActiveList(Session session,ActiveReq req){
        ActivePlayer ap = activePlayerDao.getBean(session.getPid());
        ActiveVO vo = rspActiveFactory.newActiveVO(ap);
        return vo;
    }

    @RpcAnnotation(cmd="active.reward",lock=true,req=ActiveReq.class,name="领取积分")
    public int reward(Session session,ActiveReq req) throws RuleException {
        //计算获得积分
        //检查是否可领取
        //调用网站接口加积分
        //保存领取后的结果
        ActivePlayer ap = activePlayerDao.getBean(session.getPid());
        int score = activePlayerContainer.reward(ap,req.getActive());
        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_8, ap.getId(), String.valueOf(score)));

        return score;
    }
}