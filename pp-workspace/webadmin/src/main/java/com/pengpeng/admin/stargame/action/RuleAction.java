package com.pengpeng.admin.stargame.action;

import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.ServerType;
import com.pengpeng.stargame.rpc.BackendServiceProxy;
import com.pengpeng.stargame.rpc.GmRpcRemote;
import com.pengpeng.stargame.rpc.IBackendService;
import com.tongyi.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 下午5:17
 */
@Controller
public class RuleAction extends BaseAction {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;
    @RequestMapping(value="/rule/refresh",method={RequestMethod.GET,RequestMethod.POST})
    public String index(HttpServletRequest request,HttpServletResponse response){
       List<IBackendService> backendServiceList=proxy.getAllLogicServer();
       String message="刷新成功";
        for(IBackendService backendService:backendServiceList){
            try {
                backendService.process("gm.rule.refresh",null,"");
            } catch (Exception e) {
                message=e.getMessage();
                e.printStackTrace();
            }
        }
        request.setAttribute("show",message);
         return "sucess";
    }

}
