package com.pengpeng.stargame.antiaddiction.container;

import com.pengpeng.stargame.exception.AlertException;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface IAntiAddictionContainer {

    /**
     * 验证身份证号码
     * @return
     */
    public long check(String identity)  throws AlertException;

    public boolean isEighteen(long birth);

    public int decline(String pid,int val);
}
