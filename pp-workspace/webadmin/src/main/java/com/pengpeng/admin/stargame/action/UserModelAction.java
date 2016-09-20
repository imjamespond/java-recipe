package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.IUserActionManager;
import com.pengpeng.admin.stargame.model.UserActionModel;
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
@RequestMapping("/userActionModel")
public class UserModelAction extends BaseAction {
	private static final Log log = LogFactory.getLog(UserModelAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;
 	private final String LIST_ACTION = "redirect:/UserActionModel";

 	@Autowired
	private IUserActionManager userActionManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexUserAction(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("statistics/operationViewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<UserActionModel> ajaxUserAction(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<UserActionModel> result = userActionManager.findPages(param, page, rows);
	  	return result;
	}

}