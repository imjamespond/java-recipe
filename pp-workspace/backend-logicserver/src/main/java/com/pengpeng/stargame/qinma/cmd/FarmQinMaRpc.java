package com.pengpeng.stargame.qinma.cmd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.qinma.container.IQinMaFarmDecorateRuleContainer;
import com.pengpeng.stargame.qinma.container.IQinMaFarmRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmEvaluateDao;
import com.pengpeng.stargame.model.farm.FarmEvaluate;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.qinma.dao.IQinMaDao;
import com.pengpeng.stargame.qinma.rule.QinMaFarmDecorateRule;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.vo.farm.FarmIdReq;
import com.pengpeng.stargame.vo.farm.FarmVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-8-13
 * Time: 下午4:41
 */
@Component
public class FarmQinMaRpc extends RpcHandler {
    @Autowired
    private IQinMaFarmRuleContainer qinMaFarmRuleContainer;
    @Autowired
    private IQinMaDao qinMaDao;
    @Autowired
    private IFarmEvaluateDao farmEvaluateDao;
    @Autowired
    private StatusRemote statusRemote;

    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IQinMaFarmDecorateRuleContainer qinMaFarmDecorateRuleContainer;
    @Autowired
    private RspFarmFactory rspFarmFactory;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 获取亲妈的 农场信息
     * @param session
     * @param req
     * @return
     */
    @RpcAnnotation(cmd = "farm.qinma.info", req = FarmIdReq.class, name = "取得亲妈农场信息")
    public FarmVO getFarmInfo(Session session, FarmIdReq req) {

        return qinMaFarmRuleContainer.getQinmaFarmVO();
    }

    @RpcAnnotation(cmd = "farm.qinma.decorate.info", req = FarmIdReq.class, name = "取得亲妈农场装饰信息")
    public FarmDecorateVO getFarmDecorateInfo(Session session, FarmIdReq req) {
        QinMaFarmDecorateRule farmDecorateRule=qinMaFarmDecorateRuleContainer.getElement(Constant.QINMA_ID);
        FarmDecorate farmDecorate=  gson.fromJson(farmDecorateRule.getValue(),FarmDecorate.class);
        farmDecorate.setPid(Constant.QINMA_ID);
        return rspFarmFactory.getFarDecorateVo(farmDecorate,null);
    }


    /**
     * 评价好友农场
     *
     * @param session
     * @param req     fid,farmid
     */
    @RpcAnnotation(cmd = "farm.qinma.evaluation", req = FarmIdReq.class, name = "评价亲妈农场")
    public void evaluation(Session session, FarmIdReq req) throws AlertException {

        FarmEvaluate farmEvaluate= farmEvaluateDao.getFarmEvaluate(session.getPid());
        QinMa qinMa=qinMaDao.getQinMa(Constant.QINMA_ID);

        qinMaFarmRuleContainer.checkEvaluate(farmEvaluate,Constant.QINMA_ID);

        qinMaFarmRuleContainer.evaluate(farmEvaluate,qinMa);

        farmEvaluateDao.saveBean(farmEvaluate);

        qinMaDao.saveBean(qinMa);

        //任务
        taskRuleContainer.updateTaskNum(session.getPid(), TaskConstants.CONDI_TYPE_8,"",1);


        /**
         * 推送事件
         */
        Session[] mysessions={session};
        frontendService.broadcast(mysessions, qinMaFarmRuleContainer.getQinmaFarmVO());    }

}
