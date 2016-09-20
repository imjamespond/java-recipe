package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerMapActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerMapActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerMapActionModel;
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
@Repository(value = "playerMapActionModelManager")
public class PlayerMapActionModelManagerImpl implements IPlayerMapActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerMapActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerMapActionModelDao")
	private IPlayerMapActionModelDao playerMapActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerMapActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerMapActionModelDao.findById(id);
	}

	public PlayerMapActionModel createBean(PlayerMapActionModel playerMapActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerMapActionModel);
		}		
		playerMapActionModelDao.createBean(playerMapActionModel);
		return playerMapActionModel;
	}

	@Override	
	public void updateBean(PlayerMapActionModel playerMapActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerMapActionModel);
		}
		playerMapActionModelDao.updateBean(playerMapActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerMapActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerMapActionModel playerMapActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerMapActionModel);
		}
		playerMapActionModelDao.removeBean(playerMapActionModel);
	}
	
	@Override	
	public Page<PlayerMapActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerMapActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerMapActionModel> page = new Page<PlayerMapActionModel>() ;
        List<PlayerMapActionModel> list = playerMapActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerMapActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerMapActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}