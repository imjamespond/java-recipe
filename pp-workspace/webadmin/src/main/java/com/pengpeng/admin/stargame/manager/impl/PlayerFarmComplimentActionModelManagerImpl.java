package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerFarmComplimentActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerFarmComplimentActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerFarmComplimentActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import java.util.Map;
import java.util.HashMap;
import com.tongyi.action.Page;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.role.PlayerVO;
/**
 *  managerImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "playerFarmComplimentActionModelManager")
public class PlayerFarmComplimentActionModelManagerImpl implements IPlayerFarmComplimentActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerFarmComplimentActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerFarmComplimentActionModelDao")
	private IPlayerFarmComplimentActionModelDao playerFarmComplimentActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerFarmComplimentActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerFarmComplimentActionModelDao.findById(id);
	}

	public PlayerFarmComplimentActionModel createBean(PlayerFarmComplimentActionModel playerFarmComplimentActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerFarmComplimentActionModel);
		}		
		playerFarmComplimentActionModelDao.createBean(playerFarmComplimentActionModel);
		return playerFarmComplimentActionModel;
	}

	@Override	
	public void updateBean(PlayerFarmComplimentActionModel playerFarmComplimentActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerFarmComplimentActionModel);
		}
		playerFarmComplimentActionModelDao.updateBean(playerFarmComplimentActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerFarmComplimentActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerFarmComplimentActionModel playerFarmComplimentActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerFarmComplimentActionModel);
		}
		playerFarmComplimentActionModelDao.removeBean(playerFarmComplimentActionModel);
	}
	
	@Override	
	public Page<PlayerFarmComplimentActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerFarmComplimentActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerFarmComplimentActionModel> page = new Page<PlayerFarmComplimentActionModel>() ;
        List<PlayerFarmComplimentActionModel> list = playerFarmComplimentActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerFarmComplimentActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerFarmComplimentActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}