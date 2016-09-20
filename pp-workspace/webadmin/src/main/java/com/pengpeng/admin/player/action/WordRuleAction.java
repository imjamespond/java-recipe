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
@RequestMapping("/wordRule")    
public class WordRuleAction extends BaseAction {
	private static final Log log = LogFactory.getLog(WordRuleAction.class);

	//default sort,example: username desc,createTime asc
 	protected static final String DEFAULT_SORT_COLUMNS = null;      	
 	private final String LIST_ACTION = "redirect:/wordRule";  
 	
 	@Autowired
	private IWordRuleManager wordRuleManager;

    /** list */
    @RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
        public ModelAndView indexWordRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("wordRule/viewList");
    }

    /** ajax list */
	@RequestMapping(value="/list",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Page<WordRule> ajaxWordRule(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>(request.getParameterMap());
        param.remove("page");
        param.remove("rows");
	  	Page<WordRule> result = wordRuleManager.findPages(param, page, rows);
	  	return result;
	}

    /** get */
	@RequestMapping(value="/{id}")    
 	public @ResponseBody WordRule showWordRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
  		WordRule wordRule = (WordRule)wordRuleManager.findById(id);   
  		return wordRule;
 	}
	    
	/** add save */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResResult createWordRule(
            @RequestParam (value="id",defaultValue="0")int id,
            @RequestParam (value="memo",defaultValue="1")String memo,
            @RequestParam (value="state",defaultValue="1")int state,
            @RequestParam (value="type",defaultValue="1")String type,
            @RequestParam (value="words",defaultValue="1")String words,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        WordRule wordRule = new WordRule();
        wordRule.setId(id);
        wordRule.setMemo(memo);
        wordRule.setState(state);
        wordRule.setType(type);
        wordRule.setWords(words);

        wordRuleManager.createBean(wordRule);
        return ResResult.newOk();
	}   
	    
	/** save update */
	@RequestMapping(value="/update/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult updateWordRule(@PathVariable int id,WordRule wordRuleBean,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	WordRule wordRule = (WordRule)wordRuleManager.findById(id);   
	 	bind(wordRule,wordRuleBean);   
	 	wordRuleManager.updateBean(wordRule);
        return ResResult.newOk();
	}   
	    
	/** del */
	@RequestMapping(value="/del/{id}",method=RequestMethod.POST)
	public @ResponseBody ResResult deleteWordRule(@PathVariable int id,HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	wordRuleManager.removeBean(id);
        return ResResult.newOk();
	}   
	 
	/** patch del */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public @ResponseBody ResResult batchDeleteWordRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
	 	String[] items = request.getParameterValues("items");   
	 	for(int i = 0; i <  items.length; i++) {   
	  		java.lang.Long id = new java.lang.Long(items[i]);   
	  		wordRuleManager.removeBean(id);   
	 	}
        return ResResult.newOk();
	}
	private void bind(WordRule wordRule,WordRule wordRuleBean){
				wordRule.setWords(wordRuleBean.getWords());
				wordRule.setType(wordRuleBean.getType());
				wordRule.setMemo(wordRuleBean.getMemo());
				wordRule.setState(wordRuleBean.getState());
			}
}