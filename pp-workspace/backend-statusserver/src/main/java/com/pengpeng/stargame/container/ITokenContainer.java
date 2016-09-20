package com.pengpeng.stargame.container;

import com.pengpeng.stargame.container.IMapContainer;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-16下午2:50
 */
public interface ITokenContainer {

    public void addToken(String token);
    public void removeToken(String token);
    public boolean contains(String token);
}
