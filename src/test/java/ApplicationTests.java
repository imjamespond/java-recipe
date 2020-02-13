import com.sd.Demo;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Demo.class)
@AutoConfigureMockMvc
public class ApplicationTests {
	@Autowired
	private MockMvc mvc;


	@Test
	public void getNames() throws Exception
	{
//		mvc.perform(post("/login")
//				.param("password", DigestUtils.md5Hex("1212")).param("username","root"))
//				.andDo(print());
		 mvc.perform(get("/user/getName")
				 .with(user("root").password(DigestUtils.md5Hex("1212")).roles("ADMIN")))
				 .andDo(print());
	}


}
