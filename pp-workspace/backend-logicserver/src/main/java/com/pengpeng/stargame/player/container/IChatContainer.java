package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.player.PlayerChat;

import java.util.Collection;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-20下午5:15
 */
public interface IChatContainer  {

    public void addWord(Collection<String> words);
    public void addWord(String msg);
    public void removeWord(String msg);
    /**
     * 检查能不能发言
     * @param player
     * @throws RuleException
     */
    public void checkTalk(OtherPlayer player) throws RuleException;

    /**
     * 是否能发言
     * @param player
     * @return
     */
    public boolean canTalk(OtherPlayer player);

    /**
     * 检查是否包含被过滤的敏感词
     * @param msg
     */
    public boolean hasSensitive(String msg);

    /**
     * 检查敏感词,如果有则抛出异常
     * @param msg
     * @throws RuleException
     */
    public void checkSensitive(String msg) throws RuleException;

    /**
     * 过滤敏感词,并返回过滤后的消息
     * @param msg
     * @return
     */
    public String filter(String msg);

    void init();

    /**
     * 千里传音 扣达人币
     * @param player
     * @return
     */
    public void checkCoin(Player player,PlayerChat playerChat) throws GameException;

    public void checFreeNum(Player player,PlayerChat playerChat)  throws GameException;

    /**
     * 获取每日 玩家免费 大喇叭次数
     * @param pid
     * @return
     */
    public int getFreeNum(String pid);
}
