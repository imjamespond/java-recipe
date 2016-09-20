package com.pengpeng.stargame.web;

import com.pengpeng.stargame.rpc.ApiRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.api.ApiReq;
import com.pengpeng.stargame.vo.api.BaseResult;
import com.pengpeng.stargame.vo.role.ChargeReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: mql
 * Date: 13-11-27
 * Time: 下午5:35
 */
@Component
@RequestMapping("/vip.do")
public class VipAction {
    private static  final Logger log = Logger.getLogger("recharge");
    @Autowired
    private WhiteValue whiteValue;
    @Autowired
    private ApiRpcRemote apiRpcRemote;


    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    BaseResult coin(@RequestParam int uid,@RequestParam int amount,@RequestParam String po,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String remoteIp=getIpAddr(request);
        try{
            log.info("recharge VIP start >>>>>>>>> ip: "+remoteIp+" amount:" +amount+" uid:"+uid +"po: "+po );
            if(whiteValue.getWhiteList().size()>0&&!whiteValue.getWhiteList().contains(remoteIp)){
                return new BaseResult(403,null);
            }
            ChargeReq req = new ChargeReq(uid,po,amount);
            //月转换成小时 ，方便游戏以后会出 小时卡，统一
            apiRpcRemote.chargeVip(null,req);
        }catch (Exception e){
            return new BaseResult(201,e.getMessage());
        }
        log.info("recharge VIP success >>>>>>>>> ip: "+remoteIp+" amount:" +amount+" uid:"+uid +"po: "+po );
        return new BaseResult(200,null);
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
