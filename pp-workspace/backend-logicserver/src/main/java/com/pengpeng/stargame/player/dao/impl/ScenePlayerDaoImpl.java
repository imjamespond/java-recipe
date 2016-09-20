package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.player.ScenePlayer;
import com.pengpeng.stargame.player.ScenePlayerBuilder;
import com.pengpeng.stargame.player.dao.IScenePlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-5下午3:57
 */
@Component
@DaoAnnotation(prefix = "p.scene.")
public class ScenePlayerDaoImpl extends RedisDao<ScenePlayer> implements IScenePlayerDao {
    @Autowired
    private ScenePlayerBuilder builder;

    @Override
    public Class<ScenePlayer> getClassType() {
        return ScenePlayer.class;
    }

    @Override
    public ScenePlayer getBean(String index) {
        ScenePlayer sp = super.getBean(index);
        if (null==sp){
            sp = builder.newDefalutScenePlayer(index);
            this.saveBean(sp);
            return sp;
        }
        return sp;
    }
}
