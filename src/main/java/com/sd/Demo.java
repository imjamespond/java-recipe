package com.sd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
@RestController
@EnableSwagger2
public class Demo {

	@GetMapping(path="/user/getName")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String getDemo(@RequestParam(value="name", defaultValue="no name") String name,
						  Authentication authentication) throws JsonProcessingException {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		System.out.println(new ObjectMapper().writeValueAsString(authorities));
		return name;
	}

	@GetMapping(path="/user/list")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ApiOperation(value = "示例")
	@ApiResponses(
			@ApiResponse(code = 200, message = "字段1 column1,字段2 column2,字段3 column3,字段1 column1,字段2 column2,字段3 column3,字段1 column1,字段2 column2,字段3 column3,字段1 column1,字段2 column2,字段3 column3,")
	)
	public List<Foobar> list(@RequestParam(value="name", defaultValue="no name") String name ) throws JsonProcessingException {
		return null;
	}

	@GetMapping(path="/logout/successfully")
	public String logout() throws JsonProcessingException {
		return "logout/successfully";
	}

	//param: remember-me true

	public static void main(String[] args) {
		SpringApplication.run(Demo.class, args);
	}
	
}

class Foobar {
	String foo;
	int bar;

	public String getFoo() {
		return foo;
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public int getBar() {
		return bar;
	}

	public void setBar(int bar) {
		this.bar = bar;
	}
}