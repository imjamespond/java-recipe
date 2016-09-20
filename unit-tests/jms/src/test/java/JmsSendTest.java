
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.james.jms.MessageSender;

/**
 * @author cwd
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class JmsSendTest {

	@Autowired
	private MessageSender messageSender;
	
	@Test
	public void testSave() {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String text;

			text = br.readLine();
			while (!text.isEmpty()) {
				System.out.println(String.format("send message: %s", text));
				messageSender.send(text);
				text = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}