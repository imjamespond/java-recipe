package com.pengpeng.stargame.vo.role.album;

import com.pengpeng.stargame.annotation.Desc;

import java.util.Date;

/**
 * User: mql
 * Date: 13-12-3
 * Time: 下午2:48
 */
@Desc("相册VO")
public class AlbumVO {
    @Desc("相册Id")
    private int id;
    @Desc("玩家Id")
    private int  userId;
    @Desc("相册标题")
    private String  title;
    @Desc("相册权限")
    private int  publicTo;
    @Desc("相册一共多少相片")
    private int  photoTotal;
    @Desc("相册封面")
    private String  cover;
    @Desc("相册创建时间")
    private long  createTimel;
    @Desc("相册更改时间")
    private long  updateTimel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicTo() {
        return publicTo;
    }

    public void setPublicTo(Integer publicTo) {
        this.publicTo = publicTo;
    }

    public Integer getPhotoTotal() {
        return photoTotal;
    }

    public void setPhotoTotal(Integer photoTotal) {
        this.photoTotal = photoTotal;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getCreateTimel() {
        return createTimel;
    }

    public void setCreateTimel(long createTimel) {
        this.createTimel = createTimel;
    }

    public long getUpdateTimel() {
        return updateTimel;
    }

    public void setUpdateTimel(long updateTimel) {
        this.updateTimel = updateTimel;
    }
}
