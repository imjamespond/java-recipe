package com.qianxun.service;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.UserRankPersist;

@Service
@Transactional
public class UserRankPersistService extends SqlService<UserRankPersist, Long>{
	
}