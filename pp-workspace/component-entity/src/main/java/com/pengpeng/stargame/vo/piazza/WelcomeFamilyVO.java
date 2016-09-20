package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * 欢迎加入家族的vo
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-8下午2:15
 */
@Desc("欢迎加入家族")
@EventAnnotation(name="event.family.welcome",desc="欢迎加入家族")
public class WelcomeFamilyVO {
    private String pid;
    private String name;
    private int gameCoin;
    private String content;

    public WelcomeFamilyVO(){

    }
    public WelcomeFamilyVO(String pid, String name, String content,int gameCoin) {
        this.pid = pid;
        this.name = name;
        this.content = content;
        this.gameCoin = gameCoin;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
