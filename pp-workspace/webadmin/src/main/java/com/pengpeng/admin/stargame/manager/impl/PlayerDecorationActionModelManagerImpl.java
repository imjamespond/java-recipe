package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerDecorationActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerDecorationActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerDecorationActionModel;
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
@Repository(value = "playerDecorationActionModelManager")
public class PlayerDecorationActionModelManagerImpl implements IPlayerDecorationActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerDecorationActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerDecorationActionModelDao")
	private IPlayerDecorationActionModelDao playerDecorationActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerDecorationActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerDecorationActionModelDao.findById(id);
	}

	public PlayerDecorationActionModel createBean(PlayerDecorationActionModel playerDecorationActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerDecorationActionModel);
		}		
		playerDecorationActionModelDao.createBean(playerDecorationActionModel);
		return playerDecorationActionModel;
	}

	@Override	
	public void updateBean(PlayerDecorationActionModel playerDecorationActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerDecorationActionModel);
		}
		playerDecorationActionModelDao.updateBean(playerDecorationActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerDecorationActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerDecorationActionModel playerDecorationActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerDecorationActionModel);
		}
		playerDecorationActionModelDao.removeBean(playerDecorationActionModel);
	}
	
	@Override	
	public Page<PlayerDecorationActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerDecorationActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerDecorationActionModel> page = new Page<PlayerDecorationActionModel>() ;
        List<PlayerDecorationActionModel> list = playerDecorationActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerDecorationActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerDecorationActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}