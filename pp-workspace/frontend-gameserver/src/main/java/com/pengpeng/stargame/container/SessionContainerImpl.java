package com.pengpeng.stargame.container;

import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.NodeConfig;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午3:21
 */
@Deprecated
//@Component
public class SessionContainerImpl implements ISessionContainer {
    private Set<String> sets = new ConcurrentSkipListSet<String>();

    @Autowired
    private IManageService manageService;

    @Autowired
    private StatusRemote statusRemote;
    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public Session getElement(String index) {
        boolean exists = sets.contains(index);
        if (!exists){
            return null;
        }

        Session session = statusRemote.getSession(null,index);
        return session;
    }

    @Override
    public void addElement(Session element) {
        sets.add(element.getPid());
    }

    @Override
    public void addElement(Collection<Session> colls) {
        for(Session s:colls){
            addElement(s);
        }
    }

    @Override
    public void removeElement(Session element) {
        sets.remove(element.getPid());
    }

    @Override
    public void removeElement(String index) {
        sets.remove(index);
    }

    @Override
    public Session createSession(String pid,String sceneId) {
        Session session  = new Session(pid, NodeConfig.getId());
        session.setScene(sceneId);
        addElement(session);
        return session;
    }

    @Override
    public void updateSession(Map<String, String> map) {
        String pid = map.get("pid");
        Session session = getElement(pid);
        if (session==null){
            return ;
        }
        for(Map.Entry<String,String> entry:map.entrySet()){
            session.addParam(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public List<Session> getByChannelId(String familyId) {
        return null;  //TODO:方法需要实现
    }

}
