package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.admin.stargame.manager.IPlayerOlineInfoManager;
import com.pengpeng.admin.stargame.model.PlayerOlineInfoModel;
import com.pengpeng.stargame.rpc.GmRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.tongyi.action.BaseAction;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-7-15 上午11:10
 */
@Controller
public class IndexAction extends BaseAction {

    private static final long serialVersionUID = 4210830996845584200L;
    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    @Autowired
    private IPlayerOlineInfoManager playerOlineInfoManager;




//    @Action(value="/index",results = {@Result(name="success",location = "index-fail.jsp")})
    @RequestMapping(value="/",method={RequestMethod.GET,RequestMethod.POST})
    public String index(HttpServletRequest request,HttpServletResponse response){
        Date now=new Date();
        String id= DateUtil.getDateFormat(now,"yyyy-MM-dd");
        PlayerOlineInfoModel playerOlineInfo= null;
        try {
            playerOlineInfo = playerOlineInfoManager.findById(id);
        } catch (NotFoundBeanException e) {
            e.printStackTrace();
        }

        Date yesterday= DateUtil.getAddDay(-1);
        String yid= DateUtil.getDateFormat(yesterday,"yyyy-MM-dd");

        PlayerOlineInfoModel yesterdayP= null;
        try {
            yesterdayP = playerOlineInfoManager.findById(yid);
        } catch (NotFoundBeanException e) {
            e.printStackTrace();
        }

        if(playerOlineInfo!=null){
            request.setAttribute("cDay",playerOlineInfo);
        }
        if(playerOlineInfo!=null){
            request.setAttribute("yDay",yesterdayP);
        }
        System.out.println("Index ...");
        request.setAttribute("cNum",statusRemote.size(null,""));
        return "workspace";
    }

}
