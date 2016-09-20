package com.pengpeng.admin.piazza.action;

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

import com.pengpeng.admin.piazza.manager.*;
import com.pengpeng.stargame.piazza.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/familyBuildingRule")    
public class FamilyBuildingRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FamilyBuildingRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/familyBuildingRule";  
 	
 	@Autowired
	private IFamilyBuildingRuleManager familyBuildingRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFamilyBuildingRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("familyBuildingRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FamilyBuildingRule> ajaxFamilyBuildingRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FamilyBuildingRule> result = familyBuildingRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FamilyBuildingRule showFamilyBuildingRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FamilyBuildingRule familyBuildingRule = (FamilyBuildingRule)familyBuildingRuleManager.findById(id);   
  		return familyBuildingRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFamilyBuildingRule( FamilyBuildingRule familyBuildingRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	familyBuildingRuleManager.createBean(familyBuildingRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFamilyBuildingRule(@PathVariable String id,FamilyBuildingRule familyBuildingRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FamilyBuildingRule familyBuildingRule = (FamilyBuildingRule)familyBuildingRuleManager.findById(id);   
	 	bind(familyBuildingRule,familyBuildingRuleBean);   
	 	familyBuildingRuleManager.updateBean(familyBuildingRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFamilyBuildingRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	familyBuildingRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFamilyBuildingRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		familyBuildingRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FamilyBuildingRule familyBuildingRule,FamilyBuildingRule familyBuildingRuleBean){
				familyBuildingRule.setBuildingId(familyBuildingRuleBean.getBuildingId());
				familyBuildingRule.setName(familyBuildingRuleBean.getName());
				familyBuildingRule.setType(familyBuildingRuleBean.getType());
				familyBuildingRule.setLevel(familyBuildingRuleBean.getLevel());
				familyBuildingRule.setLevelFunds(familyBuildingRuleBean.getLevelFunds());
				familyBuildingRule.setLevelRequirement(familyBuildingRuleBean.getLevelRequirement());
				familyBuildingRule.setMemo(familyBuildingRuleBean.getMemo());
				familyBuildingRule.setIcon(familyBuildingRuleBean.getIcon());
				familyBuildingRule.setMaxLevel(familyBuildingRuleBean.getMaxLevel());
				familyBuildingRule.setLevelEffect(familyBuildingRuleBean.getLevelEffect());
				familyBuildingRule.setMaxFunds(familyBuildingRuleBean.getMaxFunds());
				familyBuildingRule.setMaxItem(familyBuildingRuleBean.getMaxItem());
				familyBuildingRule.setBoon(familyBuildingRuleBean.getBoon());
			}
}