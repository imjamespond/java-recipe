package com.qianxun.web;

import java.util.List;

import org.copycat.framework.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.qianxun.service.FlyUserService;
import com.qianxun.service.JobService;
import com.test.qianxun.model.Comment;
import com.test.qianxun.model.Reply;
import com.test.qianxun.model.Topic;
import com.test.qianxun.service.CommentService;
import com.test.qianxun.service.ReplyService;
import com.test.qianxun.service.TopicService;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {
	@Autowired
	private FlyUserService flyUserService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private JobService jobService;
	private int limit = 50;


	@RequestMapping("/topic_top/{id}")
	public String update(@PathVariable long id){
		Topic topic = topicService.get(id);
		if(topic!=null){
			topic.setTop(topic.getTop()==0?1:0);
			topicService.update(topic);
		}
		
		return "redirect:/forum/topic_list";
	}
	
	@RequestMapping("/topic_remove/{id}")
	public String remove(@PathVariable long id) {
		topicService.delete(id);
		return "redirect:/forum/topic_list";
	}
	
	@RequestMapping("/topic_list")
	public String list(Model model) {
		return list(1, model);
	}

	@RequestMapping("/topic_list/{number}")
	public String list(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<Topic> list = topicService.list(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "forum/list";
	}

	@RequestMapping("/reply_remove/{id}")
	public String replyremove(@PathVariable long id) {
		replyService.delete(id);
		return "redirect:/forum/reply_list";
	}
	
	@RequestMapping("/reply_list")
	public String replylist(Model model) {
		return replylist(1, model);
	}

	@RequestMapping("/reply_list/{number}")
	public String replylist(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<Reply> list = replyService.list(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "forum/reply_list";
	}	
	
	@RequestMapping("/comment_remove/{id}")
	public String commentremove(@PathVariable long id) {
		commentService.delete(id);
		return "redirect:/forum/comment_list";
	}
	
	@RequestMapping("/comment_list")
	public String commentlist(Model model) {
		return commentlist(1, model);
	}

	@RequestMapping("/comment_list/{number}")
	public String commentlist(@PathVariable int number, Model model) {
		Page page = new Page(number, limit);
		List<Comment> list = commentService.list(page);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		return "forum/comment_list";
	}	
	
}