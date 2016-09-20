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
@RequestMapping("/familyRule")    
public class FamilyRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FamilyRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/familyRule";  
 	
 	@Autowired
	private IFamilyRuleManager familyRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFamilyRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("familyRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FamilyRule> ajaxFamilyRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FamilyRule> result = familyRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FamilyRule showFamilyRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FamilyRule familyRule = (FamilyRule)familyRuleManager.findById(id);   
  		return familyRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFamilyRule( FamilyRule familyRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	familyRuleManager.createBean(familyRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFamilyRule(@PathVariable String id,FamilyRule familyRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FamilyRule familyRule = (FamilyRule)familyRuleManager.findById(id);   
	 	bind(familyRule,familyRuleBean);   
	 	familyRuleManager.updateBean(familyRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFamilyRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	familyRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFamilyRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		familyRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FamilyRule familyRule,FamilyRule familyRuleBean){
				familyRule.setName(familyRuleBean.getName());
				familyRule.setStarName(familyRuleBean.getStarName());
				familyRule.setStarId(familyRuleBean.getStarId());
				familyRule.setStarIcon(familyRuleBean.getStarIcon());
				familyRule.setMaxMember(familyRuleBean.getMaxMember());
				familyRule.setGameCoinDevote(familyRuleBean.getGameCoinDevote());
				familyRule.setGoldCoinDevote(familyRuleBean.getGoldCoinDevote());
				familyRule.setGameCoinFunds(familyRuleBean.getGameCoinFunds());
				familyRule.setGoldCoinFunds(familyRuleBean.getGoldCoinFunds());
				familyRule.setFundsLimit(familyRuleBean.getFundsLimit());
				familyRule.setPropsDevote(familyRuleBean.getPropsDevote());
				familyRule.setTasksLimit(familyRuleBean.getTasksLimit());
				familyRule.setWelcomeCoin(familyRuleBean.getWelcomeCoin());
				familyRule.setChangeCoin(familyRuleBean.getChangeCoin());
				familyRule.setWords(familyRuleBean.getWords());
				familyRule.setTreeRipeTime1(familyRuleBean.getTreeRipeTime1());
				familyRule.setTreeRipeTime2(familyRuleBean.getTreeRipeTime2());
			}
}