package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerHarvestActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerHarvestActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerHarvestActionModel;
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
@Repository(value = "playerHarvestActionModelManager")
public class PlayerHarvestActionModelManagerImpl implements IPlayerHarvestActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerHarvestActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerHarvestActionModelDao")
	private IPlayerHarvestActionModelDao playerHarvestActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerHarvestActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerHarvestActionModelDao.findById(id);
	}

	public PlayerHarvestActionModel createBean(PlayerHarvestActionModel playerHarvestActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerHarvestActionModel);
		}		
		playerHarvestActionModelDao.createBean(playerHarvestActionModel);
		return playerHarvestActionModel;
	}

	@Override	
	public void updateBean(PlayerHarvestActionModel playerHarvestActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerHarvestActionModel);
		}
		playerHarvestActionModelDao.updateBean(playerHarvestActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerHarvestActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerHarvestActionModel playerHarvestActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerHarvestActionModel);
		}
		playerHarvestActionModelDao.removeBean(playerHarvestActionModel);
	}
	
	@Override	
	public Page<PlayerHarvestActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize,String type){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerHarvestActionModel a where type="+type;
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerHarvestActionModel> page = new Page<PlayerHarvestActionModel>() ;
        List<PlayerHarvestActionModel> list = playerHarvestActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerHarvestActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerHarvestActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}