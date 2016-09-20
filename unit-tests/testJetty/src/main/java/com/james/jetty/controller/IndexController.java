package com.james.jetty.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


//import com.james.jetty.model.Authorities;
import com.james.jetty.model.Insure;
import com.james.jetty.model.Users;
import com.james.jetty.vo.MyResult;;

@Controller
@RequestMapping("/index")
public class IndexController {
	
	private static Md5PasswordEncoder md5=new Md5PasswordEncoder();

	@Autowired  
	SessionRegistry sessionRegistry;  
	
	@ModelAttribute("numUsers")  
	public int getNumberOfUsers() {  
	   return sessionRegistry.getAllPrincipals().size();  
	}  
	
	@RequestMapping(value="/",method={RequestMethod.GET,RequestMethod.POST})
	public String index(HttpServletRequest request,HttpServletResponse response){
		
		request.setAttribute("var1","this is var1");
		
		return "index";
	}
	
	@RequestMapping("/user/{userid}")
	public @ResponseBody Object 
	user(@PathVariable String userid) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String,String> map = new HashMap<String,String>();
		map.put("param", userid);
		map.put("user", userDetails.getUsername());
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    String session = attr.getRequest().getSession(true).getId(); // true == allow create
		
	    map.put("session", session);
	    
		List<Users> users = Users.getByName(userDetails.getUsername());
		if(users!=null){
			Md5PasswordEncoder md5=new Md5PasswordEncoder();
			Users user = users.get(0);
			map.put("passord", user.getPassword());
		}
		
		return map;
	}
	
	@RequestMapping("/user/update")
	public @ResponseBody Object 
	update() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Users> users = Users.getByName(userDetails.getUsername());
		if(users!=null){
			Md5PasswordEncoder md5=new Md5PasswordEncoder();
			Users user = users.get(0);
			user.setPassword(md5.encodePassword("123", ""));
			user.update();
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("user", userDetails.getUsername());
		return map;
	}
	
	@RequestMapping("/user/info/{username}")
	public @ResponseBody Object 
	register(@PathVariable String username) {
		List<Users> users = Users.getByName(username);
		if(users.size()>0){
			return MyResult.error();
		}
		
		Users user = new Users();
		user.setUsername(username);
		user.setPassword(md5.encodePassword("1212", ""));
		user.save();
//		Authorities auth = new Authorities();
//		auth.setAuthority("ROLE_ADMIN");
//		auth.setUsername(username);
//		auth.save();
		return user;
	}
	



}


