package com.pengpeng.stargame.vo.piazza.collectcrop;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午6:20
 */
@Desc("玩家收集VO")
public class MemberCollectVO {
    @Desc("玩家Id")
    private String pid;
    @Desc("玩家名字")
    private String pname;
    @Desc("贡献数量")
    private int num;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
