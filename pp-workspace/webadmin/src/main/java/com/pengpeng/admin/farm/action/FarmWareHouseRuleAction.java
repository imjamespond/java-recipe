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
@RequestMapping("/farmWareHouseRule")    
public class FarmWareHouseRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FarmWareHouseRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/farmWareHouseRule";  
 	
 	@Autowired
	private IFarmWareHouseRuleManager farmWareHouseRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFarmWareHouseRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("farmWareHouseRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FarmWareHouseRule> ajaxFarmWareHouseRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FarmWareHouseRule> result = farmWareHouseRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FarmWareHouseRule showFarmWareHouseRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FarmWareHouseRule farmWareHouseRule = (FarmWareHouseRule)farmWareHouseRuleManager.findById(id);   
  		return farmWareHouseRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFarmWareHouseRule( FarmWareHouseRule farmWareHouseRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmWareHouseRuleManager.createBean(farmWareHouseRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFarmWareHouseRule(@PathVariable int id,FarmWareHouseRule farmWareHouseRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FarmWareHouseRule farmWareHouseRule = (FarmWareHouseRule)farmWareHouseRuleManager.findById(id);   
	 	bind(farmWareHouseRule,farmWareHouseRuleBean);   
	 	farmWareHouseRuleManager.updateBean(farmWareHouseRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFarmWareHouseRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmWareHouseRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFarmWareHouseRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		farmWareHouseRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FarmWareHouseRule farmWareHouseRule,FarmWareHouseRule farmWareHouseRuleBean){
				farmWareHouseRule.setWarehouseLevel(farmWareHouseRuleBean.getWarehouseLevel());
				farmWareHouseRule.setCapacity(farmWareHouseRuleBean.getCapacity());
				farmWareHouseRule.setProps(farmWareHouseRuleBean.getProps());
			}
}