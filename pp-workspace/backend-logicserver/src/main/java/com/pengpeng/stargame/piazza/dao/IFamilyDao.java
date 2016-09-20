package com.pengpeng.stargame.piazza.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.util.Page;
import com.pengpeng.stargame.vo.piazza.FamilyPageVO;

import java.util.List;
import java.util.Set;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-27下午3:08
 */
public interface IFamilyDao extends BaseDao<String, Family> {
    Set<String> getMembers(String fid,int star,int end);

    /**
     * 增加成员关系
     * @param fid
     * @param pid
     */
    void addMember(String fid, String pid,double score);

    /**
     * 是否家族成员
     * @param fid
     * @param pid
     * @return
     */
    boolean isMember(String fid, String pid);

    /**
     * 删除成员关系
     * @param fid
     * @param pid
     */
    public void removeMember(String fid,String pid);

    /**
     * 取得成员信息
     * @param fid
     * @param pid
     * @return
     */
    public FamilyMemberInfo getMemberInfo(String fid,String pid);

    /**
     * 取得成员列表
     * @param fid
     * @return
     */
    public List<FamilyMemberInfo> getMemberInfoList(String fid,int no,int size);

    /**
     * 取得成员列表
     * 明星第一，自己第二
     * @param fid
     * @return
     */
    public List<FamilyMemberInfo> getMemberInfoList2(String fid, String myid,int no,int size);

    long getMemberSize(String id);

    /**
     * 根据网站中的明星id取得家族对象
     * @param starId
     * @return
     */
    public Family getBeanByStarId(int starId);

    /**
     * 取得家族排行榜
     * @return
     */
    public List<Family> getRank();

    FamilyPageVO findPage(Family family, String content, int pageNo, int size);
}
