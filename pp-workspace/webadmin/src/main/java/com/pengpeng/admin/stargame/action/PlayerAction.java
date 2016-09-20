package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.ILineHandleManager;
import com.pengpeng.admin.stargame.manager.IPlayerOlineInfoManager;
import com.pengpeng.admin.stargame.manager.IUserActionManager;
import com.pengpeng.admin.stargame.model.UserActionModel;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.farm.FarmItemReq;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.fashion.FashionItemVO;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.gm.CoinReq;
import com.pengpeng.stargame.vo.gm.ItemReq;
import com.pengpeng.stargame.vo.piazza.FamilyInfoVO;
import com.pengpeng.stargame.vo.piazza.FamilyMemberVO;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import com.pengpeng.stargame.vo.role.ChargeReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.pengpeng.stargame.vo.role.TimeReq;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.stargame.vo.room.RoomItemVO;
import com.pengpeng.stargame.vo.room.RoomPkgVO;
import com.pengpeng.stargame.vo.room.RoomVO;
import com.pengpeng.stargame.vo.role.MailReq;
import com.pengpeng.stargame.vo.task.TaskInfoVO;
import com.pengpeng.stargame.vo.task.TaskReq;
import com.pengpeng.stargame.vo.task.TaskVO;
import com.pengpeng.stargame.vo.vip.VipInfoVO;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午6:23
 */
@Controller
@RequestMapping("/player")
public class PlayerAction {

    private static long DAY = 3600l*24l*1000l;
    @Autowired
    private FamilyAssistantRpcRemote familyAssistantRpcRemote;
    @Autowired
    private PayMemberRpcRemote payMemberRpcRemote;
    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Autowired
    private FamilyManagerRpcRemote familyManagerRpcRemote;
    @Autowired
    private FarmPkgRpcRemote farmPkgRpcRemote;
    @Autowired
    private FashionPkgRpcRemote fashionPkgRpcRemote;
    @Autowired
    private FamilyInfoRpcRemote familyInfoRpcRemote;
    @Autowired
    private RoomRpcRemote roomRpcRemote;

    @Autowired
    private HibernateTemplate template;
    @Autowired
    private ILineHandleManager lineHandleManager;

    @Autowired
    private IPlayerOlineInfoManager playerOlineInfoManager;

    @Autowired
    private IUserActionManager userActionManager;
    @Autowired
    private TaskRpcRemote taskRpcRemote;

