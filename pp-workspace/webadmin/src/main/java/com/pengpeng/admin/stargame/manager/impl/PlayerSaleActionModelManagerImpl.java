package com.pengpeng.admin.stargame.manager.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.pengpeng.admin.stargame.dao.IPlayerSaleActionModelDao;
import com.pengpeng.admin.stargame.manager.IPlayerSaleActionModelManager;
import com.pengpeng.admin.stargame.model.PlayerSaleActionModel;
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
@Repository(value = "playerSaleActionModelManager")
public class PlayerSaleActionModelManagerImpl implements IPlayerSaleActionModelManager{
	private static final Log log = LogFactory.getLog(PlayerSaleActionModelManagerImpl.class);
	
	@Autowired
	@Qualifier(value="playerSaleActionModelDao")
	private IPlayerSaleActionModelDao playerSaleActionModelDao;

    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    @Override
	public PlayerSaleActionModel findById(Serializable id) throws NotFoundBeanException{
		return playerSaleActionModelDao.findById(id);
	}

	public PlayerSaleActionModel createBean(PlayerSaleActionModel playerSaleActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerSaleActionModel);
		}		
		playerSaleActionModelDao.createBean(playerSaleActionModel);
		return playerSaleActionModel;
	}

	@Override	
	public void updateBean(PlayerSaleActionModel playerSaleActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerSaleActionModel);
		}
		playerSaleActionModelDao.updateBean(playerSaleActionModel);
	}
	
	@Override	
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
		playerSaleActionModelDao.removeBean(id);
	}
	
	@Override	
	public void removeBean(PlayerSaleActionModel playerSaleActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerSaleActionModel);
		}
		playerSaleActionModelDao.removeBean(playerSaleActionModel);
	}
	
	@Override	
	public Page<PlayerSaleActionModel> findPages(Map<String,Object> params,int pageNo,int pageSize){
		if (log.isDebugEnabled()){
			log.debug("findPage[pageNo="+pageNo+",pageSize="+pageSize+";params="+ToStringBuilder.reflectionToString(params)+"]");
		}
		if (pageNo<=0||pageSize<=0)
			throw new IllegalArgumentException("param.zero");

		int start = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>(params);
        map.remove("_");

        String query = "from PlayerSaleActionModel a where id>0";
        if(params.containsKey("uid")){
            query+=" and a.uid=:uid";
        }
        if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
            query+=" and a.date between :dateBegin and :dateEnd";
        }

        Page<PlayerSaleActionModel> page = new Page<PlayerSaleActionModel>() ;
        List<PlayerSaleActionModel> list = playerSaleActionModelDao.findPages(query,map,start,pageSize);

        for (PlayerSaleActionModel pm : list) {
        com.pengpeng.stargame.rpc.Session session = new com.pengpeng.stargame.rpc.Session(pm.getPid(), "");
        try {
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session, pm.getPid());
        pm.name = pv.getNickName();
        } catch (GameException e) {
        e.printStackTrace();
        }
        }

        page.setTotal(playerSaleActionModelDao.count("select count(*) "+query,map));
        page.setRows(list);
        page.setPage(pageNo);
        return page;
	}
	
}