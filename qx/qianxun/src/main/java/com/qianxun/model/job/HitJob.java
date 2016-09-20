package com.qianxun.model.job;

import com.qianxun.service.SpringService;
import com.test.qianxun.service.TopicService;


public class HitJob extends IJob {
	private long id;
	
	public HitJob( long id){
		this.id = id;
	}
	
	@Override
	public void doJob() {
		TopicService topicService = SpringService.getBean(TopicService.class);
		topicService.increaseRead(id);
	}

}
