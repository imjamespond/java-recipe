package com.pengpeng.stargame.room.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Indexable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 13-11-22
 * Time: 下午3:40
 */
@Entity
@Table(name="sg_rule_room_expansion")
public class RoomExpansionRule extends BaseEntity<Integer>  {
    @Id
    private int expansionTimes;//次数
    @Column
    private int  level; //等级
    @Column
    private int gameCoin; //需要的游戏币
    @Column
    private int time;  //花费时间
    @Column
    private float	parameter;    //达人币参数
    @Column
    private int  location;   //位置

    public int getExpansionTimes() {
        return expansionTimes;
    }

    public void setExpansionTimes(int expansionTimes) {
        this.expansionTimes = expansionTimes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getParameter() {
        return parameter;
    }

    public void setParameter(float parameter) {
        this.parameter = parameter;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    @Override
    public Integer getId() {
        return expansionTimes ;
    }

    @Override
    public void setId(Integer id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer getKey() {
        return expansionTimes;
    }
}
