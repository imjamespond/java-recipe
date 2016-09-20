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
@RequestMapping("/baseGiftRule")    
public class BaseGiftRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(BaseGiftRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/baseGiftRule";  
 	
 	@Autowired
	private IBaseGiftRuleManager baseGiftRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexBaseGiftRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("baseGiftRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<BaseGiftRule> ajaxBaseGiftRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<BaseGiftRule> result = baseGiftRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody BaseGiftRule showBaseGiftRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		BaseGiftRule baseGiftRule = (BaseGiftRule)baseGiftRuleManager.findById(id);   
  		return baseGiftRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createBaseGiftRule(@RequestParam BaseGiftRule baseGiftRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	baseGiftRuleManager.createBean(baseGiftRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateBaseGiftRule(@PathVariable String id,BaseGiftRule baseGiftRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	BaseGiftRule baseGiftRule = (BaseGiftRule)baseGiftRuleManager.findById(id);   
	 	bind(baseGiftRule,baseGiftRuleBean);   
	 	baseGiftRuleManager.updateBean(baseGiftRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteBaseGiftRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	baseGiftRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteBaseGiftRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		baseGiftRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(BaseGiftRule baseGiftRule,BaseGiftRule baseGiftRuleBean){
				baseGiftRule.setPresentId(baseGiftRuleBean.getPresentId());
				baseGiftRule.setPresentType(baseGiftRuleBean.getPresentType());
				baseGiftRule.setItemId(baseGiftRuleBean.getItemId());
				baseGiftRule.setNum(baseGiftRuleBean.getNum());
				baseGiftRule.setValidityTime(baseGiftRuleBean.getValidityTime());
			}
}