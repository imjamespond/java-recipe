package com.pengpeng.stargame.vo.role.album;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-12-3
 * Time: 下午3:05
 */
@Desc("相册请求Req")
public class AlbumReq extends BaseReq {
    @Desc("请求的页数")
    private int pageNo;
    @Desc("相册的Id")
    private int albumId;
    @Desc("玩家Id")
    private String pid;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
