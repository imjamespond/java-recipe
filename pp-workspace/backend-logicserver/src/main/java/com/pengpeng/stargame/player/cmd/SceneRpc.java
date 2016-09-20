package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.ScenePlayer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.player.container.ITransferRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.IScenePlayerDao;
import com.pengpeng.stargame.player.rule.TransferRule;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.rsp.RspMapFactory;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.pengpeng.stargame.util.SceneConstant;
import com.pengpeng.stargame.vo.map.MapReq;
import com.pengpeng.stargame.vo.map.MoveReq;
import com.pengpeng.stargame.vo.map.MoveVO;
import com.pengpeng.stargame.vo.map.ScenceVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-25上午11:22
 */
@Component()
public class SceneRpc extends RpcHandler {


    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private ISceneRuleContainer sceneRuleContainer;

    @Autowired
    private ITransferRuleContainer transferRuleContainer;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspMapFactory mapFactory;
    @Autowired
    private StatusRemote statusRemote;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IScenePlayerDao scenePlayerDao;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;

    private final Logger logger = Logger.getLogger(getClass());


    @RpcAnnotation(cmd = "scene.enter", lock = false, req = MapReq.class, name = "进入指定场景，不同场景")
    public ScenceVO enterScene(Session session, MapReq req) throws GameException {
        if (session == null || session.getPid() == null) {
            exceptionFactory.throwAlertException("p.notlogin");
        }
        Player player = playerDao.getBean(req.getPid());
        if (player == null) {
            exceptionFactory.throwAlertException("p.notplayer");
        }
        if (!sceneRuleContainer.checkEnter(req.getMapId(), player)) {
            exceptionFactory.throwAlertException("scene.notenter");
        }
        String oldChannelId = SessionUtil.getChannelScene(session);
        if (oldChannelId != null) {
            //进入新地图之前检查退出旧地图
            outerScene(session, oldChannelId);
        }

        int x = req.getX();
        int y = req.getY();
        if (x == 0 || y == 0) {
            TransferRule transferRule = transferRuleContainer.getEnterTransfer(req.getMapId());
            if (transferRule != null) {
                x = transferRule.getTargetX();
                y = transferRule.getTargetY();
            }
            if(req.getMapId().equals(sceneRuleContainer.getDefaultScene().getId())){
                x=1284;
                y=1310;
            }
        }
        ScenePlayer sp = scenePlayerDao.getBean(player.getId());
        String channelId = sceneRuleContainer.getChannelId(req.getMapId(), player, req.getFamilyId());
        session.addParam("x", String.valueOf(x));
        session.addParam("y", String.valueOf(y));
        session.addParam("scene.id", req.getMapId());
        SessionUtil.setChannelScene(session, channelId);
        //进入地图
        statusRemote.enterChannel(session, channelId);
        sp.setScene(req.getMapId(), x, y);
        scenePlayerDao.saveBean(sp);
        //同步session状态
        Map<String, String> map = session.getValues();
        frontendService.onSessionEvent(session, map);
        //取得这个频道的所有用户
        Session[] sessions = statusRemote.getMember(session, channelId);
        MoveVO move = new MoveVO(session.getPid(), x, y, SceneConstant.MOVE_TYPE_ENTER_1);
        //广播给所有人
        frontendService.broadcast(sessions, move);

        //返回场景中所有人物
        MoveVO[] vos = null;
        if (sessions == null || sessions.length == 0) {

            logger.info(String.format("玩家数据出错  玩家id: %s 场景: %s ", session.getPid(), channelId));

        }
        if (sessions != null && sessions.length > 0) {
            vos = new MoveVO[sessions.length];
            for (int i = 0; i < sessions.length; i++) {
                if (sessions[i] == null) {
                    continue;
                }
                int xx = SessionUtil.getInt(sessions[i], "x");
                int yy = SessionUtil.getInt(sessions[i], "y");
                vos[i] = new MoveVO(sessions[i].getPid(), xx, yy, SceneConstant.MOVE_TYPE_ENTER_1);
            }
        }
        SceneRule rule = sceneRuleContainer.getElement(req.getMapId());
        ScenceVO vo = mapFactory.newSceneRsp(rule);
        vo.setPlayers(vos);


        //日志
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_4, player.getId(), req.getMapId()));

        return vo;
    }

    @RpcAnnotation(cmd = "scene.to", lock = false, req = MapReq.class, name = "进入指定场景和指定坐标,同一个场景")
    public void enterTo(Session session, MapReq req) throws GameException {
        if (session == null || session.getScene() == null) {
            return;
        }
        Player player = playerDao.getBean(req.getPid());
        if (player == null) {
            exceptionFactory.throwAlertException("p.notplayer");
        }
        if (session.getScene().equals(req.getMapId())) {
            String channelId = sceneRuleContainer.getChannelId(session.getScene(), player, req.getFamilyId());
            session.addParam("x", String.valueOf(req.getX()));
            session.addParam("y", String.valueOf(req.getY()));
            session.addParam("scene.id", req.getMapId());
            //同步session状态
            Map<String, String> map = session.getValues();
            frontendService.onSessionEvent(session, map);
            //取得这个频道的所有用户
            Session[] sessions = statusRemote.getMember(session, channelId);
            MoveVO move = new MoveVO(session.getPid(), req.getX(), req.getY(), SceneConstant.MOVE_TYPE_ENTER_1);
            //广播给所有人
            frontendService.broadcast(sessions, move);
        }
    }

    @RpcAnnotation(cmd = "scene.outer", lock = false, req = String.class, name = "退出地图")
    public void outerScene(Session session, String channelId) throws GameException {
        if (session == null || session.getPid() == null) {
            exceptionFactory.throwAlertException("p.notlogin");
        }

        //退出当前频道
        statusRemote.outerChannel(session, channelId);
        //取得当前频道的所有用户,发广播
        Session[] sessions = statusRemote.getMember(session, channelId);
        int x = 0;
        int y = 0;
        MoveVO move = new MoveVO(session.getPid(), x, y, SceneConstant.MOVE_TYPE_OUTER_2);
        frontendService.broadcast(sessions, move);
        SessionUtil.setChannelScene(session, null);
        frontendService.onSessionEvent(session, session.getValues());
    }

    @RpcAnnotation(cmd = "scene.get.info", lock = false, req = String.class, name = "取得地图信息")
    public ScenceVO getScene(Session session, String sceneId) throws GameException {
        SceneRule rule = sceneRuleContainer.getElement(sceneId);
        ScenceVO vo = mapFactory.newSceneRsp(rule);
        return vo;
    }

    @RpcAnnotation(cmd = "scene.move", lock = false, req = MoveReq.class, name = "移动")
    public MoveVO move(Session session, MoveReq req) {
//        try {
        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusRemote.getMember(session, channelId);
        Map<String, String> map = SessionUtil.newPoint(session.getPid(), req.getX(), req.getY());
        frontendService.onSessionEvent(session, map);//不对坐标进行广播
        MoveVO vo = new MoveVO(req.getId(), req.getX(), req.getY(), SceneConstant.MOVE_TYPE_MOVE_0);
        frontendService.broadcast(sessions, vo);
        return vo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
