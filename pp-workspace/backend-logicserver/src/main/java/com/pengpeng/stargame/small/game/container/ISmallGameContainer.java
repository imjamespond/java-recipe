package com.pengpeng.stargame.small.game.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.small.game.rule.SmallGameRule;
import com.pengpeng.stargame.successive.rule.SuccessiveRule;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface ISmallGameContainer extends IMapContainer<String,SmallGameRule> {

    /**
     * 检测免费次数
     * @param ps
     * @return
     */
    public void checkFree(PlayerSmallGame ps,int type) throws AlertException;
    /**
     * 购买检测信息
     * @param ps
     * @return
     */
    public void buy(PlayerSmallGame ps,Player p,int type,int gold) throws AlertException;

    /**
     * 更新分数
     * @param defer 延时时间
     * @return
     */
    public void update(PlayerSmallGame ps, int type, int score,long defer) throws AlertException;

    /**
     * 扣减次数
     * @param ps
     * @return
     */
    public void deduct(PlayerSmallGame ps, int type) throws AlertException;

    /**
     * 周排行的key
     *
     * @return
     */
    public String getWeekKey(int type) ;

    /**
     * 每日排行的key
     *
     * @return
     */
    public String getDayKey(int type) ;

    /**
     * 最高排行的key
     *
     * @return
     */
    public String getMax(int type);

}
