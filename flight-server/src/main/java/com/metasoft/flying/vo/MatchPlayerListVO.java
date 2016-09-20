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
public class MatchPlayerListVO implements IPage<MatchPlayerVO> {
	@DescAnno("总页数")
	private int amount;
	@DescAnno("当前页数")
	private int page;
	@DescAnno("我的排名")
	private int myRank;
	@DescAnno("我的分数")
	private int myScore;
	@DescAnno("排行列表")
	private List<MatchPlayerVO> list;

	public MatchPlayerListVO(int amount, int page, List<MatchPlayerVO> list) {
		super();
		this.amount = amount;
		this.page = page;
		this.list = list;
	}

	public MatchPlayerListVO() {
		this.list = new ArrayList<MatchPlayerVO>();
	}


	public List<MatchPlayerVO> getList() {
		return list;
	}

	public void setList(List<MatchPlayerVO> list) {
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

	public int getMyScore() {
		return myScore;
	}

	public void setMyScore(int myScore) {
		this.myScore = myScore;
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
	public void addItem(MatchPlayerVO item) {
		MatchPlayerVO vo = new MatchPlayerVO();
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
