import java.util.concurrent.TimeUnit;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-8下午4:25
 */
public interface ILockService {

    public boolean lock(String id);

    public void unlock(String id);
}
