package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 下午6:28
 */
@Desc("家族 分页VO　")
public class FamilyPageVO {
    @Desc("起始页")
    private Integer pageNo; // 起始页 从1开始
    @Desc("最大页数")
    private Integer maxPage; // 最大页数

    @Desc("家族集合")
    private FamilyVO [] familyVOs;

    @Desc("我的排名")
    private int myRank;

    public int getMyRank() {
        return myRank;
    }

    public void setMyRank(int myRank) {
        this.myRank = myRank;
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

    public FamilyVO[] getFamilyVOs() {
        return familyVOs;
    }

    public void setFamilyVOs(FamilyVO[] familyVOs) {
        this.familyVOs = familyVOs;
    }
}
