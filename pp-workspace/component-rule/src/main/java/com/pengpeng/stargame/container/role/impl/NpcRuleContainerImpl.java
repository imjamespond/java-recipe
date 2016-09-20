package com.pengpeng.stargame.container.role.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.role.INpcRuleContainer;
import com.pengpeng.stargame.rule.role.NpcRule;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-1-10上午10:19
 */
@Component
public class NpcRuleContainerImpl  extends HashMapContainer<String,NpcRule> implements INpcRuleContainer {

    @Override
    public List<NpcRule> getNpcBySceneId(String sceneId) {
        return null;  //TODO:方法需要实现
    }
}
