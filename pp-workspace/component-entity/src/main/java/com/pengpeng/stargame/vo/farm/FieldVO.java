package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * 田地  VO  ，每个 田地是一个 VO
 * User: mql
 * Date: 13-4-26
 * Time: 上午10:03
 */
@Desc("农田")
@EventAnnotation(name="event.farmField.update",desc="农田数据更新")
public class FieldVO {
    @Desc("田地的 Id")
    private  int id ;
    @Desc("田地的状态 0 空闲状态  1 成长中  2已经成熟 3田地未开放 ")
    private  int status;
    @Desc("作物种子的Id ")
    private  String seedId;
    @Desc("成熟所剩下的时间 ，如果 status 是1，那么后端会传 成熟剩余的毫秒数 ，客户端 倒计时")
    private long ripetime;
    @Desc("种子 的名字")
    private String name;
    @Desc("图像的名字")
    private String image;
    @Desc("下一次成熟的剩余时间 毫秒")
    private long nextRipeTime;
    @Desc("增加的农场经验")
    private int addExp;
    @Desc("整个农田内下次最小收获时间")
    private long  minNextRipeTime;
    @Desc("田地的位置")
    private String position;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSeedId() {
        return seedId;
    }

    public void setSeedId(String seedId) {
        this.seedId = seedId;
    }

    public long getRipetime() {
        return ripetime;
    }

    public void setRipetime(long ripetime) {
        this.ripetime = ripetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getNextRipeTime() {
        return nextRipeTime;
    }

    public void setNextRipeTime(long nextRipeTime) {
        this.nextRipeTime = nextRipeTime;
    }

    public int getAddExp() {
        return addExp;
    }

    public void setAddExp(int addExp) {
        this.addExp = addExp;
    }

    public long getMinNextRipeTime() {
        return minNextRipeTime;
    }

    public void setMinNextRipeTime(long minNextRipeTime) {
        this.minNextRipeTime = minNextRipeTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
