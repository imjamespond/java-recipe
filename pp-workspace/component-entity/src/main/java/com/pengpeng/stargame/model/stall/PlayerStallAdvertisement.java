package com.pengpeng.stargame.model.stall;

import com.pengpeng.stargame.vo.stall.StallPlayerAdvVO;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 14-1-14
 * Time: 上午10:18
 */
public class PlayerStallAdvertisement extends StallPlayerAdvVO implements Comparable<PlayerStallAdvertisement>{



    @Override
    public int compareTo(PlayerStallAdvertisement psa) {

        if(psa.getType()>getType()){
            return 1;
        }else if(psa.getType() == getType() ){
            if(psa.getEndTime() > getEndTime()){
                return 1;
            }
        }
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
