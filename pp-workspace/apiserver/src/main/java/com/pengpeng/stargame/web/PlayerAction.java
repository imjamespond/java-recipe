package com.pengpeng.stargame.web;

import com.pengpeng.stargame.rpc.ApiRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.api.ApiReq;
import com.pengpeng.stargame.vo.api.BaseResult;
import com.pengpeng.stargame.vo.api.PlayerResult;
import com.pengpeng.stargame.vo.role.ChargeReq;
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
 * Date: 13-8-15
 * Time: 下午2:53
 */
@Component
@RequestMapping("/playerInfo.do")
public class PlayerAction {
    @Autowired
    private ApiRpcRemote apiRpcRemote;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody BaseResult info(@RequestParam("uid") int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
        BaseResult playerResult=null;
        try{
            ApiReq apiReq=new ApiReq(uid);
            playerResult=apiRpcRemote.getPlayerInfo(null,apiReq);
        }catch (Exception e){
            return new BaseResult(201,e.getMessage());
        }
        return playerResult;
    }
}
