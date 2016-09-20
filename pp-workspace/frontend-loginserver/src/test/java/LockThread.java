import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-10下午2:36
 */
public class LockThread extends Thread {

    private String name = "a";
    private long time;
    private ILockService service;

    public LockThread(String name ,long time){
        this.name = name;
        this.time = time;
        RmiProxyFactoryBean proxy1 = new RmiProxyFactoryBean();
        proxy1.setServiceUrl("rmi://192.168.10.124:1199/lock");
        proxy1.setServiceInterface(ILockService.class);
        proxy1.afterPropertiesSet();
        service = (ILockService)proxy1.getObject();
    }

    @Override
    public void run() {
        while(true){

            if (!service.lock("a")){
                 continue;
            }
            System.out.println(name+":"+"加锁");
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.unlock("a");
            System.out.println(name+":"+"解锁");
        }

    }
}
