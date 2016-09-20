package com.pengpeng.admin.stargame.manager;

import com.pengpeng.stargame.tool.LineCallback;

import java.util.List;

/**
 * User: mql
 * Date: 13-10-16
 * Time: 上午9:10
 */
public interface ILineHandleManager extends LineCallback {
    public List<String> analyse(String fileName);

    public void store(List<String> lines);
}
