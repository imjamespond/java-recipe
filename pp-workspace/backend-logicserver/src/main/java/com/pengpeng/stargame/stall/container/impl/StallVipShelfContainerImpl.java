package com.pengpeng.stargame.stall.container.impl;

import com.pengpeng.stargame.model.stall.PlayerShelf;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.stall.container.IStallVipShelfContainer;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class StallVipShelfContainerImpl implements IStallVipShelfContainer {
    @Override
    public void enable(PlayerStall ps)  {
        while( ps.getPlayerVipShelf().size()< StallConstant.SHELF_VIP_NUM){
            ps.getPlayerVipShelf().add(new PlayerShelf());
        }
    }
}

