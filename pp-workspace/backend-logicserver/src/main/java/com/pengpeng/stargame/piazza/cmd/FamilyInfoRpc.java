package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.piazza.PiazzaBuilder;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFamilyFactory;
import com.pengpeng.stargame.vo.piazza.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 家族信息
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午5:39
 */
@Component
public class FamilyInfoRpc extends RpcHandler {
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;

    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

    @Autowired
    private IFamilyBuildDao familyBuildDao;

    @Autowired
    PiazzaBuilder piazzaBuilder;
    @Autowired
    private RspFamilyFactory rspFamilyFactory;

//    @Deprecated
//    @RpcAnnotation(cmd = "family.info.my", req = FamilyReq.class, name = "自己所在的家族信息",vo= FamilyInfoVO.class)
//    public FamilyInfoVO getFamilyInfo(Session session, FamilyReq req) throws AlertException, RuleException {
//        Player player = playerDao.getBean(session.getPid());
//        Family family = familyDao.getBeanByStarId(player.getStarId());
//        if (null==family){
//            FamilyRule rule = familyRuleContainer.getRuleByStarId(familyRuleContainer.getDefaultStarId());
//            family = familyDao.getBean(rule.getId());
//            player.setStarId(rule.getStarId());
//            playerDao.saveBean(player);
//        }
//        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
//        FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(session.getPid());
//        FamilyInfoVO vo = rspFamilyFactory.newFamilyInfoVO(family,bi,info);
//        return vo;
//    }

    @RpcAnnotation(cmd = "family.get", req = FamilyReq.class, name = "取得家族资料",vo= FamilyInfoVO.class)
    public FamilyInfoVO getFamilyById(Session session, FamilyReq req) throws AlertException, RuleException {
        Family family = familyDao.getBean(req.getFamilyId());
        FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(session.getPid());
//        familyRuleContainer.checkMember(family, info);//是否家族成员
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        FamilyInfoVO vo = rspFamilyFactory.newFamilyInfoVO(family,bi,info);
        return vo;
    }

    @RpcAnnotation(cmd = "family.get.info", req = FamilyReq.class, name = "取得家族资料-用户Gm后台管理",vo= FamilyInfoVO.class)
    public FamilyInfoVO getFamilyInfo(Session session, FamilyReq req) throws AlertException, RuleException {
        Family family = familyDao.getBean(req.getFamilyId());
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        FamilyInfoVO vo = rspFamilyFactory.newFamilyInfoVO(family,bi,null);
        return vo;
    }


    @RpcAnnotation(cmd = "family.get.panel", req = FamilyReq.class, name = "获取家族面板信息",vo= FamilyPanelVO.class)
    public FamilyPanelVO getPanel(Session session, FamilyReq req) throws AlertException, RuleException {
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        Family family = familyDao.getBean(memberInfo.getFamilyId());
        familyRuleContainer.checkMember(family, memberInfo);//是否家族成员
        Player player = playerDao.getBean(session.getPid());
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        return rspFamilyFactory.newFamilyPanelVO(family, memberInfo,bi,player);
    }

    @RpcAnnotation(cmd = "family.alter",lock = true, req = FamilyReq.class, name = "修改家族信息",vo= FamilyPanelVO.class)
    public FamilyPanelVO alter(Session session, FamilyReq req) throws AlertException, RuleException {
        Family family = familyDao.getBean(req.getFamilyId());
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(session.getPid());
        familyRuleContainer.checkMember(family,memberInfo);//是否家族成员
        familyRuleContainer.checkModify(memberInfo, req.getAlterType());
        familyRuleContainer.changeFamilyInfo(family, req.getContent(), req.getAlterType());
        familyDao.saveBean(family);
        Player player = playerDao.getBean(session.getPid());
        FamilyBuildInfo bi = familyBuildDao.getBean(family.getId());
        return rspFamilyFactory.newFamilyPanelVO(family, memberInfo,bi,player);
    }

    @RpcAnnotation(cmd = "family.get.list", req = FamilyReq.class, name = "家族列表",vo= FamilyVO.class)
    public FamilyPageVO getFamilyList(Session session, final FamilyReq req) throws AlertException {
        Player player = playerDao.getBean(session.getPid());

        Family family = familyDao.getBeanByStarId(player.getStarId());
        FamilyPageVO pages = null;
        if (req.getAlterType()==5){
            pages = familyDao.findPage(family,req.getContent(),req.getPageNo(),6);
        }else{//否则是翻页
            pages = familyDao.findPage(family,null,req.getPageNo(),6);
        }
        return  pages;
    }

}
