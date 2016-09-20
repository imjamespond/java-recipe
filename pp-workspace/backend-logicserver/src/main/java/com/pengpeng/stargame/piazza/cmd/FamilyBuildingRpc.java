package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.IFamilyBuildingRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.rule.FamilyBuildingRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFamilyFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.rsp.RspSmallGameFactory;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.piazza.BuildVO;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * 家族建筑
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午5:52
 */
@Component
public class FamilyBuildingRpc extends RpcHandler {

    @Autowired
    private IFamilyBuildDao familyBuildDao;
    @Autowired
    private IFamilyDao familyDao;

    @Autowired
    private IFamilyBuildingRuleContainer familyBuildingRuleContainer;

    @Autowired
    private RspFamilyFactory familyFactory;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private StatusRemote statusService;
    @Autowired
    private MessageSource message;
    @Autowired
    private FrontendServiceProxy frontendServiceProxy;

    @RpcAnnotation(cmd = "family.list.build", req = FamilyReq.class, name = "获取建筑列表",vo=BuildVO[].class)
    public BuildVO[] getBuilds(Session session, FamilyReq req) throws AlertException {
        FamilyBuildInfo info = familyBuildDao.getBean(req.getFamilyId());
        Map<Integer,Integer> map = info.getBuilds();
        return  familyFactory.newBuildVO(map);
    }

    @RpcAnnotation(cmd = "family.up.level", req = FamilyReq.class, name = "升级建筑",vo=BuildVO[].class)
    public BuildVO[] upgradeBuild(Session session, FamilyReq req) throws AlertException, RuleException {
        String pid = session.getPid();
        Player player = playerDao.getBean(pid);

        Family family = familyDao.getBean(req.getFamilyId());
        FamilyBuildInfo info = familyBuildDao.getBean(req.getFamilyId());
        familyBuildingRuleContainer.checkUpgrade(family,info,req.getBuildType());
        familyBuildingRuleContainer.upgrade(family,info,req.getBuildType());
        familyDao.saveBean(family);
        familyBuildDao.saveBean(info);

        //全服广播
        FamilyBuildingRule rule = familyBuildingRuleContainer.getElement(req.getBuildType(), info.getLevel(req.getBuildType()));
        Session[] sessions = statusService.getMember(session, SessionUtil.KEY_CHAT_WORLD);
        String content = message.getMessage("family.build.upgrade", new String[]{player.getNickName(),family.getName() ,rule.getName(), String.valueOf(info.getLevel(req.getBuildType()))}, Locale.CHINA);
        ChatVO svo = rspRoleFactory.newShoutChat("", "", content);
        frontendServiceProxy.broadcast(sessions, svo);

        return  familyFactory.newBuildVO(info.getBuilds());
    }
}
