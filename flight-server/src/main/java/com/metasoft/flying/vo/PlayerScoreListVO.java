package com.metasoft.flying.vo;

import java.util.ArrayList;
import java.util.List;

import com.metasoft.flying.model.IPage;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserFollow;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.SpringService;

@DescAnno("排行信息")
public class PlayerScoreListVO implements IPage<PlayerScoreVO> {
	@DescAnno("总页数")
	private int amount;
	@DescAnno("当前页数")
	private int page;
	@DescAnno("我的排名")
	private int myRank;
	@DescAnno("排行列表")
	private List<PlayerScoreVO> list;

	public PlayerScoreListVO(int amount, int page, List<PlayerScoreVO> list) {
		super();
		this.amount = amount;
		this.page = page;
		this.list = list;
	}

	public PlayerScoreListVO() {
		this.list = new ArrayList<PlayerScoreVO>();
	}


	public List<PlayerScoreVO> getList() {
		return list;
	}

	public void setList(List<PlayerScoreVO> list) {
		this.list = list;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getMyRank() {
		return myRank;
	}

	public void setMyRank(int myRank) {
		this.myRank = myRank;
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public void setPage(int amount, int offset) {
		this.amount = amount;
		this.page = offset;
	}

	@Override
	public void addItem(PlayerScoreVO item) {
		PlayerScoreVO vo = new PlayerScoreVO();
		// 取在线玩家
		//FIXME is there a better approach
		UserService us = SpringService.getBean(UserService.class);
		User user = us.getOnlineUserById(item.getUserId());

		if (null != user) {
			vo.setOnline(1);
			if (null != user.getGroup()) {
				if (Integer.valueOf(user.getGroup()) == user.getId()) {
					vo.setOnline(2);
				}
			}
		}

		vo.setUserId(item.getUserId());
		vo.setUserName(item.getUserName());
		vo.setScore(item.getScore());
		// 是否关注
		User self;
		try {
			self = us.getRequestUser();
			UserFollow uf = self.getFollowingMap().get(item.getUserId());
			if (uf != null) {
				if (uf.getState() == 0) {
					vo.setFollow(1);
				}
			}
		} catch (GeneralException e) {
		}

		list.add(item);
	}

}
