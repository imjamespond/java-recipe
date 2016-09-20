package com.pengpeng.stargame;

import com.pengpeng.stargame.rpc.Session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-12下午3:29
 */
public class OnlineChannel {
    private Map<String,Session> items = new ConcurrentHashMap<String,Session>();
    private Random random = new Random();
    public OnlineChannel(){

    }

    public Session getSession(String pid) {
        return items.get(pid);
    }

    public void enter(Session session) {
        items.put(session.getPid(),session);
    }

    public void outer(Session session) {
        items.remove(session.getPid());
    }

    public int size() {
        return items.size();
    }

    public void cleanByServerId(String id){
        if (id==null){
            return;
        }
        int idx =0;
        Collection<Session> colls = items.values();
        for(Session s:colls){
            if (s.getFrontend()==null){
                items.remove(s.getPid());
                idx++;
                continue ;
            }
            if (s.getFrontend().equalsIgnoreCase(id)){
                items.remove(s.getPid());
                idx++;
            }
        }
        System.out.println("Clean Session id="+id +";size"+idx);
    }
    public Session[] random(String[] ids){
        if (items.size()<=0){
            return null;
        }
        int size = 6;
        Set<String> pids = new HashSet<String>();
        List<Session> temps = new ArrayList<Session>(size);
        for(String id:ids){
            //需要排重的id
            pids.add(id);
        }
        if (items.size()<=size){//如果很少用户
            List<Session> lists = new ArrayList<Session>(items.values());
            for(Session s:lists){//排重后返回
                if (pids.contains(s.getPid())){
                    continue;
                }
                pids.add(s.getPid());
                temps.add(s);
            }
            return temps.toArray(new Session[0]);
        }
        List<Session> lists = new ArrayList<Session>(items.values());
        int idx = 100;
        while(temps.size()<size&&idx>0){//很多用户,随机6个,并排重
            idx--;
            Session s = random(lists);
            if (pids.contains(s.getPid())){
                continue;
            }
            pids.add(s.getPid());
            temps.add(s);
        }
        return temps.toArray(new Session[0]);
    }

    private Session random(List<Session> lists){
        int idx = random.nextInt(lists.size());
        if (idx>=lists.size()){
            Session s = lists.get(lists.size()-1);
            return s;
        }
        return lists.get(idx);
    }

    public static void main(String[] args){
        OnlineChannel oc = new OnlineChannel();
        oc.enter(new Session("1","a"));
        oc.enter(new Session("2","a"));
        oc.enter(new Session("3","a"));
        oc.enter(new Session("4","a"));
        oc.enter(new Session("5","a"));
        oc.enter(new Session("6","a"));
        oc.enter(new Session("7","a"));
        oc.enter(new Session("8","a"));
        oc.enter(new Session("9","a"));
        oc.enter(new Session("10","a"));
        Session[] ss = oc.random(new String[]{"1","2","3","4","5","6"});
        oc.cleanByServerId("a");
        System.out.println(ss);
    }
}
