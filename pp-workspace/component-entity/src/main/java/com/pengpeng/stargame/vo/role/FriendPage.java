package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-25 下午2:26
 */
@Desc("好友翻页")
public class FriendPage {

	@Desc("起始页")
	private Integer pageNo; // 起始页 从1开始
	@Desc("最大页数")
	private Integer maxPage; // 最大页数

	@Desc("好友集合")
	private FriendVO [] friends;

	public FriendVO[] getFriends() {
		return friends;
	}

	public void setFriends(FriendVO[] friends) {
		this.friends = friends;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(Integer maxPage) {
		this.maxPage = maxPage;
	}

	public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
	}
}
