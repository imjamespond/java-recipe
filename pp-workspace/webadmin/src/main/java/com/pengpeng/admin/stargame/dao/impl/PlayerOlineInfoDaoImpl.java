package com.pengpeng.admin.stargame.dao.impl;

import com.pengpeng.admin.stargame.dao.IPlayerOlineInfoDao;
import com.pengpeng.admin.stargame.model.PlayerOlineInfoModel;
import com.tongyi.exception.BeanAreadyException;
import com.tongyi.exception.NotFoundBeanException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午11:29
 */
@Repository(value = "playerOlineInfoDao")
public class PlayerOlineInfoDaoImpl implements IPlayerOlineInfoDao {
    private static final Log log = LogFactory.getLog(PlayerOlineInfoDaoImpl.class);

    @Autowired
    private HibernateTemplate template;

    @Override
    public void createBean(PlayerOlineInfoModel bean) throws BeanAreadyException {
        template.save(bean);
    }

    @Override
    public void updateBean(PlayerOlineInfoModel bean) throws NotFoundBeanException {
        if (log.isDebugEnabled()){
            log.debug("updateBean:"+bean);
        }
        findById(bean.getId());
        template.update(bean);
    }

    @Override
    public void removeBean(PlayerOlineInfoModel bean) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeBean(Serializable id) throws NotFoundBeanException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public PlayerOlineInfoModel findById(Serializable id) throws NotFoundBeanException{
        if (log.isDebugEnabled()){
            log.debug("findById:"+id);
        }
        PlayerOlineInfoModel playerOlineInfo = (PlayerOlineInfoModel)template.get(PlayerOlineInfoModel.class, id);;
//        if (null==playerOlineInfo)
//            throw new NotFoundBeanException();
        return playerOlineInfo;
    }

    @Override
    public List<PlayerOlineInfoModel> findPages(String nameQuery, Map<String, Object> params, int begin, int offset) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PlayerOlineInfoModel> findByNameQuery(String nameQuery, Map<String, Object> params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
