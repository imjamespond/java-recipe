package com.pengpeng.stargame.vo.gm;

import com.pengpeng.stargame.annotation.Desc;

import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 14-2-24
 * Time: 下午6:08
 */
public class VideoFamilyInfoVO {
    @Desc("家族总量")
    private int num;
    @Desc("当前页数 数据")
    private List<Map<String,String>> values;
    @Desc("起始页")
    private Integer pageNo; // 起始页 从1开始
    @Desc("最大页数")
    private Integer maxPage; // 最大页数

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public void setValues(List<Map<String, String>> values) {
        this.values = values;
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
}
