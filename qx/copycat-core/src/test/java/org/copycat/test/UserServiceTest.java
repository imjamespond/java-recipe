package org.copycat.test;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends SpringTest {
	@Autowired
	private UserService userSerive;

	@Test
	public void get() {
		String id = "52b001e09436ea35351d719e";
		User user = userSerive.get(id);
//		System.out.println(user.getPassword());
	}

	@Test
	public void create() {
		User user = new User();
		user.setUsername("aaa");
		user.setPassword("123");
		user.setTime(System.currentTimeMillis());
		String id = userSerive.save(user);
//		System.out.println(id);
	}

	@Test
	public void update() {
		String id = "52b001e09436ea35351d719e";
		User user = new User();
		user.setId(id);
		user.setUsername("aaaaaa");
		user.setPassword("123222");
		user.setTime(System.currentTimeMillis());
		userSerive.update(user);
	}

	@Test
	public void list() {
		Page page = new Page(1, 3);
		List<User> userList = userSerive.list(page);
		for (User user : userList) {
//			System.out.println(user.getId());
		}
	}
}
