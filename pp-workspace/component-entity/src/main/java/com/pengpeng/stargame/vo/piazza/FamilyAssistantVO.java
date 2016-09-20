package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.smallgame.SmallGameRankVO;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("明星助理信息")
public class FamilyAssistantVO {

    @Desc("报名状态0未报1已报2审核")
    private int status;

    @Desc("明星助理")
    private AssistantVO[] assistants;

    public static final int NO = 0;
    public static final int YES = 1;
    public static final int AUDIT = 2;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public AssistantVO[] getAssistants() {
        return assistants;
    }

    public void setAssistants(AssistantVO[] assistants) {
        this.assistants = assistants;
    }


}
