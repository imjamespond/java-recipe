package com.pengpeng.stargame.model.farm;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


/**
 * 玩家农场
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-27下午3:53
 */
@Entity
@Table(name="pp_stargame_farm")
public class FarmPlayer extends BaseEntity<String> {
    @Id
    private String pid;//就是playerid
    @Column
    private int level;//农场等级
    @Column
    private int exp;//农场经验

    @Column
    private int dayExp;//每日经验

    private List<FarmField> fields;

    private  String nextTime;//下次刷新时间

    public FarmPlayer(){
        fields = new ArrayList<FarmField>();
    }


    public FarmPlayer(String pid,int level,int exp){
        this.pid = pid;
        this.level = level;
        this.exp = exp;
        fields = new ArrayList<FarmField>();
    }

    public String getId() {
        return pid;
    }

    public void setId(String id) {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getDayExp() {
        return dayExp;
    }

    public void setDayExp(int dayExp) {
        this.dayExp = dayExp;
    }



	@Override
	public String getKey() {
		return this.pid;
	}

    public List<FarmField> getFields() {
        return fields;
    }

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    /**
     *
     * 获取 所有种植 作物的数量
     * @return
     */
    public int getAllCropNum() {
        int num=0;
        for (int i = 0; i < fields.size(); i++) {
            FarmField ff = fields.get(i);
            if (ff.getStatus()!=FarmConstant.FIELD_STATUS_FREE){
                num++;
            }
        }
        return num;
    }

    /**
     * 获取 可以 收获的 数量
     * @return
     */
    public int getCanHarvestNum() {
        int num=0;
        for (int i = 0; i < fields.size(); i++) {
            FarmField ff = fields.get(i);
            if (ff.getStatus()!= FarmConstant.FIELD_STATUS_FREE){
                if(ff.getRipeId()==0||ff.isHarvestRipe(ff.getRipeId())){
                    continue;
                }
                num++;
            }
        }
        return num;
    }

    /**
     * 获取一个田地
     * @param fieldId
     * @return
     */
    public FarmField getOneFarmField(String fieldId){
        for (int i=0;i<fields.size();i++){
            if(fields.get(i).getId().equals(fieldId)){
                return fields.get(i);
            }
        }
        return null;
    }

    /**
     * 添加一个田地
     * @param farmField
     */
    public void addField(FarmField farmField) {
        fields.add(farmField);
    }
    /**
     * 删除一个田地
     */
    public void deleteField(String fieldId){
        FarmField farmField=null;
        for(FarmField farmField1:fields){
            if(farmField1.getId().equals(farmField.getId())) {
                farmField=farmField1;
                break;
            }
        }
        fields.remove(farmField);
    }

}
