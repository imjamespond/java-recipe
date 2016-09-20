package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerFamilyActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerFamilyActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerFamilyActionModel;
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
@Repository(value = "playerFamilyActionModelManager")
public class PlayerFamilyActionModelManagerImpl implements IPlayerFamilyActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerFamilyActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerFamilyActionModelDao")
	private IPlayerFamilyActionModelDao playerFamilyActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerFamilyActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerFamilyActionModelDao.findById(id);
	}

	public PlayerFamilyActionModel createBean(PlayerFamilyActionModel playerFamilyActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerFamilyActionModel);
		}		
		playerFamilyActionModelDao.createBean(playerFamilyActionModel);
		return playerFamilyActionModel;
	}

	@Override	
	public void updateBean(PlayerFamilyActionModel playerFamilyActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerFamilyActionModel);
		}
		playerFamilyActionModelDao.updateBean(playerFamilyActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerFamilyActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerFamilyActionModel playerFamilyActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerFamilyActionModel);
		}
		playerFamilyActionModelDao.removeBean(playerFamilyActionModel);
	}
	
	@Override	
	public Page<PlayerFamilyActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerFamilyActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerFamilyActionModel> page = new Page<PlayerFamilyActionModel>() ;
        List<PlayerFamilyActionModel> list = playerFamilyActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerFamilyActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerFamilyActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}