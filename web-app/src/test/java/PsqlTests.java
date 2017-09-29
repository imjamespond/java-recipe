

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.metasoft.boot.AppServletInitializer;
import com.metasoft.service.dao.FoobarDaoService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServletInitializer.class)
public class PsqlTests {
	@Autowired
	FoobarDaoService fs;
	
    @Test
    public void exampleTest() {
    	assertNotNull(fs);
    }

}