package com.sd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@SpringBootApplication
@RestController
public class Demo {
	
	@GetMapping(path="/user/getName")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String getDemo(@RequestParam(value="name", defaultValue="no name") String name,
						  Authentication authentication) throws JsonProcessingException {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		System.out.println(new ObjectMapper().writeValueAsString(authorities));
		return name;
	}

	public static void main(String[] args) {
		SpringApplication.run(Demo.class, args);
	}
	
}
