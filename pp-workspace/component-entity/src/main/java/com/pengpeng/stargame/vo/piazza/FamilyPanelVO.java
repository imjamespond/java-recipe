package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 上午11:12
 */
@Desc("家族面板VO 点击家族返回")
public class FamilyPanelVO {
    @Desc("家族信息")
    private FamilyInfoVO familyPanelVO;
    @Desc("个人信息")
    private FamilyMemberVO familyMemberVO;

    public FamilyInfoVO getFamilyPanelVO() {
        return familyPanelVO;
    }

    public void setFamilyPanelVO(FamilyInfoVO familyPanelVO) {
        this.familyPanelVO = familyPanelVO;
    }

    public FamilyMemberVO getFamilyMemberVO() {
        return familyMemberVO;
    }

    public void setFamilyMemberVO(FamilyMemberVO familyMemberVO) {
        this.familyMemberVO = familyMemberVO;
    }
}
