package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.MusicBox;
import com.pengpeng.stargame.piazza.dao.IMusicBoxDao;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-5上午11:27
 */
@Component()
@DaoAnnotation(prefix = "music.box.")
public class MusicBoxDaoImpl extends RedisDao<MusicBox> implements IMusicBoxDao {
    @Override
    public Class<MusicBox> getClassType() {
        return MusicBox.class;
    }

    @Override
    public MusicBox getBean(String index) {
        MusicBox box = super.getBean(index);
        if(null==box){
            box = new MusicBox();
            box.setId(index);
        }
        box.init(new Date());
        saveBean(box);
        return box;
    }
}
