import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.empire.model.UserPersist;
import com.metasoft.empire.service.UserPersistService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("home-dev")
public class DBTest {
	static private Logger log = org.slf4j.LoggerFactory.getLogger(DBTest.class);
			
	@Autowired
	private UserPersistService userPersistService;

	@Test
	public void serverTest() throws Exception {
		long before = System.currentTimeMillis();
		
		UserPersist user = new UserPersist();
		user.setUsername("metasoft");
		user.setEmail("test@test.com");
		userPersistService.save(user);

		
		for(UserPersist up: userPersistService.list()){
			log.debug("user id:{}, name:{}", up.getId(), up.getUsername());
		}
		
		System.out.printf("running time:%d\n",System.currentTimeMillis()-before);
	}
}
