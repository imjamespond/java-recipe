package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.small.game.PlayerSmallGame;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.small.game.dao.IPlayerSmallGameDao;
import com.pengpeng.stargame.small.game.dao.ISmallGameSetDao;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.Page;
import com.pengpeng.stargame.vo.smallgame.PlayerSmallGameRankVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameListVO;
import com.pengpeng.stargame.vo.smallgame.SmallGameRankVO;
import com.pengpeng.stargame.vo.successive.SuccessiveInfoVO;
import com.pengpeng.stargame.vo.successive.SuccessivePrizeVO;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */


@Component
public class RspSmallGameFactory extends RspFactory {
    @Autowired
    private ISmallGameSetDao smallGameSetDao;
    @Autowired
    private IPlayerSmallGameDao playerSmallGameDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFriendDao friendDao;

    public SmallGameRankVO smallGameRankVO(String myPid, String maxKey, String weekKey, PlayerSmallGame playerSmallGame, int type) {
        SmallGameRankVO smallGameRankVO = new SmallGameRankVO();
        int weekNum = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        Player player = playerDao.getBean(myPid);
        //我的排行
        Double myScore = smallGameSetDao.getScore(weekKey, myPid);
        Long myRank = smallGameSetDao.getRank(weekKey, myPid);

        //好友排行
        Set<PlayerSmallGameRankVO> friendPlayerSmallGameRankVOs = new TreeSet<PlayerSmallGameRankVO>(new Comparator<PlayerSmallGameRankVO>() {
            @Override
            public int compare(PlayerSmallGameRankVO o1, PlayerSmallGameRankVO o2) {
                return o1.getScore()<o2.getScore()?1:-1;
            }
        });
        //好友查询
        PlayerSmallGameRankVO fme = null;
        if (playerSmallGame.getScoreWeek().containsKey(type)) {
            if (playerSmallGame.getScore().containsKey(type)) {
                fme = new PlayerSmallGameRankVO();
                fme.setRank(0);//mark as self
                fme.setNickName(player.getNickName());
                fme.setPortrait(getUserPortrait(player.getUserId()));
                fme.setId(myPid);
                //判定本周的分数

                if (playerSmallGame.getScoreWeek().get(type) == weekNum) {
                    fme.setScore(playerSmallGame.getScore().get(type));
                }

                friendPlayerSmallGameRankVOs.add(fme);
            }
        }

        Friend my = friendDao.getBean(myPid);
        Map<String, PlayerSmallGame> fMap = playerSmallGameDao.mGet(my.getFriends().keySet());
        Iterator<Map.Entry<String, PlayerSmallGame>> fIt = fMap.entrySet().iterator();
        while (fIt.hasNext()) {
            PlayerSmallGame psg = fIt.next().getValue();
            PlayerSmallGameRankVO f = new PlayerSmallGameRankVO();
            f.setId(psg.getPid());
            if (psg.getScoreWeek() == null) {
                continue;
            } else if (!psg.getScoreWeek().containsKey(type)) {
                continue;
            } else if (psg.getScoreWeek().get(type) != weekNum) {
                continue;//忽略本周数据
            }

            if (psg.getScore() != null) {
                if (psg.getScore().containsKey(type)) {
                    f.setScore(psg.getScore().get(type));
                }
            }

            friendPlayerSmallGameRankVOs.add(f);
        }
        //取7个
        int i = 0;
        boolean top7 = false;
        int size = friendPlayerSmallGameRankVOs.size() > 7 ? 7 : friendPlayerSmallGameRankVOs.size();
        PlayerSmallGameRankVO[] friendArr = new PlayerSmallGameRankVO[size];
        PlayerSmallGameRankVO lastPsgr = null;
        Iterator<PlayerSmallGameRankVO> fIt2 = friendPlayerSmallGameRankVOs.iterator();
        while (fIt2.hasNext()) {
            PlayerSmallGameRankVO psgr = fIt2.next();
            if (i < 7) {
                setPortrait(psgr);
                friendArr[i] = psgr;
            }else if(psgr.getRank() == 1) {//marked as self
                setPortrait(lastPsgr);
                friendArr[5] = lastPsgr;
                friendArr[6] = fme;
                fme.setRank(i);
                break;
            }
            psgr.setRank(i);
            lastPsgr = psgr;
            i++;
        }
        smallGameRankVO.setFriendPlayerSmallGameRankVOs(friendArr);


        //本周排名
        PlayerSmallGameRankVO me = null;
        if (null != myRank && null != myScore) {
            me = new PlayerSmallGameRankVO();
            me.setId(myPid);
            me.setScore(myScore.intValue());
            me.setRank(myRank.intValue());
            me.setNickName(player.getNickName());
            me.setPortrait(getUserPortrait(player.getUserId()));
        }
        Set<ZSetOperations.TypedTuple<String>> weekSet = smallGameSetDao.getReverseRangeWithScores(weekKey, 0, 6);
        PlayerSmallGameRankVO[] weekArr = new PlayerSmallGameRankVO[7];
        Iterator<ZSetOperations.TypedTuple<String>> weekIt = weekSet.iterator();
        i = 0;
        top7 = false;
        while (weekIt.hasNext()) {
            PlayerSmallGameRankVO rankVO = new PlayerSmallGameRankVO();
            ZSetOperations.TypedTuple<String> typedTuple = weekIt.next();
            //ID
            String pid = typedTuple.getValue();
            rankVO.setId(pid);
            //第七名后
            if (myPid.equals(pid)) {
                top7 = true;
            }
            //积分
            rankVO.setScore(typedTuple.getScore().intValue());
            //昵称
            setPortrait(rankVO);
            //名次
            rankVO.setRank(smallGameSetDao.getRank(weekKey, pid).intValue());
            weekArr[i] = rankVO;
            i++;
        }
        if (!top7) {
            PlayerSmallGameRankVO pre = new PlayerSmallGameRankVO();

            if (null != me) {
                //显示为最末
                weekArr[6] = me;

                int preRank = me.getRank() - 2;//getRank为真实名次
                Set<String> preSet = smallGameSetDao.getMembers(weekKey, preRank, preRank);
                Iterator<String> it = preSet.iterator();
                if (it.hasNext()) {
                    String pid = it.next();
                    Double preScore = smallGameSetDao.getScore(weekKey, pid);
                    pre.setId(pid);
                    pre.setScore(preScore.intValue());
                    pre.setRank(preRank);
                    setPortrait(pre);
                }
                weekArr[5] = pre;
            }
        }
        smallGameRankVO.setWeekPlayerSmallGameRankVOs(weekArr);

        //历史最高
        Set<String> maxSet = smallGameSetDao.getMembers(maxKey, 0, 0);
        Iterator<String> maxit = maxSet.iterator();
        if (maxit.hasNext()) {
            String pid = maxit.next();
            Double score = smallGameSetDao.getScore(maxKey, pid);
            if (null != score)
                smallGameRankVO.setMaxScore(score.intValue());
        }
        //周排名
        if (null == myRank) {
            smallGameRankVO.setWeekRank(-1);
        } else {
            smallGameRankVO.setWeekRank(myRank.intValue());
        }
        if (playerSmallGame.getGoldTime().containsKey(type))
            smallGameRankVO.setGoldTime(playerSmallGame.getGoldTime().get(type));
        if (playerSmallGame.getFreeTime().containsKey(type))
            smallGameRankVO.setFreeTime(playerSmallGame.getFreeTime().get(type));
        smallGameRankVO.setType(type);
        return smallGameRankVO;
    }

