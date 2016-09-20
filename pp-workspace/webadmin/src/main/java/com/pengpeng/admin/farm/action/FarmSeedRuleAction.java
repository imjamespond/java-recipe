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
@RequestMapping("/farmSeedRule")    
public class FarmSeedRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FarmSeedRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/farmSeedRule";  
 	
 	@Autowired
	private IFarmSeedRuleManager farmSeedRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFarmSeedRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("farmSeedRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FarmSeedRule> ajaxFarmSeedRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FarmSeedRule> result = farmSeedRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FarmSeedRule showFarmSeedRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FarmSeedRule farmSeedRule = (FarmSeedRule)farmSeedRuleManager.findById(id);   
  		return farmSeedRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFarmSeedRule( FarmSeedRule farmSeedRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmSeedRuleManager.createBean(farmSeedRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFarmSeedRule(@PathVariable String id,FarmSeedRule farmSeedRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FarmSeedRule farmSeedRule = (FarmSeedRule)farmSeedRuleManager.findById(id);   
	 	bind(farmSeedRule,farmSeedRuleBean);   
	 	farmSeedRuleManager.updateBean(farmSeedRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFarmSeedRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmSeedRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFarmSeedRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		farmSeedRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FarmSeedRule farmSeedRule,FarmSeedRule farmSeedRuleBean){
				farmSeedRule.setItemsId(farmSeedRuleBean.getItemsId());
				farmSeedRule.setName(farmSeedRuleBean.getName());
				farmSeedRule.setType(farmSeedRuleBean.getType());
				farmSeedRule.setItemtype(farmSeedRuleBean.getItemtype());
				farmSeedRule.setBindingTypes(farmSeedRuleBean.getBindingTypes());
				farmSeedRule.setRecyclingPrice(farmSeedRuleBean.getRecyclingPrice());
				farmSeedRule.setGamePrice(farmSeedRuleBean.getGamePrice());
				farmSeedRule.setGoldPrice(farmSeedRuleBean.getGoldPrice());
				farmSeedRule.setFarmLevel(farmSeedRuleBean.getFarmLevel());
				farmSeedRule.setOverlay(farmSeedRuleBean.getOverlay());
				farmSeedRule.setDesc(farmSeedRuleBean.getDesc());
				farmSeedRule.setIcon(farmSeedRuleBean.getIcon());
				farmSeedRule.setShopSell(farmSeedRuleBean.getShopSell());
				farmSeedRule.setStarGift(farmSeedRuleBean.getStarGift());
				farmSeedRule.setFansValues(farmSeedRuleBean.getFansValues());
				farmSeedRule.setGrowthTime(farmSeedRuleBean.getGrowthTime());
				farmSeedRule.setOutput(farmSeedRuleBean.getOutput());
				farmSeedRule.setProduction(farmSeedRuleBean.getProduction());
				farmSeedRule.setHarvestEditor(farmSeedRuleBean.getHarvestEditor());
				farmSeedRule.setCropsReward(farmSeedRuleBean.getCropsReward());
				farmSeedRule.setExpReward(farmSeedRuleBean.getExpReward());
				farmSeedRule.setGrowthImage(farmSeedRuleBean.getGrowthImage());
				farmSeedRule.setMatureImage(farmSeedRuleBean.getMatureImage());
				farmSeedRule.setSeedsExp(farmSeedRuleBean.getSeedsExp());
			}
}