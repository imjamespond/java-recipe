package com.pengpeng.stargame;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-15下午2:34
 */
public interface ILock {

    public boolean lock(String id);

    public void unlock(String id);
}
