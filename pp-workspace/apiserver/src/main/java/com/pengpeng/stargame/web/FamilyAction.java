package com.pengpeng.stargame.web;

import com.pengpeng.stargame.rpc.ApiRpcRemote;
import com.pengpeng.stargame.vo.api.FamilyRankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-14下午5:59
 */
@Component
@RequestMapping("/family")
public class FamilyAction {

    @Autowired
    private ApiRpcRemote apiRpcRemote;

    @RequestMapping(value = "/rank.do",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody FamilyRankVO[] rank(HttpServletRequest request,HttpServletResponse response) throws Exception {

        FamilyRankVO [] familyRankVOs=apiRpcRemote.familyTop(null,null);
        return  familyRankVOs;
    }
}
