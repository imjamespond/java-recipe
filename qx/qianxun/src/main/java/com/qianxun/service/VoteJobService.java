package com.qianxun.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianxun.model.Constant;
import com.qianxun.model.job.VoteJob;
import com.test.qianxun.model.VoteLite;
import com.test.qianxun.service.GameVoteService;
import com.test.qianxun.service.VoteLiteService;
import com.test.qianxun.util.EpochUtil;
import com.test.qianxun.util.JsonUtils;

@Service
public class VoteJobService {
	//private static final Logger logger = LoggerFactory.getLogger(JobService.class);
	@Autowired
	private GameVoteService gvService;
	@Autowired
	private VoteLiteService vlService;
	private BlockingQueue<VoteJob> jobQueue;
	public static final int VOTE_SIZE = 24;
	
	VoteJobService(){
		jobQueue = new LinkedBlockingQueue<VoteJob>(Constant.MAX_VOTE_JOB);
	}
	
	@PostConstruct
	public void init(){
		Consumer c1 = new Consumer(jobQueue);
		Thread thread1 = new Thread(c1);
		thread1.setName("VoteJobService-Thread1");
		thread1.start();
	}
	
	public void produce(VoteJob job){
		try {
			jobQueue.put(job);
		} catch (InterruptedException e) {
			
		}
	}
class Consumer implements Runnable {
	private final BlockingQueue<VoteJob> queue;

	Consumer(BlockingQueue<VoteJob> q) {
		queue = q;
	}

	//run in single-thread
	public void run() {
		try {
			long now = System.currentTimeMillis();
			while (true) {
				VoteJob vj=queue.take();
				//vote limitation
				VoteLite vote = vlService.get(vj.getUid());
				if(null==vote){
					vote=new VoteLite(vj.getUid());
					vlService.save(vj.getUid(), vote);
				}
				//reset vote token
				int week = EpochUtil.getEpochWeek(now);
				long voteArr[] = null;
				if(week!=vote.getEpochweek()){
					//vote.setVote(0);
					voteArr = new long[VOTE_SIZE];
				}else{
					try {
						voteArr = JsonUtils.toObject(long[].class, vote.getVote());
					} catch (Exception e) {
						voteArr = new long[VOTE_SIZE];
					}
				}
				//投票位标记
//				int voteToken = 1<<vj.getType() * vj.getRank();
//				if((voteToken & vote.getVote()) > 0){
//					continue;
//				}
				//vote.setVote(vote.getVote() | voteToken);
				int voteToken = vj.getType() * 8 + vj.getRank();
				if(voteToken < VOTE_SIZE ){
					if(voteArr[voteToken] > 0){
						continue;
					}
					voteArr[voteToken] = vj.getGid();
				}
				vote.setVote(JsonUtils.toJson(voteArr));
				vote.setEpochweek(week);
				vlService.update(vote);
				gvService.addVote(vj);
				//FIXME just for debugging
				//Thread.sleep(1000l);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}	
}

