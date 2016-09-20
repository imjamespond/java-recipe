package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 14-1-14
 * Time: 上午10:18
 */
public class StallAssistant extends BaseEntity<String> {

    private String itemId;

    private Map<String,PlayerAssistant> shelfMap; //pid->shelfId

    public Map<String, PlayerAssistant> getShelfMap() {
        return shelfMap;
    }

    public void setShelfMap(Map<String, PlayerAssistant> shelfMap) {
        this.shelfMap = shelfMap;
    }

    @Override
    public String getId() {
        return itemId;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setId(String id) {
        itemId = id;
    }

    @Override
    public String getKey() {
        return itemId;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
