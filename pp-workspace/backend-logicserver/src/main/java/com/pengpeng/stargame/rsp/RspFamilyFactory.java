package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.piazza.FamilyBank;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyBuildingRuleContainer;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.vo.piazza.*;
import com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * User: mql
 * Date: 13-6-28
 * Time: 上午10:58
 */
@Component()
public class RspFamilyFactory extends RspFactory {
    @Autowired
    private IFamilyDao familyDao;

    @Autowired
    private IFamilyBuildingRuleContainer familyBuildingRuleContainer;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

    @Autowired
    private StatusRemote statusRemote;

    @Autowired
    private IFamilyBuildDao familyBuildDao;

    public FamilyPanelVO newFamilyPanelVO(Family family,FamilyMemberInfo memberInfo,FamilyBuildInfo bi,Player player) {
        FamilyPanelVO vo = new FamilyPanelVO();
        vo.setFamilyMemberVO(new FamilyMemberVO());
        vo.setFamilyPanelVO(newFamilyInfoVO(family,bi,memberInfo));

        FamilyMemberVO fmv = vo.getFamilyMemberVO();

        fmv.setContribution(memberInfo.getTotalContribution());//个人贡献
        fmv.setDayContribution(memberInfo.getDayContribution());
        fmv.setIdentity(memberInfo.getIdentity());
        //fmv.setLineStatus(memberInfo.get);//在面板中不用此数据
        fmv.setName(player.getNickName());
        fmv.setPid(memberInfo.getPid());
        //fmv.setProfession(player.get);//在面板中不用此数据
        fmv.setWeekContribution(memberInfo.getWeekContribution());
        int boon = familyRuleContainer.getBoon(bi,memberInfo);
        fmv.setWelfare(boon);
        return vo;
    }

    public FamilyVO newFamilyVO(Family family) {
        FamilyVO vo = new FamilyVO();
        vo.setId(family.getId());
        vo.setName(family.getName());
        long num = familyDao.getMemberSize(family.getId());
        vo.setMemberNum((int)num);
        vo.setFansValue(family.getFansValue());
        FamilyRule rule = familyRuleContainer.getElement(family.getId());
        if(rule!=null){
            vo.setStarIcon(rule.getStarIcon());
            vo.setStarId(rule.getStarId());
            vo.setStarName(rule.getStarName());
            vo.setChangeCoin(rule.getChangeCoin());
        }
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        if (bi!=null){
            vo.setLevel(bi.getLevel(FamilyConstant.BUILD_CASTLE));
        }
        return vo;
    }

    public FamilyMemberVO newFamilyMemberVO(FamilyBuildInfo bi,FamilyMemberInfo memberInfo,Player player) {
        FamilyMemberVO vo = new FamilyMemberVO();
        vo.setContribution(memberInfo.getContribution());//个人累计贡献,应该取  memberInfo.getTotalContribution()
        vo.setDayContribution(memberInfo.getDayContribution());
        vo.setWeekContribution(memberInfo.getWeekContribution());
        vo.setIdentity(memberInfo.getIdentity());
        //fmv.setLineStatus(memberInfo.get);//在面板中不用此数据
        vo.setName(player.getNickName());
        vo.setPid(memberInfo.getPid());
        //fmv.setProfession(player.get);//在面板中不用此数据
        //fmv.setWelfare(memberInfo.getwel);//在面板中不用此数据        return null;
        Session s = statusRemote.getSession(null,memberInfo.getPid());
        vo.setLineStatus(s==null?0:1);
        int boon = familyRuleContainer.getBoon(bi,memberInfo);
        vo.setWelfare(boon);
        return vo;
    }

