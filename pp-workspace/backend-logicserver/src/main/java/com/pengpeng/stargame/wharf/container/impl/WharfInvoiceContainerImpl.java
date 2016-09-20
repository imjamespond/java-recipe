package com.pengpeng.stargame.wharf.container.impl;

import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.wharf.*;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.rsp.RspFactory;
import com.pengpeng.stargame.wharf.container.IWharfInvoiceContainer;
import com.pengpeng.stargame.wharf.rule.WharfInvoiceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class WharfInvoiceContainerImpl extends HashMapContainer<String, WharfInvoiceRule> implements IWharfInvoiceContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IPlayerDao playerDao;

    @Override
    public WharfInfo checkShip(PlayerWharf pw, FarmPlayer fp) throws AlertException {
        WharfInfo wharfInfo = new WharfInfo();
        Date now = new Date();
        int animType = 0;
        long restTime = 0l;
        long diff = now.getTime() - pw.getRefreshTime().getTime();

        if (pw.isShipArrived()) {
            //船已到达,getRefreshTime为上次到达刷新时间
            if (diff > WharfConstant.HOUR29) {
                //设置船已到达,并重新计时,生成新货单
                pw.setRefreshTime(now);
                pw.setShipArrived(true);
                generateInvoices( pw, fp);
                restTime = WharfConstant.HOUR24;
                animType = WharfConstant.ANIM_ARRIVING;
            } else if (diff >= WharfConstant.HOUR24) {
                restTime = WharfConstant.HOUR29 - diff;
                //设置船已离开,并重新计时
                pw.setRefreshTime(new Date(pw.getRefreshTime().getTime() + WharfConstant.HOUR24));
                pw.setShipArrived(false);
                pw.setInvoices(null);
                pw.setCounter(0);
                animType = WharfConstant.ANIM_LEAVING;
            } else if (diff < WharfConstant.HOUR24) {
                restTime = WharfConstant.HOUR24 - diff;
            }
        } else {
            //船没到达,getRefreshTime为上次完成任务刷新时间
            if (diff < WharfConstant.HOUR5) {
                restTime = WharfConstant.HOUR5 - diff;
                pw.setInvoices(null);
            } else if (diff >= WharfConstant.HOUR5) {
                //设置船已到达,生成新货单
                pw.setRefreshTime(now);
                pw.setShipArrived(true);
                generateInvoices(pw, fp);
                restTime = WharfConstant.HOUR24;
                animType = WharfConstant.ANIM_ARRIVING;
            }
        }
        wharfInfo.setAnimType(animType);
        wharfInfo.setRestTime(restTime);
        wharfInfo.setShipArrived(pw.isShipArrived());
        wharfInfo.setEnable(pw.isEnable());
        return wharfInfo;
    }

    //生成新货单
    private void generateInvoices(PlayerWharf pw, FarmPlayer fp) throws AlertException {
        int farmLevel = fp.getLevel();
        Iterator<Entry<String,WharfInvoiceRule>> it = this.items.entrySet().iterator();
        List<WharfInvoiceRule> list = new ArrayList();
        while (it.hasNext()){
            Entry<String,WharfInvoiceRule> entry = it.next();
            WharfInvoiceRule rule = entry.getValue();
            if(farmLevel<rule.getLevel())
                continue;
            list.add(rule);
        }

        int credit = 0;
        int contribution = 0;
        int counterNum = list.size();
        if(list.size()<9)
            return;
        List<PlayerWharfInvoice> invoices = new ArrayList<PlayerWharfInvoice>();
        for(int i=0;i<9;i++){
            int index = (int) Math.floor(counterNum * Math.random());
            WharfInvoiceRule rule = list.get(index);

            PlayerWharfInvoice wharfInvoice = new PlayerWharfInvoice();
            wharfInvoice.setInvoiceId(rule.getInvoiceId());
            wharfInvoice.setHelp(false);
            wharfInvoice.setCompleted(false);
            invoices.add(wharfInvoice);

            credit += rule.getCredit();
            contribution += rule.getContribution();
        }
        pw.setCredit(credit);
        pw.setContribution(contribution);
        pw.setInvoices(invoices);
    }

    @Override
    public void needHelp(PlayerWharf pw, int order) throws AlertException {

        Calendar today = Calendar.getInstance();
        Calendar ref = Calendar.getInstance();
        ref.setTimeInMillis(pw.getReqHelpTime());

        if(today.get(Calendar.DAY_OF_YEAR) == ref.get(Calendar.DAY_OF_YEAR)) {
            if(pw.getRequestHelp()>=WharfConstant.NEED_HELP_NUM)
                exceptionFactory.throwAlertException("wharf.request.help.3time.limit");
            else
                pw.setRequestHelp(pw.getRequestHelp()+1);
        } else{
            pw.setRequestHelp(1);//隔天重计次数
        }

        List<PlayerWharfInvoice> list= pw.getInvoices();
        if(null == list){
            return;
        }
        if(list.size()<=order){
            return;
        }
        PlayerWharfInvoice wharfInvoice = list.get(order);
        wharfInvoice.setHelp(true);

        pw.setReqHelpTime(System.currentTimeMillis());
    }

    @Override
    public WharfInvoiceInfo submit(FarmPackage fp, PlayerWharf pw, int order) throws AlertException {
        List<PlayerWharfInvoice> list= pw.getInvoices();
        PlayerWharfInvoice invoice = list.get(order);
        if(null==invoice)
            return null;
        if(invoice.isCompleted()){
            exceptionFactory.throwAlertException("invoice.already.completed");
        }
        WharfInvoiceRule rule = getElement(invoice.getInvoiceId());
        int itemNum = fp.getSumByNum(rule.getItemId());
        if(null==rule){
            exceptionFactory.throwAlertException("invalid.item");
        }
        if(itemNum>=rule.getItemNum()){
            fp.deduct(rule.getItemId(), rule.getItemNum());//扣减
            invoice.setCompleted(true); //设为完成
            invoice.setHelp(false);
            if(fp.getPid().indexOf(pw.getPid())<0)
            invoice.setPlayerId(fp.getPid());//完成者id
        }else{
            exceptionFactory.throwAlertException("invoice.not.completed");
        }
        WharfInvoiceInfo wii = new WharfInvoiceInfo();
        wii.setFarmExp(rule.getFarmExp());
        wii.setGameCoin(rule.getGameCoin());
        return wii;
    }

    @Override
    public void sail(PlayerWharf pw, WharfInvoiceInfo[] wharfInvoiceInfo) throws AlertException {
        //
        if(null == wharfInvoiceInfo)
            exceptionFactory.throwAlertException("invoice.not.found");

        for(WharfInvoiceInfo invoiceInfo:wharfInvoiceInfo){
            if(!invoiceInfo.getCompleted()){
                exceptionFactory.throwAlertException("invoice.not.completed");
            }

        }

        pw.setRefreshTime(new Date());
        pw.setShipArrived(false);
        //清除旧订单
        pw.setInvoices(null);
        pw.setCounter(pw.getCounter()+1);
    }

    @Override
    public WharfInvoiceInfo[] checkInvoice(FarmPackage fp, PlayerWharf pw) throws AlertException {

        List<PlayerWharfInvoice> list= pw.getInvoices();
        if(null == list){
            return null;
        }
        WharfInvoiceInfo[] wharfInvoiceInfos = new WharfInvoiceInfo[list.size()];
        int i=0;
        for(PlayerWharfInvoice invoice:list){
            WharfInvoiceRule rule = getElement(invoice.getInvoiceId());
            if(rule==null){
               continue;
            }
            WharfInvoiceInfo wharfInvoiceInfo = new WharfInvoiceInfo();
            wharfInvoiceInfo.setInvoiceId(invoice.getInvoiceId());
            wharfInvoiceInfo.setItemId(rule.getItemId());
            wharfInvoiceInfo.setItemNum(rule.getItemNum());
            wharfInvoiceInfo.setGameCoin(rule.getGameCoin());
            wharfInvoiceInfo.setFarmExp(rule.getFarmExp());
            wharfInvoiceInfo.setCompleted(invoice.isCompleted());
            wharfInvoiceInfo.setHelp(invoice.isHelp());
            if(invoice.getPlayerId()!=null){
                Player player = playerDao.getBean(invoice.getPlayerId());
                if(null!=player){
                    wharfInvoiceInfo.setName(player.getNickName());
                    wharfInvoiceInfo.setPortrait(RspFactory.getUserPortrait(player.getUserId()));
                }
            }

            BaseItemRule item = baseItemRulecontainer.getElement(rule.getItemId());
            if(item != null){
                wharfInvoiceInfo.setItemName(item.getName());
            }
            wharfInvoiceInfo.setItemInventory(fp.getSumByNum(rule.getItemId()));

            wharfInvoiceInfos[i++] = wharfInvoiceInfo;
        }
        return wharfInvoiceInfos;
    }

    @Override
    public void checkEnable(FarmPlayer fp, Player p) throws AlertException {
        if(fp.getLevel()<30){
            exceptionFactory.throwAlertException("farm.level.less.than.30");
        }
        if(p.getGameCoin()<500000){
            exceptionFactory.throwAlertException("game.coin.less.than.50w");
        }
        p.decGameCoin(500000);
    }

    @Override
    public boolean getHelpState(PlayerWharf pw) {
        List<PlayerWharfInvoice> list= pw.getInvoices();
        if(null == list){
            return false;
        }
        for(PlayerWharfInvoice invoice:list){
            if(invoice.isHelp())
                return true;
        }
        return false;
    }

    @Override

    public int arrive(PlayerWharf pw,Player p, FarmPlayer fp) throws AlertException {

        Date now = new Date();
        int goldCoin = 0;
        long restTime = 0l;
        long diff = now.getTime() - pw.getRefreshTime().getTime();
        //船没到
        if (!pw.isShipArrived()) {
            if (diff < WharfConstant.HOUR5) {
                goldCoin = (int)((WharfConstant.HOUR5 - diff)/180000l);//180秒一达人币
                goldCoin = goldCoin<5?5:goldCoin;
                if(p.getGoldCoin()<goldCoin){
                    exceptionFactory.throwAlertException("goldcoin.notenough");
                } else {

                    pw.setRefreshTime(now);
                    pw.setShipArrived(true);
                    generateInvoices( pw, fp);

                    //p.decGoldCoin(goldCoin);
                    playerRuleContainer.decGoldCoin(p,goldCoin, PlayerConstant.GOLD_ACTION_35);
                }
            }
        }
        return goldCoin;
    }

    @Override
    public int arriveGold(PlayerWharf pw) throws AlertException {
        Date now = new Date();
        int goldCoin = 0;
        long restTime = 0l;
        long diff = now.getTime() - pw.getRefreshTime().getTime();
        //船没到
        if (!pw.isShipArrived()) {
            if (diff < WharfConstant.HOUR5) {
                goldCoin = (int)((WharfConstant.HOUR5 - diff)/180000l);//180秒一达人币
                goldCoin = goldCoin<5?5:goldCoin;
            }
        }
        return goldCoin;
    }

    @Override
    public String getWeekKey() {
        Calendar ca = Calendar.getInstance();
        int week = ca.get(Calendar.WEEK_OF_YEAR);
        int year = ca.get(Calendar.YEAR);
        String weekKey = String.valueOf(year)+String.valueOf(week);
        return weekKey;
    }
}


