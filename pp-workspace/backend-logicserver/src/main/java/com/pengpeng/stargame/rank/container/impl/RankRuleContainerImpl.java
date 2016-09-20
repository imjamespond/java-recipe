package com.pengpeng.stargame.rank.container.impl;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.dao.IFarmRankDao;
import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.fashion.dao.IFashionRankDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.IPlayerMoneyRankDao;
import com.pengpeng.stargame.rank.container.IRankRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomRankDao;
import com.pengpeng.stargame.small.game.container.ISmallGameContainer;
import com.pengpeng.stargame.small.game.dao.ISmallGameSetDao;
import com.pengpeng.stargame.vo.piazza.collectcrop.MemberCollectVO;
import com.pengpeng.stargame.vo.rank.RankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: mql
 * Date: 14-4-9
 * Time: 下午6:04
 */
@Component
public class RankRuleContainerImpl implements IRankRuleContainer {
    @Autowired
    private IPlayerMoneyRankDao playerMoneyRankDao;
    @Autowired
    private IFarmRankDao farmRankDao;
    @Autowired
    private IFashionRankDao fashionRankDao;
    @Autowired
    private IRoomRankDao roomRankDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IFamilyBuildDao familyBuildDao;
    @Autowired
    private IFamilyMemberDao familyMemberDao;
    @Autowired
    private ISmallGameSetDao smallGameSetDao;
    @Autowired
    private ISmallGameContainer smallGameContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;
    @Autowired
    private IFashionPlayerDao fashionPlayerDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;

    @Override
    public RankVO[] getRankVO(int type) {
        List<RankVO> rankVOList=new ArrayList<RankVO>();
        //    @Desc("排行类型 10游戏币排行 20家园等级 30房间豪华度 40时尚指数  50家族等级 60家族人数  70粉丝值" +
//            "2疯狂狗仔队 1明星连连看")
        /**
         * 游戏币的排行榜
         */
        if(type==10){
            Set<ZSetOperations.TypedTuple<String>> set=playerMoneyRankDao.getReverseRangeWithScores("",0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        /**
         * 家园等级
         */
        if(type==20){
            Set<ZSetOperations.TypedTuple<String>> set=farmRankDao.getReverseRangeWithScores("",0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        /**
         * 房间豪华度
         */
        if(type==30){
            Set<ZSetOperations.TypedTuple<String>> set=roomRankDao.getReverseRangeWithScores("",0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        /**
         *时尚指数
         */
        if(type==40){
            Set<ZSetOperations.TypedTuple<String>> set=fashionRankDao.getReverseRangeWithScores("",0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        /**
         * 家族等级
         */
        if(type==50){
            Set<String> keys =new HashSet<String>();
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                    keys.add(familyRule.getId());
            }

            Map<String,Family> familyMap=familyDao.mGet(keys);
           for(FamilyRule familyRule:familyRuleContainer.getAll()){
               RankVO rankVO=new RankVO();
               Family family=familyMap.get(familyRule.getId());
               if(family==null){
                   family=familyDao.getBean(familyRule.getId());
               }
               rankVO.setfId(familyRule.getId());
               rankVO.setfName(family.getName());
               FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
               if (bi!=null){
                   int allLevel=0;
                   for(int a:bi.getBuilds().values()){
                       allLevel+=a;
                   }
                   rankVO.setValue(allLevel);
                   rankVO.setValue1(bi.getLevel(FamilyConstant.BUILD_CASTLE));
               }

               rankVOList.add(rankVO);
           }
        }
        /**
         * 家族人数
         */
        if(type==60){
            Set<String> keys =new HashSet<String>();
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                keys.add(familyRule.getId());
            }

            Map<String,Family> familyMap=familyDao.mGet(keys);
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                RankVO rankVO=new RankVO();
                Family family=familyMap.get(familyRule.getId());
                if(family==null){
                    family=familyDao.getBean(familyRule.getId());
                }
                rankVO.setfId(familyRule.getId());
                rankVO.setfName(family.getName());
                rankVO.setValue((int)familyMemberDao.size(familyRule.getId()));
                FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
                if (bi!=null){
                    rankVO.setValue1(bi.getLevel(FamilyConstant.BUILD_CASTLE));
                }
                rankVOList.add(rankVO);
            }
        }
        /**
         * 家族周粉丝值
         */
        if(type==70){
            Set<String> keys =new HashSet<String>();
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                keys.add(familyRule.getId());
            }

            Map<String,Family> familyMap=familyDao.mGet(keys);
            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                RankVO rankVO=new RankVO();
                Family family=familyMap.get(familyRule.getId());
                if(family==null){
                    family=familyDao.getBean(familyRule.getId());
                }
                rankVO.setfId(familyRule.getId());
                rankVO.setfName(family.getName());
                rankVO.setValue(family.getWeekFansValue());
                rankVO.setValue1(family.getFansValue());
                rankVOList.add(rankVO);
            }
        }
        /**
         * 明星连连看
         */
        if(type==1){
            String weekKey = smallGameContainer.getWeekKey(1) ;
            Set<ZSetOperations.TypedTuple<String>> set=smallGameSetDao.getReverseRangeWithScores(weekKey,0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        /**
         * 狗仔队
         */
        if(type==2){
            String weekKey = smallGameContainer.getWeekKey(2) ;
            Set<ZSetOperations.TypedTuple<String>> set=smallGameSetDao.getReverseRangeWithScores(weekKey,0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        /**
         * 2048
         */
        if(type==3){
            String weekKey = smallGameContainer.getWeekKey(3) ;
            Set<ZSetOperations.TypedTuple<String>> set=smallGameSetDao.getReverseRangeWithScores(weekKey,0,50);
            Set<String> pids=new HashSet<String>();

            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                pids.add(typedTuple.getValue());
            }
            Map<String,Player> playerMap=playerDao.mGet(pids);
            Map<String,FamilyMemberInfo> memberInfoMap=familyMemberInfoDao.mGet(pids);
            for(ZSetOperations.TypedTuple<String> typedTuple:set){
                RankVO rankVO=new RankVO();
                String pid=typedTuple.getValue();
                rankVO.setpId(pid);
                rankVO.setValue(typedTuple.getScore().intValue());
                rankVO.setpName(playerMap.get(pid).getNickName());
                FamilyMemberInfo familyMemberInfo=memberInfoMap.get(pid);
                if(familyMemberInfo!=null){
                    String fid=memberInfoMap.get(pid).getFamilyId();
                    rankVO.setfId(fid);
                    rankVO.setfName(familyRuleContainer.getElement(fid).getName());
                }

                rankVOList.add(rankVO);
            }
        }
        return rankVOList.toArray(new RankVO[0]);
    }

    @Override
    public void initRankValue(String pid) {
        if(!playerMoneyRankDao.contains("",pid)){
           Player player=playerDao.getBean(pid);
           playerMoneyRankDao.addBean("", pid, player.getGameCoin());
        }
        if(!farmRankDao.contains("",pid)){
            int farmLevel=farmPlayerDao.getFarmLevel(pid);
            farmRankDao.addBean("",pid,farmLevel);
        }
        if(!fashionRankDao.contains("",pid)){
            FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(pid);
            FashionCupboard fashionCupboard=fashionCupboardDao.getBean(pid);
            fashionRankDao.addBean("",pid,fashionItemRuleContainer.getFasionValue(fashionPlayer,fashionCupboard));
        }
    }
}
