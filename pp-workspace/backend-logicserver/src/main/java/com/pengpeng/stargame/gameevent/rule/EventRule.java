package com.pengpeng.stargame.gameevent.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;

/**
 * User: mql
 * Date: 13-12-9
 * Time: 上午9:30
 */
@Entity
@Table(name = "sg_rule_event")
public class EventRule extends BaseEntity<String> {
    @Id
    private String id;
    //活动的描述
    @Column
    private String des;
    //活动的类型
    @Column //
    private int type;

    //开始时间
    @Column
    private String startTime;

    //结束时间
    @Column
    private String endTime;
    //圣诞 元旦活动 关联的活动的ID
    @Column
    private String taskId;
    @Column//礼包Id
    private String giftId;
    @Column//掉落活动频率
    private String frequency;
    @Column//掉落活动最多拾取多少个
    private int maxNum;
    //明星送礼物 提高百分之多少粉丝值
    @Column
    private int addP;
    @Column   //通用信息
    private String valueInfo;
    @Column  //掉落活动掉落范围
    private String dropPosition;
    @Transient //掉落活动 掉落的 礼物数组
    private String [] dropGifts;
    @Transient //家族银行 奖励数组
    private String [] familyBankRewards;
    @Transient
    String [] positions;
    public void init(){
        if(giftId!=null&&!giftId.equals("")&&!giftId.equals("0")){
            dropGifts=giftId.split(",");
        }
        if(valueInfo!=null&&!valueInfo.equals("")&&!valueInfo.equals("0")){
            familyBankRewards=valueInfo.split(";");
        }
        if(dropPosition!=null&&!dropPosition.equals("")&&!dropPosition.equals("0")){
            positions=dropPosition.split(";");
        }
        if(positions==null){
            positions=new String[0];
        }
    }

    public String[] getFamilyBankRewards() {
        return familyBankRewards;
    }

    public void setFamilyBankRewards(String[] familyBankRewards) {
        this.familyBankRewards = familyBankRewards;
    }

    public String getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(String valueInfo) {
        this.valueInfo = valueInfo;
    }

    public String[] getDropGifts() {
        return dropGifts;
    }

    public void setDropGifts(String[] dropGifts) {
        this.dropGifts = dropGifts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getKey() {
        return id;
    }

    @Override
    public void setKey(String key) {

    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getAddP() {
        return addP;
    }

    public void setAddP(int addP) {
        this.addP = addP;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getDropPosition() {
        return dropPosition;
    }

    public void setDropPosition(String dropPosition) {
        this.dropPosition = dropPosition;
    }

    public String[] getPositions() {
        return positions;
    }

    public void setPositions(String[] positions) {
        this.positions = positions;
    }
}


