package com.pengpeng.stargame.vo.farm.decorate;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.farm.FarmItemVO;

/**
 * User: mql
 * Date: 14-3-20
 * Time: 下午4:46
 */
@Desc("农场装饰 背包信息")
@EventAnnotation(name="event.farmdecorate.pkg.update",desc="农场装饰背包")
public class FarmDecoratePkgVO {
    @Desc("仓库物品集合")
    private FarmItemVO[] farmItemVO;

    public FarmItemVO[] getFarmItemVO() {
        return farmItemVO;
    }

    public void setFarmItemVO(FarmItemVO[] farmItemVO) {
        this.farmItemVO = farmItemVO;
    }
}
