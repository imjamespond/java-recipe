package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

@Desc("角色信息请求")
public class RoleReq extends BaseReq {

    @Desc("角色类型,角色创建的时候指定")
	private int roleType;
    @Desc("名称:创建帐号,修改角色名")
	private String name;
    @Desc("密码")
    private String pwd;
    @Desc("再次输入密码")
    private String pwd2;

	public int getRoleType() {
		return roleType;
	}
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }
}
