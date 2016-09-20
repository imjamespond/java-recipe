package com.pengpeng.stargame.vo.role.album;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-12-3
 * Time: 下午2:58
 */
@Desc("相片的分页VO")
public class AlbumItemPageVO {
    @Desc("起始页")
    private Integer pageNo;
    @Desc("最大页数")
    private Integer maxPage;
    @Desc("相册信息 AlbumItemVO 数组")
    private AlbumItemVO [] albumVOs;

    public AlbumItemVO[] getAlbumVOs() {
        return albumVOs;
    }

    public void setAlbumVOs(AlbumItemVO[] albumVOs) {
        this.albumVOs = albumVOs;
    }

    public Integer getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
