package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.player.container.IActiveRewardRuleContainer;
import com.pengpeng.stargame.player.rule.ActiveRewardRule;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-16下午3:39
 */
@Component
public class ActiveRewardContainerImpl extends HashMapContainer<Integer,ActiveRewardRule> implements IActiveRewardRuleContainer {
}
