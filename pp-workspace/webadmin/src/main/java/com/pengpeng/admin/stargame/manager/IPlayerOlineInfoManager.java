package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.PlayerOlineInfoModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.io.Serializable;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午11:30
 */
public interface IPlayerOlineInfoManager {

    public PlayerOlineInfoModel findById(Serializable id) throws NotFoundBeanException;
    public  void timingSave() throws NotFoundBeanException, BeanAreadyException;
    void smallGameReward();
    void designate();
    public void executeGame();

    void smallGameReward1();

    void smallGameReward2();

    void smallGameReward3();
}
