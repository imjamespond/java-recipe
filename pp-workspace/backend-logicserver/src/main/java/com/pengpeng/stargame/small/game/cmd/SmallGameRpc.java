package com.pengpeng.stargame.small.game.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.integral.container.impl.IIntegralRuleContainerImpl;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.cmd.MailRpc;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspSmallGameFactory;
import com.pengpeng.stargame.small.game.container.ISmallGameContainer;
import com.pengpeng.stargame.small.game.dao.IPlayerSmallGameDao;
import com.pengpeng.stargame.small.game.dao.ISmallGameSetDao;
import com.pengpeng.stargame.small.game.rule.SmallGameRule;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.role.MailReq;
import com.pengpeng.stargame.vo.smallgame.SmallGameListVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameRankVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameReq;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:04
 */
@Component
public class SmallGameRpc extends RpcHandler {
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private ISmallGameContainer smallGameContainer;

    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private SiteDao siteDao;
    @Autowired
    private RspSmallGameFactory rspSmallGameFactory;

    @Autowired
    private IPlayerSmallGameDao playerSmallGameDao;
    @Autowired
    private ISmallGameSetDao smallGameSetDao;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    private MessageSource message;
    @Autowired
    private MailRpc mailRpc;

    @Autowired
    IActivePlayerContainer activePlayerContainer;
    @Autowired
     private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IIntegralRuleContainerImpl iIntegralRuleContainer;

    private static int FANSVAL = 10;//加粉丝值
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

    @RpcAnnotation(cmd="update.rank",vo=SmallGameRankVO.class,req=SmallGameReq.class,name="更新排行",lock = true)
    public SmallGameRankVO updateRank(Session session,SmallGameReq req) throws GameException {
        String pid = session.getPid();

        PlayerSmallGame playerSmallGame = playerSmallGameDao.getPlayerSmallGame(pid);
        smallGameContainer.update(playerSmallGame, req.getType(),req.getScore(),req.getNum()*10000L);
        playerSmallGameDao.saveBean(playerSmallGame);

        //历史最高
        String maxKey = smallGameContainer.getMax(req.getType()) ;
        smallGameSetDao.addBean(maxKey,pid,req.getScore());

        //本周最高
        String weekKey = smallGameContainer.getWeekKey(req.getType()) ;
        smallGameSetDao.addBeanExpire(weekKey,pid,req.getScore(),10);

        //每天最高
        //String dayKey = smallGameContainer.getDayKey(req.getType()) ;
        //smallGameSetDao.addBeanExpire(dayKey,pid,req.getScore(),2);

        //增加粉丝值
        FamilyMemberInfo familyMemberInfo = familyMemberInfoDao.getFamilyMember(pid);
        Family family =familyDao.getBean(familyMemberInfo.getFamilyId());
        family.setFansValue(family.getFansValue()+FANSVAL);
        familyDao.saveBean(family);

        /**
         * 添加活跃度
         */
        if(req.getScore()>=500){
        activePlayerContainer.finish(session, session.getPid(), PlayerConstant.ACTIVE_TYPE_7, 1);
        }
        /**
         * 任务的
         */
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_18,"",req.getScore());

