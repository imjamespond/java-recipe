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
@RequestMapping("/transferRule")    
public class TransferRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(TransferRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/transferRule";  
 	
 	@Autowired
	private ITransferRuleManager transferRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexTransferRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("transferRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<TransferRule> ajaxTransferRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<TransferRule> result = transferRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody TransferRule showTransferRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		TransferRule transferRule = (TransferRule)transferRuleManager.findById(id);   
  		return transferRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createTransferRule( TransferRule transferRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	transferRuleManager.createBean(transferRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateTransferRule(@PathVariable String id,TransferRule transferRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	TransferRule transferRule = (TransferRule)transferRuleManager.findById(id);   
	 	bind(transferRule,transferRuleBean);   
	 	transferRuleManager.updateBean(transferRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteTransferRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	transferRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteTransferRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		transferRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(TransferRule transferRule,TransferRule transferRuleBean){
				transferRule.setCurrId(transferRuleBean.getCurrId());
				transferRule.setCurrX(transferRuleBean.getCurrX());
				transferRule.setCurrY(transferRuleBean.getCurrY());
				transferRule.setTargetId(transferRuleBean.getTargetId());
				transferRule.setTargetX(transferRuleBean.getTargetX());
				transferRule.setTargetY(transferRuleBean.getTargetY());
			}
}