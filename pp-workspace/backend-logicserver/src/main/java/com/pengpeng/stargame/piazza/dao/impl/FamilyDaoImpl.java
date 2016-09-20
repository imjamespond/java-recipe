package com.pengpeng.stargame.piazza.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rsp.RspFamilyFactory;
import com.pengpeng.stargame.vo.piazza.FamilyPageVO;
import com.pengpeng.stargame.vo.piazza.FamilyVO;
import org.apache.commons.lang.StringUtils;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午3:11
 */
@Component("familyDao")
@DaoAnnotation(prefix = "pza.family.")
public class FamilyDaoImpl extends RedisDao<Family> implements IFamilyDao {
    @Autowired
    private IFamilyMemberDao memberDao;

    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;

    @Autowired
    private RspFamilyFactory rspFamilyFactory;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private IPlayerDao playerDao;

    @Override
    public Family getBean(String index) {
        Family family = super.getBean(index);
        if (family ==null){
            FamilyRule rule = familyRuleContainer.getElement(index);
            if (rule==null){
                return null;
            }
            family = new Family();
            family.setId(rule.getId());
            family.setName(rule.getName());
            super.saveBean(family);
        }

        if(family.refresh()){
            saveBean(family);
        }

        return family;
    }

    @Override
    public Class<Family> getClassType() {
        return Family.class;
    }
    @Override
    public Set<String> getMembers(String fid,int star,int end){
        return memberDao.getMembers(fid, star, end);
    }
    @Override
    public void addMember(String fid,String pid,double score){
        memberDao.addBean(fid,pid,score);
    }

    @Override
    public boolean isMember(String fid, String pid) {
        return memberDao.contains(fid,pid);
    }

    @Override
    public void removeMember(String fid,String pid){
        memberDao.removeBean(fid,pid);
    }

    @Override
    public FamilyMemberInfo getMemberInfo(String fid, String pid) {
        FamilyMemberInfo m = familyMemberInfoDao.getFamilyMember(pid);
        if (m==null){
            return null;
        }
        if (m.getFamilyId().equalsIgnoreCase(fid)){
            return m;
        }
        return null;
    }

    @Override
    public List<FamilyMemberInfo> getMemberInfoList(String fid, int no, int size) {

        int star = (no-1)*size;
        int end = no*size-1;
        Set<String> ids = memberDao.getMembers(fid,star,end);
        List<FamilyMemberInfo> list = new ArrayList<FamilyMemberInfo>();
        for(String id:ids){
            FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(id);
            list.add(info);
        }
        return list;
    }

    @Override
    public List<FamilyMemberInfo> getMemberInfoList2(String fid, String myid, int no, int size) {
        if (no<1){
            no =1;
        }
        if (size<7||size>=10){
            size = 7;
        }
        List<FamilyMemberInfo> list = new ArrayList<FamilyMemberInfo>();
        int star = (no-1)*size;
        int end = no*size-1;
        String starPid = "";

        //我的信息
        FamilyMemberInfo selfInfo = familyMemberInfoDao.getFamilyMember(myid);
        if(no == 1){
            list.add(selfInfo);
        }else{
            star--;
            end--;
        }

        //第一页最多取7个,可能存在自己
        Set<String> ids = memberDao.getMembersAsc(fid,0,7);
        for(String id:ids){
            FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(id);
            //如果是自己,忽略,增加偏移
            if(myid.equals(info.getPid())) {
                star++;
                end++;
                continue;
            }
            if(no == 1){
                if(list.size() >= size){
                    break;
                }
                list.add(info);
            }
        }

        if(no==1){
            return list;
        }

        ids = memberDao.getMembersAsc(fid,star,end);
        for(String id:ids){
            FamilyMemberInfo info = familyMemberInfoDao.getFamilyMember(id);
            list.add(info);
        }
        return list;
    }



    @Override
    public long getMemberSize(String id) {
        return memberDao.size(id);
    }

    @Override
    public Family getBeanByStarId(int starId) {
        FamilyRule rule = familyRuleContainer.getRuleByStarId(starId);
        if (rule==null){
            return null;
        }
        Family family = this.getBean(rule.getId());
        return family;
    }

    @Override
    public List<Family> getRank() {
        List<FamilyRule> rules = familyRuleContainer.getAll();
        Set<String> keys =new HashSet<String>();
        for(FamilyRule rule:rules){
            keys.add(rule.getId());
        }
        List<Family> list = new ArrayList<Family>();
        for(String id:keys){
            Family family = getBean(id);
            list.add(family);
        }

        Collections.sort(list,new Comparator<Family>() {
            @Override
            public int compare(Family o1, Family o2) {
                if (o1.getFansValue()>o2.getFansValue()){
                    return -1;
                }
                if (o1.getFansValue()<o2.getFansValue()){
                    return 1;
                }
                return 0;
            }
        });
        return list;
    }

    @Override
    public FamilyPageVO findPage(Family family, String content, int no, final int size){
        List<Family> list = getRank();//取得数据
        List<FamilyVO> voList = rankVO(list);//先排名
        int rank = rankIndex(family,voList);
        if (!StringUtils.isBlank(content)){
            List<FamilyVO> temp = new ArrayList<FamilyVO>();
            for(FamilyVO vo:voList){
                if (vo.getName().indexOf(content)>=0){
                    temp.add(vo);
                }
            }
            voList = temp;
        }

        //做翻页
        if (no<=0){
            no = 1;
        }

        FamilyPageVO page = newPage(voList,no,size);
        page.setMyRank(rank);
        final int star = (no-1)*size;
        int end = no*size;
        if (end>=voList.size()){
            end = voList.size();
        }
        voList = voList.subList(star,end);
        page.setFamilyVOs(voList.toArray(new FamilyVO[0]));
        return page;
    }

    private int rankIndex(Family family, List<FamilyVO> voList) {
        if (family==null){
            return voList.size();
        }
        for(FamilyVO vo:voList){
            if (family.getId().equalsIgnoreCase(vo.getId())){
                return vo.getRank();
            }
        }
        return voList.size();
    }

    private FamilyPageVO newPage(List<FamilyVO> list,int no ,int size){
        FamilyPageVO page = new FamilyPageVO();
        int maxPage = 1;
        int count = list.size();
        if (count>0){
            maxPage = count/size;
            if (count%size>0){
                maxPage++;
            }
        }
        page.setPageNo(no);
        page.setMaxPage(maxPage);
        return page;
    }
    private List<FamilyVO> rankVO(List<Family> list){
        List<FamilyVO> vos = new ArrayList<FamilyVO>(list.size());
        for(int i=0;i<list.size();i++){
            FamilyVO vo = rspFamilyFactory.newFamilyVO(list.get(i));
            vos.add(vo);
            vo.setRank(i+1);
        }
        return vos;
    }
}
