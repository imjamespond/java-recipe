package com.metasoft.flying.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.data.NpcData;
import com.metasoft.flying.model.data.PveNpcData;
import com.metasoft.flying.util.RandomUtils;

@Service
public class NpcService {

	@Autowired
	private FlightService flightService;
	@Autowired
	private com.metasoft.flying.service.UserService userService;	
	@Autowired
	private com.qianxun.service.WebUserService webUserService;	
	@Autowired
	private UserPersistService userPersistService;	
	@Autowired
	private StaticDataService staticDataService;
	
	public User npc;
	private List<User> npcs = new ArrayList<User>();
	private List<User> pvenpcs = new ArrayList<User>();
	private Map<Long, Integer> npcMatch = new HashMap<Long, Integer>();
	
	//@PostConstruct
	public void init() {
		initNpc();		
	}

	private void initNpc() {
		//String[] players = {"foo","bar","hello","world"};
		
		for(Entry<Integer, NpcData> entry:staticDataService.npcDataMap.entrySet()){
			com.qianxun.model.User webUser = webUserService.findByUsername(entry.getValue().getName());
			long uid = 0;
			if(null==webUser){
				uid = webUserService.register(entry.getValue().getName());				
			}else{
				uid = webUser.getId();
			}
			
			UserPersist up = userPersistService.getByUserName(entry.getValue().getName());
			if(null == up){
				up = new UserPersist(uid, entry.getValue().getName(), "");
				up.setCreatedate(System.currentTimeMillis());
				up.setNickname(entry.getValue().getNickname());
				up.setGender(RandomUtils.nextInt(1, 2));
				userPersistService.save(uid, up);
			}
			//System.out.printf("npcs id:%d name:%s\n",uid,up.getUsername());
			
			try {
				User user = userService.getAnyUserById(uid);
				if(entry.getKey()==1){
					npc = user;
				}else{
					npcs.add(user);
				}
				
			} catch (Exception e) {
				
			}
			
			Collections.shuffle(npcs);
		}
		
		staticDataService.pveNpcNameDataMap = new HashMap<String, Long>();	
		for(Entry<Integer, PveNpcData> entry:staticDataService.pveNpcDataMap.entrySet()){
			com.qianxun.model.User webUser = webUserService.findByUsername(entry.getValue().getName());
			long uid = 0;
			if(null==webUser){
				uid = webUserService.register(entry.getValue().getName());				
			}else{
				uid = webUser.getId();
			}
			staticDataService.pveNpcNameDataMap.put(entry.getValue().getName(), uid);
			
			UserPersist up = userPersistService.getByUserName(entry.getValue().getName());
			if(null == up){
				up = new UserPersist(uid, entry.getValue().getName(), "");
				up.setCreatedate(System.currentTimeMillis());
				up.setNickname(entry.getValue().getNickname());
				up.setGender(RandomUtils.nextInt(1, 2));
				userPersistService.save(uid, up);
			}
			//System.out.printf("pvenpcs id:%d name:%s\n",uid,up.getUsername());
			
			try {
				User user = userService.getAnyUserById(uid);
				pvenpcs.add(user);
			} catch (Exception e) {
				
			}
		}

	}

	public Long[] getPlayers() {
		int random = RandomUtils.nextInt(0,npcs.size());
		int size = random%2+1;
		Long[] userArr = new Long[size];//we need at least one npc
		for(int i=0; i<size; i++){
			userArr[i] = npcs.get((i+random)%npcs.size()).getId();
		}
		return userArr;
	}
	
	public Long getPlayer(int i) {
		return npcs.get(i).getId();
	}

	public Long getOnePlayer() {
		int random = RandomUtils.nextInt(0,npcs.size());
		return npcs.get(random%npcs.size()).getId();
	}
	
	private static AtomicInteger gNext = new AtomicInteger();
	public Long getNextPlayer() {
		int next = gNext.getAndIncrement();
		return npcs.get(next%npcs.size()).getId();
	}

	private static AtomicInteger gMatchNext = new AtomicInteger();
	public void resetMatchNpcPlayer(){
		gMatchNext.set(0);
	}
	public long getMatchNpcPlayer(){
		int next = gMatchNext.getAndIncrement();
		for(int i=next%npcs.size();i<npcs.size();i++){
			User npc = npcs.get(i);
			Integer num = npcMatch.get(npc.getId());
			if(num!=null&&num<GeneralConstant.MATCH_LIMIT){
				npcMatch.put(npc.getId(), num+1);
				return npc.getId();
			}else if(num==null){
				npcMatch.put(npc.getId(), 0);
				return npc.getId();
			}
		}
		
		return getNextPlayer();
	}
}