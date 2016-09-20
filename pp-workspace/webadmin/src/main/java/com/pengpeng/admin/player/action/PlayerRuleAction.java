package com.pengpeng.admin.player.action;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.tongyi.action.BaseAction;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.commons.logging.Log;
import javax.servlet.http.HttpServletResponse;

import com.pengpeng.admin.player.manager.*;
import com.pengpeng.stargame.player.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/playerRule")    
public class PlayerRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/playerRule";  
 	
 	@Autowired
	private IPlayerRuleManager playerRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerRule> ajaxPlayerRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerRule> result = playerRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody PlayerRule showPlayerRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerRule playerRule = (PlayerRule)playerRuleManager.findById(id);   
  		return playerRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerRule(
            @RequestParam (value="exp",defaultValue="1")int exp,
            @RequestParam (value="id",defaultValue="0")String id,
            @RequestParam (value="level",defaultValue="1")int level,
            @RequestParam (value="type",defaultValue="1")int type,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PlayerRule playerRule = new PlayerRule();
        playerRule.setExp(exp);
        playerRule.setId(id);
        playerRule.setLevel(level);
        playerRule.setType(type);

        playerRuleManager.createBean(playerRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerRule(@PathVariable String id,PlayerRule playerRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerRule playerRule = (PlayerRule)playerRuleManager.findById(id);   
	 	bind(playerRule,playerRuleBean);   
	 	playerRuleManager.updateBean(playerRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		playerRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerRule playerRule,PlayerRule playerRuleBean){
				playerRule.setLevel(playerRuleBean.getLevel());
				playerRule.setExp(playerRuleBean.getExp());
				playerRule.setType(playerRuleBean.getType());
			}
}