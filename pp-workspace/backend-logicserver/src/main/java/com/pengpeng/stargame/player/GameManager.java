package com.pengpeng.stargame.player;

import com.mchange.v1.util.StringTokenizerUtils;
import com.pengpeng.framework.utils.DateUtil;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.cmd.FarmGiftRpc;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.FarmSeedRule;
import com.pengpeng.stargame.farm.rule.Product;
import com.pengpeng.stargame.fashion.cmd.FashionGiftRpc;
import com.pengpeng.stargame.lottery.cmd.LotteryRpc;
import com.pengpeng.stargame.lucky.tree.cmd.LuckyTreeRpc;
import com.pengpeng.stargame.manager.IRuleLoader;
import com.pengpeng.stargame.model.farm.FarmField;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.model.player.ScenePlayer;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.model.vip.PlayerVip;
import com.pengpeng.stargame.piazza.cmd.FamilyManagerRpc;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IMoneyTreeRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IMoneyTreeDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.gm.cmd.GmRpc;
import com.pengpeng.stargame.player.cmd.BubbleRpc;
import com.pengpeng.stargame.player.cmd.SceneRpc;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.IScenePlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.small.game.cmd.SmallGameRpc;
import com.pengpeng.stargame.stall.cmd.StallRpc;
import com.pengpeng.stargame.stall.dao.IPlayerStallPassengerDao;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.task.dao.ITaskDao;
import com.pengpeng.stargame.vip.dao.IVipDao;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.lottery.LotteryReq;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeReq;
import com.pengpeng.stargame.vo.lucky.tree.LuckyTreeVO;
import com.pengpeng.stargame.vo.map.BubbleReq;
import com.pengpeng.stargame.vo.map.MapReq;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import com.pengpeng.stargame.vo.role.GiftReq;
import com.pengpeng.stargame.vo.role.TimeReq;
import com.pengpeng.stargame.vo.smallgame.SmallGameReq;
import com.pengpeng.stargame.vo.stall.StallAssistantReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-9下午8:10
 */
@Component
public class GameManager {

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private FamilyManagerRpc familyManagerRpc;
    @Autowired
    private IMoneyTreeDao moneyTreeDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IMoneyTreeRuleContainer moneyTreeRuleContainer;

    @Autowired
    private IFamilyDao familyDao;

    @Autowired
    private IFamilyBuildDao familyBuildDao;

    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;

    @Autowired
    private IScenePlayerDao scenePlayerDao;

    @Autowired
    private RspRoleFactory rspRoleFactory;

    @Autowired
    private FrontendServiceProxy frontendServiceProxy;

    @Autowired
    private MessageSource message;

    @Autowired
    private StatusRemote statusService;

    @Autowired
    private SceneRpc sceneRpc;

    @Autowired
    private GmRpc gmRpc;
    @Autowired
    private IVipDao vipDao;

//test
    @Autowired
    private IPlayerStallPassengerDao dao;
    //@Autowired
    //private StallRpc rpc;
    //@Autowired
    //private SmallGameRpc rpc;
    //@Autowired
    //private LuckyTreeRpc rpc;
    @Autowired
    //private FashionGiftRpc rpc;
    //private FarmGiftRpc rpc;
    //private BubbleRpc rpc;
    private LotteryRpc rpc;


