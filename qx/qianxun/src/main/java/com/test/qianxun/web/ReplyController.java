package com.test.qianxun.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.qianxun.model.Message;
import com.test.qianxun.model.Reply;
import com.test.qianxun.model.Topic;
import com.test.qianxun.service.MessageService;
import com.test.qianxun.service.ReplyService;
import com.test.qianxun.service.SessionService;
import com.test.qianxun.service.TopicService;
import com.test.qianxun.util.JsonUtils;

@Controller
@RequestMapping(value = "/reply")
public class ReplyController {
	@Autowired
	private ReplyService replyService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private MessageService messageService;
	private double limit = 20;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(Reply reply) {
		long uid = Long.parseLong(sessionService.getUid());
		String username = sessionService.getUsername();
		topicService.increaseReply(reply.getTid(), sessionService.get()
				.getUsername());
		int floor = replyService.countByTid(reply.getTid()) + 1;
		reply.setFloor(floor);
		reply.setCreateTime(System.currentTimeMillis());
		reply.setUid(uid);
		reply.setUsername(username);
		long id = replyService.save(reply);
		reply.setId(id);

		int number = (int) Math.ceil(floor / limit);
		Topic topic = topicService.get(reply.getTid());
		if(topic.getUid() != reply.getUid()){
			Message message = new Message();
			message.setUid(topic.getUid());
			message.setDiscription(reply.getUsername() + "回复了我的主题:"
					+ topic.getTitle());
			message.setContent(reply.getUsername() + "说：" + reply.getContent());
			message.setUrl("/topic/show/" + topic.getId() + "/" + number
					+ "#reply_" + reply.getId());
			messageService.save(message);
		}

		return JsonUtils.toJson(reply);
	}
}