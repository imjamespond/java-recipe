package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.lottery.Lottery;
import com.pengpeng.stargame.model.lottery.LotteryItem;
import com.pengpeng.stargame.model.lottery.OneLotteryInfo;
import com.pengpeng.stargame.model.lottery.RouletteHist;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.vo.lottery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */
@Component
public class RspLotteryFactory extends RspFactory {
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IPlayerDao playerDao;

    public LotteryVO lotteryVO(Lottery lottery){
        LotteryVO vo = new LotteryVO();

        LotteryGoodsVO gvo = new LotteryGoodsVO();
        gvo.setItemId(lottery.getItemId());
        gvo.setNum(lottery.getNum());
        gvo.setGameCoin(lottery.getGameCoin());
        BaseItemRule item = baseItemRulecontainer.getElement(lottery.getItemId());
        if(item != null){
            gvo.setName(item.getName());
            gvo.setType(item.getType());
        }
        vo.setGiftVO(gvo);

        List<LotteryGoodsVO> listVO = new ArrayList();
        for(LotteryItem li:lottery.getLotteryItem()){
            LotteryGoodsVO gvo_ = new LotteryGoodsVO();
            BaseItemRule item_ = baseItemRulecontainer.getElement(li.getItemId());
            if(item_ != null){
                gvo_.setName(item_.getName());
                gvo_.setType(item_.getType());
            }
            gvo_.setItemId(li.getItemId());
            gvo_.setNum(li.getNum());
            gvo_.setGameCoin(li.getGameCoin());
            listVO.add(gvo_);
        }
        LotteryGoodsVO[] arr = new LotteryGoodsVO[listVO.size()];
        listVO.toArray(arr);
        vo.setGoodsVOs(arr);
        return vo;
    }

    public LotteryInfoVO lotteryInfoVO(int num,List<OneLotteryInfo> list ){
        LotteryInfoVO vo = new LotteryInfoVO();
        vo.setNum(num);
        List<OneLotteryInfoVO> listVO = new ArrayList();
        for(OneLotteryInfo one:list){
            OneLotteryInfoVO oneVO = new OneLotteryInfoVO();
            Player player = playerDao.getBean(one.getPid());
            if(null != player){
                oneVO.setName(player.getNickName());
            }else{
                oneVO.setName("碰碰玩家");
            }
            BaseItemRule item = baseItemRulecontainer.getElement(one.getItemId());
            if(item != null){
                oneVO.setItem(item.getName());
                oneVO.setNum(one.getNum());
            }
            oneVO.setDate(one.getDate().getTime());
            oneVO.setGameCoin(one.getGameCoin());
            listVO.add(oneVO);
        }
        OneLotteryInfoVO[] arr = new OneLotteryInfoVO[listVO.size()];
        listVO.toArray(arr);
        vo.setOneLotteryInfoVOs(arr);
        return vo;
    }

    public void rouletteInfoVO(List<RouletteHist> list, RouletteVO vo){
        RouletteHistVO[] vos = new RouletteHistVO[list.size()];
        int i = 0;
        for(RouletteHist rh:list){
           vos[i++] = rh.getVo();
        }
        vo.setInfoVO(vos);
    }

}
