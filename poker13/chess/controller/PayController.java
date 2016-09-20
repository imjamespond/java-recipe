package com.chitu.chess.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.IdUtils;
import cn.gecko.commons.utils.WgetUtils;

import com.chitu.chess.model.ChessErrorCodes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.payment.PaymentHolder;
import com.chitu.chess.payment.PaymentHolder.PayStatus;
import com.chitu.chess.payment.PaymentHolder.PayType;
import com.chitu.chess.payment.PersistPayOrder;
import com.chitu.chess.service.ChessPlayerManager;

@Controller
public class PayController extends MultiGeneralController {
    
	@Autowired
	private ChessPlayerManager chessPlayerManager;
	
	/**
	 * 申请神州行充值卡充值
	 * @param mz 金额
	 * @param xlh 神州行充值卡序列号
	 * @param mm 神州行充值卡刮开密码
	 */
	public void requestCard(int mz,String xlh,String mm){
		if(!( mz == 50 || mz == 100)){
			throw new GeneralException(ChessErrorCodes.PAY_MZ_ERROR);
		}
		if(StringUtils.isBlank(xlh)){
			throw new GeneralException(ChessErrorCodes.CARD_XLH_ERROR);
		}
		if(StringUtils.isBlank(mm)){
			throw new GeneralException(ChessErrorCodes.CARD_MM_ERROR);
		}
		
		long sporderid = IdUtils.generateLongId();
		String md5key = sporderid + PaymentHolder.CARD_SPID + PaymentHolder.CARD_SPPWD + mz + xlh + mm;
		String md5x = StringUtils.upperCase(DigestUtils.md5Hex(md5key));
		
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		StringBuilder param = new StringBuilder(100);
		param.append("?").append("mz").append("=").append(mz);
		param.append("&").append("ip").append("=").append(player.ip);
		param.append("&").append("md5x").append("=").append(md5x);
		param.append("&").append("sporderid").append("=").append(sporderid);
		param.append("&").append("spid").append("=").append(PaymentHolder.CARD_SPID);
		param.append("&").append("xlh").append("=").append(xlh);
		param.append("&").append("mm").append("=").append(mm);
		param.append("&").append("uid").append("=").append(player.id);
		
		String url = PaymentHolder.CARD_BASE_URL + param.toString();
		String content = null;
		try {
			content = WgetUtils.fetchContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(content == null){
			throw new GeneralException(ChessErrorCodes.VNETONE_ERROR_CODE_50007);
		}
		if(!content.equals("00000")){
			throw new GeneralException(Integer.valueOf(content));
		}
		
		PersistPayOrder order = new PersistPayOrder();
		order.setId(sporderid);
		order.setPlayerId(player.id);
		order.setPlayerName(player.nickname);
		order.setPayTime(System.currentTimeMillis());
		order.setPayMoney(mz);
		order.setPayType(PayType.Card.ordinal());
		order.setPayStatus(PayStatus.Payed.ordinal());
		order.setXlh(xlh);
		order.setMm(mm);
		order.save();
	}
	
	/**
	 * 申请短信充值
	 * @param mz 金额
	 * @param payMob 手机号码
	 */
	public void requestMessage(int mz,String payMob){
		if(mz < 5 || mz > 50){
			throw new GeneralException(ChessErrorCodes.PAY_MZ_ERROR);
		}
		if(StringUtils.isBlank(payMob)){
			throw new GeneralException(ChessErrorCodes.PAY_MOD_ERROR);
		}
		
		long sporderid = IdUtils.generateLongId();
		String spzdy = String.valueOf(sporderid);
		String spreq = "http://www.chitu.com:8088/";
		String spsuc = "http://www.chitu.com:8088/";
		String md5key = PaymentHolder.MESSAGE_SPID + sporderid + PaymentHolder.MESSAGE_SPPWD + mz + spreq + spsuc + payMob;
		String md5x = StringUtils.upperCase(DigestUtils.md5Hex(md5key));
		
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		StringBuilder param = new StringBuilder(100);
		param.append("?").append("sp").append("=").append(PaymentHolder.MESSAGE_SPID);
		param.append("&").append("od").append("=").append(sporderid);
		param.append("&").append("mz").append("=").append(mz);
		param.append("&").append("md5").append("=").append(md5x);
		param.append("&").append("spreq").append("=").append(spreq);
		param.append("&").append("spsuc").append("=").append(spsuc);
		param.append("&").append("spzdy").append("=").append(spzdy);
		param.append("&").append("mob").append("=").append(payMob);
		param.append("&").append("uid").append("=").append(player.id);
		
		String url = PaymentHolder.MESSAGE_BASE_URL + param.toString();
		String content = null;
		try {
			content = WgetUtils.fetchContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(content == null){
			throw new GeneralException(ChessErrorCodes.VNETONE_ERROR_CODE_50007);
		}
		String[] result = content.split("|");
		if(result[0].equalsIgnoreCase("yhxffail")){
			throw new GeneralException(ChessErrorCodes.VNETONE_ERROR_CODE_50014);
		}
		
		PersistPayOrder order = new PersistPayOrder();
		order.setId(sporderid);
		order.setPlayerId(player.id);
		order.setPlayerName(player.nickname);
		order.setPayTime(System.currentTimeMillis());
		order.setPayMoney(mz);
		order.setPayType(PayType.Message.ordinal());
		order.setPayStatus(PayStatus.Request.ordinal());
		order.setPayMob(payMob);
		order.save();
	}
	
	
}
