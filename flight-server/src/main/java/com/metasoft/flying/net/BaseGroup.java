package com.metasoft.flying.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.flying.model.User;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.RoomUserVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public abstract class BaseGroup extends DefaultChannelGroup {
	private static final Logger logger = LoggerFactory.getLogger(BaseGroup.class);

	private ConcurrentMap<Long, Boolean> userIdMap = new ConcurrentHashMap<Long, Boolean>();

	public BaseGroup(String name) {
		super(name);
	}

	public void addUser(User user) {
		BaseConnection conn = user.getConn();
		if (null!=conn && user.getId() > 0) {
			add(((NettyConnection) conn).getBindChannel());
			userIdMap.put(user.getId(), true);
			user.setGroup(getName());
		}
	}

	public void removeUser(User user) {
		BaseConnection conn = user.getConn();
		if (null!=conn && user.getId() > 0) {
			remove(((NettyConnection) conn).getBindChannel());
			userIdMap.remove(user.getId());
			//user.setGroup(null);
		}
	}

	public void broadcast(GeneralResponse response) {
		logger.debug("broadcast:{}" , response.getCode());
		write(response);
	}

	public List<RoomUserVO> getMemberList() {
		UserService userService = SpringService.getBean(UserService.class);
		List<RoomUserVO> listVO = new ArrayList<RoomUserVO>();
		for (Entry<Long, Boolean> entry : userIdMap.entrySet()) {
			Long userId = entry.getKey();
			User user = userService.getOnlineUserById(userId);
			if (user == null) {
				continue;
			}
			RoomUserVO vo = new RoomUserVO();
			vo.setUserId(userId);
			vo.setUserName(user.getUserPersist().getNickname());
			vo.setState(-1);
			//vo.setContribute(user.getUserPersist().getContribute());
			//vo.setScore(user.getUserPersist().getScore());
			listVO.add(vo);
		}
		return listVO;
	}
	
	public Set<Entry<Long, Boolean>> getEntrySet() {
		return userIdMap.entrySet();
	}

	public boolean containMember(long userId) {
		return userIdMap.containsKey(userId);
	}

	public int groupSize() {
		return userIdMap.size();
	}

	public String toString() {
		return super.toString() + " name:" + getName();
	}
	
	//public abstract int getState();
}