package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.container.INpcRuleContainer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.container.ITransferRuleContainer;
import com.pengpeng.stargame.player.rule.NpcRule;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.pengpeng.stargame.player.rule.TransferRule;
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

    private String defaultScene = "map_12";

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

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
    public boolean checkEnter(String sceneId,Player player) {
//        SceneRule rule = this.getElement(sceneId);
//        if (rule==null){
//            return false;
//        }
//        rule.checkEnter(player);
        return true;
    }

    @Override
    public boolean checkOuter(String sceneId, Player player) {
//        SceneRule rule = this.getElement(sceneId);
//        if (rule==null){
//            return false;
//        }
//        rule.cheeckOuter(player);
        return true;
    }

    @Override
    public String getChannelId(String sceneId, Player player,String familyId) {
        SceneRule rule = this.getElement(sceneId);
        if (rule==null){
            return sceneId;
        }
        if (rule.getType()==2){//个人房间,个人农场
            return sceneId+"."+player.getId();
        }
        if (rule.getType()==1){//明星广场,明星城堡
            //可以访问其它家族的明星城堡
            FamilyRule familyRule=null;
            if(player!=null){
                 familyRule = familyRuleContainer.getRuleByStarId(player.getStarId());
            }

            if(null!=familyId&&!familyId.equals("")){
              familyRule=familyRuleContainer.getElement(familyId);
            }
            if(familyRule==null){
                return sceneId;
            }
            return sceneId+"."+familyRule.getId();
        }
        return sceneId;
    }

    @Override
    public SceneRule getDefaultScene() {
        return items.get(defaultScene);
    }

}
