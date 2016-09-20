package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerFamilyAwardActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerFamilyAwardActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerFamilyAwardActionModel;
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
@Repository(value = "playerFamilyAwardActionModelManager")
public class PlayerFamilyAwardActionModelManagerImpl implements IPlayerFamilyAwardActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerFamilyAwardActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerFamilyAwardActionModelDao")
	private IPlayerFamilyAwardActionModelDao playerFamilyAwardActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerFamilyAwardActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerFamilyAwardActionModelDao.findById(id);
	}

	public PlayerFamilyAwardActionModel createBean(PlayerFamilyAwardActionModel playerFamilyAwardActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerFamilyAwardActionModel);
		}		
		playerFamilyAwardActionModelDao.createBean(playerFamilyAwardActionModel);
		return playerFamilyAwardActionModel;
	}

	@Override	
	public void updateBean(PlayerFamilyAwardActionModel playerFamilyAwardActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerFamilyAwardActionModel);
		}
		playerFamilyAwardActionModelDao.updateBean(playerFamilyAwardActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerFamilyAwardActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerFamilyAwardActionModel playerFamilyAwardActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerFamilyAwardActionModel);
		}
		playerFamilyAwardActionModelDao.removeBean(playerFamilyAwardActionModel);
	}
	
	@Override	
	public Page<PlayerFamilyAwardActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerFamilyAwardActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerFamilyAwardActionModel> page = new Page<PlayerFamilyAwardActionModel>() ;
        List<PlayerFamilyAwardActionModel> list = playerFamilyAwardActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerFamilyAwardActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerFamilyAwardActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}