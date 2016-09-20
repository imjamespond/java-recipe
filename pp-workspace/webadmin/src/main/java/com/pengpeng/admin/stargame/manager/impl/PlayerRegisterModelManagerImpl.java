package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerRegisterModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerRegisterModelManager;
import com.pengpeng.admin.stargame.model.PlayerRegisterModel;
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
@Repository(value = "playerRegisterModelManager")
public class PlayerRegisterModelManagerImpl implements IPlayerRegisterModelManager{
	private static final Log log = LogFactory.getLog(PlayerRegisterModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerRegisterModelDao")
	private IPlayerRegisterModelDao playerRegisterModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerRegisterModel findById(Serializable id) throws NotFoundBeanException{
		return playerRegisterModelDao.findById(id);
	}

	public PlayerRegisterModel createBean(PlayerRegisterModel playerRegisterModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerRegisterModel);
		}		
		playerRegisterModelDao.createBean(playerRegisterModel);
		return playerRegisterModel;
	}

	@Override	
	public void updateBean(PlayerRegisterModel playerRegisterModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerRegisterModel);
		}
		playerRegisterModelDao.updateBean(playerRegisterModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerRegisterModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerRegisterModel playerRegisterModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerRegisterModel);
		}
		playerRegisterModelDao.removeBean(playerRegisterModel);
	}
	
	@Override	
	public Page<PlayerRegisterModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerRegisterModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerRegisterModel> page = new Page<PlayerRegisterModel>() ;
        List<PlayerRegisterModel> list = playerRegisterModelDao.findPages(query,map,start,pageSize);

        for (PlayerRegisterModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerRegisterModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}