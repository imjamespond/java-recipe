package com.pengpeng.asy;

import com.pengpeng.stargame.datasave.AsyDataDaoImpl;
import com.pengpeng.stargame.datasave.IAsyDataDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: mql
 * Date: 14-1-14
 * Time: 下午4:39
 */
public class Asy {
    private static int THREAD_NUM=10;
    private static ExecutorService service = Executors.newFixedThreadPool(THREAD_NUM);
    private static final Logger logger = Logger.getLogger("asylog");
    private IAsyDataDao dataDao;
    private AtomicLong count = new AtomicLong();
    public IAsyDataDao getDataDao() {
        return dataDao;
    }

    public void setDataDao(IAsyDataDao dataDao) {
        this.dataDao = dataDao;
    }

    @PostConstruct
    public void save(){
        for(int i=0;i<THREAD_NUM;i++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String key=dataDao.pop();
                        if(key!=null&&!key.equals("")){
                            try{
                                count.incrementAndGet();
                                dataDao.reidsToDb(key);
                                logger.info(String.format("surplus num：%d",dataDao.size()));
//                                System.out.println(String.format("当前队列数量：%d",dataDao.size()));
                            }catch (Exception e){
                                e.printStackTrace();
                                logger.info(String.format("保存异常...线程:%s, key:%s",Thread.currentThread().getName(),key));
                                dataDao.put(key);
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            long all = count.get();
                            logger.info(String.format("queue empy . all:%d",count.get()));
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }


}
