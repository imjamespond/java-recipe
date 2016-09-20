package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.stargame.farm.rule.BaseItemRule;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;

import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerBuyActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerBuyActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerBuyActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;

import java.util.Map;
import java.util.HashMap;

import com.tongyi.action.Page;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.role.PlayerVO;

/**
 * managerImpl.vm
 *
 * @author fangyaoxia
 */
@Repository(value = "playerBuyActionModelManager")
public class PlayerBuyActionModelManagerImpl implements IPlayerBuyActionModelManager {
    private static final Log log = LogFactory.getLog(PlayerBuyActionModelManagerImpl.class);

    @Autowired
    @Qualifier(value = "playerBuyActionModelDao")
    private IPlayerBuyActionModelDao playerBuyActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    @Autowired
    private HibernateTemplate template;

    @Override
    public PlayerBuyActionModel findById(Serializable id) throws NotFoundBeanException {
        return playerBuyActionModelDao.findById(id);
    }

    public PlayerBuyActionModel createBean(PlayerBuyActionModel playerBuyActionModel) throws BeanAreadyException {
        if (log.isDebugEnabled()) {
            log.debug("createBean:" + playerBuyActionModel);
        }
        playerBuyActionModelDao.createBean(playerBuyActionModel);
        return playerBuyActionModel;
    }

    @Override
    public void updateBean(PlayerBuyActionModel playerBuyActionModel) throws NotFoundBeanException {
        if (log.isDebugEnabled()) {
            log.debug("updateBean:" + playerBuyActionModel);
        }
        playerBuyActionModelDao.updateBean(playerBuyActionModel);
    }

    @Override
    public void removeBean(Serializable id) throws NotFoundBeanException {
        if (log.isDebugEnabled()) {
            log.debug("removeBean:" + id);
        }
        playerBuyActionModelDao.removeBean(id);
    }

    @Override
    public void removeBean(PlayerBuyActionModel playerBuyActionModel) throws NotFoundBeanException {
        if (log.isDebugEnabled()) {
            log.debug("removeBean:" + playerBuyActionModel);
        }
        playerBuyActionModelDao.removeBean(playerBuyActionModel);
    }

    @Override
    public Page<PlayerBuyActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize) {
        if (log.isDebugEnabled()) {
            log.debug("findPage[pageNo=" + pageNo + ",pageSize=" + pageSize + ";params=" + ToStringBuilder.reflectionToString(params) + "]");
        }
        if (pageNo <= 0 || pageSize <= 0)
            throw new IllegalArgumentException("param.zero");

        int start = (pageNo - 1) * pageSize;
        Map<String, Object> map = new HashMap<String, Object>(params);
        map.remove("_");

        String query = "from PlayerBuyActionModel a where id>0";
        if (params.containsKey("uid")) {
            query += " and a.uid=:uid";
        }
        if (params.containsKey("dateBegin") && params.containsKey("dateEnd")) {
            query += " and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerBuyActionModel> page = new Page<PlayerBuyActionModel>();
        List<PlayerBuyActionModel> list = playerBuyActionModelDao.findPages(query, map, start, pageSize);

        for (PlayerBuyActionModel pm : list) {
            com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
            try {
                PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
                pm.name = pv.getNickName();
                if (pm.getItemId() != null) {
                    BaseItemRule bir = template.get(BaseItemRule.class, pm.getItemId());
                    pm.itemName = bir.getName();
                }
            } catch (GameException e) {
                e.printStackTrace();
            }
        }

        page.setTotal(playerBuyActionModelDao.count("select count(*) " + query, map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
    }

}