package com.pengpeng.admin.stargame.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-8-27 上午10:31
 */
@Controller
@RequestMapping(value = "/security")
public class SecurityController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("security/login");
        return mv;
    }
}
