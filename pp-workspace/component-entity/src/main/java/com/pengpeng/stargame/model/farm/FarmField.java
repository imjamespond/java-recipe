package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 玩家农田
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-17上午10:07
 */
@Entity
public class FarmField extends BaseEntity<String> {
    @Id
    @Column
    private String id;
	@Column
	private String pid;
    private String seedId;//种子Id
    private Date plantTime;//作物种植时间
    private Date harvestTime;//从种植到全部成熟时间
    private int ripeId;//表示 此时 作物是 第几次成熟
    private int status;//状态  0 空闲  1生长  2全部成熟
    private List <Integer> harvestIds;//已经收获的 成熟Id
    private String position; //田地的位置
    public FarmField(){
        harvestIds = new ArrayList<Integer>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return id;
    }

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

    public String getSeedId() {
        return seedId;
    }

    public void setSeedId(String seedId) {
        this.seedId = seedId;
    }



    public int getRipeId() {
        return ripeId;
    }

    public void setRipeId(int ripeId) {
        this.ripeId = ripeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public List<Integer> getHarvestIds() {
        return harvestIds;
    }

    public boolean isHarvestRipe(int ripeId){
        return harvestIds.contains(ripeId);
    }

    public Date getPlantTime() {
        return plantTime;
    }

    public void setPlantTime(Date plantTime) {
        this.plantTime = plantTime;
    }

    public Date getHarvestTime() {
        return harvestTime;
    }

    public void setHarvestTime(Date harvestTime) {
        this.harvestTime = harvestTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
