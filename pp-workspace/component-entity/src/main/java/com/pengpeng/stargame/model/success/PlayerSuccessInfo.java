package com.pengpeng.stargame.model.success;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 上午11:02
 * 玩家成就信息
 */
public class PlayerSuccessInfo extends BaseEntity<String> {
    private String pid;
    private String useId;//正在使用的 称号 成就Id
    private List<OneSuccess> oneSeccessList;

    public void addSuccess(OneSuccess oneSeccess){
        oneSeccessList.add(oneSeccess);
    }
    public PlayerSuccessInfo(){
    }
    public PlayerSuccessInfo(String pid) {
        this.pid = pid;
        this.oneSeccessList = new ArrayList<OneSuccess>();
    }

    public OneSuccess getOneSuccess(String id){
        for(OneSuccess oneSeccess:oneSeccessList){
            if(oneSeccess.getId().equals(id)){
                return oneSeccess;
            }
        }
        return null;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<OneSuccess> getOneSeccessList() {
        return oneSeccessList;
    }

    public void setOneSeccessList(List<OneSuccess> oneSeccessList) {
        this.oneSeccessList = oneSeccessList;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pid;
    }
}
