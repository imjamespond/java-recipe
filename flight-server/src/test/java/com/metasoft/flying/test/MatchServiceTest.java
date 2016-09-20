package com.metasoft.flying.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.NpcService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserDataService;
import com.metasoft.flying.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"/applicationContext.xml"})  
@ActiveProfiles("home-dev")
public class MatchServiceTest {// extends SpringTest
	@Autowired
	private MatchService matchService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private NpcService npcService;
	@Autowired
	private UserDataService userDataService;
	@Autowired
	private UserService userService;

	@Test
	public void save() throws GeneralException {
		
		staticDataService.init();
		npcService.init();
		matchService.init();
		
		matchService.enroll(19849);
		matchService.enroll(19829);
		matchService.enroll(19830);
		matchService.enroll(19888);
		matchService.test();
		
	}
}
