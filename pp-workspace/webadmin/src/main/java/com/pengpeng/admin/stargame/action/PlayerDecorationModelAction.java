package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.IPlayerDecorationActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerDecorationActionModel;
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
@RequestMapping("/playerDecorationActionModel")
public class PlayerDecorationModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerDecorationModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;
 	private final String LIST_ACTION = "redirect:/playerDecorationActionModel";

 	@Autowired
	private IPlayerDecorationActionModelManager playerDecorationActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerDecorationActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("statistics/decorationViewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerDecorationActionModel> ajaxPlayerDecorationActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerDecorationActionModel> result = playerDecorationActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")
 	public @ResponseBody PlayerDecorationActionModel showPlayerDecorationActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerDecorationActionModel playerDecorationActionModel = (PlayerDecorationActionModel)playerDecorationActionModelManager.findById(id);
  		return playerDecorationActionModel;
 	}

	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerDecorationActionModel(PlayerDecorationActionModel playerDecorationActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerDecorationActionModelManager.createBean(playerDecorationActionModel);
        return ResResult.newOk();
	}

	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerDecorationActionModel(@PathVariable String id,PlayerDecorationActionModel playerDecorationActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerDecorationActionModel playerDecorationActionModel = (PlayerDecorationActionModel)playerDecorationActionModelManager.findById(id);
	 	bind(playerDecorationActionModel,playerDecorationActionModelBean);
	 	playerDecorationActionModelManager.updateBean(playerDecorationActionModel);
        return ResResult.newOk();
	}

	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerDecorationActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerDecorationActionModelManager.removeBean(id);
        return ResResult.newOk();
	}

	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerDecorationActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");
	 	for(int i = 0; i <  items.length; i++) {
	  		Long id = new Long(items[i]);
	  		playerDecorationActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerDecorationActionModel playerDecorationActionModel,PlayerDecorationActionModel playerDecorationActionModelBean){
				playerDecorationActionModel.setPid(playerDecorationActionModelBean.getPid());
				playerDecorationActionModel.setDate(playerDecorationActionModelBean.getDate());
				playerDecorationActionModel.setUid(playerDecorationActionModelBean.getUid());
			}
}