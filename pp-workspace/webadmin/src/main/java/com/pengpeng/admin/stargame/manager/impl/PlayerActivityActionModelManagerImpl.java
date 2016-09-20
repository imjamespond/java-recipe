package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerActivityActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerActivityActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerActivityActionModel;
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
@Repository(value = "playerActivityActionModelManager")
public class PlayerActivityActionModelManagerImpl implements IPlayerActivityActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerActivityActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerActivityActionModelDao")
	private IPlayerActivityActionModelDao playerActivityActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerActivityActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerActivityActionModelDao.findById(id);
	}

	public PlayerActivityActionModel createBean(PlayerActivityActionModel playerActivityActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerActivityActionModel);
		}		
		playerActivityActionModelDao.createBean(playerActivityActionModel);
		return playerActivityActionModel;
	}

	@Override	
	public void updateBean(PlayerActivityActionModel playerActivityActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerActivityActionModel);
		}
		playerActivityActionModelDao.updateBean(playerActivityActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerActivityActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerActivityActionModel playerActivityActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerActivityActionModel);
		}
		playerActivityActionModelDao.removeBean(playerActivityActionModel);
	}
	
	@Override	
	public Page<PlayerActivityActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerActivityActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerActivityActionModel> page = new Page<PlayerActivityActionModel>() ;
        List<PlayerActivityActionModel> list = playerActivityActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerActivityActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerActivityActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}