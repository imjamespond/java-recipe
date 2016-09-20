package com.pengpeng.stargame.qinma.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.ScenePlayer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.container.ITransferRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.IScenePlayerDao;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.pengpeng.stargame.player.rule.TransferRule;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspMapFactory;
import com.pengpeng.stargame.util.SceneConstant;
import com.pengpeng.stargame.vo.map.MapReq;
import com.pengpeng.stargame.vo.map.MoveVO;
import com.pengpeng.stargame.vo.map.ScenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-14下午4:20
 */
@Component
public class QinmaSceneRpc extends RpcHandler {

    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private ISceneRuleContainer sceneRuleContainer;


    @Autowired
    private ITransferRuleContainer transferRuleContainer;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private RspMapFactory mapFactory;

    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private IScenePlayerDao scenePlayerDao;

    @RpcAnnotation(cmd="qinma.scene.enter",lock = false,req=MapReq.class,name="进入亲妈农场")
    public ScenceVO enterFarm(Session session, MapReq req) throws GameException {
        Player player = playerDao.getBean(session.getPid());
        if(player==null){
            exceptionFactory.throwAlertException("p.notplayer");
        }
        //退出原来的场景
        //进入亲妈农场
        //设置场景id 到session
        //同步session数据

        String oldChannelId = SessionUtil.getChannelScene(session);
        if (oldChannelId!=null){
            //进入新地图之前检查退出旧地图
            outerScene(session,oldChannelId);
        }
        int x = req.getX();
        int y = req.getY();
        if (x==0||y==0){
            TransferRule transferRule = transferRuleContainer.getEnterTransfer(req.getMapId());
            if (transferRule!=null){
                x = transferRule.getTargetX();
                y = transferRule.getTargetY();
            }
        }
        ScenePlayer sp = scenePlayerDao.getBean(player.getId());
        String channelId = sceneRuleContainer.getChannelId(req.getMapId(),player,req.getFamilyId());
        session.addParam("x",String.valueOf(x));
        session.addParam("y",String.valueOf(y));
        session.addParam("scene.id",req.getMapId());
        SessionUtil.setChannelScene(session, channelId);
        //进入地图
        statusRemote.enterChannel(session, channelId);
        sp.setScene(req.getMapId(),x,y);
        scenePlayerDao.saveBean(sp);
        //同步session状态
        Map<String,String> map = session.getValues();
        frontendService.onSessionEvent(session,map);
        //取得这个频道的所有用户
        Session[] sessions = statusRemote.getMember(session,channelId);
        MoveVO move = new MoveVO(session.getPid(),x,y, SceneConstant.MOVE_TYPE_ENTER_1);
        //广播给所有人
        frontendService.broadcast(sessions,move);

        //返回场景中所有人物
        MoveVO[] vos = null;
        if (sessions!=null&&sessions.length>0){
            vos = new MoveVO[sessions.length];
            for(int i=0;i<sessions.length;i++){
                if (sessions[i]==null){
                    continue;
                }
                int xx = SessionUtil.getInt(sessions[i], "x");
                int yy= SessionUtil.getInt(sessions[i], "y");
                vos[i] = new MoveVO(sessions[i].getPid(),xx,yy,SceneConstant.MOVE_TYPE_ENTER_1);
            }
        }
        SceneRule rule = sceneRuleContainer.getElement(req.getMapId());
        ScenceVO vo = mapFactory.newSceneRsp(rule);
        vo.setPlayers(vos);
        return vo;
    }

    private void outerScene(Session session,String channelId) throws GameException {
        if (session==null||session.getPid()==null){
            exceptionFactory.throwAlertException("p.notlogin");
        }

        //退出当前频道
        statusRemote.outerChannel(session,channelId);
        //取得当前频道的所有用户,发广播
        Session[] sessions = statusRemote.getMember(session,channelId);
        int x = 0;
        int y = 0;
        MoveVO move = new MoveVO(session.getPid(),x,y,SceneConstant.MOVE_TYPE_OUTER_2);
        frontendService.broadcast(sessions,move);
        SessionUtil.setChannelScene(session, null);
        frontendService.onSessionEvent(session, session.getValues());
    }
}
