package com.pengpeng.admin.stargame.action;

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

import com.pengpeng.admin.stargame.manager.*;
import com.pengpeng.admin.stargame.model.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/playerLotteryActionModel")    
public class PlayerLotteryActionModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerLotteryActionModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/playerLotteryActionModel";  
 	
 	@Autowired
	private IPlayerLotteryActionModelManager playerLotteryActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerLotteryActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerLotteryActionModel/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerLotteryActionModel> ajaxPlayerLotteryActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerLotteryActionModel> result = playerLotteryActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody PlayerLotteryActionModel showPlayerLotteryActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerLotteryActionModel playerLotteryActionModel = (PlayerLotteryActionModel)playerLotteryActionModelManager.findById(id);   
  		return playerLotteryActionModel;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerLotteryActionModel(PlayerLotteryActionModel playerLotteryActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerLotteryActionModelManager.createBean(playerLotteryActionModel);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerLotteryActionModel(@PathVariable String id,PlayerLotteryActionModel playerLotteryActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerLotteryActionModel playerLotteryActionModel = (PlayerLotteryActionModel)playerLotteryActionModelManager.findById(id);   
	 	bind(playerLotteryActionModel,playerLotteryActionModelBean);   
	 	playerLotteryActionModelManager.updateBean(playerLotteryActionModel);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerLotteryActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerLotteryActionModelManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerLotteryActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		playerLotteryActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerLotteryActionModel playerLotteryActionModel,PlayerLotteryActionModel playerLotteryActionModelBean){
				playerLotteryActionModel.setPid(playerLotteryActionModelBean.getPid());
				playerLotteryActionModel.setDate(playerLotteryActionModelBean.getDate());
				playerLotteryActionModel.setUid(playerLotteryActionModelBean.getUid());
			}
}