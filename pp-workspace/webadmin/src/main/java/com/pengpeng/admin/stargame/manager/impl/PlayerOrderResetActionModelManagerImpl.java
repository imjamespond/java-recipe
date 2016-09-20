package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerOrderResetActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerOrderResetActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerOrderResetActionModel;
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
@Repository(value = "playerOrderResetActionModelManager")
public class PlayerOrderResetActionModelManagerImpl implements IPlayerOrderResetActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerOrderResetActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerOrderResetActionModelDao")
	private IPlayerOrderResetActionModelDao playerOrderResetActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerOrderResetActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerOrderResetActionModelDao.findById(id);
	}

	public PlayerOrderResetActionModel createBean(PlayerOrderResetActionModel playerOrderResetActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerOrderResetActionModel);
		}		
		playerOrderResetActionModelDao.createBean(playerOrderResetActionModel);
		return playerOrderResetActionModel;
	}

	@Override	
	public void updateBean(PlayerOrderResetActionModel playerOrderResetActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerOrderResetActionModel);
		}
		playerOrderResetActionModelDao.updateBean(playerOrderResetActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerOrderResetActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerOrderResetActionModel playerOrderResetActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerOrderResetActionModel);
		}
		playerOrderResetActionModelDao.removeBean(playerOrderResetActionModel);
	}
	
	@Override	
	public Page<PlayerOrderResetActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerOrderResetActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerOrderResetActionModel> page = new Page<PlayerOrderResetActionModel>() ;
        List<PlayerOrderResetActionModel> list = playerOrderResetActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerOrderResetActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerOrderResetActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}