import com.feign.FeignApp;
import com.feign.SparkClient;
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
    @Autowired
    SparkClient spark;
    @Test
    public void test(){
        try {
            spark.dbTest();
        }catch (Exception e){
            System.out.println("something wrong just happened");
            System.out.println(e.getMessage());
        }

    }
}