        return getRank(session,req);
    }

    @RpcAnnotation(cmd="small.game.deduct",vo=SmallGameRankVO.class,req=SmallGameReq.class,name="扣减次数",lock = true)
    public SmallGameRankVO deduct(Session session,SmallGameReq req) throws GameException {
        String pid = session.getPid();
        //扣次数
        PlayerSmallGame playerSmallGame = playerSmallGameDao.getPlayerSmallGame(pid);
        smallGameContainer.deduct(playerSmallGame, req.getType());
        playerSmallGameDao.saveBean(playerSmallGame);
        /**
         * 成就排行 玩小游戏次数
         */
        successRuleContainer.updateSuccessNum(session.getPid(),18,1,"");
        return rspSmallGameFactory.smallGameRankVO2(playerSmallGame,req.getType());
    }

    @RpcAnnotation(cmd="get.list",vo=SmallGameRankVO.class,req=SmallGameReq.class,name="获取小游戏列表",lock = true)
    public SmallGameListVO getlist(Session session,SmallGameReq req) throws GameException {
        String pid = session.getPid();

        PlayerSmallGame playerSmallGame = playerSmallGameDao.getPlayerSmallGame(pid);

        SmallGameListVO smallGameListVO = rspSmallGameFactory.SmallGameListVO(smallGameContainer.values().size());
        Iterator<SmallGameRule> it= smallGameContainer.values().iterator();
        int i = 0;
        while (it.hasNext()){
            SmallGameRule smallGameRule = it.next();
            //历史最高
            String maxKey = smallGameContainer.getMax(smallGameRule.getType()) ;
            //本周最高
            String weekKey = smallGameContainer.getWeekKey(smallGameRule.getType());
            smallGameContainer.checkFree(playerSmallGame,smallGameRule.getType());
            smallGameListVO.getSmallGameRankVOs()[i++] = rspSmallGameFactory.smallGameRankVO3(pid,smallGameRule.getName(),smallGameRule.getPriceStrategy(),maxKey,weekKey, playerSmallGame,smallGameRule.getType());
        }
        //更新登陆时间
        playerSmallGame.setLastLoginTime(new Date());
        playerSmallGameDao.saveBean(playerSmallGame);

        smallGameListVO.setBuyList(rspSmallGameFactory.buyList(playerSmallGame));

        return smallGameListVO;
    }

    //个人信息调用
    public SmallGameListVO getMyRank(Session session,SmallGameReq req) {
        String pid = req.getPid();

        PlayerSmallGame playerSmallGame = playerSmallGameDao.getPlayerSmallGame(pid);

        SmallGameListVO smallGameListVO = rspSmallGameFactory.SmallGameListVO(smallGameContainer.values().size());
        Iterator<SmallGameRule> it= smallGameContainer.values().iterator();
        int i = 0;
        while (it.hasNext()){
            SmallGameRule smallGameRule = it.next();
            //历史最高
            String maxKey = smallGameContainer.getMax(smallGameRule.getType()) ;
            //本周最高
            String weekKey = smallGameContainer.getWeekKey(smallGameRule.getType());
            smallGameListVO.getSmallGameRankVOs()[i++] = rspSmallGameFactory.smallGameRankVO4(pid,smallGameRule.getName(),playerSmallGame,smallGameRule.getPriceStrategy(),maxKey,weekKey,smallGameRule.getType());
        }

        return smallGameListVO;
    }

    @RpcAnnotation(cmd="get.rank",vo=SmallGameRankVO.class,req=SmallGameReq.class,name="获取排行",lock = true)
    public SmallGameRankVO getRank(Session session,SmallGameReq req) throws GameException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);

        //历史最高
        String maxKey = smallGameContainer.getMax(req.getType()) ;

        //本周最高
        String weekKey = smallGameContainer.getWeekKey(req.getType()) ;

        PlayerSmallGame playerSmallGame = playerSmallGameDao.getPlayerSmallGame(pid);
        return rspSmallGameFactory.smallGameRankVO(pid,maxKey, weekKey, playerSmallGame, req.getType());
    }


    @RpcAnnotation(cmd="gold.buy",vo=SmallGameRankVO.class,req=SmallGameReq.class,name="达人币购买次数",lock = true)
    public SmallGameListVO goldBuy(Session session,SmallGameReq req) throws GameException {
        String pid = session.getPid();

        Player player = playerDao.getBean(pid);
        PlayerSmallGame playerSmallGame = playerSmallGameDao.getPlayerSmallGame(pid);
        //当日购买记录
        int key = 100*req.getGold() + req.getType();//key=gold+type
        playerSmallGame.getBuyMap().put(key,new Date());

        smallGameContainer.buy(playerSmallGame,player,req.getType(),req.getGold());
        playerSmallGameDao.saveBean(playerSmallGame);
        playerDao.saveBean(player);

        //财富通知
        Session[] mysessions = {session};
        frontendService.broadcast(mysessions, roleFactory.newPlayerVO(player));
        //VO
        SmallGameListVO voList = new SmallGameListVO();
        SmallGameRankVO[] list = new SmallGameRankVO[1];
        list[0] = rspSmallGameFactory.smallGameRankVO2(playerSmallGame,req.getType());
        voList.setSmallGameRankVOs(list);
        voList.setBuyList(rspSmallGameFactory.buyList(playerSmallGame));
        return voList;
    }


    @RpcAnnotation(cmd="day.reward",vo=SmallGameRankVO.class,req=SmallGameReq.class,name="产生每天奖励",lock = true)
    public SmallGameRankVO dayReward(Session session,SmallGameReq req) throws GameException {

        SmallGameRule sgr = smallGameContainer.getElement(String.valueOf(req.getType()));

        if(sgr == null){
            exceptionFactory.throwRuleException("small.game.type.invalid");
        }
        String gameName = sgr.getName();

        //Calendar ca = Calendar.getInstance();
        //int day = ca.get(Calendar.DAY_OF_YEAR);
        //String dayKey = String.valueOf(req.getType())+"."+String.valueOf(day);
        String dayKey = smallGameContainer.getWeekKey(req.getType()) ;

        Set<ZSetOperations.TypedTuple<String>> daySet = smallGameSetDao.getReverseRangeWithScores(dayKey, 0, 4);
        if(daySet.size()==0){
            return null;
        }

        Iterator<ZSetOperations.TypedTuple<String>> dayIt = daySet.iterator();
        String champ = "";
        String others = "";
        int i = 0;
        while (dayIt.hasNext()) {
            ZSetOperations.TypedTuple<String> typedTuple = dayIt.next();
            String pid = typedTuple.getValue();
            Player player = playerDao.getBean(pid);
            if (player==null){
                exceptionFactory.throwRuleException("p.notfound");
            }
            i++;
            int score = 0;
            switch (i){
                case 1:
                    champ = player.getNickName();
                    score = 50;
                    break;
                case 2:
                    others = player.getNickName();
                    score = 30;
                    break;
                case 3:
                    others += "、"+player.getNickName();
                    score = 20;
                    break;
                case 4:
                case 5:
                    others += "、"+player.getNickName();
                    score = 10;
                    break;
            }
            if(score>0){
            //赠送积分

/*            try {
                siteDao.addCustomPointsByGame(player.getUserId(),score);
            } catch (Exception e) {
                exceptionFactory.throwRuleException("active.gamecoin");
            }*/

                MailReq mailReq = new MailReq();
                String title = message.getMessage("small.game.mail.title", new String[]{gameName}, Locale.CHINA);
                mailReq.setTitle(title);
                mailReq.setIntegralType(IIntegralRuleContainerImpl.INTEGRAL_ACTION_1);
                String content = message.getMessage("small.game.mail.content", new String[]{gameName, String.valueOf(i),String.valueOf(score)}, Locale.CHINA);
                mailReq.setContent(content);
                mailReq.setAttachments("?:"+score+":4,");
                Session ss = new Session(pid,"");
                mailRpc.send(ss,mailReq);
            }

        }//while

        //广播
        Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
        String content = message.getMessage("small.game.credit.reward", new String[]{champ, others,gameName}, Locale.CHINA);
        ChatVO svo = roleFactory.newShoutChat("", "", content);
        frontendService.broadcast(sessions, svo);

        return null;
    }
}
