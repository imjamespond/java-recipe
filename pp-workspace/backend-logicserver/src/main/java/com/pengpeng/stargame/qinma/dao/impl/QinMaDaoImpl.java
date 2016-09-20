package com.pengpeng.stargame.qinma.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.QinMa;
import com.pengpeng.stargame.qinma.dao.IQinMaDao;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-8-14
 * Time: 上午11:15
 */
@Component
@DaoAnnotation(prefix = "qinma.")
public class QinMaDaoImpl extends RedisDao<QinMa>  implements IQinMaDao {
    @Override
    public QinMa getQinMa(String id) {

        QinMa qinMa=getBean(id);
        if(qinMa==null){
            qinMa=new QinMa(id, DateUtil.getNextCountTime());
            this.saveBean(qinMa);
            return qinMa;
        }
        Date now=new Date();
        if(now.after(qinMa.getRefreshTime())){
            qinMa.setFarmEvaluation(0);
            qinMa.setRoomEvaluation(0);
            qinMa.setRefreshTime(DateUtil.getNextCountTime());
            this.saveBean(qinMa);
            return qinMa;
        }


        return qinMa;
    }

    @Override
    public Class<QinMa> getClassType() {
        return QinMa.class;
    }
}
