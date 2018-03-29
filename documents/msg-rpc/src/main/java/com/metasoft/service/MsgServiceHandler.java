package com.metasoft.service;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metasoft.messageclient.MessageInfoWebService;
import com.metasoft.messageclient.MessageInfoWebServiceService;
import com.metasoft.thrift.MsgFinished;
import com.metasoft.thrift.MsgService;
import com.metasoft.thrift.MsgStruct;
import com.metasoft.thrift.MsgToProcess;

@Service
public class MsgServiceHandler implements MsgService.Iface {
	static Logger log = LoggerFactory.getLogger(MsgServiceHandler.class);

	@Autowired
	JobQueue queue;
	
	@Autowired
	LocalizationService localService;
	
	@Override
	public void send(final MsgStruct msg) throws TException {
		queue.produce(new IJob(){
			@Override
			public void doJob() {
				try {
					Thread.sleep(3000l);
					log.debug(msg.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public void sendMsgToProcess(final MsgToProcess tfmsg) throws TException {
		queue.produce(new IJob(){
			@Override
			public void doJob() {
				String[] strs = {tfmsg.getAdmin(),tfmsg.getStatus(),tfmsg.getSubmituser(),tfmsg.getTitle(), tfmsg.getApplid()};
				String msg = localService.getLocalString("application.to.process", strs);
				log.debug(msg);
				sendMsg(tfmsg.getAdmin(), msg);
			}
		});
	}
	@Override
	public void sendMsgFinished(final MsgFinished tfmsg) throws TException {
		queue.produce(new IJob(){
			@Override
			public void doJob() {
				String[] strs = {tfmsg.getUser(), tfmsg.getTitle(), tfmsg.getApplid()};
				String msg = localService.getLocalString("application.finished", strs);
				log.debug(msg);
				sendMsg(tfmsg.getUser(), msg);
			}
		});
	}
	
	void sendMsg(String userId, String msg){
		MessageInfoWebServiceService serviceservice = new MessageInfoWebServiceService();
		MessageInfoWebService service = serviceservice.getMessageInfoWebServicePort();
		service.saveMessageInfo(userId, msg, "BIStore");
	}
}