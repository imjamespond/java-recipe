package com.pengpeng.stargame.fashion.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.fashion.dao.IFashionPlayerDao;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.FashionItem;
import com.pengpeng.stargame.model.room.FashionPkg;
import com.pengpeng.stargame.model.room.FashionPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 时装衣柜 ，包含所有分类
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 下午2:13
 */
@Component("fashionCupboardDao")
@DaoAnnotation(prefix = "fashion.pkg.")
public class FashionCupboardDaoImpl extends RedisDao<FashionCupboard> implements IFashionCupboardDao {
    @Autowired
    private IFashionPlayerDao fashionPlayerDao;
	@Override
	public Class<FashionCupboard> getClassType() {
		return FashionCupboard.class;
	}

	@Override
	public FashionPkg getFashionPkg(String pid ,String type) {
		FashionCupboard fashionCupboard = getBean(pid);
		return fashionCupboard.getFashionPkg(type);
	}
    @Override
    public FashionCupboard getBean(String pid){
        FashionCupboard fashionCupboard=super.getBean(pid);
        if(fashionCupboard==null){
            fashionCupboard =new FashionCupboard();
            fashionCupboard.setId(pid);
            this.saveBean(fashionCupboard);
        }

        /**
         * 检测过期物品 并且从身上脱掉
         */
        List<String> values=new ArrayList<String>();
        for(Map.Entry<String,FashionPkg> fashionPkgEntry:fashionCupboard.getFashionPkgMap().entrySet()){
            values.addAll(fashionPkgEntry.getValue().checkTime());
        }
        if(values.size()>0){
            FashionPlayer fashionPlayer=fashionPlayerDao.getFashionPlayer(pid);

            for(String value:values){
                fashionPlayer.removeByValue(value);
            }
            this.saveBean(fashionCupboard);
            fashionPlayerDao.saveBean(fashionPlayer);
        }
        return fashionCupboard;

    }
}
