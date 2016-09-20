package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 下午6:21
 */
@Desc("家族　成员 分页VO　")
public class FamilyMemberPageVO {

    @Desc("起始页")
    private Integer pageNo; // 起始页 从1开始
    @Desc("最大页数")
    private Integer maxPage; // 最大页数

    @Desc("家族成员集合")
    private FamilyMemberVO [] familyMemberVOs;

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

    public FamilyMemberVO[] getFamilyMemberVOs() {
        return familyMemberVOs;
    }

    public void setFamilyMemberVOs(FamilyMemberVO[] familyMemberVOs) {
        this.familyMemberVOs = familyMemberVOs;
    }
}
