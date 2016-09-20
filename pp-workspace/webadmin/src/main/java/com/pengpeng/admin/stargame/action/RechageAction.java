package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.analyse.model.CommonData;
import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.player.RechargeLog;
import com.pengpeng.stargame.rpc.GmRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.role.ChargeReq;
import com.tongyi.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: mql
 * Date: 13-12-9
 * Time: 下午2:29
 */
@Controller
@RequestMapping("/rechage")
public class RechageAction extends BaseAction {
    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    /** list */
    @RequestMapping(value="/goldList",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView indexGold(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("recharge/viewList");
    }

    @RequestMapping(value="/vipList",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView indexVip(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("recharge/viewVipList");
    }

    @RequestMapping(value="/goldRechage",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    List<RechargeLog> listGold(HttpServletRequest request,HttpServletResponse response) throws GameException, ModelAndViewDefiningException {
        Map<String,CommonData> map = new HashMap<String,CommonData>();
        String uid=request.getParameter("uid");
        Date s = DateUtil.toDate(request.getParameter("dateBegin"), "yyyy-MM-dd");
        Date e = DateUtil.toDate(request.getParameter("dateEnd"),"yyyy-MM-dd");
        ChargeReq chargeReq=new ChargeReq();
        List<RechargeLog> rechargeLogList=new ArrayList<RechargeLog>();
        if(uid!=null&&!uid.equals("")){
            chargeReq.setUid(Integer.parseInt(uid));
            if(playerRpcRemote.getPid(null,Integer.parseInt(uid))==null){
                return rechargeLogList;
            }
        }
        chargeReq.setStarTime(s);
        chargeReq.setEndTime(e);
        chargeReq.setType(1);
        rechargeLogList=new ArrayList<RechargeLog>();
        RechargeLog [] rechargeLogs=gmRpcRemote.chargeInfo(null,chargeReq);
        int all=0;
        for(RechargeLog rechargeLog:rechargeLogs){
            all+=rechargeLog.getAmount();
            rechargeLogList.add(rechargeLog);
        }
        for(RechargeLog rechargeLog:rechargeLogList){
            rechargeLog.setAll(all);
        }
        request.setAttribute("all",all);
        return rechargeLogList;
    }
    @RequestMapping(value="/vipRechage",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    List<RechargeLog> listVip(HttpServletRequest request,HttpServletResponse response) throws GameException, ModelAndViewDefiningException {
        Map<String,CommonData> map = new HashMap<String,CommonData>();
        String uid=request.getParameter("uid");
        Date s = DateUtil.toDate(request.getParameter("dateBegin"), "yyyy-MM-dd");
        Date e = DateUtil.toDate(request.getParameter("dateEnd"),"yyyy-MM-dd");
        ChargeReq chargeReq=new ChargeReq();
        List<RechargeLog> rechargeLogList=new ArrayList<RechargeLog>();
        if(uid!=null&&!uid.equals("")){
            chargeReq.setUid(Integer.parseInt(uid));
            if(playerRpcRemote.getPid(null,Integer.parseInt(uid))==null){
                return rechargeLogList;
            }
        }
        chargeReq.setStarTime(s);
        chargeReq.setEndTime(e);
        chargeReq.setType(2);
         rechargeLogList=new ArrayList<RechargeLog>();
        RechargeLog [] rechargeLogs=gmRpcRemote.chargeInfo(null,chargeReq);
        int all=0;
        for(RechargeLog rechargeLog:rechargeLogs){
            all+=rechargeLog.getAmount();
            rechargeLogList.add(rechargeLog);
        }
        for(RechargeLog rechargeLog:rechargeLogList){
            rechargeLog.setAll(all);
        }
        return rechargeLogList;
    }

}
