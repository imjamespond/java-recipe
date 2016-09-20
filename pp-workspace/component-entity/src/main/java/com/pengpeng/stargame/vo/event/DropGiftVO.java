package com.pengpeng.stargame.vo.event;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午10:48
 */
@Desc("单个掉落礼物VO")
public class DropGiftVO {
    @Desc("唯一Id  捡礼物的时候需要传给后台")
    private String id;
    @Desc("类型 1表示掉落的星星，2表示钱袋 3表示掉落树枝 4表示礼物盒 5新春利是 6气球")
    private int type;
    @Desc("名字")
    private String name;
    @Desc("位置")
    private String position;
    @Desc("剩余时间")
    private long expirationTime;
    @Desc("放气球的玩家名字")
    private String pname;
    @Desc("留言")
    private String word;
    @Desc("气球图标")
    private String icon;
    @Desc("家族名字")
    private String fName;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}
