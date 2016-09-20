package com.pengpeng.admin.stargame.manager.impl;

import com.pengpeng.admin.stargame.dao.IPlayerDonationActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerDonationActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerDonationActionModel;
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
@Repository(value = "playerDonationActionModelManager")
public class PlayerDonationActionModelManagerImpl implements IPlayerDonationActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerDonationActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerDonationActionModelDao")
	private IPlayerDonationActionModelDao playerDonationActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerDonationActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerDonationActionModelDao.findById(id);
	}

	public PlayerDonationActionModel createBean(PlayerDonationActionModel playerDonationActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerDonationActionModel);
		}		
		playerDonationActionModelDao.createBean(playerDonationActionModel);
		return playerDonationActionModel;
	}

	@Override	
	public void updateBean(PlayerDonationActionModel playerDonationActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerDonationActionModel);
		}
		playerDonationActionModelDao.updateBean(playerDonationActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerDonationActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerDonationActionModel playerDonationActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerDonationActionModel);
		}
		playerDonationActionModelDao.removeBean(playerDonationActionModel);
	}
	
	@Override	
	public Page<PlayerDonationActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerDonationActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerDonationActionModel> page = new Page<PlayerDonationActionModel>() ;
        List<PlayerDonationActionModel> list = playerDonationActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerDonationActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerDonationActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}