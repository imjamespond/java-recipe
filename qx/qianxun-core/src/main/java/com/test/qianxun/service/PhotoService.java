package com.test.qianxun.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.qianxun.model.Photo;
@Deprecated
@Service
@Transactional
public class PhotoService extends SqlService<Photo, Long> {
	public int countBySid(long sid) {
		String sql = "select count(*) from photo where sid = ?";
		return sqlTemplate.queryForInt(sql, sid);
	}

	public List<Photo> listBySid(long sid) {
		String sql = "select * from photo where sid = ?";
		return sqlTemplate.queryForList(sql, Photo.class, sid);
	}
}