package com.pengpeng.stargame.container;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-16下午2:53
 */
@Component()
public class TokenContainerImpl implements ITokenContainer {
    private Set<String> tokens = new HashSet<String>();

    @Override
    public void addToken(String token) {
        tokens.add(token);
    }

    @Override
    public void removeToken(String token) {
        tokens.remove(token);
    }

    @Override
    public boolean contains(String token) {
        return tokens.contains(token);
    }
}
