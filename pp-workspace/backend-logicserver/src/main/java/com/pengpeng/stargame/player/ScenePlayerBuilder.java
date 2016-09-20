package com.pengpeng.stargame.player;

import com.pengpeng.stargame.model.player.ScenePlayer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.rule.SceneRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-5下午3:58
 */
@Component
public class ScenePlayerBuilder {

    @Autowired
    private ISceneRuleContainer sceneRuleContainer;

    public ScenePlayer newDefalutScenePlayer(String pid){
        SceneRule rule = sceneRuleContainer.getDefaultScene();
        ScenePlayer sp = new ScenePlayer(pid,rule.getId());
        return sp;
    }
}
