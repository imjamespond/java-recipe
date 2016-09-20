package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.player.Bubble;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.map.BubbleVO;

/**
 * @auther james
 * @since: 13-5-28下午5:48
 */
public interface IBubbleRuleContainer {

    /**
     * 解析附件
     */
    public void addAttachList(String item,String name, int num, int type, int probability);

    public BubbleVO getRandomAttach(Bubble bubble);

    public BubbleVO getBubble(String pid, int type);

    public int accept(Player player, Bubble bubble) throws AlertException;
}
