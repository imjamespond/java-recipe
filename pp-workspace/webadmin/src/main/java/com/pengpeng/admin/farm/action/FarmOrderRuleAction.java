package com.pengpeng.admin.farm.action;

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

import com.pengpeng.admin.farm.manager.*;
import com.pengpeng.stargame.farm.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/farmOrderRule")    
public class FarmOrderRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FarmOrderRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/farmOrderRule";  
 	
 	@Autowired
	private IFarmOrderRuleManager farmOrderRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFarmOrderRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("farmOrderRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FarmOrderRule> ajaxFarmOrderRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FarmOrderRule> result = farmOrderRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FarmOrderRule showFarmOrderRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FarmOrderRule farmOrderRule = (FarmOrderRule)farmOrderRuleManager.findById(id);   
  		return farmOrderRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFarmOrderRule( FarmOrderRule farmOrderRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmOrderRuleManager.createBean(farmOrderRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFarmOrderRule(@PathVariable String id,FarmOrderRule farmOrderRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FarmOrderRule farmOrderRule = (FarmOrderRule)farmOrderRuleManager.findById(id);   
	 	bind(farmOrderRule,farmOrderRuleBean);   
	 	farmOrderRuleManager.updateBean(farmOrderRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFarmOrderRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmOrderRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFarmOrderRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		farmOrderRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FarmOrderRule farmOrderRule,FarmOrderRule farmOrderRuleBean){
				farmOrderRule.setName(farmOrderRuleBean.getName());
				farmOrderRule.setFarmLevel(farmOrderRuleBean.getFarmLevel());
				farmOrderRule.setCurrencyReward(farmOrderRuleBean.getCurrencyReward());
				farmOrderRule.setExpReward(farmOrderRuleBean.getExpReward());
				farmOrderRule.setOrderRequest(farmOrderRuleBean.getOrderRequest());
			}
}