    @Autowired
    private MailRpcRemote mailRpcRemote;
    /** list */
    @RequestMapping(method={RequestMethod.GET})
    public ModelAndView indexFarmLevelRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("player/view");
    }

    /** list */
    @RequestMapping(method={RequestMethod.POST})
    public @ResponseBody PlayerVO search(@RequestParam int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        PlayerVO vo = playerRpcRemote.getPlayerInfo(null,pid);
        return vo;
    }

    /** list */
    @RequestMapping(value="/u/{uid}.json",method={RequestMethod.GET})
    public @ResponseBody PlayerVO get(@PathVariable int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        PlayerVO vo = playerRpcRemote.getPlayerInfo(null,pid);
        return vo;
    }

    /** ajax list*/
    @RequestMapping(value="/user/{uid}.json",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    Page<Kv> ajaxFarmPkg(@PathVariable int uid, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<Kv> list = new ArrayList<Kv>();

        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session,pid);

        TaskReq taskReq=new TaskReq();
        TaskInfoVO taskInfoVO=taskRpcRemote.getTaskList(session,taskReq);
        TaskVO newTaskVo=null;
        for(TaskVO taskVO:taskInfoVO.getTaskVOs()){
            if(taskVO.getNewTask()==1){
                newTaskVo=taskVO;
                break;
            }
        }
        StringBuffer stringBuffer=new StringBuffer("");
        for(TaskVO taskVO:taskInfoVO.getTaskVOs()){
            stringBuffer.append(taskVO.getId()+" "+taskVO.getName()+",");
        }

        if(pv != null){
        list.add(new Kv("网站ID",Integer.toString(pv.getUserId())));
        list.add(new Kv("登陆账号",pid));
        list.add(new Kv("昵称",pv.getNickName()));
        list.add(new Kv("粉丝达人游戏币",Integer.toString(pv.getGold())));
        list.add(new Kv("粉丝达人币",Integer.toString(pv.getRmb())));
        list.add(new Kv("农场等级",Integer.toString(pv.getFarmLevel())));
        }

        if(pv.getFamilyId()!=null){
            FamilyReq famReq = new FamilyReq();
            famReq.setPid(pid);
            famReq.setFamilyId(pv.getFamilyId());
            FamilyMemberVO fvo = familyManagerRpcRemote.getMembers(session, famReq);
            if(null!=fvo){
                String iden = null;
                if(fvo.getIdentity() == FamilyConstant.TYPE_FS)
                    iden = "粉丝";
                if(fvo.getIdentity() == FamilyConstant.TYPE_ZL)
                    iden = "助理";
                if(fvo.getIdentity() == FamilyConstant.TYPE_MX)
                    iden = "明星";
                if(fvo.getIdentity() == FamilyConstant.TYPE_CJFS)
                    iden = "超粉";
                list.add(new Kv("粉丝身份",iden));
            }

            FamilyInfoVO fivo = familyInfoRpcRemote.getFamilyById(session, famReq);
            list.add(new Kv("家族", fivo.getName()));

        }





        VipInfoVO vv = payMemberRpcRemote.getinfo(session,null);
        if(null!=vv){
            long now = System.currentTimeMillis();
            Date date = new Date(now+=vv.getSurplusTime());
            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            list.add(new Kv("vip身份", vv.getVip()==1?"是":"否"));
            list.add(new Kv("vip剩余天数", String.valueOf(vv.getSurplusTime() / DAY)));
            list.add(new Kv("vip到期时间",format.format(date) ));
        }


        list.add(new Kv("任务数据","==========================="));
        if(newTaskVo!=null){
         list.add(new Kv("正在进行的新手任务",newTaskVo.getId()+" "+newTaskVo.getName()));
        }else{
            list.add(new Kv("正在进行的新手任务","无"));
        }
        list.add(new Kv("正在进行的任务",stringBuffer.toString()));
        RoomIdReq romReq = new RoomIdReq();
        romReq.setRoomId(pid);
        RoomVO romv = roomRpcRemote.getRoomInfo(session, romReq);
        if(romv != null){
            list.add(new Kv("房间名誉",Integer.toString(romv.getGoodReputation())));
            list.add(new Kv("房间豪华度",Integer.toString(romv.getGlamour())));
        }

        list.add(new Kv("农场","=============="));
        FarmItemReq req = new FarmItemReq();
        req.setPid(pid);
        FarmPkgVO fpv = farmPkgRpcRemote.getItemAll(session, req);
        FarmItemVO[] fiv = fpv.getFarmItemVO();
        if(fiv != null){
        for(FarmItemVO fi:fiv){
            BaseItemRule bir = template.get(BaseItemRule.class,fi.getItemId());
            list.add(new Kv(bir.getName(),Integer.toString(fi.getNum())));
        }
        }

        list.add(new Kv("时装","=============="));
        FashionIdReq req2 = new FashionIdReq();
        req2.setPid(pid);
        for(int i=1;i<8;i++){
        req2.setType(Integer.toString(i));
        FashionPkgVO fpv2 = fashionPkgRpcRemote.getFashionPkg(session, req2);
        FashionItemVO[] fiv2 = fpv2.getBaseItemVO();
            if(fiv2 != null){
                for(FashionItemVO fi:fiv2){
                    BaseItemRule bir = template.get(BaseItemRule.class,fi.getItemId());
                    list.add(new Kv(bir.getName(),Integer.toString(fi.getNum())));
                }
            }
        }//i类型

        list.add(new Kv("房间","=============="));
        RoomIdReq req3 = new RoomIdReq();
        req3.setRoomId(pid);
        RoomPkgVO rp = roomRpcRemote.getRoomItemList(session, req3);
        if(rp != null){
            RoomItemVO[] ri_ = rp.getBaseItemVO();
            for(RoomItemVO ri:ri_){
                BaseItemRule bir = template.get(BaseItemRule.class,ri.getItemId());
                list.add(new Kv(bir.getName(),Integer.toString(ri.getNum())));
            }
        }

        Page<Kv> page = new Page<Kv>();
        page.setPage(1);
        page.setRows(list);
        return page;
    }

    /** 查询道具 */
    @RequestMapping(value="/loadItems",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody Page<Kv> getItem(HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<Kv> list = new ArrayList<Kv>();
        List<BaseItemRule> lb = template.loadAll(BaseItemRule.class);
        for(BaseItemRule b:lb){
            list.add(new Kv(b.getName(),b.getItemsId()));
        }
        Page<Kv> page = new Page<Kv>();
        page.setPage(1);
        page.setRows(list);
        return page;
    }

    /** 冻结账户 */
    @RequestMapping(value="/freeze/u/{uid}/{time}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult freeze(@PathVariable int uid,@PathVariable long time,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");

        TimeReq req = new TimeReq();
        req.setUid(uid);
        req.setTime(System.currentTimeMillis() + time * 60000);
        gmRpcRemote.freeze(null,req);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_BAN_PLAYER);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }

    /** 冻结账户 */
    @RequestMapping(value="/freeze/p/{pid}/{time}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult freeze(@PathVariable String pid,@PathVariable long time,HttpServletRequest request,HttpServletResponse response) throws Exception {
        TimeReq req = new TimeReq();
        req.setPid(pid);
        req.setTime(time);
        gmRpcRemote.freeze(null,req);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_BAN_PLAYER);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }

    /** 禁言 */
    @RequestMapping(value="/speak/u/{uid}/{time}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult speak(@PathVariable int uid,@PathVariable long time,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");

        TimeReq req = new TimeReq();
        req.setUid(uid);
        req.setTime(System.currentTimeMillis() + time * 60000);
        gmRpcRemote.speak(session,req);


        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_BAN_CHAT);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }

    /** 禁言 */
    @RequestMapping(value="/speak/p/{pid}/{time}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult speak(@PathVariable String pid,@PathVariable long time,HttpServletRequest request,HttpServletResponse response) throws Exception {
        TimeReq req = new TimeReq();
        req.setPid(pid);
        req.setTime(time);
        gmRpcRemote.speak(null,req);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_BAN_CHAT);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }

    /** 添加游戏币 */
    @RequestMapping(value="/gameCoin/u/{uid}/{amount}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult gameCoin(@PathVariable int uid,@PathVariable int amount,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");

        if(pid == null){
            return ResResult.newFailed();
        }

        CoinReq req = new CoinReq();
        req.setPid(pid);
        req.setAmount(amount);
        req.setType(1);
        gmRpcRemote.coin(session,req);


        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_ADD_GAME_COIN);
        ua.setNum(amount);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }
    /** 添加达人币 */
    @RequestMapping(value="/addFarmExp/u/{uid}/{amount}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult addFarmExp(@PathVariable int uid,@PathVariable int amount,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");

        if(pid == null){
            return ResResult.newFailed();
        }

        CoinReq req = new CoinReq();
        req.setPid(pid);
        req.setAmount(amount);
        req.setType(3);
        gmRpcRemote.farmExpAdd(session,req);
        return ResResult.newOk();
    }


    /** 添加达人币 */
    @RequestMapping(value="/goldCoin/u/{uid}/{amount}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult goldCoin(@PathVariable int uid,@PathVariable int amount,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");

        if(pid == null){
            return ResResult.newFailed();
        }

        CoinReq req = new CoinReq();
        req.setPid(pid);
        req.setAmount(amount);
        req.setType(2);
        gmRpcRemote.coin(session,req);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_ADD_GOLD_COIN);
        ua.setNum(amount);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }

    /** 添加道具 */
    @RequestMapping(value="/addItems/u/{uid}/{items}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult addItems(@PathVariable int uid,@PathVariable String items,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");

        if(pid == null){
            return ResResult.newFailed();
        }

        String[] arrItems = items.split(",");//id:num;...
        if(arrItems.length<=0){
            return ResResult.newFailed();
        }
        String itemLog = new String();
        for (int i = 0; i < arrItems.length; i++) {
            String[] item = arrItems[i].split(":");
            if(item.length!=2){
                continue;
            }
            ItemReq req = new ItemReq();
            req.setPid(pid);
            req.setNum(Integer.valueOf(item[1]));
            req.setItemId(item[0]);
            gmRpcRemote.item(session,req);

            itemLog += item[0] + ":" + item[1] + ",";
        }


        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActionModel ua = new UserActionModel();
        ua.setUserName(userDetails.getUsername());
        ua.setPid(pid);
        ua.setDate(new Date());
        ua.setType(UserActionModel.T_ADD_ITEM);
        ua.setReason(itemLog);
        userActionManager.createBean(ua);

        return ResResult.newOk();
    }

    /** set vip */
    @RequestMapping(value="/chargeVip/u/{uid}/{num}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody ResResult chargeVip(@PathVariable int uid,@PathVariable int num,HttpServletRequest request,HttpServletResponse response) throws Exception {
        ChargeReq req = new ChargeReq();
        req.setUid(uid);
        req.setAmount(num);
        gmRpcRemote.addVip(null, req);

        return ResResult.newOk();
    }


    @RequestMapping(value="/parseLog",method={RequestMethod.GET})
    public  @ResponseBody ResResult parseLog(HttpServletRequest request,HttpServletResponse response) throws Exception {
        //lineHandleManager.analyse("C:\\Users\\Administrator\\logs\\rpc.log");
        playerOlineInfoManager.smallGameReward();
        return ResResult.newOk();
    }
    @RequestMapping(value="/finishTask/{uid}/",method={RequestMethod.GET})
    public  @ResponseBody ResResult finishTask(@PathVariable int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");
        IdReq idReq=new IdReq();
        idReq.setId(pid);
        gmRpcRemote.finishNew(session,idReq);
        return ResResult.newOk();
    }

    @RequestMapping(value="/finishAllTask/{uid}/",method={RequestMethod.GET})
    public  @ResponseBody ResResult finishAllTask(@PathVariable int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
       if(uid==0){
           return null;
       }
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,"");
        IdReq idReq=new IdReq();
        idReq.setId(pid);
        gmRpcRemote.finishAllNew(session,idReq);
        return ResResult.newOk();
    }

    @RequestMapping(value="/setAssistant/{uid}/",method={RequestMethod.GET})
    public  @ResponseBody ResResult setAssistant(@PathVariable int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        Session session = new Session(pid,null);
        familyAssistantRpcRemote.approve(session,null);
        return ResResult.newOk();
    }

    @RequestMapping(value="/sendMail/",method={RequestMethod.POST})
    public  @ResponseBody ResResult sendMail(HttpServletRequest request,HttpServletResponse response) throws Exception {
        int uid = Integer.valueOf(request.getParameter("uid"));
        MailReq req = new MailReq();
        req.setAttachments(request.getParameter("attachment"));
        req.setTitle(request.getParameter("title"));
        req.setContent(request.getParameter("content"));
        String pid = playerRpcRemote.getPid(null,uid);
        mailRpcRemote.send(new Session(pid,null),req);
        return ResResult.newOk();
    }

    private class Kv{
        public String key;
        public String val;

        public Kv(String key,String val){
            this.key = key;
            this.val = val;
        }
    }
}



