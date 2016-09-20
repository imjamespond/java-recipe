package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 13-7-8
 * Time: 上午10:21
 */
@Desc("赠送礼物 面板VO")
public class StarSendPageVO {
    @Desc("默认留言")
    private String  words;
    @Desc("明星头像 ")
    private String icon;
    @Desc("1表示送礼物掉落 粉丝值增加的 活动开始")
    private int eventStart;
    @Desc("礼物列表")
    private StarGiftVO [] starGiftVOs;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public StarGiftVO[] getStarGiftVOs() {
        return starGiftVOs;
    }

    public void setStarGiftVOs(StarGiftVO[] starGiftVOs) {
        this.starGiftVOs = starGiftVOs;
    }

    public int getEventStart() {
        return eventStart;
    }

    public void setEventStart(int eventStart) {
        this.eventStart = eventStart;
    }
}
