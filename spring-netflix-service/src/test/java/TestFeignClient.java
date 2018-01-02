import com.feign.FeignApp;
import com.feign.StoreClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FeignApp.class)
@ActiveProfiles("feign")
public class TestFeignClient {

    @Autowired
    StoreClient store;

    @Test
    public void test(){
        System.out.println(store.getStores());
    }
}
