package com.pengpeng.stargame.container.role.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.container.role.INpcRuleContainer;
import com.pengpeng.stargame.container.role.ISceneRuleContainer;
import com.pengpeng.stargame.container.role.ITransferRuleContainer;
import com.pengpeng.stargame.rule.role.NpcRule;
import com.pengpeng.stargame.rule.role.SceneRule;
import com.pengpeng.stargame.rule.role.TransferRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 林佛权
 * Date: 12-12-24
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SceneRuleContainerImpl extends HashMapContainer<String,SceneRule> implements ISceneRuleContainer {
	@Autowired
	private ITransferRuleContainer transferRuleContainer;

    @Autowired
    private INpcRuleContainer npcRuleContainer;

    private String defaultScene = "map_01";

    public void setDefaultScene(String id){
        defaultScene = id;
    }
    private List<NpcBean> newNpcBean(SceneRule rule){
        List<NpcBean> list = new ArrayList<NpcBean>();
        String m = rule.getNpc();
        if (m!=null){
            String[] npcs = m.split(";");
            for(String item:npcs){
                String[] items = item.split(",");
                if (items.length<3){
                    continue;
                }
                String nid = items[0];
                int x = Integer.parseInt(items[1]);
                int y = Integer.parseInt(items[2]);
                NpcRule mRule = npcRuleContainer.getElement(nid);
                list.add(new NpcBean(mRule,x,y));
            }
        }
        return list;
    }
    @Override
    public List<TransferRule> getTransfer(String sceneId){
        List<TransferRule> transferRuleList = transferRuleContainer.getTransferByScene(sceneId);
        return transferRuleList;
    }

    @Override
    public SceneRule getDefaultScene() {
        return items.get(defaultScene);
    }
}
