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
@RequestMapping("/playerLoginActionModel")    
public class PlayerLoginActionModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerLoginActionModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/playerLoginActionModel";  
 	
 	@Autowired
	private IPlayerLoginActionModelManager playerLoginActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerLoginActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerLoginActionModel/viewList");
    }

    /** list */
    @RequestMapping(value="/2",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView indexPlayerLoginActionModel2(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerLoginActionModel/viewList2");
    }

    /** ajax list */
	@RequestMapping(value="/list/{type}",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerLoginActionModel> ajaxPlayerLoginActionModel(@PathVariable String type,@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerLoginActionModel> result = playerLoginActionModelManager.findPages(param, page, rows, type);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody PlayerLoginActionModel showPlayerLoginActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerLoginActionModel playerLoginActionModel = (PlayerLoginActionModel)playerLoginActionModelManager.findById(id);   
  		return playerLoginActionModel;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerLoginActionModel(PlayerLoginActionModel playerLoginActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerLoginActionModelManager.createBean(playerLoginActionModel);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerLoginActionModel(@PathVariable String id,PlayerLoginActionModel playerLoginActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerLoginActionModel playerLoginActionModel = (PlayerLoginActionModel)playerLoginActionModelManager.findById(id);   
	 	bind(playerLoginActionModel,playerLoginActionModelBean);   
	 	playerLoginActionModelManager.updateBean(playerLoginActionModel);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerLoginActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerLoginActionModelManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerLoginActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		playerLoginActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerLoginActionModel playerLoginActionModel,PlayerLoginActionModel playerLoginActionModelBean){
				playerLoginActionModel.setPid(playerLoginActionModelBean.getPid());
				playerLoginActionModel.setDate(playerLoginActionModelBean.getDate());
				playerLoginActionModel.setUid(playerLoginActionModelBean.getUid());
				playerLoginActionModel.setType(playerLoginActionModelBean.getType());
			}
}