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
@RequestMapping("/moneyTreeRule")    
public class MoneyTreeRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(MoneyTreeRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/moneyTreeRule";  
 	
 	@Autowired
	private IMoneyTreeRuleManager moneyTreeRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexMoneyTreeRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("moneyTreeRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<MoneyTreeRule> ajaxMoneyTreeRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<MoneyTreeRule> result = moneyTreeRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody MoneyTreeRule showMoneyTreeRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		MoneyTreeRule moneyTreeRule = (MoneyTreeRule)moneyTreeRuleManager.findById(id);   
  		return moneyTreeRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createMoneyTreeRule( MoneyTreeRule moneyTreeRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	moneyTreeRuleManager.createBean(moneyTreeRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateMoneyTreeRule(@PathVariable int id,MoneyTreeRule moneyTreeRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	MoneyTreeRule moneyTreeRule = (MoneyTreeRule)moneyTreeRuleManager.findById(id);   
	 	bind(moneyTreeRule,moneyTreeRuleBean);   
	 	moneyTreeRuleManager.updateBean(moneyTreeRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteMoneyTreeRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	moneyTreeRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteMoneyTreeRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		moneyTreeRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(MoneyTreeRule moneyTreeRule,MoneyTreeRule moneyTreeRuleBean){
				moneyTreeRule.setLevel(moneyTreeRuleBean.getLevel());
				moneyTreeRule.setBlessingMax(moneyTreeRuleBean.getBlessingMax());
				moneyTreeRule.setNumberOfBlessing(moneyTreeRuleBean.getNumberOfBlessing());
				moneyTreeRule.setGameCoin(moneyTreeRuleBean.getGameCoin());
				moneyTreeRule.setAcquireDevote(moneyTreeRuleBean.getAcquireDevote());
				moneyTreeRule.setBlessing(moneyTreeRuleBean.getBlessing());
				moneyTreeRule.setOutputPar(moneyTreeRuleBean.getOutputPar());
				moneyTreeRule.setRewardPar1(moneyTreeRuleBean.getRewardPar1());
				moneyTreeRule.setRewardPar2(moneyTreeRuleBean.getRewardPar2());
				moneyTreeRule.setRewardPar3(moneyTreeRuleBean.getRewardPar3());
				moneyTreeRule.setDropPar(moneyTreeRuleBean.getDropPar());
				moneyTreeRule.setDropDenomination(moneyTreeRuleBean.getDropDenomination());
				moneyTreeRule.setRewardEdit(moneyTreeRuleBean.getRewardEdit());
				moneyTreeRule.setDropFrequency(moneyTreeRuleBean.getDropFrequency());
				moneyTreeRule.setDropChance(moneyTreeRuleBean.getDropChance());
				moneyTreeRule.setDropPosition(moneyTreeRuleBean.getDropPosition());
				moneyTreeRule.setPositions(moneyTreeRuleBean.getPositions());
			}
}