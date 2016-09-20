package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.FarmEvaluate;
import com.pengpeng.stargame.model.farm.FarmPlayer;

/**
 * User: mql
 * Date: 13-5-3
 * Time: 上午9:52
 */
public interface IFarmRuleContainer {
    /**
     * 根据 时间 获取一个农场 信息
     * @param farmPlayer
     * @param time
     * @return
     */
    public  boolean runFarmPlayer(FarmPlayer farmPlayer,long time);


    /**
     * 可否评价 好友的农场
     * @param farmEvaluate
     * @param friendId
     * @throws AlertException
     */
    public void checkEvaluate(FarmEvaluate farmEvaluate,String friendId) throws AlertException;

    /**
     * 评价好友农场
     * @param farmEvaluate
     * @param friendFarmEvaluate
     */
    public void evaluate(FarmEvaluate farmEvaluate,FarmEvaluate friendFarmEvaluate);

    /**
     * 好友的农场 可否帮助收获
     * @param farmPlayer
     * @return
     */
    public boolean canHelp(FarmPlayer farmPlayer);

    /**
     * 设置农场作物 马上成熟
     * @param farmPlayer
     */
    public void setRipe(FarmPlayer farmPlayer);

    /**
     * 获取 已经 开放田地的数量
     * @param pid
     * @return
     */
    public int getOpenFieldNum(String pid);

}