    public void execute(Session session, String msg) throws AlertException, RuleException {
        String[] cmds = StringTokenizerUtils.tokenizeToArray(msg," ");
        Player player = playerDao.getBean(session.getPid());
        if ("p.coin".equalsIgnoreCase(cmds[1])){

            player.setGameCoin(Integer.parseInt(cmds[2]));
            player.setGoldCoin(Integer.parseInt(cmds[2]));
            playerDao.saveBean(player);
            return;
        }

        if ("farm.update.level".equalsIgnoreCase(cmds[1])){
            FarmPlayer  farmPlayer  = farmPlayerDao.getFarmPlayer(session.getPid(),System.currentTimeMillis());
            farmPlayer.setLevel(Integer.parseInt(cmds[2]));
            farmPlayerDao.saveBean(farmPlayer);
            return;
        }
        if ("farm.addexp".equalsIgnoreCase(cmds[1])){
            FarmPlayer  farmPlayer  = farmPlayerDao.getFarmPlayer(session.getPid(),System.currentTimeMillis());
            farmLevelRuleContainer.addFarmExp(farmPlayer,Integer.parseInt(cmds[2]));
            farmPlayerDao.saveBean(farmPlayer);
            return;
        }

        if ("goods.add".equalsIgnoreCase(cmds[1])){
            baseItemRulecontainer.addGoods(player,cmds[2],Integer.parseInt(cmds[3]));
            return;
        }

        if ("clean.rule".equalsIgnoreCase(cmds[1])){
            Map<String,IRuleLoader> loaders = ctx.getBeansOfType(IRuleLoader.class);
            for(IRuleLoader loader:loaders.values()){
                loader.load();
            }
            return;
        }

        if ("quit.family".equalsIgnoreCase(cmds[1])){
            FamilyReq req = new FamilyReq();
            FamilyRule rule = familyRuleContainer.getRuleByStarId(familyRuleContainer.getDefaultStarId());
            req.setFamilyId(rule.getId());
            if (cmds.length>=3&&!StringUtils.isBlank(cmds[2])){
                req.setFamilyId(cmds[2]);
            }
            familyManagerRpc.changeFamily(session,req);
            return ;
        }

        if ("family.iden".equalsIgnoreCase(cmds[1])){
            FamilyReq req = new FamilyReq();
            req.setAlterType(10);
            req.setMemberType(Integer.parseInt(cmds[2]));
            familyManagerRpc.changeIdentity(session,req);
            return ;
        }

        if ("moneyTreeRipe".equalsIgnoreCase(cmds[1])){
            FamilyMemberInfo familyMemberInfo=familyMemberInfoDao.getFamilyMember(session.getPid());
            MoneyTree moneyTree= moneyTreeDao.getMoneyTree(familyMemberInfo.getFamilyId(),new Date());
            if(cmds.length>2){
                moneyTreeRuleContainer.setRipe(moneyTree,Integer.parseInt(cmds[2]));
            }
            else {
                moneyTreeRuleContainer.setRipe(moneyTree,0);
            }

            moneyTreeDao.saveBean(moneyTree);
            return ;
        }

        if ("family.funds".equalsIgnoreCase(cmds[1])){
            Family family=familyDao.getBeanByStarId(player.getStarId());
            family.setFunds(Integer.parseInt(cmds[2]));
            familyDao.saveBean(family);
            return ;
        }

        if ("deleteTask".equalsIgnoreCase(cmds[1])){
            taskDao.deleteBean(session.getPid());
            return;
        }

        if ("p.type".equalsIgnoreCase(cmds[1])){
            player.setType(Integer.parseInt(cmds[2]));
            playerDao.saveBean(player);
            return;
        }

        if ("gmfinish".equalsIgnoreCase(cmds[1])){
            TaskPlayer taskPlayer=taskDao.getBean(session.getPid());
            taskRuleContainer.gmFinish(null,taskPlayer);
            taskDao.saveBean(taskPlayer);
            return;
        }



        if ("member.contribute".equalsIgnoreCase(cmds[1])){
            FamilyMemberInfo member = familyMemberInfoDao.getFamilyMember(session.getPid());
//            member.setContribution(Integer.parseInt(cmds[2]));
            member.incDevote(Integer.parseInt(cmds[2]),new Date());
            familyMemberInfoDao.saveBean(member);
            return;
        }

        if ("fans.value".equalsIgnoreCase(cmds[1])){
            Family family=familyDao.getBeanByStarId(player.getStarId());
            family.setFansValue(Integer.parseInt(cmds[2]));
            familyDao.saveBean(family);
            return;
        }

        if ("scene.location".equalsIgnoreCase(cmds[1])){
            String content = message.getMessage("gm.location", new String[]{player.getNickName(),session.getParam("scene.id"),session.getParam("x"),session.getParam("y")}, Locale.CHINA);
            ChatVO cvo = rspRoleFactory.newTalkChat("",player.getNickName(),content);
            frontendServiceProxy.broadcast(session,cvo);
            return;
        }

        if ("scene.to".equalsIgnoreCase(cmds[1])){
            MapReq req = new MapReq();
            req.setMapId(cmds[2]);
            req.setPid(session.getPid());
            req.setX(Integer.parseInt(cmds[3]));
            req.setY(Integer.parseInt(cmds[4]));
            try{
                sceneRpc.enterTo(session, req);
            }catch (GameException e){
                e.printStackTrace();
            }
            return;
        }

        if ("scene.teleport".equalsIgnoreCase(cmds[1])){
            String pid = playerDao.getPid(Integer.valueOf(cmds[2]));
            ScenePlayer sp = scenePlayerDao.getBean(pid);
            System.out.println("==="+sp.getSceneId()+" "+sp.getX()+" "+sp.getY()+"-"+pid);
            if(sp == null){
                return;
            }

            MapReq req = new MapReq();
            req.setMapId(sp.getSceneId());
            req.setPid(session.getPid());
            req.setX(sp.getX());
            req.setY(sp.getY());
            try{
                sceneRpc.enterTo(session, req);
            }catch (GameException e){
                e.printStackTrace();
            }
            return;
        }

        if ("player.userid".equalsIgnoreCase(cmds[1])){
            String content = message.getMessage("gm.general", new String[]{player.getNickName(),Integer.toString(player.getUserId()),"","","",""}, Locale.CHINA);
            ChatVO cvo = rspRoleFactory.newTalkChat("",player.getNickName(),content);
            frontendServiceProxy.broadcast(session,cvo);
            return;
        }

        if ("ban.chat".equalsIgnoreCase(cmds[1])){
            TimeReq req = new TimeReq();
            req.setTime(System.currentTimeMillis() + Integer.valueOf(cmds[3]) * 60000);
            req.setUid(Integer.valueOf(cmds[2]));
            gmRpc.speak(session, req);
            return;
        }

        if ("bulletin".equalsIgnoreCase(cmds[1])){
            Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            ChatVO vo = rspRoleFactory.newShoutChat("", "", cmds[2]);
            frontendServiceProxy.broadcast(sessions,vo);
            return;
        }

        if ("system.info".equalsIgnoreCase(cmds[1])){
            ChatVO cvo = rspRoleFactory.newTalkChat("","",cmds[2]);
            Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
            frontendServiceProxy.broadcast(sessions,cvo);
            return;
        }

        if ("farm.ripen".equalsIgnoreCase(cmds[1])){
            int n = Integer.valueOf(cmds[2]);
            FarmPlayer  farmPlayer  = farmPlayerDao.getFarmPlayer(session.getPid(),System.currentTimeMillis());
            List<FarmField> fieldList = farmPlayer.getFields();
            Iterator<FarmField> it = fieldList.iterator();
            while (it.hasNext()){//每一块田
                FarmField farmField = it.next();

                FarmSeedRule farmItemRule = (FarmSeedRule) baseItemRulecontainer.getElement(farmField.getSeedId());
                if(farmField.getStatus() == FarmConstant.FIELD_STATUS_FREE){
                    continue;
                }
                //System.out.println("==="+farmField.getId()+" "+farmField.getSeedId()+" "+farmItemRule.getProductList().size());
                for (int j = 0; j < farmItemRule.getProductList().size(); j++) {//作物成长过程
                    Product p = farmItemRule.getProductList().get(j);//System.out.println("===="+p.getId()+" "+p.getTime());
                    if(j!=n-1)//N熟
                        continue;
                    farmField.setPlantTime(new Date(farmField.getPlantTime().getTime() - p.getTime() * 1000));
                    //farmField.setStatus(FarmConstant.FIELD_STATUS_RIP);
                }
            }
            farmPlayerDao.saveBean(farmPlayer);
            return;
        }

        if ("family.level".equalsIgnoreCase(cmds[1])){
            Family family=familyDao.getBeanByStarId(player.getStarId());
            FamilyBuildInfo info = familyBuildDao.getBean(family.getId());
            info.setLevel(FamilyConstant.BUILD_CASTLE,Integer.valueOf(cmds[2]));
            familyBuildDao.saveBean(info);
            return;
        }

        if("setVip".equalsIgnoreCase(cmds[1])){
            PlayerVip playerVip=vipDao.getPlayerVip(player.getId());
            playerVip.setViP(1);
            playerVip.setEndTime(DateUtil.addMinute(new Date(),Integer.parseInt(cmds[2])*60));
            vipDao.saveBean(playerVip);
        }



        if("test".equalsIgnoreCase(cmds[1])){
            try{
                //73fe10df127946d3be1d2882f6e31081
                //fb339d5467c141179120025d058cdf30
               Session ss = new Session("73fe10df127946d3be1d2882f6e31081","");
                //rpc.getInfo(session,null);
                /* StallAssistantReq req = new StallAssistantReq(); req.setItemId("items_12001");
               rpc.checkAssistant(ss,req);
             SmallGameReq req = new SmallGameReq();req.setType(Integer.valueOf(cmds[2]));
           rpc.dayReward(session,req);
            LuckyTreeReq req = new LuckyTreeReq();req.setPid(session.getPid());
            if(Integer.valueOf(cmds[2]) == 1)
                rpc.getInfo(session,req);
            if(Integer.valueOf(cmds[2]) == 2)
                rpc.water(session,req);
            if(Integer.valueOf(cmds[2]) == 3)
                rpc.call(session,req);
                GiftReq req = new GiftReq();
                req.setMessage("dwdwdwdw");
                String[] to = {"0","2","3","4"};
                req.setTo(to);
                req.setItemId("items_11011");
                //rpc.give(session,req);
                rpc.accept(ss,req);


                BubbleReq req = new BubbleReq();
                for(int i=0;i<100;i++){
                    rpc.activate(session,req);
                } */
                LotteryReq req = new LotteryReq();
                if(Integer.valueOf(cmds[2]) == 1)
                    rpc.rouletteDraw(session,req);
                if(Integer.valueOf(cmds[2]) == 2)
                    rpc.rouletteAccept(session,req);


/*                //通知
                LuckyTreeVO vo = new LuckyTreeVO();
                Session fSession = new Session(null,null);
                fSession =statusService.getSession(fSession,session.getPid());
                frontendServiceProxy.broadcast(fSession,vo);*/
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }


}
