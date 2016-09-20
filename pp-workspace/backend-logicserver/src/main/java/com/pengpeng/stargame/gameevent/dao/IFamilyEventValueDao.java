package com.pengpeng.stargame.gameevent.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.FamilyEventValue;

/**
 * User: mql
 * Date: 13-12-31
 * Time: 下午3:30
 */
public interface IFamilyEventValueDao extends BaseDao<String,FamilyEventValue> {

    FamilyEventValue getFamilyEventValue(String familyId);
}
