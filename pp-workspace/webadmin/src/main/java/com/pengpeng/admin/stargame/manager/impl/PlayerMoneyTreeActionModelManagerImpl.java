package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerMoneyTreeActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerMoneyTreeActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerMoneyTreeActionModel;
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
@Repository(value = "playerMoneyTreeActionModelManager")
public class PlayerMoneyTreeActionModelManagerImpl implements IPlayerMoneyTreeActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerMoneyTreeActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerMoneyTreeActionModelDao")
	private IPlayerMoneyTreeActionModelDao playerMoneyTreeActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerMoneyTreeActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerMoneyTreeActionModelDao.findById(id);
	}

	public PlayerMoneyTreeActionModel createBean(PlayerMoneyTreeActionModel playerMoneyTreeActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerMoneyTreeActionModel);
		}		
		playerMoneyTreeActionModelDao.createBean(playerMoneyTreeActionModel);
		return playerMoneyTreeActionModel;
	}

	@Override	
	public void updateBean(PlayerMoneyTreeActionModel playerMoneyTreeActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerMoneyTreeActionModel);
		}
		playerMoneyTreeActionModelDao.updateBean(playerMoneyTreeActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerMoneyTreeActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerMoneyTreeActionModel playerMoneyTreeActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerMoneyTreeActionModel);
		}
		playerMoneyTreeActionModelDao.removeBean(playerMoneyTreeActionModel);
	}
	
	@Override	
	public Page<PlayerMoneyTreeActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerMoneyTreeActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerMoneyTreeActionModel> page = new Page<PlayerMoneyTreeActionModel>() ;
        List<PlayerMoneyTreeActionModel> list = playerMoneyTreeActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerMoneyTreeActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerMoneyTreeActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}