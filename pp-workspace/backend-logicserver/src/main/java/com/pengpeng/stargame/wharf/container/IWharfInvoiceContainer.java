package com.pengpeng.stargame.wharf.container;

import com.pengpeng.stargame.container.IMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.wharf.PlayerWharf;
import com.pengpeng.stargame.model.wharf.WharfInfo;
import com.pengpeng.stargame.model.wharf.WharfInvoiceInfo;
import com.pengpeng.stargame.wharf.rule.WharfInvoiceRule;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:03
 */
public interface IWharfInvoiceContainer extends IMapContainer<String,WharfInvoiceRule> {

    /**
     * 检测货单
     * @param fp
     * @return
     */
    public WharfInvoiceInfo[] checkInvoice(FarmPackage fp, PlayerWharf pw) throws AlertException;
    /**
     * 检测开启码头条件
     * @param fp
     * @return
     */
    public void checkEnable(FarmPlayer fp, Player p) throws AlertException;
    /**
     * 检测货船是否到
     * @param fp
     * @return
     */
    public WharfInfo checkShip(PlayerWharf pw, FarmPlayer fp) throws AlertException;
    /**
     * 请求帮助
     * @param pw
     * @return
     */
    public void needHelp(PlayerWharf pw, int order) throws AlertException;
    /**
     * 提交货单
     * @param pw
     * @return
     */
    public WharfInvoiceInfo submit(FarmPackage fp,PlayerWharf pw, int order) throws AlertException;
    /**
     * 启航
     *
     * @param pw
     * @return
     */
    public void sail(PlayerWharf pw, WharfInvoiceInfo[] wharfInvoiceInfo) throws AlertException;
    /**
     * 周key
     * @return
     */
    public String getWeekKey();
    /**
     * 检测是否需要协作
     * @return
     */
    public boolean getHelpState(PlayerWharf pw);

    /**
     * 货船马上到来
     * @return
     */

    public int arriveGold(PlayerWharf pw) throws AlertException;

    public int arrive(PlayerWharf pw, Player p, FarmPlayer fp) throws AlertException;

}
