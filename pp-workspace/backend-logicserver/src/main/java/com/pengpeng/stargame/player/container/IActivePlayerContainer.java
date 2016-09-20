package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.model.player.ActivePlayer;
import com.pengpeng.stargame.player.rule.ActiveRule;
import com.pengpeng.stargame.rpc.Session;

/**
 * 活跃度规则数据操作
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-11下午4:44
 */
public interface IActivePlayerContainer {

    /**
     * 活跃度,在这里读取和保存数据
     * @param type
     * @param num
     */
    public void finish(Session session,String pid,int type ,int num);

    public int reward(ActivePlayer ap, int active) throws RuleException;

    /**
     * 完成任务调用
     * @param session
     * @param pid
     * @param taskId
     */
    public void finish(Session session,String pid,String taskId);


}
