package com.metasoft.flying.test;

import java.util.List;

import org.jboss.netty.channel.ChannelFuture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.metasoft.flying.controller.ItemController;
import com.metasoft.flying.controller.LoginController;
import com.metasoft.flying.controller.RelationController;
import com.metasoft.flying.controller.StaticDataController;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.vo.DressRequest;
import com.metasoft.flying.vo.ItemRequest;
import com.metasoft.flying.vo.LoginRequest;
import com.metasoft.flying.vo.RelationRequest;
import com.metasoft.flying.vo.UserGiftVO;
import com.metasoft.flying.vo.general.GeneralResponse;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"/applicationContext.xml"})  
@ActiveProfiles("development")
public class ControllerTest {
	
	@Autowired
	private LoginController loginCtr;
	
	@Autowired
	private RelationController relationCtr;
	
	@Autowired
	private ItemController itemCtr;
	@Autowired
	private StaticDataController staticDataCtr;
	@Autowired
	private LocalizationService localService;
	
	@Autowired
	UserPersistService userPersistService;
	@Autowired
	UserService userService;	
	@Test
	public void serverTest() throws GeneralException, InterruptedException{
		
		//ChatVO serverInfo = loginCtr.serverInfo(null);
		BaseConnection conn = new ConnectionTest();
		RequestUtils.setCurrentConn(null);
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setName("james");	
		loginCtr.login(loginRequest );
		
		User self = userService.getRequestUser();
		self.setGroup("test");
			
/*		RelationRequest relationRequest = new RelationRequest();
		relationRequest.setId(conn.getUserId());
		//relationCtr.follow(relationRequest);
		relationCtr.black(relationRequest);
		relationCtr.relationInfo(null);
		
		DressRequest dressRequest = new DressRequest();
		dressRequest.setUserId(conn.getUserId());
		dressRequest.setItemId(120001);
		dressRequest.setPos(2);
		//itemCtr.dressUp(dressRequest);
		//itemCtr.dressInfo(dressRequest);
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setItemId(120001);
		itemRequest.setNum(1);
		itemCtr.buy(itemRequest);
		itemCtr.itemInfo(null);
		self.addGems(88, "dummy");*/
		
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setItemId(120001);
		itemRequest.setNum(1);
		itemRequest.setUserId(self.getId());
		
		itemCtr.buyNGive(itemRequest);
		//List<UserGiftVO> list = itemCtr.gift(null);System.out.println(list);
		//userPersistService.update(self.getUserPersistObj());
		itemCtr.itemNum(itemRequest);
		
		staticDataCtr.item(null);
		System.out.println(localService.getLocalString("chess.room.give",new String[]{"james","呵呵",String.valueOf(100)}));
		
		System.out.println("Finish Test");
	}
	
	class ConnectionTest extends BaseConnection{

		@Override
		public String getIpAddress() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ChannelFuture send(Object response) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void deliver(Object response) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendAndClose(Object response) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isConnected() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
