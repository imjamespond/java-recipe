package com.pengpeng.admin.stargame.dao.impl;

import com.pengpeng.admin.stargame.dao.IPlayerFamilyActionModelDao;
import com.pengpeng.admin.stargame.model.PlayerFamilyActionModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *  daoImpl.vm
 * @author fangyaoxia
 */
@Repository(value = "playerFamilyActionModelDao")
public class PlayerFamilyActionModelDaoImpl implements IPlayerFamilyActionModelDao{
	private static final Log log = LogFactory.getLog(PlayerFamilyActionModelDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

	@Override
	public void createBean(PlayerFamilyActionModel playerFamilyActionModel) throws BeanAreadyException{
		if (log.isDebugEnabled()){
			log.debug("createBean:"+playerFamilyActionModel);
		}
		try{
			findById(playerFamilyActionModel.getId());
		}catch(NotFoundBeanException e){
            template.save(playerFamilyActionModel);
		}		
	}
	
	@Override
	public void updateBean(PlayerFamilyActionModel playerFamilyActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("updateBean:"+playerFamilyActionModel);
		}
		findById(playerFamilyActionModel.getId());
        template.update(playerFamilyActionModel);
	}
	
	@Override
	public void removeBean(PlayerFamilyActionModel playerFamilyActionModel) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+playerFamilyActionModel);
		}
		findById(playerFamilyActionModel.getId());
        template.delete(playerFamilyActionModel);
	}
	
	@Override
	public void removeBean(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("removeBean:"+id);
		}
        PlayerFamilyActionModel playerFamilyActionModel = findById(id);
        template.delete(playerFamilyActionModel);
	}

	@Override
	public PlayerFamilyActionModel findById(Serializable id) throws NotFoundBeanException{
		if (log.isDebugEnabled()){
			log.debug("findById:"+id);
		}
		PlayerFamilyActionModel playerFamilyActionModel = (PlayerFamilyActionModel)template.get(PlayerFamilyActionModel.class, id);;
		if (null==playerFamilyActionModel)
			throw new NotFoundBeanException();
		return playerFamilyActionModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerFamilyActionModel> findByNameQuery(String nameQuery,Map<String,Object> params){
		if (log.isDebugEnabled()){
			log.debug("findByNameQuery:"+nameQuery);
		}
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <PlayerFamilyActionModel> list = (List<PlayerFamilyActionModel>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
	}

                @SuppressWarnings("unchecked")
                @Override
                public List<PlayerFamilyActionModel> findPages(final String nameQuery, final Map<String, Object> params, final int begin, final int offset) {
                    if (log.isDebugEnabled()) {
                    log.debug("findPages:[begin=" + begin + ",offset=" + offset + ",nameQuery=" + nameQuery + "]");
                    }
                    List<PlayerFamilyActionModel> list = (List<PlayerFamilyActionModel>) template.execute(new HibernateCallback
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