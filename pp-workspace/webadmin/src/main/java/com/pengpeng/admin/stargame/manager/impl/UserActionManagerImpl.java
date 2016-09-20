package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IUserActionDao;
import com.pengpeng.admin.stargame.manager.IUserActionManager;
import com.pengpeng.admin.stargame.model.PlayerTaskActionModel;
import com.pengpeng.admin.stargame.model.UserActionModel;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.role.PlayerVO;
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
@Repository(value = "userActionManager")
public class UserActionManagerImpl implements IUserActionManager {
    @Autowired
    @Qualifier(value="useractiondao")
    private IUserActionDao userActionDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    @Override
    public UserActionModel createBean(UserActionModel userActionModel) throws BeanAreadyException {
        userActionDao.createBean(userActionModel);
        return userActionModel;
    }

    @Override
    public Page<UserActionModel> findPages(Map<String, Object> params, int pageNo, int pageSize) {
        if (pageNo<=0||pageSize<=0)
            throw new IllegalArgumentException("param.zero");

        int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from UserActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<UserActionModel> page = new Page<UserActionModel>() ;
        List<UserActionModel> list = userActionDao.findPages(query,map,start,pageSize);

        for (UserActionModel pm : list) {
            com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
            try {
                PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
                pm.name = pv.getNickName();
            } catch (GameException e) {
                e.printStackTrace();
            }

            switch (pm.getType()){
                case UserActionModel.T_ADD_GAME_COIN:
                    pm.typeName = "加游戏币";
                    break;
                case UserActionModel.T_ADD_GOLD_COIN:
                    pm.typeName = "加达人币";
                    break;
                case UserActionModel.T_BAN_CHAT:
                    pm.typeName = "禁言";
                    break;
                case UserActionModel.T_BAN_PLAYER:
                    pm.typeName = "封帐户";
                    break;
                case UserActionModel.T_ADD_ITEM:
                    pm.typeName = "加道具";
                    break;
            }
        }

        page.setTotal(userActionDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
    }
}
