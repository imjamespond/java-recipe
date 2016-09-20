package com.qianxun.model;

import org.springframework.beans.BeanUtils;

import com.qianxun.model.job.IJob;
import com.qianxun.service.SpringService;
import com.test.qianxun.model.Exchange;
import com.test.qianxun.service.ExchangeService;

public class ExchangeJob extends IJob{
	public Exchange ex;
	public ExchangeJob(Exchange ex) {
		this.ex = ex;
	}

	@Override
	public void doJob() {
		Exchange e = new Exchange();
		BeanUtils.copyProperties(ex, e);
		long elapsed = System.currentTimeMillis()-ex.getTime();
		e.setRemark("remark time elapsed:"+elapsed);
		e.setTime(System.currentTimeMillis());
		
		ExchangeService exService = SpringService.getBean(ExchangeService.class);
		exService.saveWithTransaction(e);
	}

}
