package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-1-20
 * Time: 下午2:30
 */
@Desc("单个气球VO")
public class BalloonVO {
    @Desc("气球的Id")
    private String id;

    @Desc("名字")
    private String name;
    @Desc("资源")
    private String icon;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
