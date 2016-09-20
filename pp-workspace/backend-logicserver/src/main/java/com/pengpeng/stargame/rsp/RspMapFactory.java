package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.player.container.ITransferRuleContainer;
import com.pengpeng.stargame.player.rule.SceneRule;
import com.pengpeng.stargame.player.rule.TransferRule;
import com.pengpeng.stargame.vo.map.ScenceVO;
import com.pengpeng.stargame.vo.map.TransferVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:25
 */
@Component()
public class RspMapFactory extends RspFactory {

    @Autowired
    private ITransferRuleContainer transferRuleContainer;


    public ScenceVO newSceneRsp(SceneRule rule) {
        ScenceVO vo = new ScenceVO();
        BeanUtils.copyProperties(rule, vo);
        List<TransferRule> transferRuleList = transferRuleContainer.getTransferByScene(rule.getKey());
        TransferVO[] transfers = getTransferVOList(transferRuleList);
        vo.setTransfers(transfers);
        return vo;
    }

    private TransferVO [] getTransferVOList(List<TransferRule> transferBeans){
        List<TransferVO> list = new ArrayList<TransferVO>();
        if(transferBeans==null || transferBeans.isEmpty()){
            return list.toArray(new TransferVO[0]);
        }

        for(TransferRule bean : transferBeans){
            TransferVO vo = new TransferVO();
            BeanUtils.copyProperties(bean, vo);
            list.add(vo);
        }
        return list.toArray(new TransferVO[0]);
    }
}
