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
@RequestMapping("/qinMaRoomRule")    
public class QinMaRoomRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(QinMaRoomRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/qinMaRoomRule";  
 	
 	@Autowired
	private IQinMaRoomRuleManager qinMaRoomRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexQinMaRoomRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("qinMaRoomRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<QinMaRoomRule> ajaxQinMaRoomRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<QinMaRoomRule> result = qinMaRoomRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody QinMaRoomRule showQinMaRoomRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		QinMaRoomRule qinMaRoomRule = (QinMaRoomRule)qinMaRoomRuleManager.findById(id);   
  		return qinMaRoomRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createQinMaRoomRule( QinMaRoomRule qinMaRoomRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	qinMaRoomRuleManager.createBean(qinMaRoomRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateQinMaRoomRule(@PathVariable String id,QinMaRoomRule qinMaRoomRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	QinMaRoomRule qinMaRoomRule = (QinMaRoomRule)qinMaRoomRuleManager.findById(id);   
	 	bind(qinMaRoomRule,qinMaRoomRuleBean);   
	 	qinMaRoomRuleManager.updateBean(qinMaRoomRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteQinMaRoomRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	qinMaRoomRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteQinMaRoomRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		qinMaRoomRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(QinMaRoomRule qinMaRoomRule,QinMaRoomRule qinMaRoomRuleBean){
				qinMaRoomRule.setName(qinMaRoomRuleBean.getName());
				qinMaRoomRule.setX(qinMaRoomRuleBean.getX());
				qinMaRoomRule.setY(qinMaRoomRuleBean.getY());
				qinMaRoomRule.setDecoratePositions(qinMaRoomRuleBean.getDecoratePositions());
			}
}