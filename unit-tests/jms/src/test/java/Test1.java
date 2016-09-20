import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.james.jms.MessageSender;



    @RunWith(SpringJUnit4ClassRunner.class)  
    @ContextConfiguration("/applicationContext.xml")  
    public class Test1 {  
       
        @Autowired  
        private MessageSender producerService;  
        //@Autowired  
        //@Qualifier("sessionAwareQueue")  
        //private Destination sessionAwareQueue;  
          
        @Test  
        public void testSessionAwareMessageListener() {  
            producerService.send("测试SessionAwareMessageListener");  
        }  
          
    }  