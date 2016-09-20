package org.copycat.test;

import org.copycat.framework.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookServiceTest extends SpringTest {
	@Autowired
	private BookService bookService;
	
	@Test
	public void get(){
		Book book = bookService.get(464L);
		System.out.println(book);
	}
}
