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
@RequestMapping("/playerActivityActionModel")    
public class PlayerActivityActionModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerActivityActionModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/playerActivityActionModel";  
 	
 	@Autowired
	private IPlayerActivityActionModelManager playerActivityActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerActivityActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerActivityActionModel/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerActivityActionModel> ajaxPlayerActivityActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerActivityActionModel> result = playerActivityActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody PlayerActivityActionModel showPlayerActivityActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerActivityActionModel playerActivityActionModel = (PlayerActivityActionModel)playerActivityActionModelManager.findById(id);   
  		return playerActivityActionModel;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerActivityActionModel(PlayerActivityActionModel playerActivityActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerActivityActionModelManager.createBean(playerActivityActionModel);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerActivityActionModel(@PathVariable String id,PlayerActivityActionModel playerActivityActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerActivityActionModel playerActivityActionModel = (PlayerActivityActionModel)playerActivityActionModelManager.findById(id);   
	 	bind(playerActivityActionModel,playerActivityActionModelBean);   
	 	playerActivityActionModelManager.updateBean(playerActivityActionModel);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerActivityActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerActivityActionModelManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerActivityActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		playerActivityActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerActivityActionModel playerActivityActionModel,PlayerActivityActionModel playerActivityActionModelBean){
				playerActivityActionModel.setPid(playerActivityActionModelBean.getPid());
				playerActivityActionModel.setDate(playerActivityActionModelBean.getDate());
				playerActivityActionModel.setUid(playerActivityActionModelBean.getUid());
				playerActivityActionModel.setPoint(playerActivityActionModelBean.getPoint());
			}
}