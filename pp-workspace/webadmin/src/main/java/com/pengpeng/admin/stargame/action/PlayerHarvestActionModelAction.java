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
@RequestMapping("/playerHarvestActionModel")    
public class PlayerHarvestActionModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerHarvestActionModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/playerHarvestActionModel";  
 	
 	@Autowired
	private IPlayerHarvestActionModelManager playerHarvestActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerHarvestActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerHarvestActionModel/viewList");
    }
    /** list */
    @RequestMapping(value="/2",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView indexPlayerHarvestActionModel2(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerHarvestActionModel/viewList2");
    }
    /** list */
    @RequestMapping(value="/3",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView indexPlayerHarvestActionModel3(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("playerHarvestActionModel/viewList3");
    }

    /** ajax list */
	@RequestMapping(value="/list/{type}",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerHarvestActionModel> ajaxPlayerHarvestActionModel(@PathVariable String type,@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerHarvestActionModel> result = playerHarvestActionModelManager.findPages(param, page, rows, type);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody PlayerHarvestActionModel showPlayerHarvestActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerHarvestActionModel playerHarvestActionModel = (PlayerHarvestActionModel)playerHarvestActionModelManager.findById(id);   
  		return playerHarvestActionModel;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerHarvestActionModel(PlayerHarvestActionModel playerHarvestActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerHarvestActionModelManager.createBean(playerHarvestActionModel);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerHarvestActionModel(@PathVariable String id,PlayerHarvestActionModel playerHarvestActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerHarvestActionModel playerHarvestActionModel = (PlayerHarvestActionModel)playerHarvestActionModelManager.findById(id);   
	 	bind(playerHarvestActionModel,playerHarvestActionModelBean);   
	 	playerHarvestActionModelManager.updateBean(playerHarvestActionModel);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerHarvestActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerHarvestActionModelManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerHarvestActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		playerHarvestActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerHarvestActionModel playerHarvestActionModel,PlayerHarvestActionModel playerHarvestActionModelBean){
				playerHarvestActionModel.setPid(playerHarvestActionModelBean.getPid());
				playerHarvestActionModel.setDate(playerHarvestActionModelBean.getDate());
				playerHarvestActionModel.setUid(playerHarvestActionModelBean.getUid());
				playerHarvestActionModel.setType(playerHarvestActionModelBean.getType());
				playerHarvestActionModel.setFieldId(playerHarvestActionModelBean.getFieldId());
				playerHarvestActionModel.setRipenNum(playerHarvestActionModelBean.getRipenNum());
				playerHarvestActionModel.setPlantId(playerHarvestActionModelBean.getPlantId());
				playerHarvestActionModel.setFriendId(playerHarvestActionModelBean.getFriendId());
			}
}