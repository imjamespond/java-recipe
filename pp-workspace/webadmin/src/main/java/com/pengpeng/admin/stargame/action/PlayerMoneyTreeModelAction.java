package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.IPlayerMoneyTreeActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerMoneyTreeActionModel;
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
@RequestMapping("/playerMoneyTreeActionModel")
public class PlayerMoneyTreeModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(PlayerMoneyTreeModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;
 	private final String LIST_ACTION = "redirect:/playerMoneyTreeModelAction";

 	@Autowired
	private IPlayerMoneyTreeActionModelManager playerMoneyTreeActionModelManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexPlayerMoneyTreeActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("statistics/moneyTreeViewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<PlayerMoneyTreeActionModel> ajaxPlayerMoneyTreeActionModel(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<PlayerMoneyTreeActionModel> result = playerMoneyTreeActionModelManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")
 	public @ResponseBody PlayerMoneyTreeActionModel showPlayerMoneyTreeActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		PlayerMoneyTreeActionModel playerMoneyTreeActionModel = (PlayerMoneyTreeActionModel)playerMoneyTreeActionModelManager.findById(id);
  		return playerMoneyTreeActionModel;
 	}

	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createPlayerMoneyTreeActionModel(PlayerMoneyTreeActionModel playerMoneyTreeActionModel,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerMoneyTreeActionModelManager.createBean(playerMoneyTreeActionModel);
        return ResResult.newOk();
	}

	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updatePlayerMoneyTreeActionModel(@PathVariable String id,PlayerMoneyTreeActionModel playerMoneyTreeActionModelBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	PlayerMoneyTreeActionModel playerMoneyTreeActionModel = (PlayerMoneyTreeActionModel)playerMoneyTreeActionModelManager.findById(id);
	 	bind(playerMoneyTreeActionModel,playerMoneyTreeActionModelBean);
	 	playerMoneyTreeActionModelManager.updateBean(playerMoneyTreeActionModel);
        return ResResult.newOk();
	}

	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deletePlayerMoneyTreeActionModel(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	playerMoneyTreeActionModelManager.removeBean(id);
        return ResResult.newOk();
	}

	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeletePlayerMoneyTreeActionModel(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");
	 	for(int i = 0; i <  items.length; i++) {
	  		Long id = new Long(items[i]);
	  		playerMoneyTreeActionModelManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(PlayerMoneyTreeActionModel playerMoneyTreeActionModel,PlayerMoneyTreeActionModel playerMoneyTreeActionModelBean){

				playerMoneyTreeActionModel.setPid(playerMoneyTreeActionModelBean.getPid());
				playerMoneyTreeActionModel.setDate(playerMoneyTreeActionModelBean.getDate());
				playerMoneyTreeActionModel.setUid(playerMoneyTreeActionModelBean.getUid());
			}
}