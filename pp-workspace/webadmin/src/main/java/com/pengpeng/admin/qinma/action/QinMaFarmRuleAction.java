package com.pengpeng.admin.qinma.action;

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

import com.pengpeng.admin.qinma.manager.*;
import com.pengpeng.stargame.qinma.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/qinMaFarmRule")    
public class QinMaFarmRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(QinMaFarmRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/qinMaFarmRule";  
 	
 	@Autowired
	private IQinMaFarmRuleManager qinMaFarmRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexQinMaFarmRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("qinMaFarmRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<QinMaFarmRule> ajaxQinMaFarmRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<QinMaFarmRule> result = qinMaFarmRuleManager.findPages(param, page, rows);
        for(QinMaFarmRule rule:result.getRows()){
            rule.init();
        }
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody QinMaFarmRule showQinMaFarmRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		QinMaFarmRule qinMaFarmRule = (QinMaFarmRule)qinMaFarmRuleManager.findById(id);   
  		return qinMaFarmRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createQinMaFarmRule( QinMaFarmRule qinMaFarmRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	qinMaFarmRuleManager.createBean(qinMaFarmRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateQinMaFarmRule(@PathVariable String id,QinMaFarmRule qinMaFarmRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	QinMaFarmRule qinMaFarmRule = (QinMaFarmRule)qinMaFarmRuleManager.findById(id);   
	 	bind(qinMaFarmRule,qinMaFarmRuleBean);   
	 	qinMaFarmRuleManager.updateBean(qinMaFarmRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteQinMaFarmRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	qinMaFarmRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteQinMaFarmRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		qinMaFarmRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(QinMaFarmRule qinMaFarmRule,QinMaFarmRule qinMaFarmRuleBean){
				qinMaFarmRule.setName(qinMaFarmRuleBean.getName());
				qinMaFarmRule.setLevel(qinMaFarmRuleBean.getLevel());
				qinMaFarmRule.setFieldsValue(qinMaFarmRuleBean.getFieldsValue());
			}
}