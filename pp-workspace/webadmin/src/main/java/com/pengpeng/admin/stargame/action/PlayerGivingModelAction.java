package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.IPlayerGivingActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerGivingActionModel;
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
@RequestMapping("/playerGivingActionModel")
public class PlayerGivingModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerGivingModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;
 	private final String LIST_ACTION = "redirect:/playerGivingModelAction";

 	@Autowired
	private IPlayerGivingActionModelManager playerGivingActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerGivingActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("statistics/givingViewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerGivingActionModel> ajaxPlayerGivingActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerGivingActionModel> result = playerGivingActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")
 	public @ResponseBody PlayerGivingActionModel showPlayerGivingActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerGivingActionModel playerGivingActionModel = (PlayerGivingActionModel)playerGivingActionModelManager.findById(id);
  		return playerGivingActionModel;
 	}

	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerGivingActionModel(PlayerGivingActionModel playerGivingActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerGivingActionModelManager.createBean(playerGivingActionModel);
        return ResResult.newOk();
	}

	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerGivingActionModel(@PathVariable String id,PlayerGivingActionModel playerGivingActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerGivingActionModel playerGivingActionModel = (PlayerGivingActionModel)playerGivingActionModelManager.findById(id);
	 	bind(playerGivingActionModel,playerGivingActionModelBean);
	 	playerGivingActionModelManager.updateBean(playerGivingActionModel);
        return ResResult.newOk();
	}

	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerGivingActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerGivingActionModelManager.removeBean(id);
        return ResResult.newOk();
	}

	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerGivingActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");
	 	for(int i = 0; i <  items.length; i++) {
	  		Long id = new Long(items[i]);
	  		playerGivingActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerGivingActionModel playerGivingActionModel,PlayerGivingActionModel playerGivingActionModelBean){
				playerGivingActionModel.setItemId(playerGivingActionModelBean.getItemId());
				playerGivingActionModel.setPid(playerGivingActionModelBean.getPid());
				playerGivingActionModel.setDate(playerGivingActionModelBean.getDate());
				playerGivingActionModel.setUid(playerGivingActionModelBean.getUid());
			}
}