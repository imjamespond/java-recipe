package com.pengpeng.admin.stargame.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.pengpeng.admin.stargame.dao.IPlayerFarmComplimentActionModelDao;
import com.pengpeng.admin.stargame.model.PlayerFarmComplimentActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import java.util.Map;
import com.tongyi.dao.IBaseDao;
import java.util.ArrayList;
import java.util.List;
import com.tongyi.action.Page;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *  daoImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "playerFarmComplimentActionModelDao")
public class PlayerFarmComplimentActionModelDaoImpl implements IPlayerFarmComplimentActionModelDao{
	private static final Log log = LogFactory.getLog(PlayerFarmComplimentActionModelDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(PlayerFarmComplimentActionModel playerFarmComplimentActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerFarmComplimentActionModel);
		}
		try{
			findById(playerFarmComplimentActionModel.getId());
		}catch(NotFoundBeanException e){
            template.save(playerFarmComplimentActionModel);
		}		
	}
	
	@Override
	public void updateBean(PlayerFarmComplimentActionModel playerFarmComplimentActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerFarmComplimentActionModel);
		}
		findById(playerFarmComplimentActionModel.getId());
        template.update(playerFarmComplimentActionModel);
	}
	
	@Override
	public void removeBean(PlayerFarmComplimentActionModel playerFarmComplimentActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerFarmComplimentActionModel);
		}
		findById(playerFarmComplimentActionModel.getId());
        template.delete(playerFarmComplimentActionModel);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        PlayerFarmComplimentActionModel playerFarmComplimentActionModel = findById(id);
        template.delete(playerFarmComplimentActionModel);
	}

	@Override
	public PlayerFarmComplimentActionModel findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		PlayerFarmComplimentActionModel playerFarmComplimentActionModel = (PlayerFarmComplimentActionModel)template.get(PlayerFarmComplimentActionModel.class, id);;
		if (null==playerFarmComplimentActionModel)
			throw new NotFoundBeanException();
		return playerFarmComplimentActionModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerFarmComplimentActionModel> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <PlayerFarmComplimentActionModel> list = (List<PlayerFarmComplimentActionModel>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}

                @SuppressWarnings("unchecked")
                @Override
                public List<PlayerFarmComplimentActionModel> findPages(final String nameQuery, final Map<String, Object> params, final int begin, final int offset) {
                    if (log.isDebugEnabled()) {
                    log.debug("findPages:[begin=" + begin + ",offset=" + offset + ",nameQuery=" + nameQuery + "]");
                    }
                    List<PlayerFarmComplimentActionModel> list = (List<PlayerFarmComplimentActionModel>) template.execute(new HibernateCallback
                        <Object>() {
                            @Override
                            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                            Query query = session.createQuery(nameQuery);
                            if (params.containsKey("uid")) {
                            query.setString("uid", ((String[]) params.get("uid"))[0]);
                            }
                            if (params.containsKey("dateBegin") && params.containsKey("dateEnd")) {
                            String dateStr1 = ((String[]) params.get("dateBegin"))[0];
                            String dateStr2 = ((String[]) params.get("dateEnd"))[0];
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date d1, d2;
                            try {
                            d1 = formatter.parse(dateStr1);
                            d2 = formatter.parse(dateStr2);
                            query.setDate("dateBegin", d1).setDate("dateEnd", d2);
                            } catch (ParseException e) {
                            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
                            }
                            }

                            return query.setFirstResult(begin).setMaxResults(offset).list();
                            }
                            });


                            return list;
                            }

                            @Override
                            public int count(final String nameQuery, final Map<String , Object> params) {
                            List<Long> count;
                                count = (List<Long>) template.execute(new HibernateCallback<Object>() {
                                    @Override
                                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                                    Query queryCount = session.createQuery(nameQuery);
                                    if (params.containsKey("uid")) {
                                    queryCount.setString("uid", ((String[]) params.get("uid"))[0]);
                                    }
                                    if (params.containsKey("dateBegin") && params.containsKey("dateEnd")) {
                                    String dateStr1 = ((String[]) params.get("dateBegin"))[0];
                                    String dateStr2 = ((String[]) params.get("dateEnd"))[0];
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d1, d2;
                                    try {
                                    d1 = formatter.parse(dateStr1);
                                    d2 = formatter.parse(dateStr2);
                                    queryCount.setDate("dateBegin", d1).setDate("dateEnd", d2);
                                    } catch (ParseException e) {
                                    e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
                                    }
                                    }

                                    return queryCount.list();
                                    }
                                    });

                                    return count.get(0).intValue();
                                    }

                                    }