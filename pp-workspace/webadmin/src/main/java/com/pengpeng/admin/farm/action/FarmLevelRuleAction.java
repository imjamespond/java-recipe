package com.pengpeng.admin.farm.action;

import com.pengpeng.admin.farm.manager.IFarmLevelRuleManager;
import com.pengpeng.stargame.farm.rule.FarmLevelRule;
import com.tongyi.action.BaseAction;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/farmLevelRule")    
public class FarmLevelRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FarmLevelRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/farmLevelRule";  
 	
 	@Autowired
	private IFarmLevelRuleManager farmLevelRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFarmLevelRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("farmLevelRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FarmLevelRule> ajaxFarmLevelRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FarmLevelRule> result = farmLevelRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FarmLevelRule showFarmLevelRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FarmLevelRule farmLevelRule = (FarmLevelRule)farmLevelRuleManager.findById(id);   
  		return farmLevelRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFarmLevelRule( FarmLevelRule farmLevelRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmLevelRuleManager.createBean(farmLevelRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFarmLevelRule(@PathVariable int id,FarmLevelRule farmLevelRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FarmLevelRule farmLevelRule = (FarmLevelRule)farmLevelRuleManager.findById(id);   
	 	bind(farmLevelRule,farmLevelRuleBean);   
	 	farmLevelRuleManager.updateBean(farmLevelRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFarmLevelRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	farmLevelRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFarmLevelRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		farmLevelRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FarmLevelRule farmLevelRule,FarmLevelRule farmLevelRuleBean){
				farmLevelRule.setLevel(farmLevelRuleBean.getLevel());
				farmLevelRule.setNeedExp(farmLevelRuleBean.getNeedExp());
				farmLevelRule.setMaxGold(farmLevelRuleBean.getMaxGold());
			}
}