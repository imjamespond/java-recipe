import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-8下午4:26
 */
public class TakeService implements ILockService {
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    private AtomicLong lock = new AtomicLong();
    @Override
    public boolean lock(String channelId) {
        if (channelId==null){
            return false;
        }
        if (lock==null){
            lock = new AtomicLong();
        }
            return false;
    }

    @Override
    public void unlock(String channelId) {
        if (channelId==null){
            return ;
        }
    }


    public static void main(String[] args) throws RemoteException {
        TakeService service = new TakeService();
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setService(service);
        exporter.setServiceInterface(ILockService.class);
        exporter.setRegistryPort(1199);
        exporter.setServiceName("lock");
        exporter.afterPropertiesSet();


        new LockThread("a",10).start();
        new LockThread("b",11).start();

    }
}
