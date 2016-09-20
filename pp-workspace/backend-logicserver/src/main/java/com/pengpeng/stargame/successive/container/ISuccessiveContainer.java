package com.pengpeng.stargame.successive.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.successive.rule.SuccessiveRule;
import com.pengpeng.stargame.vo.successive.SuccessivePrizeVO;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface ISuccessiveContainer extends IMapContainer<String,SuccessiveRule> {

    /**
     * 检测
     * @param ps 玩家连续登陆信息
     * @return
     */
    public void detect(PlayerSuccessive ps);

    /**
     * 领奖
     *
     * @param ps 玩家连续登陆信息
     * @param player
     * @param farmPackage
     * @param roomPackege
     * @param fashionCupboard @return
     * */
    public void getPrize(String day,PlayerSuccessive ps, Player player, FarmPackage farmPackage, RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException;


    /**
     * 可领奖数
     * @param day 连续登陆天数
     * @param getPrize 已经领取信息
     * @return
     */
    public int getNum(int day,int getPrize);
}
