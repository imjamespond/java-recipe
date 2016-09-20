package com.pengpeng.stargame.util;

import java.util.UUID;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-21下午12:20
 */
public class Uid {

    public static String uuid(){
        String key = UUID.randomUUID().toString();
        key = key.replaceAll("-","");
        return key;
    }
}
