package com.pengpeng.stargame.player.dao.impl;


import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.model.successive.PlayerSuccessive;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.successive.dao.IPlayerSuccessiveDao;
import com.pengpeng.stargame.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-12下午7:52
 */
@Component()
@DaoAnnotation(prefix = "friend.")
public class FriendDaoImpl extends RedisDao<Friend> implements IFriendDao {

    @Override
    public Friend getBean(String index) {
        Friend friend = super.getBean(index);
        if (friend==null){
            friend = new Friend();
            friend.setPid(index);
            saveBean(friend);
        }


        return friend;
    }

    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IPlayerSuccessiveDao playerSuccessiveDao;
    @Override
    public Page<FriendItem> findPage(Friend friend,int no, int size){
        List<FriendItem> list = new ArrayList<FriendItem>(friend.getFriends().values());

/*        for(FriendItem item:list){
            if (null==item){
                continue;
            }
            FarmPlayer fp = farmPlayerDao.getBean(item.getFid());
            if (null==fp){
                continue;
            }
            item.setSort(fp.getLevel());
            item.setSort1(fp.getExp());
        }*/
        //改成批量获取
        Set<String> keys=new HashSet<String>();
        for(FriendItem item:list){
            keys.add(item.getFid());
        }

        Map<String,PlayerSuccessive> successivePlayerMap=playerSuccessiveDao.mGet(keys);
        Date now = new Date();
        Map<String,FarmPlayer> farmPlayerMap=farmPlayerDao.mGet(keys);
        for(FriendItem item:list){
            FarmPlayer fp = farmPlayerMap.get(item.getFid());
            if (null==fp){
                continue;
            }
            item.setSort(fp.getLevel());
            item.setSort1(fp.getExp());

            PlayerSuccessive ps = successivePlayerMap.get(item.getFid());
            if (null==ps){
                continue;
            }
            if(ps.getLastLogin() == null) {
                continue;
            }
            item.setSort2((now.getTime()-ps.getLastLogin().getTime())> PlayerConstant.OFFLINE_7DAY?1:0);

        }


        Collections.sort(list, new Comparator<FriendItem>() {
            @Override
            public int compare(FriendItem o1, FriendItem o2) {

//                if (o1.getSort()>o2.getSort()){
//                    return -1;
//                }
//                if (o1.getSort()<o2.getSort()){
//                    return 1;
//                }
//                if (o1.getSort1()>o2.getSort1()){
//                    return -1;
//                }
//                if (o1.getSort1()>o2.getSort1()){
//                    return 1;
//                }
//
//                return 0;
                if((o2.getSort3()-o1.getSort3()!=0)){//顶置
                    return o2.getSort3()-o1.getSort3();
                }
                if((o2.getSort2()-o1.getSort2()!=0)){//登陆时间
                    return -(o2.getSort2()-o1.getSort2());
                }
                return o2.getSort()-o1.getSort();
            }
        });
        int allsize=list.size()+1;
        int len = 0;
        int start = 0;
        if (no==1){//如果是第一页,只要取3个
            start = 0;
            len = 4;
        }else{
            start = (no-1)*size-1;
            len = no*size-1;
        }

        if (len>=list.size()){
            len = list.size();
        }
        list = list.subList(start,len);
        int mod= allsize%size;
        int maxPage = allsize/size;
        if(mod>0){
            maxPage +=1;
        }
        if (maxPage<=0){
            maxPage = 1;
        }
        if (no==1){
            list.add(0,newQinMaItem());
        }
        Page<FriendItem> page = new Page<FriendItem>();
        page.setBegin(no);
        page.setSize(size);
        page.setMaxPage(maxPage);
        page.setElements(list);
        return page;
    }

    @Override
    public int getFriendNum(String pid) {
        Friend friend = this.getBean(pid);
        return friend.getFriendSize();
    }

    private FriendItem newQinMaItem(){
        return new FriendItem(Constant.QINMA_ID,new Date());
    }
    @Override
    public Class<Friend> getClassType() {
        return Friend.class;
    }
}
