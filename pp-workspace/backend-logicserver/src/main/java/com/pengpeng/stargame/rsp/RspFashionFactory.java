package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.fashion.container.IFashionItemRuleContainer;
import com.pengpeng.stargame.fashion.rule.FashionItemRule;
import com.pengpeng.stargame.gameevent.GameEventConstant;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionItem;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.model.success.PlayerSuccessInfo;
import com.pengpeng.stargame.model.task.TaskPlayer;
import com.pengpeng.stargame.success.container.ISuccessRuleContainer;
import com.pengpeng.stargame.success.dao.IPlayerSuccessDao;
import com.pengpeng.stargame.success.rule.SuccessRule;
import com.pengpeng.stargame.task.dao.ITaskDao;
import com.pengpeng.stargame.vo.fashion.FashionVO;
import com.pengpeng.stargame.vo.fashion.PlayerFashionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.event.EventContext;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-29
 * Time: 上午11:56
 */
@Component
public class RspFashionFactory extends RspFactory {

    @Autowired
    private IFashionItemRuleContainer fashionItemRuleContainer;
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IPlayerSuccessDao playerSuccessDao;
    @Autowired
    private ISuccessRuleContainer successRuleContainer;

   public PlayerFashionVO getPlayerFashionVO(FashionPlayer fashionPlayer,FashionCupboard fashionCupboard){
       PlayerFashionVO playerFashionVO=new PlayerFashionVO(fashionPlayer.getPid());
       List<FashionVO> fashionVOList=new ArrayList<FashionVO>();
       for(String key:fashionPlayer.getFashionKeys()){
           FashionVO fashionVO=new FashionVO(fashionPlayer.getFashion(key));

           FashionPkg fashionPkg=fashionCupboard.getFashionPkg(key);
           FashionItem fashionItem= fashionPkg.getItemByGrid(fashionPlayer.getFashion(key));

           if(fashionItem==null){
               continue;
           }
           FashionItemRule fashionItemRule=fashionItemRuleContainer.getElement(fashionItem.getItemId()) ;

           fashionVO.setItemId(fashionItemRule.getId());
           fashionVO.setType(Integer.parseInt(key));
           fashionVO.setFashionIndex(fashionItemRule.getFashionIndex());
           fashionVO.setImage(fashionItemRule.getImage());
           fashionVO.setIcon(fashionItemRule.getIcon());
           fashionVOList.add(fashionVO);
       }
       /**
        * 设置头顶 元旦快乐 圣诞快乐
        */
       TaskPlayer taskPlayer=taskDao.getBean(fashionPlayer.getPid());
       if(taskPlayer.isFinished(GameEventConstant.EVENT_1_TASKID)){
           playerFashionVO.setStatus(1);
       }
       if(taskPlayer.isFinished(GameEventConstant.EVENT_2_TASKID)){
           playerFashionVO.setStatus(2);
       }
       if(taskPlayer.isFinished(GameEventConstant.EVENT_3_TASKID)){
           playerFashionVO.setStatus(3);
       }
       /**
        * 设置称号
        */
       PlayerSuccessInfo playerSuccessInfo=playerSuccessDao.getPlayerSuccessInfo(fashionPlayer.getPid());
       if(playerSuccessInfo.getUseId()!=null&&!playerSuccessInfo.getUseId().equals("")){

           SuccessRule successRule=successRuleContainer.getElement(playerSuccessInfo.getUseId());
           playerFashionVO.setTitle(successRule.getTitle());
       }

       playerFashionVO.setFashionVOs(fashionVOList.toArray(new FashionVO[0]));
       return playerFashionVO;
   }
}
