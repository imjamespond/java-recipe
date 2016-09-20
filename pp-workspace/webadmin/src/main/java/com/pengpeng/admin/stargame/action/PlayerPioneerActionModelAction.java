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
@RequestMapping("/playerPioneerActionModel")    
public class PlayerPioneerActionModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerPioneerActionModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/playerPioneerActionModel";  
 	
 	@Autowired
	private IPlayerPioneerActionModelManager playerPioneerActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerPioneerActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerPioneerActionModel/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerPioneerActionModel> ajaxPlayerPioneerActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerPioneerActionModel> result = playerPioneerActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody PlayerPioneerActionModel showPlayerPioneerActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerPioneerActionModel playerPioneerActionModel = (PlayerPioneerActionModel)playerPioneerActionModelManager.findById(id);   
  		return playerPioneerActionModel;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerPioneerActionModel(PlayerPioneerActionModel playerPioneerActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerPioneerActionModelManager.createBean(playerPioneerActionModel);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerPioneerActionModel(@PathVariable String id,PlayerPioneerActionModel playerPioneerActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerPioneerActionModel playerPioneerActionModel = (PlayerPioneerActionModel)playerPioneerActionModelManager.findById(id);   
	 	bind(playerPioneerActionModel,playerPioneerActionModelBean);   
	 	playerPioneerActionModelManager.updateBean(playerPioneerActionModel);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerPioneerActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerPioneerActionModelManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerPioneerActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		playerPioneerActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerPioneerActionModel playerPioneerActionModel,PlayerPioneerActionModel playerPioneerActionModelBean){
				playerPioneerActionModel.setPid(playerPioneerActionModelBean.getPid());
				playerPioneerActionModel.setDate(playerPioneerActionModelBean.getDate());
				playerPioneerActionModel.setUid(playerPioneerActionModelBean.getUid());
			}
}