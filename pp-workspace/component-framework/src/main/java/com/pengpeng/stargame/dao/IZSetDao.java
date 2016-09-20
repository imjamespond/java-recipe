package com.pengpeng.stargame.dao;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:10
 */
public interface IZSetDao {

    public long size(String id);

    public void addBean(String id, String bean, double score);

    public void addBeanExpire(String id, String bean, double score, int day);

    public void removeBean(String id, String bean);

    public Set<String> getMembers(String id, int start, int end);

    public Set<String> getMembersAsc(String id, int start, int end);

    public Set<ZSetOperations.TypedTuple<String>> getReverseRangeWithScores(String id, int start, int end);

    public boolean contains(String id, String bean);

    public Double getScore(String id, String bean);

    public Long getRank(String id, String bean);

    public Set<String> getMembersByScore(String id, int score, int star, int end);

    public void incrementScore(String id,int score,String  bean);

    public void removeRange(String id,long start,long end);

}
