package com.pengpeng.stargame.container;

import com.pengpeng.stargame.rpc.Session;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-8下午4:21
 */
public interface ISessionContainer {
    public void init();
    public Session getElement(String index);
    public void addElement(Session element);
    public void addElement(Collection<Session> colls);
    public void removeElement(Session element);
    public void removeElement(String index);

    public Session createSession(String pid,String sceneId);

    public void updateSession(Map<String,String> map);

    public List<Session> getByChannelId(String familyId);
}
