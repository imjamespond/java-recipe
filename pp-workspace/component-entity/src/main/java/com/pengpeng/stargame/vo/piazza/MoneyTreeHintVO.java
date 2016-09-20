package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * User: mql
 * Date: 13-8-6
 * Time: 下午2:40
 */
@Desc("摇钱树提示信息 VO")
public class MoneyTreeHintVO {
    @Desc("家族的Id")
    private String familyId;
    @Desc("提示的时间")
    private Date date;
    @Desc("提示的内容")
    private String content;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
