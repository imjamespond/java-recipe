package com.pengpeng.admin.fashion.action;

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

import com.pengpeng.admin.fashion.manager.*;
import com.pengpeng.stargame.fashion.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/fashionItemRule")    
public class FashionItemRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(FashionItemRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/fashionItemRule";  
 	
 	@Autowired
	private IFashionItemRuleManager fashionItemRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexFashionItemRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("fashionItemRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<FashionItemRule> ajaxFashionItemRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<FashionItemRule> result = fashionItemRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody FashionItemRule showFashionItemRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		FashionItemRule fashionItemRule = (FashionItemRule)fashionItemRuleManager.findById(id);   
  		return fashionItemRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createFashionItemRule( FashionItemRule fashionItemRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	fashionItemRuleManager.createBean(fashionItemRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateFashionItemRule(@PathVariable String id,FashionItemRule fashionItemRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	FashionItemRule fashionItemRule = (FashionItemRule)fashionItemRuleManager.findById(id);   
	 	bind(fashionItemRule,fashionItemRuleBean);   
	 	fashionItemRuleManager.updateBean(fashionItemRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteFashionItemRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	fashionItemRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteFashionItemRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		fashionItemRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(FashionItemRule fashionItemRule,FashionItemRule fashionItemRuleBean){
				fashionItemRule.setItemsId(fashionItemRuleBean.getItemsId());
				fashionItemRule.setName(fashionItemRuleBean.getName());
				fashionItemRule.setType(fashionItemRuleBean.getType());
				fashionItemRule.setItemtype(fashionItemRuleBean.getItemtype());
				fashionItemRule.setBindingTypes(fashionItemRuleBean.getBindingTypes());
				fashionItemRule.setRecyclingPrice(fashionItemRuleBean.getRecyclingPrice());
				fashionItemRule.setGamePrice(fashionItemRuleBean.getGamePrice());
				fashionItemRule.setGoldPrice(fashionItemRuleBean.getGoldPrice());
				fashionItemRule.setFarmLevel(fashionItemRuleBean.getFarmLevel());
				fashionItemRule.setOverlay(fashionItemRuleBean.getOverlay());
				fashionItemRule.setDesc(fashionItemRuleBean.getDesc());
				fashionItemRule.setIcon(fashionItemRuleBean.getIcon());
				fashionItemRule.setShopSell(fashionItemRuleBean.getShopSell());
				fashionItemRule.setStarGift(fashionItemRuleBean.getStarGift());
				fashionItemRule.setFansValues(fashionItemRuleBean.getFansValues());
				fashionItemRule.setSex(fashionItemRuleBean.getSex());
				fashionItemRule.setImage(fashionItemRuleBean.getImage());
				fashionItemRule.setFashionIndex(fashionItemRuleBean.getFashionIndex());
			}
}