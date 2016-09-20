package com.pengpeng.stargame.gameevent.constiner;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.OneDrop;
import com.pengpeng.stargame.model.gameEvent.PlayerEvent;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.vo.RewardVO;

/**
 * User: mql
 * Date: 13-12-6
 * Time: 下午4:43
 */
public interface IEventRuleContainer extends IMapContainer<String,EventRule> {
    /**
     * 通过掉落礼物Id 获取活动规则数据
     * @param giftId
     * @return
     */
    public EventRule getEventRuleByGiftId(String giftId);
    /**
     * 获取春节活动规则数据
     */
    public EventRule getSpringEventRule();
    /**
     * 获取家族银行 活动规则数据
     * @return
     */
   public EventRule getFamilyBankEventRule();

    /**
     * 获取 送明星礼物 概率掉落活动 规则数据
     * @return
     */
    public EventRule getStarSendGiftDropEventRule();
    /**
     * 是否 开始 送明星礼物 掉落活动
     * @return
     */
    public boolean openStarSendGiftDrop();

    /**
     * 是否开启 明星广场 掉落活动
     * @return
     */
    public boolean openStarPlazaEvent();

    /**
     * 返回  送明星 礼物掉落 增加的 粉丝值百分比
     * @return
     */
    public int getStarSendGiftAddP();

    /**
     * 给明星 送礼 掉落礼物调用
     * @param channelId
     * @param probability
     */
    public boolean starSendGiftDrop(String channelId,int probability);

    /**
     * 明星广场的掉落
     */
    public void starPlazaDrop();

    /**
     * 春节活动 的掉落
     * @param channelId
     */
    public void springEventDrop(String channelId);

    /**
     * 捡钱 地上的 礼物
     * @param player
     * @param farmPlayer
     * @param oneDrop
     */
    public RewardVO pickDropGift(PlayerEvent playerEvent,Player player,FarmPlayer farmPlayer,FarmPackage farmPackage,FashionCupboard fashionCupboard,RoomPackege roomPackege,OneDrop oneDrop,EventDrop eventDrop,FarmDecoratePkg farmDecoratePkg) throws AlertException, RuleException;
}
