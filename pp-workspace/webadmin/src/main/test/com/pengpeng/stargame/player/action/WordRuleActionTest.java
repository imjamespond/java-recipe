package com.pengpeng.stargame.player.action;

import java.util.HashMap;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.tongyi.action.BaseAction;


/**
 * 生成模版 actionTest.vm
 * @author fangyaoxia
 */
@Controller  
@RequestMapping("/wordRule")    
public class WordRuleActionTest extends BaseActionTest {
	@Test
	public void testSave() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/wordRule");
		request.addParameter("name", "方遥侠test");
		request.setMethod("POST");
		// 执行URI对应的action
		final ModelAndView mav = this.excuteAction(request, response);
		// Assert logic
		Assert.assertEquals("redirect:/wordRule", mav.getViewName());
		String msg = (String) request.getAttribute("msg");
		System.out.println(msg);
	}
	@Test
	public void testNew() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/wordRule/new");
		request.setMethod("GET");
		// 执行URI对应的action
		final ModelAndView mav = this.excuteAction(request, response);
		// Assert logic
		Assert.assertEquals("/wordRule/new", mav.getViewName());
		String msg = (String) request.getAttribute("msg");
		System.out.println(msg);
	}
	@Test
	public void testId() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/wordRule/3");
		request.setMethod("GET");
		// 执行URI对应的action
		final ModelAndView mav = this.excuteAction(request, response);
		// Assert logic
		Assert.assertEquals("/wordRule/show", mav.getViewName());
		String msg = (String) request.getAttribute("msg");
		System.out.println(msg);
	}
	
	@Test
	public void testUpdate() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/wordRule/3");
		request.setMethod("PUT");
		request.addParameter("name", "12221方遥侠test");
		request.addParameter("score", "10021");
		// 执行URI对应的action
		final ModelAndView mav = this.excuteAction(request, response);
		// Assert logic
		Assert.assertEquals("redirect:/wordRule", mav.getViewName());
		String msg = (String) request.getAttribute("msg");
		System.out.println(msg);
	}
	@Test
	public void testEdit() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/wordRule/3/edit");
		request.setMethod("GET");
		// 执行URI对应的action
		final ModelAndView mav = this.excuteAction(request, response);
		// Assert logic
		Assert.assertEquals("/wordRule/edit", mav.getViewName());
		String msg = (String) request.getAttribute("msg");
		System.out.println(msg);
	}
//	@Test
//	public void testDel() throws Exception {
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		MockHttpServletResponse response = new MockHttpServletResponse();
//		request.setRequestURI("/wordRule/2");
//		request.setMethod("DELETE");
//		// 执行URI对应的action
//		final ModelAndView mav = this.excuteAction(request, response);
//		// Assert logic
//		Assert.assertEquals("redirect:/list", mav.getViewName());
//		String msg = (String) request.getAttribute("msg");
//		System.out.println(msg);
//	}	
}