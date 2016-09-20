package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerLotteryActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerLotteryActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerLotteryActionModel;
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
@Repository(value = "playerLotteryActionModelManager")
public class PlayerLotteryActionModelManagerImpl implements IPlayerLotteryActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerLotteryActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerLotteryActionModelDao")
	private IPlayerLotteryActionModelDao playerLotteryActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerLotteryActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerLotteryActionModelDao.findById(id);
	}

	public PlayerLotteryActionModel createBean(PlayerLotteryActionModel playerLotteryActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerLotteryActionModel);
		}		
		playerLotteryActionModelDao.createBean(playerLotteryActionModel);
		return playerLotteryActionModel;
	}

	@Override	
	public void updateBean(PlayerLotteryActionModel playerLotteryActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerLotteryActionModel);
		}
		playerLotteryActionModelDao.updateBean(playerLotteryActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerLotteryActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerLotteryActionModel playerLotteryActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerLotteryActionModel);
		}
		playerLotteryActionModelDao.removeBean(playerLotteryActionModel);
	}
	
	@Override	
	public Page<PlayerLotteryActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerLotteryActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerLotteryActionModel> page = new Page<PlayerLotteryActionModel>() ;
        List<PlayerLotteryActionModel> list = playerLotteryActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerLotteryActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerLotteryActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}