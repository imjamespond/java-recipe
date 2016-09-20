package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.model.UserModel;
import com.pengpeng.stargame.rpc.GmRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.gm.MsgReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午6:23
 */
@Controller
@RequestMapping("/password")
public class PasswordAction {

    @Autowired
    private HibernateTemplate template;
    /** list */
    @RequestMapping(method={RequestMethod.GET})
    public ModelAndView indexFarmLevelRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("adm/password");
    }


    @RequestMapping(value="/change",method={RequestMethod.GET})
    public @ResponseBody ResResult change(@RequestParam(value="prepwd",defaultValue="")String prepwd,
                                           @RequestParam(value="newpwd",defaultValue="")String newpwd,
                                           HttpServletRequest request,HttpServletResponse response) throws Exception {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel um = template.get(UserModel.class,userDetails.getUsername());
        Md5PasswordEncoder md5=new Md5PasswordEncoder();
        if(um.getPassword().equals(md5.encodePassword(prepwd,""))){
            um.setEnabled((short)1);
            um.setPassword(md5.encodePassword(newpwd,""));
            template.update(um);
        };

        return ResResult.newOk();
    }


}


