package org.copycat.test;

import org.copycat.framework.SpringTest;
import org.copycat.framework.sql.SqlTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SqlTemplateTest extends SpringTest {
	@Autowired
	private SqlTemplate sqlTemplate;

	@Test
	public void get() {

		Book book = sqlTemplate.get(Book.class, 464);
		System.out.println(book);
	}

	// @Test
	public void save() {
		Book book = new Book();
		book.setTitle("python in action");
		book.setAuthor("John");
		long id = sqlTemplate.save(book);
		System.out.println(id);

	}

	// @Test
	public void update() {
		Book book = sqlTemplate.get(Book.class, 121);
		book.setTitle("java in action");
		book.setAuthor("wayne");
		sqlTemplate.update(book);
	}

	// @Test
	public void delete() {
		sqlTemplate.delete(Book.class, 1L);
	}

}
