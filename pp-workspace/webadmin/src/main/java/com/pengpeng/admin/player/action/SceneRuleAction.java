package com.pengpeng.admin.player.action;

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

import com.pengpeng.admin.player.manager.*;
import com.pengpeng.stargame.player.rule.*;


/**
 * template action.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/sceneRule")    
public class SceneRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(SceneRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/sceneRule";  
 	
 	@Autowired
	private ISceneRuleManager sceneRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexSceneRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("sceneRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<SceneRule> ajaxSceneRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<SceneRule> result = sceneRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody SceneRule showSceneRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		SceneRule sceneRule = (SceneRule)sceneRuleManager.findById(id);   
  		return sceneRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createSceneRule(SceneRule sceneRuleBean, HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	sceneRuleManager.createBean(sceneRuleBean);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateSceneRule(@PathVariable String id,SceneRule sceneRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	SceneRule sceneRule = (SceneRule)sceneRuleManager.findById(id);   
	 	bind(sceneRule,sceneRuleBean);   
	 	sceneRuleManager.updateBean(sceneRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteSceneRule(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	sceneRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteSceneRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		sceneRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(SceneRule sceneRule,SceneRule sceneRuleBean){
				sceneRule.setName(sceneRuleBean.getName());
				sceneRule.setType(sceneRuleBean.getType());
				sceneRule.setWithTheScreen(sceneRuleBean.getWithTheScreen());
				sceneRule.setPkType(sceneRuleBean.getPkType());
				sceneRule.setMonster(sceneRuleBean.getMonster());
				sceneRule.setNpc(sceneRuleBean.getNpc());
				sceneRule.setImagePath(sceneRuleBean.getImagePath());
				sceneRule.setMusicPath(sceneRuleBean.getMusicPath());
				sceneRule.setShareType(sceneRuleBean.getShareType());
			}
}