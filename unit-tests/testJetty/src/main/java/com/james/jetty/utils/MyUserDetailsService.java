package com.james.jetty.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.james.jetty.model.Users;
import com.james.jetty.vo.MyResult;

@Component
public class MyUserDetailsService implements UserDetailsService {
	
	//should be puted in cached
	public static Map<String,Long> userMap = new ConcurrentHashMap<String,Long>();

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// Retrieve the user from wherever you store it, e.g. a database
		List<Users> users = Users.getByName(username);
		if (users.size() == 0) {
			throw new UsernameNotFoundException("Invalid username/password.");
		}
		userMap.put(username, users.get(0).getId());
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils
				.createAuthorityList("ROLE_DIRECTOR", "ROLE_ADMIN");
		return new User(username, users.get(0).getPassword(), authorities);
	}

}