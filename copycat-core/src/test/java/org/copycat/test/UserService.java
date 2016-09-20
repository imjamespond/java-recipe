package org.copycat.test;

import org.copycat.framework.nosql.NosqlService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends NosqlService<User> {

	public void getUser() {
		User user = query().where("uname").is("aaa").get();
		System.out.println(user.getPassword());
	}
}
