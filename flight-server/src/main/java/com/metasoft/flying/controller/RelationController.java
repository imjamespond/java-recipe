package com.metasoft.flying.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserFollow;
import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.vo.RelationRequest;
import com.metasoft.flying.vo.UserLiteVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class RelationController implements GeneralController {

	@Autowired
	UserService userService;
	@Autowired
	GameRoomService roomService;
	@Autowired
	ChatController chatController;

	@HandlerAnno(name = "好友信息", cmd = "relation.info", req = GeneralRequest.class)
	public List<UserLiteVO> relationInfo(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		Map<Long, UserFollow> map = self.getFollowingMap();
		List<UserLiteVO> list = new ArrayList<UserLiteVO>(4);
		Iterator<Entry<Long, UserFollow>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			UserFollow uf = it.next().getValue();
			if (GeneralConstant.RELATION_FOLLOW != uf.getState()) {
				continue;
			}
			User user = null;
			try {
				user = userService.getAnyUserById(uf.getUserId());

				UserPersist up = user.getUserPersist();
				UserLiteVO vo = new UserLiteVO();
				vo.setUserId(uf.getUserId());
				vo.setName(up.getNickname());
				vo.setCount(uf.getCount());
				if (user.getConn() != null) {
					vo.setOnline(1);
					if (null != user.getGroup()) {
						if (Integer.valueOf(user.getGroup()) == user.getId()) {
							vo.setOnline(2);
						}
					}
				}
				list.add(vo);
			} catch (GeneralException e) {
				it.remove();
				e.printStackTrace();
			}
		}
		return list;
	}

	@HandlerAnno(name = "关注", cmd = "relation.follow", req = RelationRequest.class)
	public void follow(RelationRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		Map<Long, UserFollow> map = user.getFollowingMap();
		UserFollow uf = map.get(req.getId());
		if (uf != null) {
			if (uf.getState() == 0) {
				throw new GeneralException(0, "already.followed");
			}
			uf.setState(0);
		} else {
			// FIXME　validate userId
			// exist ?
			userService.getAnyUserById(req.getId());
			// is self ?
			if (map.size() > 100) {
				throw new GeneralException(0, "followed.limit");
			}
			map.put(req.getId(), new UserFollow(0, GeneralConstant.RELATION_FOLLOW, req.getId()));
		}

		user.updateData = true;
	}

	@HandlerAnno(name = "不关注", cmd = "relation.unfollow", req = RelationRequest.class)
	public void unfollow(RelationRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		Map<Long, UserFollow> map = user.getFollowingMap();
		UserFollow uf = map.get(req.getId());
		if (uf != null) {
			if (uf.getState() == 0) {
				map.remove(req.getId());
			}
			user.updateData = true;
		}
	}

	@HandlerAnno(name = "黑名单信息", cmd = "relation.black.info", req = GeneralRequest.class)
	public List<UserLiteVO> blackInfo(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		Map<Long, UserFollow> map = self.getFollowingMap();
		List<UserLiteVO> list = new ArrayList<UserLiteVO>(4);
		Iterator<Entry<Long, UserFollow>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			UserFollow uf = it.next().getValue();
			if (GeneralConstant.RELATION_BLACK != uf.getState()) {
				continue;
			}
			User user = null;
			try {
				user = userService.getAnyUserById(uf.getUserId());

				UserPersist up = user.getUserPersist();
				UserLiteVO vo = new UserLiteVO();
				vo.setUserId(uf.getUserId());
				vo.setName(up.getNickname());
				// vo.setCount(uf.getCount());
				list.add(vo);
			} catch (GeneralException e) {
				it.remove();
				e.printStackTrace();
			}
		}
		return list;
	}

	@HandlerAnno(name = "黑名单", cmd = "relation.black", req = RelationRequest.class)
	public void black(RelationRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		Map<Long, UserFollow> map = self.getFollowingMap();
		UserFollow uf = map.get(req.getId());
		User user = userService.getAnyUserById(req.getId());
		if (uf != null) {
			if (uf.getState() == GeneralConstant.RELATION_BLACK) {
				throw new GeneralException(0, "already.in.black");
			}
			uf.setState(GeneralConstant.RELATION_BLACK);
		} else {
			uf = new UserFollow(0, GeneralConstant.RELATION_BLACK, req.getId());
			if (map.size() > GeneralConstant.RELATION_LIMIT) {
				throw new GeneralException(0, "black.limit");
			}
			map.put(req.getId(), uf);
		}

		self.updateData = true;

		// 广播踢走
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		if (user.getConn() != null) {
			user.getConn().deliver(GeneralResponse.newObject(VoFactory.getRoomUserVO(user, GeneralConstant.KICK_OUT)));
		}

		// 强制离开
		chatController.leave(user, user.getGroup());
	}

	@HandlerAnno(name = "取消黑名单", cmd = "relation.unblack", req = RelationRequest.class)
	public void unblack(RelationRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		Map<Long, UserFollow> map = user.getFollowingMap();
		UserFollow uf = map.get(req.getId());
		if (uf != null) {
			if (uf.getState() == GeneralConstant.RELATION_BLACK) {
				map.remove(req.getId());
			}
			user.updateData = true;
		}
	}

	public boolean inBlackList(User user, long id) {
		Map<Long, UserFollow> map = user.getFollowingMap();
		UserFollow black = map.get(id);
		if (black != null) {
			if (black.getState() == 1) {
				return true;
			}
		}
		return false;
	}
}
