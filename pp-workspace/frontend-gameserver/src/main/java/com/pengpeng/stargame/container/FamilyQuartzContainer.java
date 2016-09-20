package com.pengpeng.stargame.container;

import com.pengpeng.stargame.Broadcast;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.MoneyTreeRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.MoneyTreeHintVO;
import com.pengpeng.stargame.vo.piazza.MoneyTreeReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-6上午11:50
 */
@Component
public class FamilyQuartzContainer {
    private static final Logger logger = Logger.getLogger(FamilyQuartzContainer.class);

    @Autowired
    private ISessionContainer sessionContainer;

    @Autowired
    private Broadcast broadcast;

    @Autowired
    private MoneyTreeRpcRemote moneyTreeRpcRemote;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    @PostConstruct
    private void init(){
        logger.info("开始初始化摇钱树通告");
        try {
            String[] ids = moneyTreeRpcRemote.getFamilyIds(null, new MoneyTreeReq());
            if (ids==null||ids.length<=0){
                logger.error("初始化摇钱树通告没有找到家族:"+ids);
                return ;
            }
            for(String familyId:ids){
                MoneyTreeHintVO vo = moneyTreeRpcRemote.getMoneyTreeHint(null, new MoneyTreeReq(familyId));
                long delay = vo.getDate().getTime() - System.currentTimeMillis();
                executor.schedule(new FamilyRunnable(vo),delay, TimeUnit.MILLISECONDS);
            }
        } catch (GameException e) {
            logger.error("初始化摇钱树通告异常:",e);
        }

    }

    private class FamilyRunnable implements Runnable {
        private MoneyTreeHintVO vo;

        public FamilyRunnable(MoneyTreeHintVO vo) {
            this.vo = vo;
        }

        public void run() {
            logger.debug("开始广播摇钱树通告");
            try {
                String content = vo.getContent();
                logger.debug("广播摇钱树通告:"+content);
                List<Session> session = sessionContainer.getByChannelId(vo.getFamilyId());
                broadcast.broadcast(session, content);
                MoneyTreeHintVO newVO = moneyTreeRpcRemote.getMoneyTreeHint(null, new MoneyTreeReq(vo.getFamilyId()));
                long delay = newVO.getDate().getTime() - System.currentTimeMillis();
                executor.schedule(new FamilyRunnable(newVO),delay, TimeUnit.MILLISECONDS);
            } catch (GameException e) {
                logger.error("广播摇钱树通告异常:",e);
            }
        }
    }

}
