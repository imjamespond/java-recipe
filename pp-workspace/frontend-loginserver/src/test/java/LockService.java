import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-8下午4:26
 */
public class LockService implements ILockService {
    Map<String,ReentrantLock> locks = new HashMap<String,ReentrantLock>();
    @Override
    public boolean lock(String channelId) {
        if (channelId==null){
            return false;
        }
        ReentrantLock lock = locks.get(channelId);
        if (lock==null){
            lock = new ReentrantLock();
            locks.put(channelId,lock);
        }
        try {
            boolean flag =  lock.tryLock() || lock.tryLock(100, TimeUnit.MILLISECONDS);
            if (flag){
                System.out.println(Thread.currentThread().getId()+":"+"加锁");
            }else{
                System.out.println(Thread.currentThread().getId()+"------------->未得到锁");
            }
            return flag;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unlock(String channelId) {
        if (channelId==null){
            return ;
        }
        ReentrantLock lock = locks.get(channelId);
        if (lock==null){
            return ;
        }
        if (lock.isHeldByCurrentThread()){
//            System.out.println(Thread.currentThread().getId()+":"+"解锁");
            lock.unlock();
        }
    }


    public static void main(String[] args) throws RemoteException, InterruptedException {
        LockService  service = new LockService();
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setService(service);
        exporter.setServiceInterface(ILockService.class);
        exporter.setRegistryPort(1199);
        exporter.setServiceName("lock");
        exporter.prepare();
        exporter.afterPropertiesSet();


        new LockThread("a",20).start();
        Thread.sleep(10);
//        new LockThreadA("b",10).start();

    }
}
