package com.pengpeng.admin.stargame.dao.impl;

import com.pengpeng.admin.stargame.dao.IPlayerItemModelDao;
import com.pengpeng.admin.stargame.model.PlayerItemModel;
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
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-13下午6:43
 */
@Repository(value = "playerItemModelDao")
public class PlayerItemModelDaoImpl implements IPlayerItemModelDao{
    private static final Log log = LogFactory.getLog(PlayerHarvestActionModelDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

    @Override
    public void createBean(PlayerItemModel playerHarvestActionModel) throws BeanAreadyException {
        if (log.isDebugEnabled()){
            log.debug("createBean:"+playerHarvestActionModel);
        }
        try{
            findById(playerHarvestActionModel.getId());
        }catch(NotFoundBeanException e){
            template.save(playerHarvestActionModel);
        }
    }

    @Override
    public void updateBean(PlayerItemModel playerHarvestActionModel) throws NotFoundBeanException{
        if (log.isDebugEnabled()){
            log.debug("updateBean:"+playerHarvestActionModel);
        }
        findById(playerHarvestActionModel.getId());
        template.update(playerHarvestActionModel);
    }

    @Override
    public void removeBean(PlayerItemModel playerHarvestActionModel) throws NotFoundBeanException{
        if (log.isDebugEnabled()){
            log.debug("removeBean:"+playerHarvestActionModel);
        }
        findById(playerHarvestActionModel.getId());
        template.delete(playerHarvestActionModel);
    }

    @Override
    public void removeBean(Serializable id) throws NotFoundBeanException{
        if (log.isDebugEnabled()){
            log.debug("removeBean:"+id);
        }
        PlayerItemModel playerHarvestActionModel = findById(id);
        template.delete(playerHarvestActionModel);
    }

    @Override
    public PlayerItemModel findById(Serializable id) throws NotFoundBeanException{
        if (log.isDebugEnabled()){
            log.debug("findById:"+id);
        }
        PlayerItemModel playerHarvestActionModel = (PlayerItemModel)template.get(PlayerItemModel.class, id);;
        if (null==playerHarvestActionModel)
            throw new NotFoundBeanException();
        return playerHarvestActionModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PlayerItemModel> findByNameQuery(String nameQuery,Map<String,Object> params){
        if (log.isDebugEnabled()){
            log.debug("findByNameQuery:"+nameQuery);
        }
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        for(Map.Entry<String, Object> entry:params.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        List <PlayerItemModel> list = (List<PlayerItemModel>)template.findByNamedParam(nameQuery, keys.toArray(new String[0]),values.toArray(new Object[0]));
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PlayerItemModel> findPages(final String nameQuery, final Map<String, Object> params, final int begin, final int offset) {
        if (log.isDebugEnabled()) {
            log.debug("findPages:[begin=" + begin + ",offset=" + offset + ",nameQuery=" + nameQuery + "]");
        }
        List<PlayerItemModel> list = (List<PlayerItemModel>) template.execute(new HibernateCallback<Object>() {
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
