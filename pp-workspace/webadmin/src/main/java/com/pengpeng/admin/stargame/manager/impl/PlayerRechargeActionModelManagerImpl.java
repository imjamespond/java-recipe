package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerRechargeActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerRechargeActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerRechargeActionModel;
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
@Repository(value = "playerRechargeActionModelManager")
public class PlayerRechargeActionModelManagerImpl implements IPlayerRechargeActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerRechargeActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerRechargeActionModelDao")
	private IPlayerRechargeActionModelDao playerRechargeActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerRechargeActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerRechargeActionModelDao.findById(id);
	}

	public PlayerRechargeActionModel createBean(PlayerRechargeActionModel playerRechargeActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerRechargeActionModel);
		}		
		playerRechargeActionModelDao.createBean(playerRechargeActionModel);
		return playerRechargeActionModel;
	}

	@Override	
	public void updateBean(PlayerRechargeActionModel playerRechargeActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerRechargeActionModel);
		}
		playerRechargeActionModelDao.updateBean(playerRechargeActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerRechargeActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerRechargeActionModel playerRechargeActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerRechargeActionModel);
		}
		playerRechargeActionModelDao.removeBean(playerRechargeActionModel);
	}
	
	@Override	
	public Page<PlayerRechargeActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerRechargeActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerRechargeActionModel> page = new Page<PlayerRechargeActionModel>() ;
        List<PlayerRechargeActionModel> list = playerRechargeActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerRechargeActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerRechargeActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}