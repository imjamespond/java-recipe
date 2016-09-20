package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.IPlayerTaskActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerTaskActionModel;
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
@RequestMapping("/playerTaskActionModel")
public class PlayerTaskModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerTaskModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;
 	private final String LIST_ACTION = "redirect:/playerTaskActionModel";

 	@Autowired
	private IPlayerTaskActionModelManager playerTaskActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerTaskActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("statistics/taskViewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerTaskActionModel> ajaxPlayerTaskActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerTaskActionModel> result = playerTaskActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")
 	public @ResponseBody PlayerTaskActionModel showPlayerTaskActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerTaskActionModel playerTaskActionModel = (PlayerTaskActionModel)playerTaskActionModelManager.findById(id);
  		return playerTaskActionModel;
 	}

	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerTaskActionModel(PlayerTaskActionModel playerTaskActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerTaskActionModelManager.createBean(playerTaskActionModel);
        return ResResult.newOk();
	}

	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerTaskActionModel(@PathVariable String id,PlayerTaskActionModel playerTaskActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerTaskActionModel playerTaskActionModel = (PlayerTaskActionModel)playerTaskActionModelManager.findById(id);
	 	bind(playerTaskActionModel,playerTaskActionModelBean);
	 	playerTaskActionModelManager.updateBean(playerTaskActionModel);
        return ResResult.newOk();
	}

	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerTaskActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerTaskActionModelManager.removeBean(id);
        return ResResult.newOk();
	}

	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerTaskActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");
	 	for(int i = 0; i <  items.length; i++) {
	  		Long id = new Long(items[i]);
	  		playerTaskActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerTaskActionModel playerTaskActionModel,PlayerTaskActionModel playerTaskActionModelBean){
				playerTaskActionModel.setTaskid(playerTaskActionModelBean.getTaskid());
				playerTaskActionModel.setPid(playerTaskActionModelBean.getPid());
				playerTaskActionModel.setDate(playerTaskActionModelBean.getDate());
				playerTaskActionModel.setUid(playerTaskActionModelBean.getUid());
			}
}