    public SmallGameRankVO smallGameRankVO2(PlayerSmallGame playerSmallGame, int type) {
        SmallGameRankVO smallGameRankVO = new SmallGameRankVO();
        if (playerSmallGame.getGoldTime().containsKey(type))
            smallGameRankVO.setGoldTime(playerSmallGame.getGoldTime().get(type));
        if (playerSmallGame.getFreeTime().containsKey(type))
            smallGameRankVO.setFreeTime(playerSmallGame.getFreeTime().get(type));
        smallGameRankVO.setType(type);
        return smallGameRankVO;
    }

    public SmallGameRankVO smallGameRankVO3(String myPid, String name, String price, String maxKey, String weekKey, PlayerSmallGame playerSmallGame, int type) {
        SmallGameRankVO smallGameRankVO = smallGameRankVO(myPid, maxKey, weekKey, playerSmallGame, type);

        smallGameRankVO.setFanVal(10);
        smallGameRankVO.setName(name);
        smallGameRankVO.setPrice(price);
        return smallGameRankVO;
    }

    public SmallGameRankVO smallGameRankVO4(String myPid, String name, PlayerSmallGame playerSmallGame, String price, String maxKey, String weekKey, int type) {

        SmallGameRankVO smallGameRankVO = new SmallGameRankVO();
        int weekNum = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        //我的排行
        Double myScore = smallGameSetDao.getScore(weekKey, myPid);
        Long myRank = smallGameSetDao.getRank(weekKey, myPid);

        //好友排行
        Set<PlayerSmallGameRankVO> friendPlayerSmallGameRankVOs = new TreeSet<PlayerSmallGameRankVO>();
        //好友查询
        PlayerSmallGameRankVO fme = null;
        if (null != myRank && null != myScore) {
            fme = new PlayerSmallGameRankVO();
            fme.setRank(0);//mark as self
            if (playerSmallGame.getScoreWeek().containsKey(type)) {
                if (playerSmallGame.getScoreWeek().get(type) == weekNum) {
                    if (playerSmallGame.getScore().containsKey(type)) {
                        fme.setScore(playerSmallGame.getScore().get(type));
                    }
                }
            }

            friendPlayerSmallGameRankVOs.add(fme);
        }
        Friend my = friendDao.getBean(myPid);

        Map<String, PlayerSmallGame> fMap = playerSmallGameDao.mGet(my.getFriends().keySet());
        Iterator<Map.Entry<String, PlayerSmallGame>> fIt = fMap.entrySet().iterator();
        while (fIt.hasNext()) {
            PlayerSmallGame psg = fIt.next().getValue();
            PlayerSmallGameRankVO f = new PlayerSmallGameRankVO();
            if (psg.getScore() != null) {
                if (psg.getScoreWeek() == null) {
                    continue;
                } else if (!psg.getScoreWeek().containsKey(type)) {
                    continue;
                } else if (psg.getScoreWeek().get(type) != weekNum) {
                    continue;//忽略本周数据
                }
                if (psg.getScore().containsKey(type)) {
                    f.setScore(psg.getScore().get(type));
                }
            }
            friendPlayerSmallGameRankVOs.add(f);
        }

        int i = 0;
        PlayerSmallGameRankVO[] friendArr = new PlayerSmallGameRankVO[1];
        Iterator<PlayerSmallGameRankVO> fIt2 = friendPlayerSmallGameRankVOs.iterator();
        while (fIt2.hasNext()) {
            PlayerSmallGameRankVO tempVO = fIt2.next();
            if (tempVO.getRank() == 1) {
                tempVO.setRank(i);//更新名次
                friendArr[0] = tempVO;
                break;
            }
            ++i;
        }
        smallGameRankVO.setFriendPlayerSmallGameRankVOs(friendArr);

        //本周排名
        PlayerSmallGameRankVO[] weekArr = new PlayerSmallGameRankVO[1];
        PlayerSmallGameRankVO me = null;
        if (null != myRank && null != myScore) {
            me = new PlayerSmallGameRankVO();
            me.setScore(myScore.intValue());
            me.setRank(myRank.intValue());
        }
        weekArr[0] = me;
        smallGameRankVO.setWeekPlayerSmallGameRankVOs(weekArr);

        smallGameRankVO.setName(name);
        smallGameRankVO.setType(type);
        return smallGameRankVO;
    }

    public SmallGameListVO SmallGameListVO(int size) {
        SmallGameListVO smallGameListVO = new SmallGameListVO();
        smallGameListVO.setSmallGameRankVOs(new SmallGameRankVO[size]);
        return smallGameListVO;
    }

    private void setPortrait(PlayerSmallGameRankVO vo){
        //昵称
        Player tempPlayer = playerDao.getBean(vo.getId());
        if (null != tempPlayer) {
            vo.setNickName(tempPlayer.getNickName());
            vo.setPortrait(getUserPortrait(tempPlayer.getUserId()));
        }
    }

    public int[] buyList(PlayerSmallGame playerSmallGame){
        List<Integer> buyList = new ArrayList<Integer>();
        int i = 0;
        int day = DateUtil.getDayOfYear(new Date());
        Iterator<Map.Entry<Integer,Date>> it = playerSmallGame.getBuyMap().entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Integer,Date> entry = it.next();
            if(DateUtil.getDayOfYear(entry.getValue()) == day){
                buyList.add(entry.getKey());
            }
        }
        return ArrayUtils.toPrimitive(buyList.toArray(new Integer[0]));
    }

}
