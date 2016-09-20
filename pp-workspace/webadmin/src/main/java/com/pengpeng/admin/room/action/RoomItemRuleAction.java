package com.pengpeng.admin.room.action;

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

import com.pengpeng.admin.room.manager.*;
import com.pengpeng.stargame.room.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/roomItemRule")    
public class RoomItemRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(RoomItemRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/roomItemRule";  
 	
 	@Autowired
	private IRoomItemRuleManager roomItemRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexRoomItemRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("roomItemRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<RoomItemRule> ajaxRoomItemRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<RoomItemRule> result = roomItemRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody RoomItemRule showRoomItemRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		RoomItemRule roomItemRule = (RoomItemRule)roomItemRuleManager.findById(id);   
  		return roomItemRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createRoomItemRule( RoomItemRule roomItemRule,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	roomItemRuleManager.createBean(roomItemRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateRoomItemRule(@PathVariable String id,RoomItemRule roomItemRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	RoomItemRule roomItemRule = (RoomItemRule)roomItemRuleManager.findById(id);   
	 	bind(roomItemRule,roomItemRuleBean);   
	 	roomItemRuleManager.updateBean(roomItemRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteRoomItemRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	roomItemRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteRoomItemRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		roomItemRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(RoomItemRule roomItemRule,RoomItemRule roomItemRuleBean){
				roomItemRule.setItemsId(roomItemRuleBean.getItemsId());
				roomItemRule.setName(roomItemRuleBean.getName());
				roomItemRule.setType(roomItemRuleBean.getType());
				roomItemRule.setItemtype(roomItemRuleBean.getItemtype());
				roomItemRule.setBindingTypes(roomItemRuleBean.getBindingTypes());
				roomItemRule.setRecyclingPrice(roomItemRuleBean.getRecyclingPrice());
				roomItemRule.setGamePrice(roomItemRuleBean.getGamePrice());
				roomItemRule.setGoldPrice(roomItemRuleBean.getGoldPrice());
				roomItemRule.setFarmLevel(roomItemRuleBean.getFarmLevel());
				roomItemRule.setOverlay(roomItemRuleBean.getOverlay());
				roomItemRule.setDesc(roomItemRuleBean.getDesc());
				roomItemRule.setIcon(roomItemRuleBean.getIcon());
				roomItemRule.setShopSell(roomItemRuleBean.getShopSell());
				roomItemRule.setStarGift(roomItemRuleBean.getStarGift());
				roomItemRule.setFansValues(roomItemRuleBean.getFansValues());
				roomItemRule.setLuxuryDegree(roomItemRuleBean.getLuxuryDegree());
				roomItemRule.setImage(roomItemRuleBean.getImage());
				roomItemRule.setRotation(roomItemRuleBean.getRotation());
				roomItemRule.setRepeatPurchase(roomItemRuleBean.getRepeatPurchase());
				roomItemRule.setLargestNumber(roomItemRuleBean.getLargestNumber());
				roomItemRule.setStack(roomItemRuleBean.getStack());
			}
}