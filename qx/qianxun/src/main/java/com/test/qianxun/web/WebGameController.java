package com.test.qianxun.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/webgame")
public class WebGameController{
	
	@RequestMapping("/fly")
	public String fly(){
		return "webgame/fly";
	}
	
	@RequestMapping("/xz")
	public String xz(){
		return "webgame/xz";
	}
	
	@RequestMapping("/flying")
	public String flying(){
		return "webgame/flying";
	}
	
	@RequestMapping("/game")
	public String game(){
		return "webgame/game";
	}
}