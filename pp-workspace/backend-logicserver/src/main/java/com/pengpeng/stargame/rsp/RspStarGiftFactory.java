package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.StarGift;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IGiftBoxRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IStarGiftDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.vo.piazza.StarGiftPageVO;
import com.pengpeng.stargame.vo.piazza.StarGiftVO;
import com.pengpeng.stargame.vo.piazza.StarRankVO;
import com.pengpeng.stargame.vo.piazza.StarSendPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-7-5
 * Time: 上午9:46
 */
@Component()
public class RspStarGiftFactory extends RspFactory{
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IStarGiftDao starGiftDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IGiftBoxRuleContainer giftBoxRuleContainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    public StarGiftPageVO getStarGiftPageVO(String familyId,int pageNo,int type){
        String familyIdKey = new String(familyId);
        if(type == 1){
            familyIdKey += starGiftDao.goldCoin();
        }
        int pageSize=3;
        StarGiftPageVO starGiftPageVO=new StarGiftPageVO();
        Family family=familyDao.getBean(familyId);
        starGiftPageVO.setStar(familyRuleContainer.getElement(familyId).getStarName());
        starGiftPageVO.setFansValues(family.getFansValue());
        int maxSize=  starGiftDao.getGiftSize(familyIdKey) ;
        int maxPage=maxSize%pageSize>0?maxSize/pageSize+1: maxSize/pageSize;
        starGiftPageVO.setMaxPage(maxPage);
        if(pageNo==0){
            pageNo=1;
        }
        starGiftPageVO.setPageNo(pageNo);
        if(eventRuleContainer.openStarSendGiftDrop()){
           starGiftPageVO.setEventStart(1);
        }
        /**
         * 设置 礼物 列表数据
         */
        List<StarGift>   starGiftList=starGiftDao.getGiftList(familyIdKey,(pageNo-1)*pageSize,pageNo*pageSize-1);
        List<StarGiftVO> starGiftVOList=new ArrayList<StarGiftVO>();
        for(StarGift starGift:starGiftList){
            StarGiftVO starGiftVO=new StarGiftVO();
            Player player=playerDao.getBean(starGift.getPid());
            BaseItemRule baseItemRule=baseItemRulecontainer.getElement(starGift.getId());
            starGiftVO.setPname(player.getNickName());
            starGiftVO.setName(baseItemRule.getName());
            starGiftVO.setIcon(baseItemRule.getIcon());
            starGiftVO.setFansValues(baseItemRule.getFansValues());
            starGiftVO.setPortrait(getUserPortrait(player.getUserId()));
            starGiftVO.setWords(starGift.getWords());
            starGiftVO.setNum(starGift.getNum());
            starGiftVO.setEventStart(starGift.getEventStart());
            if(starGiftVO.getEventStart()==1){
                starGiftVO.setFansValues((int) (baseItemRule.getFansValues()*1.5));
            } else {
                starGiftVO.setFansValues(baseItemRule.getFansValues());
            }
            starGiftVOList.add(starGiftVO);
        }
        starGiftPageVO.setGiftVOs(starGiftVOList.toArray(new StarGiftVO[0]));

        /**
         * 设置 前 三名 排行

        List<Family> familyList=familyDao.getRank();
        List<Family> familyListTop=familyList.subList(0,3);
        List<StarRankVO> starRankVOListTop=new ArrayList<StarRankVO>();
        int i=0;
        for(Family familyTemp:familyListTop){
            i++;
            starRankVOListTop.add(getStarRankVO(familyTemp,i));
        }
        starGiftPageVO.setTopRank(starRankVOListTop.toArray(new StarRankVO[0]));
        */
        /**
         * 设置 自己的排行

        int myRank=0;
        for(int j=0;j<familyList.size();j++){
             if(familyList.get(j).getId().equals(familyId)){
                   myRank=j+1;
                  break;
             }
        }
        if(myRank<1000){
        List<StarRankVO> starRankVOListMy=new ArrayList<StarRankVO>();
        if(myRank>1){
            starRankVOListMy.add(getStarRankVO(familyList.get(myRank-2),myRank-1));
        }
        starRankVOListMy.add(getStarRankVO(familyList.get(myRank-1),myRank));
        starGiftPageVO.setMyRank(starRankVOListMy.toArray(new StarRankVO[0]));
        }
        */
        /**
         * 设置 前 5名 排行
         */
         List<Family> familyList=familyDao.getRank();//排序
         List<Family> familyListTop=familyList.subList(0,5);//取前五
         List<StarRankVO> starRankVOListTop=new ArrayList<StarRankVO>(); //VO
        boolean meInTop5 = false;//我在前五
         int i=0;
         for(Family familyTemp:familyListTop){
             if(familyId.equals(familyTemp.getId())){
                 meInTop5 = true;
             }
         }
         if(meInTop5){
             for(Family familyTemp:familyListTop){
                 i++;
                 starRankVOListTop.add(getStarRankVO(familyTemp,i));
             }
         }else{//不在前5只取前3
             for(Family familyTemp:familyListTop){
                 if(++i>3){
                     break;
                 }
                 starRankVOListTop.add(getStarRankVO(familyTemp,i));
             }
             //我的排名
             int myRank=0;
             boolean detected = false;
             Family previous = null;
             int preRank = 0;
             for(int j=0;j<familyList.size();j++){

                 if(familyList.get(j).getId().equals(familyId)){
                     myRank=j+1;
                     detected = true;//找到了我
                 }else{
                     preRank=j+1;
                     previous = familyList.get(j);//保存做为前一名
                 }

                 if(detected){
                     starRankVOListTop.add(getStarRankVO(previous,preRank));
                     starRankVOListTop.add(getStarRankVO(familyList.get(j),myRank));
                     break;
                 }
             }

         }

         starGiftPageVO.setTopRank(starRankVOListTop.toArray(new StarRankVO[0]));


        return starGiftPageVO;
    }

