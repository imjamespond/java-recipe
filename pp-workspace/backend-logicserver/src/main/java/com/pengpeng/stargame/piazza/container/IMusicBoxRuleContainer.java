package com.pengpeng.stargame.piazza.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.piazza.MusicBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.piazza.rule.MusicBoxRule;
import com.pengpeng.stargame.vo.piazza.MusicItemVO;
import com.pengpeng.stargame.vo.piazza.MusicZanVO;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-5上午11:09
 */
public interface IMusicBoxRuleContainer extends IMapContainer<String,MusicBoxRule> {
    /**
     * 获取排行榜  游戏这边次数的排行榜
     * @param num
     * @return
     */
    public List<MusicItemVO> getTopList(int num);

    public List<MusicItemVO> getList();


    public MusicZanVO getZanInfo(MusicBox box);

    /**
     * 检查是否可赞
     * @throws com.pengpeng.stargame.exception.GameException
     */
    public void checkZan(Player player, MusicBox box,Integer listId,Integer listItemId) throws GameException;

    /**
     * 赞
     * @throws com.pengpeng.stargame.exception.GameException
     */
    public void zan(Player player, MusicBox box, Integer listId, Integer listItemId) throws GameException;

    /**
     * 获取免费赞次数
     * @param pid
     * @return
     */
    public int getFreeZanNum(String pid);
}
