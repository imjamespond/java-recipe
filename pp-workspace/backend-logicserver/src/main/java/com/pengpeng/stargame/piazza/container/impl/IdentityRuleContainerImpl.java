package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.piazza.container.IIdentityRuleContainer;
import com.pengpeng.stargame.piazza.rule.IdentityRule;
import org.springframework.stereotype.Component;

/**
 * 家族身份规则
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午5:37
 */
@Component
public class IdentityRuleContainerImpl extends HashMapContainer<Integer,IdentityRule> implements IIdentityRuleContainer{
}
