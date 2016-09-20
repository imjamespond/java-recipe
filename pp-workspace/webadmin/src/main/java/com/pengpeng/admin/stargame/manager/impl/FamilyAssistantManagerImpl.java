package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.common.DateUtil;
import com.pengpeng.admin.stargame.dao.IPlayerOlineInfoDao;
import com.pengpeng.admin.stargame.manager.IFamilyAssistantManager;
import com.pengpeng.admin.stargame.manager.IPlayerOlineInfoManager;
import com.pengpeng.admin.stargame.model.PlayerOlineInfoModel;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午11:31
 */

public class FamilyAssistantManagerImpl implements IFamilyAssistantManager {

    @Autowired
    private StatusRemote statusRemote;


    @Override
    public void election() throws NotFoundBeanException, BeanAreadyException {
          System.out.println("election begin");
    }
}
