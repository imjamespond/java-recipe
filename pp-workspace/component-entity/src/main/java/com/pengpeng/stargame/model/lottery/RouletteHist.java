package com.pengpeng.stargame.model.lottery;

import com.pengpeng.stargame.model.Indexable;
import com.pengpeng.stargame.vo.lottery.RouletteHistVO;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:38
 */
public class RouletteHist implements Indexable<String>  {
    private String pid;
    private RouletteHistVO vo;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public RouletteHistVO getVo() {
        return vo;
    }

    public void setVo(RouletteHistVO vo) {
        this.vo = vo;
    }

    @Override
    public String getKey() {
        return pid;
    }

    @Override
    public void setKey(String key) {
        pid = key;
    }
}
