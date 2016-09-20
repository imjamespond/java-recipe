package com.pengpeng.stargame.model.wharf;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.model.BaseEntity;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerWharf extends BaseEntity<String> {
    private String pid;
    private Date refreshTime;     //刷新时间
    private boolean shipArrived; //是否已到达
    private boolean enable;   //是否开启
    private Date enableDate;   //开启时间
    private List<PlayerWharfInvoice> invoices;//所有货单
    private int credit;        //货单总积分
    private int contribution; //货单总贡献
    private int amountContri; //总贡献记录
    private int requestHelp; //请求协作数
    private long reqHelpTime; //请求协作时间
    private int counter;        //连续启航数
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public boolean isShipArrived() {
        return shipArrived;
    }

    public void setShipArrived(boolean shipArrived) {
        this.shipArrived = shipArrived;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(Date enableDate) {
        this.enableDate = enableDate;
    }

    public List<PlayerWharfInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<PlayerWharfInvoice> invoices) {
        this.invoices = invoices;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public int getAmountContri() {
        return amountContri;
    }

    public void setAmountContri(int amountContri) {
        this.amountContri = amountContri;
    }

    public void addContri() {
        this.amountContri += contribution;
    }

    public int getRequestHelp() {
        return requestHelp;
    }

    public void setRequestHelp(int requestHelp) {
        this.requestHelp = requestHelp;
    }

    public long getReqHelpTime() {
        return reqHelpTime;
    }

    public void setReqHelpTime(long reqHelpTime) {
        this.reqHelpTime = reqHelpTime;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;
    }

}
