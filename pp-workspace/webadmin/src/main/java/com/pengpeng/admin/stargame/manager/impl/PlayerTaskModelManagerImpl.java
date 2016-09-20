package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerTaskActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerTaskActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerTaskActionModel;
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
@Repository(value = "playerTaskActionModelManager")
public class PlayerTaskModelManagerImpl implements IPlayerTaskActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerTaskModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerTaskActionModelDao")
	private IPlayerTaskActionModelDao playerTaskActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerTaskActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerTaskActionModelDao.findById(id);
	}

	public PlayerTaskActionModel createBean(PlayerTaskActionModel playerTaskActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerTaskActionModel);
		}		
		playerTaskActionModelDao.createBean(playerTaskActionModel);
		return playerTaskActionModel;
	}

	@Override	
	public void updateBean(PlayerTaskActionModel playerTaskActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerTaskActionModel);
		}
		playerTaskActionModelDao.updateBean(playerTaskActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerTaskActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerTaskActionModel playerTaskActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerTaskActionModel);
		}
		playerTaskActionModelDao.removeBean(playerTaskActionModel);
	}
	
	@Override	
	public Page<PlayerTaskActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from UserActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerTaskActionModel> page = new Page<PlayerTaskActionModel>() ;
        List<PlayerTaskActionModel> list = playerTaskActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerTaskActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerTaskActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}