    public FamilyInfoVO newFamilyInfoVO(Family family,FamilyBuildInfo bi,FamilyMemberInfo memberInfo) {
        FamilyInfoVO info = new FamilyInfoVO();
        info.setFunds(family.getFunds());
        if (bi!=null){
            info.setLevel(bi.getLevel(FamilyConstant.BUILD_CASTLE));
        }
        info.setName(family.getName());
        info.setNotice(family.getNotice());
        info.setQq(family.getQq());
        info.setYy(family.getYy());
        info.setId(family.getId());
        FamilyBuildingRule r = familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK,bi.getLevel(FamilyConstant.BUILD_BANK));
        if (r!=null){
            info.setMaxFunds(r.getMaxFunds());
        }
        FamilyRule rule = familyRuleContainer.getElement(family.getId());
        if (rule!=null){
            info.setStarName(rule.getStarName());
            info.setStarId(rule.getStarId());
            info.setStarIcon(rule.getStarIcon());
        }
        if(memberInfo!=null){
            boolean isboon = memberInfo.isBoon(new Date());
            info.setBooned(isboon);
            boolean free = !memberInfo.isWeekChangeFamily(new Date());
//            info.setWeekFree(free);
            if(memberInfo.getChangeFamilyNum()>=1){
                info.setWeekFree(false);
            }else {
                info.setWeekFree(true);
            }
        }
        return info;
    }
    public BuildVO newBuildVO(int type,int level) {
        BuildVO  vo = new BuildVO(type,level);
        FamilyBuildingRule rule = familyBuildingRuleContainer.getElement(type, level);
        FamilyBuildingRule nextRule = familyBuildingRuleContainer.getElement(type, level+1);
        if (rule!=null){
            if(nextRule!=null){
            vo.setFunds(nextRule.getLevelFunds());
            }else{
                vo.setFunds(0);
            }
            vo.setIcon(rule.getIcon());
            vo.setLevelRequirement(rule.getLevelRequirement());
            vo.setMemo(rule.getMemo());
            vo.setLevelEffect(rule.getLevelEffect());
            vo.setName(rule.getName());
            vo.setMaxLevel(rule.getMaxLevel());
        }
        return vo;
    }

    public BuildVO[] newBuildVO(Map<Integer, Integer> map) {
        if (map==null){
            return null;
        }
        BuildVO[] vos = new BuildVO[map.size()];
        int idx = 0;
        for(Map.Entry<Integer,Integer> entry:map.entrySet()){
            int type = entry.getKey();
            int val = entry.getValue();
            vos[idx] = newBuildVO(type,val);
            idx++;
        }
        return vos;
    }

    public FamilyDevoteVO newFamilyDevoteVO(FamilyRule rule) {
        FamilyDevoteVO vo = new FamilyDevoteVO();
        vo.setId(rule.getId());
        vo.setGameCoinDevote(rule.getGameCoinDevote());
        vo.setGameCoinFunds(rule.getGameCoinFunds());
        vo.setGoldCoinDevote(rule.getGoldCoinDevote());
        vo.setGoldCoinFunds(rule.getGoldCoinFunds());
        vo.setPropsDevote(rule.getPropsDevote());
        return vo;
    }

    public FamilyBankVO newFamilyBankVO(Player player,FamilyBank familyBank,FamilyBuildInfo familyBuildInfo,Family family){
        FamilyBankVO familyBankVO=new FamilyBankVO();
        FamilyBuildingRule familyBuildingRule=familyBuildingRuleContainer.getElement(FamilyConstant.BUILD_BANK,familyBuildInfo.getLevel(FamilyConstant.BUILD_BANK));
        familyBankVO.setFamilyId(familyBank.getFamilyId());
        familyBankVO.setRankLevel(familyBuildInfo.getLevel(FamilyConstant.BUILD_BANK));
        familyBankVO.setMaxRankLevel(familyBuildingRule.getMaxLevel());
        familyBankVO.setMaxValue(familyBuildingRule.getMaxSave());
        familyBankVO.setMyValue(familyBank.getMoenyByPid(player.getId()));
        familyBankVO.setMaxFund(familyBuildingRule.getMaxFunds());
        familyBankVO.setFund(family.getFunds());
        return familyBankVO;
    }
}
