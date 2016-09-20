package com.james.jetty.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.james.jetty.model.Users;
import com.james.jetty.vo.MyResult;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-8-27 上午10:31
 */
@Controller
@RequestMapping(value = "/security")
public class UserController {
	
	private static Md5PasswordEncoder md5=new Md5PasswordEncoder();

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("security/login");
		return mv;
	}
	
	@RequestMapping(value = "/registry")
	public String 
	registry(HttpServletRequest request) {
		return "security/registry";
	}
	
	@RequestMapping(value = "/registry/post", method = RequestMethod.POST)
	public @ResponseBody Object 
	post(HttpServletRequest request) {
	    //获取当前用户的session ID  
	    //String sessionId = request.getSession().getId();  
	    String sessionToken = CaptchaController.getToken(request.getSession());
	    String requestToken = request.getParameter("captcha");

	    if (!sessionToken.equals(requestToken)){
	    	return new MyResult("wrong captcha!");
	    }

		String j_username = request.getParameter("j_username");
		String j_password = request.getParameter("j_password");
		String realname = request.getParameter("realname");
		String contact = request.getParameter("contact");
		String email = request.getParameter("email");
		
		List<Users> users = Users.getByName(j_username);
		if(users.size()>0){
			return MyResult.error();
		}			
	
		Users user = new Users();
		user.setUsername(j_username);
		user.setPassword(md5.encodePassword(j_password, ""));
		user.setRealname(realname);
		user.setContact(contact);
		user.setEmail(email);
		user.save();
		return MyResult.ok();
	}
}

