package com.pengpeng.stargame.container.role;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.rule.role.SceneRule;
import com.pengpeng.stargame.rule.role.TransferRule;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 林佛权
 * Date: 12-12-24
 * Time: 下午3:02
 * To change this template use File | Settings | File Templates.
 */
public interface ISceneRuleContainer extends IMapContainer<String, SceneRule> {
    public SceneRule getDefaultScene();
    public List<TransferRule> getTransfer(String sceneId);

}
