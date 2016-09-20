package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"/applicationContext.xml"})  
public class JdbcTest {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	private static final int[] TYPE_UPDATE = new int[] {java.sql.Types.INTEGER, java.sql.Types.BIGINT};
	private static final String SQL_UPDATE = "update users set gems=gems+? where id = ?";

	private static Logger logger = LoggerFactory.getLogger(JdbcTest.class);
	@Test
	public void serverTest(){
		
		int gems = 8;
		Object[] params = new Object[]{gems, 128l};
		
		try {
			jdbcTemplate.update(SQL_UPDATE, params, TYPE_UPDATE);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
		}
		
		System.out.println("Finish Test");
	}
	
	
}
