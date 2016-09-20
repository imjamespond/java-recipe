package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 14-1-14
 * Time: 上午10:18
 */
public class StallAdvertisement extends BaseEntity<String> {

    private List<PlayerStallAdvertisement> advList; //

    public List<PlayerStallAdvertisement> getAdvList() {
        return advList;
    }

    public void setAdvList(List<PlayerStallAdvertisement> advList) {
        this.advList = advList;
    }

    @Override
    public String getId() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
