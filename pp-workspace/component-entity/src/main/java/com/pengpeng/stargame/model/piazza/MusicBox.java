package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-5上午11:15
 */
public class MusicBox extends BaseEntity<String> {
    private String pid;
    //赞次数
    private int zan;
    //赞时间
    private Date zanDate;

    private Set<Integer> musics;
    //赞免费次数 通过任务或者其它途径获得
    private int freeNum;

    private int songListId;//榜单Id
    private Map<Integer,Integer> zanInfo;//本次榜单赞扬的次数记录

    public MusicBox(){
        musics = new HashSet<Integer>();
    }
    public void init(Date date){
        if (null==zanDate){
            zan = 0;
            zanDate = new Date();
            return ;
        }
        if (DateUtil.getDayOfYear(zanDate)!=DateUtil.getDayOfYear(date)){
            zan = 0;
            freeNum=0;
            zanDate = date;
            musics.clear();
        }
        if(zanInfo==null){
            zanInfo=new HashMap<Integer, Integer>();
        }

    }

    public boolean isZan(Integer id){
        return musics.contains(id);
    }
    public void incZan(int val,Integer id){
        this.zan += Math.abs(val);
        musics.add(id);
    }
    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String pid) {
        this.pid = pid;
    }

    @Override
    public String getKey() {
        return pid;
    }

    public String getPid() {
        return pid;
    }

    public int getZan() {
        return zan;
    }

    public Date getZanDate() {
        return zanDate;
    }

    public int getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

    public Set<Integer> getMusics() {
        return musics;
    }

    public void setMusics(Set<Integer> musics) {
        this.musics = musics;
    }

    public int getSongListId() {
        return songListId;
    }

    public void setSongListId(int songListId) {
        this.songListId = songListId;
    }

    public Map<Integer, Integer> getZanInfo() {
        return zanInfo;
    }

    public void setZanInfo(Map<Integer, Integer> zanInfo) {
        this.zanInfo = zanInfo;
    }
}
