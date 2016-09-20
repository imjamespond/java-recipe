package com.pengpeng.stargame.fashion.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.fashion.dao.IFashionRankDao;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.log.GameLogger;
import com.pengpeng.stargame.log.GameLoggerWrite;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFashionFactory;
import com.pengpeng.stargame.rsp.RspFashionPkgFactory;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.fashion.PlayerFashionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午7:26
 */
@Component
public class FashionRpc extends RpcHandler {
    @Autowired
    private RspFashionFactory rspFashionFactory;
    @Autowired
    private IFashionPlayerDao fashionPlayerDao;
    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private RspFashionPkgFactory factory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private GameLoggerWrite gameLoggerWrite;
    @Autowired
    private IFashionRankDao fashionRankDao;

    public PlayerFashionVO getPlayerFashionVOByPlayerId(String pId) {
        FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(pId);
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(pId);
        return rspFashionFactory.getPlayerFashionVO(fashionPlayer,fashionCupboard);
    }
    @RpcAnnotation(cmd = "fashion.get",lock = false,req = FashionIdReq.class, name = "获取身上穿的服装")
    public PlayerFashionVO  getFashion(Session session, FashionIdReq req) throws AlertException{
        return getPlayerFashionVOByPlayerId(req.getPid());
    }

    @RpcAnnotation(cmd = "fashion.change", req = FashionIdReq.class, name = "换装")
    public void  change(Session session, FashionIdReq req) throws AlertException{
        String id=req.getFashionId();
        String itemId=req.getItemId();
        FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(session.getPid());
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(session.getPid());

        fashionItemRuleContainer.checkChange(fashionPlayer, fashionCupboard, itemId, id);

        fashionItemRuleContainer.change(fashionPlayer,fashionCupboard,itemId,id);

        fashionPlayerDao.saveBean(fashionPlayer);
        fashionCupboardDao.saveBean(fashionCupboard);



        /**
         * 广播数据
         */
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, getPlayerFashionVOByPlayerId(session.getPid()));

        FashionItemRule rule = fashionItemRuleContainer.getElement(itemId);
        String type = String.valueOf(rule.getItemtype());
        FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
        FashionPkgVO vo = factory.getFashionPkg(fashionPkg,type);
        frontendService.broadcast(mysessions,vo);

        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions,this.getPlayerFashionVOByPlayerId(session.getPid()));

        /**
         * 排行榜的处理
         */
        if(fashionRankDao.contains("",session.getPid())){
            fashionRankDao.removeBean("",session.getPid());
        }
        fashionRankDao.addBean("",session.getPid(),fashionItemRuleContainer.getFasionValue(fashionPlayer,fashionCupboard));

        BaseItemRule baseItemRule=  baseItemRulecontainer.getElement(itemId);
        String value = itemId + GameLogger.SPLIT + String.valueOf(1)+ GameLogger.SPLIT + baseItemRule.getType()+GameLogger.SPLIT + baseItemRule.getItemtype();
        gameLoggerWrite.write(new GameLogger(GameLogger.LOG_17, fashionPlayer.getId(), value));
    }

    @RpcAnnotation(cmd = "fashion.takeOff", req = FashionIdReq.class, name = "脱掉服装")
    public void takeOff(Session session,FashionIdReq req) throws AlertException {
        String id=req.getFashionId();
        String type=req.getType();

        FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(session.getPid());
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(session.getPid());

        fashionItemRuleContainer.takeOff(fashionPlayer,fashionCupboard,id,type);

        fashionPlayerDao.saveBean(fashionPlayer);
        fashionCupboardDao.saveBean(fashionCupboard);

        /**
         * 广播数据
         */
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, getPlayerFashionVOByPlayerId(session.getPid()));

//        FashionItemRule rule = fashionItemRuleContainer.getElement(itemId);
//        String type = String.valueOf(rule.getItemtype());
        FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
        FashionPkgVO vo = factory.getFashionPkg(fashionPkg,type);
        frontendService.broadcast(mysessions,vo);

        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions,this.getPlayerFashionVOByPlayerId(session.getPid()));

        /**
         * 排行榜的处理
         */
        if(fashionRankDao.contains("",session.getPid())){
            fashionRankDao.removeBean("",session.getPid());
        }
        fashionRankDao.addBean("",session.getPid(),fashionItemRuleContainer.getFasionValue(fashionPlayer,fashionCupboard));

    }


    /**
     * 卸下 所有 。。
     */
    @RpcAnnotation(cmd = "fashion.takeOffAll", req = FashionIdReq.class, name = "卸下所有服装")
    public void takeOffAll(Session session,FashionIdReq req){
        FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(session.getPid());
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(session.getPid());

        fashionItemRuleContainer.takeOffAll(fashionPlayer,fashionCupboard);


        fashionPlayerDao.saveBean(fashionPlayer);
        fashionCupboardDao.saveBean(fashionCupboard);

        /**
         * 广播数据
         */
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, getPlayerFashionVOByPlayerId(session.getPid()));

        for(int i=1;i<9;i++){
            String type =String.valueOf(i);
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            FashionPkgVO vo = factory.getFashionPkg(fashionPkg,type);
            frontendService.broadcast(mysessions,vo);
        }

        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions,this.getPlayerFashionVOByPlayerId(session.getPid()));

        /**
         * 排行榜的处理
         */
        if(fashionRankDao.contains("",session.getPid())){
            fashionRankDao.removeBean("",session.getPid());
        }
        fashionRankDao.addBean("",session.getPid(),fashionItemRuleContainer.getFasionValue(fashionPlayer,fashionCupboard));
    }

    /**
     * 随机  搭配
     */
    @RpcAnnotation(cmd = "fashion.randomFromPkg", req = FashionIdReq.class, name = "从 背包里面随机搭配 穿戴")
     public void  randomFromPkg(Session session,FashionIdReq req) throws  AlertException{
        FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(session.getPid());
        FashionCupboard fashionCupboard=fashionCupboardDao.getBean(session.getPid());



        fashionItemRuleContainer.checkRandomFromPkg(fashionPlayer,fashionCupboard);


        fashionItemRuleContainer.randomFromPkg(fashionPlayer,fashionCupboard);

        fashionPlayerDao.saveBean(fashionPlayer);
        fashionCupboardDao.saveBean(fashionCupboard);
        /**
         * 广播数据
         */
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, getPlayerFashionVOByPlayerId(session.getPid()));

        for(int i=1;i<9;i++){
            String type =String.valueOf(i);
            FashionPkg fashionPkg = fashionCupboard.getFashionPkg(type);
            FashionPkgVO vo = factory.getFashionPkg(fashionPkg,type);
            frontendService.broadcast(mysessions,vo);
        }

        String channelId = SessionUtil.getChannelScene(session);
        Session[] sessions = statusService.getMember(session, channelId);
        frontendService.broadcast(sessions,this.getPlayerFashionVOByPlayerId(session.getPid()));

        /**
         * 排行榜的处理
         */
        if(fashionRankDao.contains("",session.getPid())){
            fashionRankDao.removeBean("",session.getPid());
        }
        fashionRankDao.addBean("",session.getPid(),fashionItemRuleContainer.getFasionValue(fashionPlayer,fashionCupboard));
     }
}
