import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.empire.utils.RsaUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("home-dev")
public class RsaTest {

	@Test
	public void serverTest() throws Exception {
		long before = System.currentTimeMillis();
		RsaUtils.main(null);
		System.out.printf("running time:%d\n",System.currentTimeMillis()-before);
	}
}
