package com.pengpeng.stargame.vo.room;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.BaseItemVO;
import com.pengpeng.stargame.vo.BasePkgVO;

/**
 * User: mql
 * Date: 13-5-30
 * Time: 下午4:46
 */
@Desc("房间内的 仓库Vo")
@EventAnnotation(name="event.room.pkg.update",desc="个人房间仓库数据更新")
public class RoomPkgVO extends BasePkgVO {

    private RoomItemVO[] baseItemVO;


    public RoomItemVO[] getBaseItemVO() {
        return baseItemVO;
    }

    public void setBaseItemVO(RoomItemVO[] baseItemVO) {
        this.baseItemVO = baseItemVO;
    }
}
