package com.pengpeng.stargame.web;

import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.role.ChargeReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-7下午3:58
 */
@Component
@RequestMapping("/recharge.do")
public class CoinAction  {

    private static  final Logger log = Logger.getLogger("recharge");
    @Autowired
    private WhiteValue whiteValue;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    ResResult coin(@RequestParam int uid,@RequestParam int amount,@RequestParam String po,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String remoteIp=getIpAddr(request);
        try{
            log.info("recharge start >>>>>>>>> ip: "+remoteIp+" amount:" +amount+" uid:"+uid+" p0:"+po );
            if(whiteValue.getWhiteList().size()>0&&!whiteValue.getWhiteList().contains(remoteIp)){
                return new ResResult(403,null);
            }
            ChargeReq req = new ChargeReq(uid,po,amount);
            playerRpcRemote.charge(null,req);
        }catch (Exception e){
            return new ResResult(201,e.getMessage());
        }
         log.info("recharge success >>>>>>>>> ip: "+remoteIp+" amount:" +amount+" uid:"+uid+" p0:"+po );
        return new ResResult(200,null);
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


    private class ResResult{
        private int status = 0;
        private String msg;

        private ResResult() {
        }

        private ResResult(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
