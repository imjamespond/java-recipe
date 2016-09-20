package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-11-29 上午10:46
 */
@Desc("明星助理")
public class AssistantVO {

    @Desc("昵称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
