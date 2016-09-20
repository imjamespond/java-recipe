package com.metasoft.flying.service;

import java.util.List;

import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.metasoft.flying.model.UserDataPersist;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserDataService extends SqlService<UserDataPersist, Long> {
	//private static final Logger logger =  LoggerFactory.getLogger(UserDataService.class);
	private static final String TABLE = UserDataPersist.class.getAnnotation(Table.class).value();
	private static final String SQL_SEL_CONTR = String.format(
			"select * from %s WHERE contribute>0 ORDER BY contribute DESC LIMIT ? OFFSET ?", TABLE);
	private static final String SQL_SEL_SCORE = String.format(
			"select * from %s WHERE score>0 ORDER BY score DESC LIMIT ? OFFSET ?", TABLE);
	private static final String SQL_RANK_PVE = String.format(
			"select * from %s WHERE pvetime>0 ORDER BY pve DESC, pvetime DESC LIMIT ? OFFSET ?", TABLE);
	private static final String SQL_RANK_GOLD = String.format(
			"select * from %s WHERE gold>0 ORDER BY gold DESC LIMIT ? OFFSET ?", TABLE);
	
	public List<UserDataPersist> getByContributeDesc(int offset, int size) {
		Object[] objs = { size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_CONTR, UserDataPersist.class, objs);
	}
	public List<UserDataPersist> getByScoreDesc(int offset, int size) {
		Object[] objs = { size, offset };
		return this.sqlTemplate.queryForList(SQL_SEL_SCORE, UserDataPersist.class, objs);
	}
	public List<UserDataPersist> getByPveRank(int offset, int size) {
		Object[] objs = { size, offset };
		return this.sqlTemplate.queryForList(SQL_RANK_PVE, UserDataPersist.class, objs);
	}	
	public List<UserDataPersist> getByGoldRank(int offset, int size) {
		Object[] objs = { size, offset };
		return this.sqlTemplate.queryForList(SQL_RANK_GOLD, UserDataPersist.class, objs);
	}
}
