package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerLoginActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerLoginActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerLoginActionModel;
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
@Repository(value = "playerLoginActionModelManager")
public class PlayerLoginActionModelManagerImpl implements IPlayerLoginActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerLoginActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerLoginActionModelDao")
	private IPlayerLoginActionModelDao playerLoginActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerLoginActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerLoginActionModelDao.findById(id);
	}

	public PlayerLoginActionModel createBean(PlayerLoginActionModel playerLoginActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerLoginActionModel);
		}		
		playerLoginActionModelDao.createBean(playerLoginActionModel);
		return playerLoginActionModel;
	}

	@Override	
	public void updateBean(PlayerLoginActionModel playerLoginActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerLoginActionModel);
		}
		playerLoginActionModelDao.updateBean(playerLoginActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerLoginActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerLoginActionModel playerLoginActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerLoginActionModel);
		}
		playerLoginActionModelDao.removeBean(playerLoginActionModel);
	}
	
	@Override	
	public Page<PlayerLoginActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize,String type){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerLoginActionModel a where type="+type;
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerLoginActionModel> page = new Page<PlayerLoginActionModel>() ;
        List<PlayerLoginActionModel> list = playerLoginActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerLoginActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerLoginActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}