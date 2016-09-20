package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.rule.FarmProcessRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.vo.farm.GoodsVO;

import java.util.List;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 上午11:59
 */
public interface IFarmProcessRuleContainer extends IMapContainer<String,FarmProcessRule> {

    public List<FarmProcessRule> getByType(int type);

    /**
     * 检测 开始 生产
     * @param farmProcessQueue
     * @param processIds
     */
    public void checkStart(FarmProcessQueue farmProcessQueue,String [] processIds) throws AlertException;

    /**
     * 开始生产
     * @param farmProcessQueue
     * @param processIds
     */
    public void start(FarmProcessQueue farmProcessQueue,String [] processIds,FarmPackage farmPackage);

    /**
     * 检测 取消 队列
     * @param farmProcessQueue
     * @param id
     */
    public  void checkCancel(FarmProcessQueue farmProcessQueue,String id) throws AlertException;

    /**
     * 取消 队列
     * @param farmProcessQueue
     * @param id
     */
    public  void cancel(FarmProcessQueue farmProcessQueue,String id);
    /**
     * 完成一个 正在进行的 队列
     * @param farmProcessQueue
     * @param id
     */
    public  void finishOne(FarmProcessQueue farmProcessQueue,String id,Player player) throws AlertException;

    /**
     * 返回直接完成需要的 金币
     * @param farmProcessQueue
     * @return
     */
    public int getNeedgold(FarmProcessQueue farmProcessQueue);

    /**
     * 返回 完成一个队列需要的达人币
     * @param farmProcessQueue
     * @param id
     * @return
     */
    public int getOneNeedgold(FarmProcessQueue farmProcessQueue,String id) throws AlertException;

    /**
     * 完成正在进行的队列
     * @param farmProcessQueue
     * @param player
     */
    public void finish(FarmProcessQueue farmProcessQueue,Player player) throws AlertException;

    /**
     * 开格子
     * @param farmProcessQueue
     * @param player
     */
    public void openGrid(FarmProcessQueue farmProcessQueue,Player player) throws AlertException;

    /**
     * 领取生产完成的 物品
     * @param farmProcessQueue
     * @param farmPackage
     */
    public void  getProecessGoods(FarmProcessQueue farmProcessQueue,FarmPackage farmPackage,RoomPackege roomPackege, FashionCupboard fashionCupboard,FarmDecoratePkg farmDecoratePkg) throws AlertException;

    /**
     * 获取可以领取的物品
     * @param farmProcessQueue
     * @return
     */
    public List<GoodsVO> getFinish(FarmProcessQueue farmProcessQueue);

    /**
     * 加速
     * @param player
     * @param farmProcessQueue
     */
    public void speedAll(Player player,FarmProcessQueue farmProcessQueue) throws AlertException;

}
