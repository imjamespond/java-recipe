package com.metasoft.flying.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.action.ActionNode;
import com.metasoft.flying.model.action.TakeOffNode;
import com.metasoft.flying.model.action.DispelsNode;
import com.metasoft.flying.model.action.MoveNode;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.common.SpringService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("home-dev")
public class TestAI {
	
	@Test
	public void test() throws GeneralException {
		StaticDataService service = SpringService.getBean(StaticDataService.class);
		service.init();
		GameRoom room = new GameRoom("test");
		Flight flight = new Flight(room);
		//flight.begin();

		// (先检测buff)
		// ==扔色子前
		// ->如果龙卷风在前面10格内(驱散)
		// ->如果外边的棋大于4个(迷雾),最后阶段使用
		// ->如果有可走的棋(加油;*3,*2)

		// (先检测buff)
		// ==npc作弊扔色子
		// ->有可走的棋(主动捡道具,主动吃棋

		// ==扔色子
		// ->扔5,6(如果待飞区有一架飞机不出)出飞机
		// ->预设走棋方式

		ActionNode action = new TakeOffNode();
		ActionNode node = action;
		node.next = new MoveNode();
		node = node.next;
		node.next = new DispelsNode();
		action.act(flight);
	}

}
