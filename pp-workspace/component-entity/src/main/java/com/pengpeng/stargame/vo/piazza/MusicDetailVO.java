package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-4上午10:45
 */
@Desc("音乐盒详细")
public class MusicDetailVO {
    @Desc("年")
    private int year;
    @Desc("期")
    private int listNum;
    @Desc("前三首歌曲")
    private MusicItemVO[] top3VO;
    @Desc("歌曲")
    private MusicItemVO[] musicItemVO;

    @Desc("剩余次数")
    private int restNum;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getListNum() {
        return listNum;
    }

    public void setListNum(int listNum) {
        this.listNum = listNum;
    }

    public MusicItemVO[] getTop3VO() {
        return top3VO;
    }

    public void setTop3VO(MusicItemVO[] top3VO) {
        this.top3VO = top3VO;
    }

    public MusicItemVO[] getMusicItemVO() {
        return musicItemVO;
    }

    public void setMusicItemVO(MusicItemVO[] musicItemVO) {
        this.musicItemVO = musicItemVO;
    }

    public int getRestNum() {
        return restNum;
    }

    public void setRestNum(int restNum) {
        this.restNum = restNum;
    }
}
