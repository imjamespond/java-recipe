package com.james.jetty.utils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
//import org.springframework.security.authentication.encoding.PasswordEncoder;  
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 重写DaoAuthenticationProvider的验证方法 可以在这里扩展验证
 * 
 * @ClassName: LoginAuthenticationManager
 * @Description: TODO
 * @author: wys
 * @date: 2013-9-11
 * 
 */
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

	private static final String SUCCESS = "SUCCESS";

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		if (authentication.getPrincipal() == null
				|| "".equals(authentication.getPrincipal())) {
			logger.debug("-----用户名不能为空！-----");

			throw new BadCredentialsException("-----用户名不能为空！-----");
		}

		if (authentication.getCredentials() == null
				|| "".equals(authentication.getCredentials())) {
			logger.debug("-----密码不能为空！-----");

			throw new BadCredentialsException("-----密码不能为空！-----");
		}

		String presentedPassword = authentication.getCredentials().toString();

		boolean validResult = !getPasswordEncoder().isPasswordValid(
				userDetails.getPassword(), presentedPassword, null);
		if (validResult) {
			logger.debug("---- 用户名或密码错误！-----");
			throw new BadCredentialsException("-----用户名或密码错误！-----");
		}

	}

}