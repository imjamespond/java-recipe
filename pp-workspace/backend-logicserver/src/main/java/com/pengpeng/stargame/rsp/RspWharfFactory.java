package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.wharf.PlayerWharf;
import com.pengpeng.stargame.model.wharf.WharfConstant;
import com.pengpeng.stargame.model.wharf.WharfInfo;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.vo.wharf.WharfInfoVO;
import com.pengpeng.stargame.vo.wharf.WharfPlayerRankVO;
import com.pengpeng.stargame.wharf.dao.IPlayerWharfDao;
import com.pengpeng.stargame.wharf.dao.IWharfRankSetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */


@Component
public class RspWharfFactory extends RspFactory {
    @Autowired
    private IPlayerWharfDao playerWharfDao;
    @Autowired
    private IWharfRankSetDao wharfRankSetDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    public WharfInfoVO wharfInfoVO(WharfInfo wharfInfo) {

        return wharfInfo;
    }

    public WharfInfoVO wharfInfoSailVO(PlayerWharf wharfInfo) {
        WharfInfoVO wharfInfoVO = new WharfInfoVO();
        wharfInfoVO.setAnimType(WharfConstant.ANIM_LEAVING);
        wharfInfoVO.setEnable(wharfInfo.isEnable());
        wharfInfoVO.setShipArrived(wharfInfo.isShipArrived());
        wharfInfoVO.setRestTime(WharfConstant.HOUR5);
        wharfInfoVO.setCredit(wharfInfo.getCredit());
        wharfInfoVO.setContribution(wharfInfo.getContribution());
        return wharfInfoVO;
    }

    public WharfInfo wharfInfoVO1(PlayerWharf wharfInfo) {
        WharfInfo wharfInfoVO = new WharfInfo();
        wharfInfoVO.setEnable(wharfInfo.isEnable());
        wharfInfoVO.setShipArrived(wharfInfo.isShipArrived());
        Date now = new Date();
        long restTime = 0l;
        long diff = now.getTime() - wharfInfo.getRefreshTime().getTime();
        if (wharfInfo.isShipArrived()) {
            if (diff < WharfConstant.HOUR24) {
                restTime = WharfConstant.HOUR24 - diff;
            }else {
                wharfInfoVO.setAnimType(WharfConstant.ANIM_LEAVING);
                wharfInfoVO.setShipArrived(false);
            }
        } else {
            if (diff < WharfConstant.HOUR5) {
                restTime = WharfConstant.HOUR5 - diff;
            }
        }
        wharfInfoVO.setRestTime(restTime);
        return wharfInfoVO;
    }

    public WharfInfoVO wharfRankVO2(String weekKey, String familyId) {
        WharfInfoVO wharfInfo = new WharfInfo();
        Set<ZSetOperations.TypedTuple<String>> set= wharfRankSetDao.getReverseRangeWithScores(weekKey,0,50);
        WharfPlayerRankVO[] pRanks = new WharfPlayerRankVO[set.size()];
        int i = 0;
        Iterator<ZSetOperations.TypedTuple<String>> it = set.iterator();
        while(it.hasNext()){
            ZSetOperations.TypedTuple<String> tuple = it.next();
            String pid = tuple.getValue();
            WharfPlayerRankVO wp = new WharfPlayerRankVO();
            wp.setwContribution(tuple.getScore().intValue());

            PlayerWharf pw = playerWharfDao.getPlayerWharf(pid);
            if(null!=pw){
                wp.setContribution(pw.getAmountContri());
            }

            Player prePlayer = playerDao.getBean(pid);
            if (prePlayer != null) {
                wp.setName(prePlayer.getNickName());
                wp.setAvatar(getUserPortrait(prePlayer.getUserId()));
            }

            FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
            if (farmPlayer != null) {
                wp.setLevel(farmPlayer.getLevel());
            }

            pRanks[i++] = wp;
        }
        wharfInfo.setRank(pRanks);

        Set<ZSetOperations.TypedTuple<String>> fset= wharfRankSetDao.getReverseRangeWithScores(familyId+weekKey,0,50);
        WharfPlayerRankVO[] fpRanks = new WharfPlayerRankVO[set.size()];
        i = 0;
        Iterator<ZSetOperations.TypedTuple<String>> fit = fset.iterator();
        while(fit.hasNext()){
            ZSetOperations.TypedTuple<String> tuple = fit.next();
            String pid = tuple.getValue();
            WharfPlayerRankVO wp = new WharfPlayerRankVO();
            wp.setwContribution(tuple.getScore().intValue());

            PlayerWharf pw = playerWharfDao.getPlayerWharf(pid);
            if(null!=pw){
                wp.setContribution(pw.getAmountContri());
            }

            Player prePlayer = playerDao.getBean(pid);
            if (prePlayer != null) {
                wp.setName(prePlayer.getNickName());
                wp.setAvatar(getUserPortrait(prePlayer.getUserId()));
            }

            FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(pid, System.currentTimeMillis());
            if (farmPlayer != null) {
                wp.setLevel(farmPlayer.getLevel());
            }

            fpRanks[i++] = wp;
        }
        wharfInfo.setFamRank(fpRanks);
        return wharfInfo;
    }

    public WharfInfoVO wharfInfoVO3() {
        WharfInfoVO wharfInfoVO = new WharfInfo();
        wharfInfoVO.setEnable(false);
        return wharfInfoVO;
    }

}
