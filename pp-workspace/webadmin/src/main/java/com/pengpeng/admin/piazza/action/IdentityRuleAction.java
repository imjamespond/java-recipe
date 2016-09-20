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
@RequestMapping("/identityRule")    
public class IdentityRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(IdentityRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/identityRule";  
 	
 	@Autowired
	private IIdentityRuleManager identityRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexIdentityRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("identityRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<IdentityRule> ajaxIdentityRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<IdentityRule> result = identityRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody IdentityRule showIdentityRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		IdentityRule identityRule = (IdentityRule)identityRuleManager.findById(id);   
  		return identityRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createIdentityRule(
            @RequestParam (value="type",defaultValue="1")int type,
            @RequestParam (value="boonRate",defaultValue="1")float boonRate,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IdentityRule identityRule = new IdentityRule();
        identityRule.setBoonRate(boonRate);
        identityRule.setType(type);

	 	identityRuleManager.createBean(identityRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateIdentityRule(@PathVariable int id,IdentityRule identityRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	IdentityRule identityRule = (IdentityRule)identityRuleManager.findById(id);   
	 	bind(identityRule,identityRuleBean);   
	 	identityRuleManager.updateBean(identityRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteIdentityRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	identityRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteIdentityRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		identityRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(IdentityRule identityRule,IdentityRule identityRuleBean){
				identityRule.setType(identityRuleBean.getType());
				identityRule.setBoonRate(identityRuleBean.getBoonRate());
			}
}