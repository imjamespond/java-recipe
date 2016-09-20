package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.pengpeng.stargame.player.rule.TransferRule;

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

    public boolean checkEnter(String sceneId,Player player);

    public boolean checkOuter(String sceneId,Player player);

    /**
     * 取得频道的id
     * 如果是(私有)农场类,则场景id+pid
     * 如果是(广场类)广场类,则返回场景id
     * @param sceneId
     * @param player
     * @return
     */
    public String getChannelId(String sceneId,Player player,String familyId);
}
