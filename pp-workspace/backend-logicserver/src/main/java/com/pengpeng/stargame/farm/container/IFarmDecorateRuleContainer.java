package com.pengpeng.stargame.farm.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.farm.rule.FarmDecorateRule;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.room.DecorateVO;

import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 14-3-14
 * Time: 下午3:29
 */
public interface IFarmDecorateRuleContainer extends IMapContainer<String,FarmDecorateRule> {

    /**
     *
     */
    public void checkAndBuy(FarmPlayer farmPlayer,List<String> buyList,List<String> removeList,Player player,FarmDecorate farmDecorate) throws AlertException;
    /**
     * 回收装饰品  或者 小动物
     * @param farmDecorate
     * @param farmDecoratePkg
     */
    public void recycleDecorate(FarmDecorate farmDecorate,FarmDecoratePkg farmDecoratePkg,DecorateVO decorateVO,FarmPlayer farmPlayer);

    /**
     * 添加装饰
     * @param farmDecorate
     * @param decorateVO
     * @param farmPlayer
     */
    public void addDecorate(FarmDecorate farmDecorate,DecorateVO decorateVO,FarmPlayer farmPlayer,FarmDecoratePkg farmDecoratePkg);

    /**
     * 修改装饰位置
     * @param farmDecorate
     * @param decorateVO
     * @param farmPlayer
     */
    public void updateDecorate(FarmDecorate farmDecorate,DecorateVO decorateVO,FarmPlayer farmPlayer);

    /**
     * 小动物 控制
     * @param farmDecorate
     * @return
     */
    public boolean refreshAnimal(FarmDecorate farmDecorate);

    /**
     * 操作小动物
     * @param farmDecorate
     * @param id
     */
    public void animalOperation(FarmDecorate farmDecorate,String id) throws AlertException;

    /**
     * 获取提示信息
     * @param farmDecorate
     * @return
     */
    public List<String> getHints(FarmDecorate farmDecorate);

    /**
     * 获取 收获的时候 减少农场作物的数量
     * @param pid
     * @return
     */
    public int getCropReduce(String pid);

    /**
     * 获取收获的时候  增加农场作物的数量
     * @param pid
     * @return
     */
    public int getCropAdd(String pid);

    /**
     * 获取初始化列表
     * @return
     */
    public List<FarmDecorateRule> getInitList();

    /**
     * 获取初始化土地的位置
     * @param fieldId
     * @return
     */
    public String getFieldPosition(int fieldId);

}
