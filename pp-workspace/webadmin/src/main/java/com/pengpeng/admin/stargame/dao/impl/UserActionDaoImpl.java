package com.pengpeng.admin.stargame.dao.impl;

import com.pengpeng.admin.stargame.dao.IUserActionDao;
import com.pengpeng.admin.stargame.model.PlayerTaskActionModel;
import com.pengpeng.admin.stargame.model.UserActionModel;
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
@Repository(value = "useractiondao")
public class UserActionDaoImpl implements IUserActionDao{
    @Autowired
    private HibernateTemplate template;
    private static final Log log = LogFactory.getLog(PlayerTaskActionModelDaoImpl.class);

    @Override
    public void createBean(UserActionModel bean) throws BeanAreadyException {
        template.save(bean);
    }

    @Override
    public void updateBean(UserActionModel bean) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeBean(UserActionModel bean) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeBean(Serializable id) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public UserActionModel findById(Serializable id) throws NotFoundBeanException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public List<UserActionModel> findByNameQuery(String nameQuery, Map<String, Object> params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserActionModel> findPages(final String nameQuery, final Map<String, Object> params, final int begin, final int offset) {
        if (log.isDebugEnabled()) {
            log.debug("findPages:[begin=" + begin + ",offset=" + offset + ",nameQuery=" + nameQuery + "]");
        }
        List<UserActionModel> list = (List<UserActionModel>) template.execute(new HibernateCallback<Object>() {
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
