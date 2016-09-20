package com.pengpeng.stargame.farm.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;

/**
 * @author jinli.yuan@com.pengpeng.com
 * @since 13-3-27 下午4:43
 */
public interface IFarmPlayerDao extends BaseDao<String,FarmPlayer> {
    /**
     * 获取农场 信息
     * @param pId
     * @param time   绝对时间(毫秒数) ，默认传当前时间 ，为了方便测试 ，可以传以后的时间，获取那个时间点的农场田地 状态
     * @return
     */

    public FarmPlayer getFarmPlayer(String pId,long time);

    /**
     * 获取农场等级
     * @param pId
     * @return
     */
    public int getFarmLevel(String pId) ;


}
