package com.test.qianxun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.qianxun.model.Comment;
import com.test.qianxun.service.CommentService;
import com.test.qianxun.service.MessageService;
import com.test.qianxun.service.ReplyService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.TopicService;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private TopicService topicService;
	//private double limit = 20;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(Comment comment) {
		long uid = Long.parseLong(sessionService.getUid());
		String username = sessionService.getUsername();
		comment.setUid(uid);
		comment.setUsername(username);
		comment.setCreateTime(System.currentTimeMillis());
		comment.setContent(comment.getContent().replaceAll("\\<.*?\\>", ""));
		long id = commentService.save(comment);
		comment.setId(id);

//		int floor = replyService.get(comment.getRid()).getFloor();
//		int number = (int) Math.ceil(floor / limit);
//				
//		Message message = new Message();
//		long targetUid = 0;
//		if (comment.getCid() != 0) {
//			Comment c = commentService.get(comment.getCid());
//			targetUid = c.getUid();
//		} else {
//			Reply reply = replyService.get(comment.getRid());
//			targetUid = reply.getUid();
//		}
//		if(comment.getUid() != targetUid){
//			message.setDiscription(comment.getUsername() + "评论了我的主题:"
//					+ topicService.get(comment.getTid()).getTitle());
//			message.setContent(comment.getUsername() + "说：" +comment.getContent());
//			message.setUrl("/topic/show/"
//					+ topicService.get(comment.getTid()).getId() + "/" + number
//					+ "#reply_" + comment.getRid());
//			messageService.save(message);
//		}

		return JsonUtils.toJson(comment);
	}
}