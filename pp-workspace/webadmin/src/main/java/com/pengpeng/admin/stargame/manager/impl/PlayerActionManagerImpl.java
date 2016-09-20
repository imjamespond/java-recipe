package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerActionDao;
import com.pengpeng.admin.stargame.manager.IPlayerActionManager;
import com.pengpeng.admin.stargame.model.PlayerActionModel;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:11
 */
@Repository(value = "playerActionManager")
public class PlayerActionManagerImpl implements IPlayerActionManager {
    @Autowired
    @Qualifier(value="playeractiondao")
    private IPlayerActionDao playerActionDao;
    private static String NameQuery = "from PlayerActionModel a where id>0";
    @Override
    public PlayerActionModel createBean(PlayerActionModel pam) throws BeanAreadyException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Page<PlayerActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize) {
        if (pageNo<=0||pageSize<=0)
            throw new IllegalArgumentException("param.zero");

        int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");
        String query = NameQuery;
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerActionModel> page = new Page<PlayerActionModel>();
        playerActionDao.findNcount(query,map,start,pageSize,page);
        page.setPage(pageNo);
        return page;
    }
}
