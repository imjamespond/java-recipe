package com.pengpeng.stargame.fashion.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionPkg;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 下午2:12
 */
public interface IFashionCupboardDao extends BaseDao<String,FashionCupboard> {

	/**
	 * 获取指定类型的时装仓库
	 * @param pid
	 * @param type
	 * @return FashionPkg
	 */
	public FashionPkg getFashionPkg(String pid ,String type);

}
