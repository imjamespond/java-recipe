package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-22 下午4:50
 */
@Desc("好友信息")
public class FriendVO {

	@Desc("friendId,Friend主键")
	private String id;

	@Desc("网站用户ID")
	private Integer userId;
	@Desc("昵称")
	private String nickName;
	@Desc("性别")
	private Integer sex;
    @Desc("七天未登陆")
    private Integer inactive;
    @Desc("置顶")
    private Integer top;

	@Desc("用户主站头像")
	private String portrait;// 用户主站头像

//    @Desc("玩家农田状态")
//    private String state;

    public FriendVO() {
    }

    public FriendVO(String id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

    public Integer getInactive() {
        return inactive;
    }

    public void setInactive(Integer inactive) {
        this.inactive = inactive;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }

    public String toString(){
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE, true, true);
	}
}
