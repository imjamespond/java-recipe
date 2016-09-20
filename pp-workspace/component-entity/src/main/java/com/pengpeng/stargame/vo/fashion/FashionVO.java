package com.pengpeng.stargame.vo.fashion;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:49
 */
@Desc("装扮VO，身上的单个装扮")
public class FashionVO {
    @Desc("装扮的Id")
    private String id;
    @Desc("装扮的物品Id")
    private String itemId;
    @Desc("装扮的 时尚值")
    private int fashionIndex;
    @Desc("装扮的 类型")
    private int type;
    @Desc("装扮的 图片")
    private String image;

    private String icon;

    public FashionVO() {
    }

    public FashionVO(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getFashionIndex() {
        return fashionIndex;
    }

    public void setFashionIndex(int fashionIndex) {
        this.fashionIndex = fashionIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
