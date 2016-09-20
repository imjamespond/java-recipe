package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午2:14
 */
@Desc("明星礼物单个VO")
public class StarGiftVO {
    @Desc("礼物Id")
    private String id ;
    @Desc("礼物名字")
    private  String name;
    @Desc("赠言")
    private String words;
    @Desc("需要的金币")
    private int gold;
    @Desc("增加的粉丝值")
    private int fansValues;
    @Desc("自己有的数量")
    private int num;
    @Desc("礼物图标")
    private String icon ;
    @Desc("用户主站头像")
    private String portrait;
    @Desc("用户名字")
    private String pname;
    @Desc("是否是活动期间 送的")
    private int eventStart;
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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getFansValues() {
        return fansValues;
    }

    public void setFansValues(int fansValues) {
        this.fansValues = fansValues;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getEventStart() {
        return eventStart;
    }

    public void setEventStart(int eventStart) {
        this.eventStart = eventStart;
    }
}
