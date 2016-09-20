package com.pengpeng.stargame.vo.success;

import com.pengpeng.stargame.annotation.Desc;

/**
 * User: mql
 * Date: 14-5-4
 * Time: 下午2:54
 */
@Desc("玩家成就信息")
public class PlayerSuccessInfoVO {
    @Desc("成就数组")
    private SuccessVO [] successVOs;

    public SuccessVO[] getSuccessVOs() {
        return successVOs;
    }

    public void setSuccessVOs(SuccessVO[] successVOs) {
        this.successVOs = successVOs;
    }
}
