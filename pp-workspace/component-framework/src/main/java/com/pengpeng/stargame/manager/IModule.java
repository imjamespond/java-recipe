package com.pengpeng.stargame.manager;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-12上午11:49
 */
public interface IModule {
    public void lock();
    public void unlock();
    public void store();//保存数据
    public void load();//加载数据
}
