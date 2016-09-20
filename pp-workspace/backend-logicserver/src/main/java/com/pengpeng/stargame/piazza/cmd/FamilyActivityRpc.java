package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IActiveControlRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyBuildDao;
import com.pengpeng.stargame.piazza.rule.ActiveControlRule;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspFamilyActiveFactory;
import com.pengpeng.stargame.vo.piazza.ActivityVO;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 家族活动
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-25下午6:11
 */
@Component
public class FamilyActivityRpc extends RpcHandler {
    @Autowired
    private RspFamilyActiveFactory familyActiveFactory;
    @Autowired
    private IActiveControlRuleContainer activeControlRuleContainer;
    @Autowired
    private IFamilyBuildDao familyBuildDao;

    @RpcAnnotation(cmd = "familyinfo.getEvents", req = FamilyReq.class, name = "家族活动列表",vo=  ActivityVO[].class)
    public ActivityVO[] getEvents(Session session, FamilyReq req) throws AlertException {
        FamilyBuildInfo info = familyBuildDao.getBean(req.getFamilyId());
        int level = info.getLevel(FamilyConstant.BUILD_CASTLE);
        List<ActiveControlRule> list = activeControlRuleContainer.getAll(level);
        return  familyActiveFactory.getActivetyVos(list);
    }
}
