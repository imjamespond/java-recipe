package com.pengpeng.stargame.vo.piazza.collectcrop;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 14-3-25
 * Time: 上午9:18
 */
@Desc("家族收集请求")
public class FamilyCollectReq extends BaseReq {
    @Desc("家族Id")
    private String fid;
    @Desc("数量")
    private int num;
    @Desc("页数")
    private int page;
    @Desc("总页数")
    private int maxPage;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
