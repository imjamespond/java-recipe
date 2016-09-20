package org.copycat.test;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

@Service("bookService")
public class BookService extends SqlService<Book, Long> {

}
