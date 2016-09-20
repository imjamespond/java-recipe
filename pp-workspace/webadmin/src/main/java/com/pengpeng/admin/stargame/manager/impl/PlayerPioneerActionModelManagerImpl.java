package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerPioneerActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerPioneerActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerPioneerActionModel;
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
@Repository(value = "playerPioneerActionModelManager")
public class PlayerPioneerActionModelManagerImpl implements IPlayerPioneerActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerPioneerActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerPioneerActionModelDao")
	private IPlayerPioneerActionModelDao playerPioneerActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerPioneerActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerPioneerActionModelDao.findById(id);
	}

	public PlayerPioneerActionModel createBean(PlayerPioneerActionModel playerPioneerActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerPioneerActionModel);
		}		
		playerPioneerActionModelDao.createBean(playerPioneerActionModel);
		return playerPioneerActionModel;
	}

	@Override	
	public void updateBean(PlayerPioneerActionModel playerPioneerActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerPioneerActionModel);
		}
		playerPioneerActionModelDao.updateBean(playerPioneerActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerPioneerActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerPioneerActionModel playerPioneerActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerPioneerActionModel);
		}
		playerPioneerActionModelDao.removeBean(playerPioneerActionModel);
	}
	
	@Override	
	public Page<PlayerPioneerActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerPioneerActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerPioneerActionModel> page = new Page<PlayerPioneerActionModel>() ;
        List<PlayerPioneerActionModel> list = playerPioneerActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerPioneerActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerPioneerActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}