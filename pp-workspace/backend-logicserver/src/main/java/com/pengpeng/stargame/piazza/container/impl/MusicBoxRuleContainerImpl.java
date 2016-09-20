package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MusicBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IMusicBoxRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IMusicBoxDao;
import com.pengpeng.stargame.piazza.rule.MusicBoxRule;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.piazza.MusicItemVO;
import com.pengpeng.stargame.vo.piazza.MusicZanVO;
import com.pengpeng.user.SongListItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-5上午11:10
 */
@Component
public class MusicBoxRuleContainerImpl extends HashMapContainer<String,MusicBoxRule> implements IMusicBoxRuleContainer {
    @Autowired
    private IMusicBoxDao musicBoxDao;
    @Autowired
    private IPlayerDao playerDao;

    public static String id = "music_zan";
    public static int COIN = 5;
    //public static int ZAN_TOTAL = 5;

    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;

    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public List<MusicItemVO> getTopList(int num) {
        List<MusicItemVO> list = new ArrayList<MusicItemVO>();
        List<SongListItemInfo> items = siteDao.getTopSongList(num);
        for(SongListItemInfo info:items){
           MusicItemVO musicItemVO= new MusicItemVO(info.getSongListId(),info.getSongListItemId(),info.getSongName(),info.getSongLink(),info.getSingerName());
           musicItemVO.setYear(info.getYear());
            musicItemVO.setIssue(info.getIssue());
            musicItemVO.setVotes(info.getDoyenAmount());

            list.add(musicItemVO);
        }
        return list;
    }

    @Override
    public List<MusicItemVO> getList() {
        List<MusicItemVO> list = new ArrayList<MusicItemVO>();
        List<SongListItemInfo> items = siteDao.getSongList();
        for(SongListItemInfo info:items){

            list.add(new MusicItemVO(info.getSongListId(),info.getSongListItemId(),info.getSongName(),info.getSongLink(),info.getSingerName()));
        }
        return list;
    }

    @Override
    public MusicZanVO getZanInfo(MusicBox box) {
        int num = box.getZan();//已赞次数
        int coin = COIN;//赞一次多少钱
        int free = getFreeZanNum(box.getPid())-num;//免费赞次数
        MusicZanVO info = new MusicZanVO(num,coin,free);
        return info;
    }

    @Override
    public void checkZan(Player player,MusicBox box,Integer listId,Integer listItemId) throws GameException {
        //MusicBoxRule rule = this.getElement(id);
//        if (!rule.checkZan(box.getZan())){
//            exceptionFactory.throwRuleException("music.box.maxzan");
//        }
//        if (box.isZan(listItemId)){
//            exceptionFactory.throwRuleException("music.box.onezan");
//        }

//
//        boolean member = payMemberRuleContainer.isPayMember(box.getPid());
//        if (member){//如果不是vip 或者 免费次数超过3次
//            if (box.getZan()>=getFreeZanNum(player.getId())&&player.getGoldCoin()<COIN){
//                exceptionFactory.throwRuleException("goldcoin.notenough");
//            }
//        }else{
//            if (player.getGoldCoin()<COIN){
//                exceptionFactory.throwRuleException("goldcoin.notenough");
//            }
//        }

        if(getFreeZanNum(player.getId())<=box.getZan()){ //如果 没有赞的免费 次数，扣钱
            if (player.getGoldCoin()<COIN){
                exceptionFactory.throwRuleException("goldcoin.notenough");
            }
        }



    }

    @Override
    public void zan(Player player, MusicBox box,Integer listId,Integer listItemId) throws GameException {

        FamilyMemberInfo familyMemberInfo =familyMemberInfoDao.getFamilyMember(player.getId());


        int vote=0;

        if(getFreeZanNum(player.getId())<=box.getZan()){ //如果 没有赞的免费 次数，扣钱

//                player.decGoldCoin(COIN);//扣除5个达人币
            playerRuleContainer.decGoldCoin(player,COIN, PlayerConstant.GOLD_ACTION_11);
        } else {
            box.incZan(1,listItemId);
        }
        if(familyMemberInfo.getIdentity()== FamilyConstant.TYPE_ZL){
            vote=2;
        } else {
            vote=1;
        }



        /**
         * 统计票数
         */
        if(box.getZanInfo()==null||box.getSongListId()!=listId){
            box.setZanInfo(new HashMap<Integer, Integer>());
            box.setSongListId(listId);
        }
        if(box.getZanInfo().containsKey(listItemId)){
            box.getZanInfo().put(listItemId,box.getZanInfo().get(listItemId)+vote);
        }else{
            box.getZanInfo().put(listItemId,vote);
        }
        musicBoxDao.saveBean(box);
        playerDao.saveBean(player);




        int val=0;
        if(familyMemberInfo.getIdentity()== FamilyConstant.TYPE_ZL){//明星助理 赞一次 算两票

             val =  siteDao.zan(player.getUserId(),listId,listItemId,2);
        } else {

             val =  siteDao.zan(player.getUserId(),listId,listItemId,1);
        }

        if (val==0){//成功
            return;
        }else if (val==1){//失败
            exceptionFactory.throwRuleException("music.box.zanerror");
        }else{//超过上限
//            exception?Factory.throwRuleException("music.box.zanerror");
        }

    }

    @Override
    public int getFreeZanNum(String pid) {
        MusicBox box =  musicBoxDao.getBean(pid);
        int maxNum= box.getFreeNum();
        if(payMemberRuleContainer.isPayMember(pid)){
            maxNum+=payMemberRuleContainer.getFreeZanNum(pid);
        }
        return maxNum;
    }
}
