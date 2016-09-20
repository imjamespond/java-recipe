package com.pengpeng.stargame.vo.piazza.collectcrop;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午6:19
 */
@Desc("家族收集排行VO")
public class MemberColletPageVO {
    @Desc("当前页")
    private int page;
    @Desc("收集作物的名字")
    private String name;
    @Desc("总页数")
    private int maxPage;
    @Desc("本次收集 需要的作物数量")
    private int needNum;
    @Desc("单个数据")
    private  MemberCollectVO [] memberCollectVOs;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public MemberCollectVO[] getMemberCollectVOs() {
        return memberCollectVOs;
    }

    public void setMemberCollectVOs(MemberCollectVO[] memberCollectVOs) {
        this.memberCollectVOs = memberCollectVOs;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
