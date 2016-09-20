package com.test.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Contact;

@Service
@Transactional
public class ContactService extends SqlService<Contact, Long> {
	@Autowired
	private UserService userService;

}