    public StarRankVO getStarRankVO(Family family,int rank){
        StarRankVO starRankVO=new StarRankVO();
        starRankVO.setStar(familyRuleContainer.getElement(family.getId()).getStarName());
        starRankVO.setIcon(familyRuleContainer.getElement(family.getId()).getStarIcon());
        starRankVO.setFansValues(family.getFansValue());
        starRankVO.setRank(rank);
        return starRankVO;
    }

    public StarSendPageVO getStarSendPageVO(String pid,int type){
        StarSendPageVO starSendPageVO=new StarSendPageVO();
        List <StarGiftVO> starGiftVOList=new ArrayList<StarGiftVO>();
        FamilyRule familyRule =familyRuleContainer.getElement(familyMemberInfoDao.getFamilyMember(pid).getFamilyId());
        starSendPageVO.setIcon(familyRule.getStarIcon());
        starSendPageVO.setWords(familyRule.getWords());
        if(eventRuleContainer.openStarSendGiftDrop()){
            starSendPageVO.setEventStart(1);
        }
        if(type==1){

            List<BaseItemRule> baseItemRuleList=giftBoxRuleContainer.getExquisitetGift();
            for(BaseItemRule baseItemRule:baseItemRuleList){
                StarGiftVO starGiftVO=new StarGiftVO();
                starGiftVO.setId(baseItemRule.getId());
                starGiftVO.setName(baseItemRule.getName());
                starGiftVO.setIcon(baseItemRule.getIcon());
                if(eventRuleContainer.openStarSendGiftDrop()){
                    starGiftVO.setFansValues((int) (baseItemRule.getFansValues()*1.5));
                }else {
                    starGiftVO.setFansValues(baseItemRule.getFansValues());
                }
                starGiftVO.setGold(baseItemRule.getGoldPrice());
                starGiftVOList.add(starGiftVO);
            }

        }

        if(type==2){
            List<BaseItemRule> baseItemRuleList=giftBoxRuleContainer.getPtGift();
            for(BaseItemRule baseItemRule:baseItemRuleList){
                StarGiftVO starGiftVO=new StarGiftVO();
                starGiftVO.setId(baseItemRule.getId());
                starGiftVO.setName(baseItemRule.getName());
                starGiftVO.setIcon(baseItemRule.getIcon());
                if(eventRuleContainer.openStarSendGiftDrop()){
                    starGiftVO.setFansValues((int) (baseItemRule.getFansValues()*1.5));
                }else {
                    starGiftVO.setFansValues(baseItemRule.getFansValues());
                }
                starGiftVO.setGold(baseItemRule.getGoldPrice());

                int num=baseItemRulecontainer.getGoodsNum(pid,baseItemRule.getId());


                if(num>0){
                    starGiftVO.setNum(num);
                    starGiftVOList.add(starGiftVO);
                }

            }

        }
        starSendPageVO.setStarGiftVOs(starGiftVOList.toArray(new StarGiftVO[0]));

        return  starSendPageVO;
    }
}
