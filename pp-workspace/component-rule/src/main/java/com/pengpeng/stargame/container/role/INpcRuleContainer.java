package com.pengpeng.stargame.container.role;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.rule.role.NpcRule;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-10上午10:18
 */
public interface INpcRuleContainer  extends IMapContainer<String, NpcRule> {

    /**
     * 根据当前地图,取出地图中的npc
     * @param sceneId
     * @return
     */
    public List<NpcRule> getNpcBySceneId(String sceneId);

}
