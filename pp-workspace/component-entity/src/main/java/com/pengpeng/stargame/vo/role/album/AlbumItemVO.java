package com.pengpeng.stargame.vo.role.album;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-12-3
 * Time: 下午2:52
 */
@Desc("相册里面的 相片VO")
public class AlbumItemVO {
    @Desc("相片Id")
    private int id;
    private int  userId;
    @Desc("所属相册Id")
    private int  userAlbum;
    @Desc("描述")
    private String  description;
    @Desc("相片标题")
    private java.lang.String title;
    private java.lang.String originalPath;
    private java.lang.String largePath;
    private java.lang.String smallPath;
    private java.lang.String mimeType;
    private java.lang.Integer viewTimes;
    @Desc("创建时间")
    private long createTimel;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserAlbum() {
        return userAlbum;
    }

    public void setUserAlbum(int userAlbum) {
        this.userAlbum = userAlbum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getLargePath() {
        return largePath;
    }

    public void setLargePath(String largePath) {
        this.largePath = largePath;
    }

    public String getSmallPath() {
        return smallPath;
    }

    public void setSmallPath(String smallPath) {
        this.smallPath = smallPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Integer viewTimes) {
        this.viewTimes = viewTimes;
    }

    public long getCreateTimel() {
        return createTimel;
    }

    public void setCreateTimel(long createTimel) {
        this.createTimel = createTimel;
    }
}
