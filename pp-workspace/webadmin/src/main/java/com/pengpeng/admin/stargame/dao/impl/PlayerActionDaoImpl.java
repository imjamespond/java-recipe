package com.pengpeng.admin.stargame.dao.impl;

import com.pengpeng.admin.stargame.dao.IPlayerActionDao;
import com.pengpeng.admin.stargame.model.PlayerActionModel;
import com.tongyi.action.Page;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:10
 */
@Repository(value = "playeractiondao")
public class PlayerActionDaoImpl implements IPlayerActionDao{
    private static final Log log = LogFactory.getLog(PlayerActionDaoImpl.class);
    @Autowired
    private HibernateTemplate template;

    @Override
    public void createBean(PlayerActionModel bean) throws BeanAreadyException {
        template.save(bean);
    }

    @Override
    public void updateBean(PlayerActionModel bean) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeBean(PlayerActionModel bean) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeBean(Serializable id) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PlayerActionModel findById(Serializable id) throws NotFoundBeanException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PlayerActionModel> findPages(final String nameQuery, final Map<String, Object> params, final int begin, final int pagesize) {
        if (log.isDebugEnabled()){
            log.debug("findPages:[begin="+begin+",pagesize="+pagesize+",nameQuery="+nameQuery+"]");
        }

        List<PlayerActionModel> list = (List<PlayerActionModel>)template.execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(nameQuery);
                if(params.containsKey("uid")){
                    query.setString("uid", ((String[]) params.get("uid"))[0]);
                }
                if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
                    String dateStr1 = ((String[])params.get("dateBegin"))[0];
                    String dateStr2 = ((String[])params.get("dateEnd"))[0];
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1,d2;
                    try {
                        d1 = formatter.parse(dateStr1);
                        d2 = formatter.parse(dateStr2);
                        query.setDate("dateBegin", d1).setDate("dateEnd", d2);
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                return query.setFirstResult(begin).setMaxResults(pagesize).list();
            }
        });
        return list;
    }

    @Override
    public List<PlayerActionModel> findByNameQuery(String nameQuery, Map<String, Object> params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void findNcount(final String nameQuery, final Map<String, Object> params, final int begin, final int pagesize, Page<PlayerActionModel> page){
        Result<PlayerActionModel> rs = (Result<PlayerActionModel>)template.execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(nameQuery);
                Query queryCount = session.createQuery("select count(*) "+nameQuery);
                if(params.containsKey("uid")){
                    query.setString("uid", ((String[]) params.get("uid"))[0]);
                    queryCount.setString("uid", ((String[]) params.get("uid"))[0]);
                }
                if(params.containsKey("dateBegin")&&params.containsKey("dateEnd")){
                    String dateStr1 = ((String[])params.get("dateBegin"))[0];
                    String dateStr2 = ((String[])params.get("dateEnd"))[0];
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1,d2;
                    try {
                        d1 = formatter.parse(dateStr1);
                        d2 = formatter.parse(dateStr2);
                        query.setDate("dateBegin", d1).setDate("dateEnd", d2);
                        queryCount.setDate("dateBegin", d1).setDate("dateEnd", d2);
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                Result rs = new Result();
                rs.list = query.setFirstResult(begin).setMaxResults(pagesize).list();
                rs.count = queryCount.list();
                return rs;
            }
        });
        page.setRows(rs.list);
        page.setTotal(rs.count.get(0).intValue());
    }
}

class Result<T>{
    public List<Long> count;
    public List<T> list;
}