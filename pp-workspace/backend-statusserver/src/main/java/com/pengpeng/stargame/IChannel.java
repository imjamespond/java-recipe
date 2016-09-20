package com.pengpeng.stargame;

import com.pengpeng.stargame.model.Indexable;
import com.pengpeng.stargame.rpc.Session;

import java.util.Collection;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午3:13
 */
public interface IChannel extends Indexable<String>{

//    public Session getSession(String pid);

    public void enter(String pid);

    public void outer(String pid);

    public Collection<String> getMembers();
    public int size();
}
