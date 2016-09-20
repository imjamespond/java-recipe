package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 上午11:15
 */
@Desc("家族请求")
public class FamilyReq extends BaseReq {
    @Desc("玩家id")
    private String pid;
    @Desc("家族id")
    private String familyId;
    @Desc("家族信息修改的类型 1修改名字  2QQ群 3YY号  4公告,5搜索查询,6免费欢迎,7游戏币欢迎,8游戏币捐献,9达人币捐献,10变更家族身份(使用memberType传身份值)")
    private int alterType;
    @Desc("修改的内容")
    private String content;
    @Desc("建筑升级的时候传的 建筑的类型")
    private int buildType;
    @Desc("查询的成员列表信息 0 显示所有  1 只显示在线成员列表")
    private int memberType;
    @Desc("请求的页数")
    private int pageNo;
    @Desc("跟alertType配合使用,例如:金额,数量等")
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pId) {
        this.pid = pId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public int getAlterType() {
        return alterType;
    }

    public void setAlterType(int alterType) {
        this.alterType = alterType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBuildType() {
        return buildType;
    }

    public void setBuildType(int buildType) {
        this.buildType = buildType;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
