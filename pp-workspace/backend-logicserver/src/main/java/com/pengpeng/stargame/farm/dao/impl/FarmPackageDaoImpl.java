package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-27 下午4:04
 */
@Component("farmPackagedao")
@DaoAnnotation(prefix = "farm.pkg.")
public class FarmPackageDaoImpl extends RedisDao<FarmPackage> implements IFarmPackageDao {

	@Autowired
	private FarmBuilder farmBuilder;

	@Override
	public Class<FarmPackage> getClassType() {
		return FarmPackage.class;
	}

	@Override
	public FarmPackage getBean(String pid) {
		FarmPackage farmPackage = super.getBean(pid);
		if(farmPackage == null){
			farmPackage = farmBuilder.newFarmPackage(pid);
			super.saveBean(farmPackage);
		}
        /**
         * 检测过期物品
         */
        List<String > list=farmPackage.checkTime();
        if(list.size()>0){
            saveBean(farmPackage);
        }

		return farmPackage;
	}
}
