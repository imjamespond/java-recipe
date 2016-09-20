package org.copycat.test;

import org.copycat.framework.SpringTest;
import org.copycat.framework.nosql.NosqlTemplate;
import org.copycat.framework.nosql.Query;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NosqlTemplateTest extends SpringTest {
	@Autowired
	private NosqlTemplate nosqlTemplate;
	
	@Test
	public void query(){
		Query<User> query = nosqlTemplate.query(User.class);
		User user = query.where("uname").is("aaa").get();
		System.out.println(user.getPassword());
	}
}
