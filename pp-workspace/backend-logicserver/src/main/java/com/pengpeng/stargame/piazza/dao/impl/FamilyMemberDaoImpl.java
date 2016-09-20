package com.pengpeng.stargame.piazza.dao.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.AnnotationUtils;
import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDB;
import com.pengpeng.stargame.dao.RedisZSetDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午3:25
 */
@Component
@DaoAnnotation(prefix = "pza.member.")
public class FamilyMemberDaoImpl extends RedisZSetDao implements IFamilyMemberDao {

}