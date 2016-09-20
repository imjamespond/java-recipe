package com.james.jetty.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.james.jetty.model.Insure;
import com.james.jetty.utils.MyUserDetailsService;
import com.james.jetty.vo.MyResult;

@Controller
@RequestMapping("/insure")
public class InsureController {

	@RequestMapping(value="/",method={RequestMethod.GET,RequestMethod.POST})
	public String index(HttpServletRequest request,HttpServletResponse response){
		
		return "insure";
	}	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object 
	list(HttpServletRequest request) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Long uid = MyUserDetailsService.userMap.get(userDetails.getUsername());
		if(uid == null){
			return MyResult.error();
		}
		
		List<Insure> list = Insure.getByUid(uid,0,100);
		MyResult<List<Insure>> result = new MyResult<List<Insure>>();
		result.setResult(list);
		return result;		
		//return list;
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public @ResponseBody Object 
	post(HttpServletRequest request) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String express = request.getParameter("express");
		String expressid = request.getParameter("expressid");
		String item = request.getParameter("item");
		String ivalue = request.getParameter("ivalue");
		String cost = request.getParameter("cost");
		Long uid = MyUserDetailsService.userMap.get(userDetails.getUsername());
		if(uid == null){
			return MyResult.error();
		}
		
		Insure insure = new Insure();
		insure.setExpress(express);
		insure.setExpressid(expressid);
		insure.setItem(item);
		insure.setIvalue(Double.valueOf(ivalue));
		insure.setCost(Double.valueOf(cost));
		insure.setUid(uid);
		insure.save();

		MyResult<Insure> result = new MyResult<Insure>();
		result.setResult(insure);
		return result;
	}


}


