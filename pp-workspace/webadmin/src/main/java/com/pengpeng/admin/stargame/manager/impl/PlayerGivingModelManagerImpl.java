package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerGivingActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerGivingActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerGivingActionModel;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.tongyi.action.Page;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "playerGivingActionModelManager")
public class PlayerGivingModelManagerImpl implements IPlayerGivingActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerGivingModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerGivingActionModelDao")
	private IPlayerGivingActionModelDao playerGivingActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerGivingActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerGivingActionModelDao.findById(id);
	}

	public PlayerGivingActionModel createBean(PlayerGivingActionModel playerGivingActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerGivingActionModel);
		}		
		playerGivingActionModelDao.createBean(playerGivingActionModel);
		return playerGivingActionModel;
	}

	@Override	
	public void updateBean(PlayerGivingActionModel playerGivingActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerGivingActionModel);
		}
		playerGivingActionModelDao.updateBean(playerGivingActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerGivingActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerGivingActionModel playerGivingActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerGivingActionModel);
		}
		playerGivingActionModelDao.removeBean(playerGivingActionModel);
	}
	
	@Override	
	public Page<PlayerGivingActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerGivingActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerGivingActionModel> page = new Page<PlayerGivingActionModel>() ;
        List<PlayerGivingActionModel> list = playerGivingActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerGivingActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerGivingActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}