package com.metasoft.flying.test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserMatch;
import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserDataService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.vo.ChessScoreVO;
//test branch
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"/applicationContext.xml"})  
@ActiveProfiles("home-dev")
public class UserServiceTest {// extends SpringTest
	@Autowired
	private UserPersistService userPersistService;
	@Autowired
	private UserDataService userDataService;
	@Autowired
	private UserService userService;
	@Autowired
	private org.apache.tomcat.jdbc.pool.DataSource dataSource;
	//private NamedParameterJdbcTemplate jdbcTemplate;
	//@Autowired
	//private UserRankPersistService userRankPersistenceService;
	//@Autowired
	//private FlightMatch fm;
	@Test
	public void save() throws GeneralException {
/*		UserPersist user = new UserPersist();
		user.setId(1234l);
		user.setUsername("metasoft");
		user.setEmail("test@test.com");
		user.setRelationship(new byte[2]);
		user.setFashion(new byte[0]);
		user.setItem(new byte[0]);
		//userService.save(user);
		*/
		UserPersist up = userPersistService.getByName("qq");
		User user = userService.getAnyUserById(up.getId());
		Deque<UserMatch> list = user.getmatchDeq();
		
		UserMatch um = new UserMatch();
		List<ChessScoreVO> scores = new ArrayList<ChessScoreVO>(4);
		ChessScoreVO vo = new ChessScoreVO();
		scores.add(vo);
		um.setScores(scores);
		um.setMtime(System.currentTimeMillis());
		um.setDuration(123);
		um.setWin(0);
		list.add(um);
		userDataService.update(user.getUserDataObj());
	}
}
