package com.pengpeng.stargame.container;

import com.pengpeng.stargame.managed.IManageService;
import com.pengpeng.stargame.managed.NodeConfig;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-10下午3:21
 */
@Component
public class SessionContainerImpl2 implements ISessionContainer {
    private Map<String,Session> sessions = new ConcurrentHashMap<String,Session>();

    @Autowired
    private IManageService manageService;

    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public Session getElement(String index) {
        return sessions.get(index);
    }

    @Override
    public void addElement(Session element) {
        sessions.put(element.getPid(),element);
    }

    @Override
    public void addElement(Collection<Session> colls) {
        for(Session s:colls){
            addElement(s);
        }
    }

    @Override
    public void removeElement(Session element) {
        sessions.remove(element.getPid());
    }

    @Override
    public void removeElement(String index) {
        sessions.remove(index);
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
        if (StringUtils.isBlank(familyId)){
            return null;
        }
        Collection<Session> colls = Collections.unmodifiableCollection(sessions.values());
        List<Session> list = new ArrayList<Session>();
        for(Session s:colls){
            String fid = s.getParam(SessionUtil.KEY_CHANNEL_FAMILY);
            if(familyId.equalsIgnoreCase(fid)){
                list.add(s);
            }
        }
        return list;
    }

}
