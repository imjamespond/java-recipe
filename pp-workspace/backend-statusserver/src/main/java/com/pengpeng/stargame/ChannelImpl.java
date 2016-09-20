package com.pengpeng.stargame;

import com.pengpeng.stargame.rpc.Session;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午3:19
 */
public class ChannelImpl implements IChannel {
    private String id;
    private Set<String> items = new HashSet<String>();
    private Lock lock = new ReentrantLock();

    protected ChannelImpl(String id){
        this.id = id;
    }
    @Override
    public String getKey() {
        return id;
    }

    @Override
    public void setKey(String key) {
    }

    public Collection<String> getMembers(){
        try{
            lock.lock();
            return new HashSet<String>(items);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public void enter(String pid) {
        try{
            lock.lock();
            items.add(pid);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void outer(String pid) {
        try{
            lock.lock();
            items.remove(pid);
        }finally{
            lock.unlock();
        }
    }
}
