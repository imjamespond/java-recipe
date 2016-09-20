package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.collectcrop.CollectControl;

/**
 * User: mql
 * Date: 14-3-24
 * Time: 下午5:16
 */
public interface IFamilyCollectControlDao extends BaseDao<String,CollectControl> {

    public CollectControl getCollectControl();
}
