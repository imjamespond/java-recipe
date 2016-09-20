package com.chitu.chess.payment;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.utils.SpringUtils;

import com.chitu.chess.model.ChessBillTypes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.service.ChessPlayerManager;


public class PaymentHolder {
	
	/**卡充值URL**/
	public static final String CARD_BASE_URL = "http://sj.vnetone.com/getxdata.aspx";
	
	/**短信充值URL**/
	public static final String MESSAGE_BASE_URL = "http://ydzf.vnetone.com/Default_app.aspx";
	
	/**商户代码**/
	public static final String CARD_SPID = "50051";
	/**商户密钥**/
	public static final String CARD_SPPWD = "jn6ns230klpdf6hb4s";
	
	
	/**商户代码**/
	public static final String MESSAGE_SPID = "30175";
	/**商户密钥**/
	public static final String MESSAGE_SPPWD = "n7hd20kj3dbvsh6g8i";
	
	

	/**支付类型**/
	public enum PayType {
		/**短信0**/
		Message,
		/**充值卡1**/
		Card;
		
		public static PayType from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	/**支付状态**/
	public enum PayStatus {
		/**等待付款0**/
		Request,
		/**已付款1**/
		Payed,
		/**完成**/
		Complete;
		
		public static PayStatus from(int value) {
			if (value < 0 || value >= values().length)
				return null;
			return values()[value];
		}
	};
	
	/**
	 * 给玩家加点
	 * @param order
	 */
	public static void addRmb(PersistPayOrder order){
		ChessPlayer player = SpringUtils.getBeanOfType(ChessPlayerManager.class).getAnyPlayerById(order.getPlayerId());
		
		int billtype = ChessBillTypes.PAY_CARD_INC_RMB;
		if(order.getPayType() == PayType.Message.ordinal()){
			billtype = ChessBillTypes.PAY_MESSAGE_INC_RMB;
		}
		
		player.wealthHolder.increaseMoney(0, 0, order.getPayMoney(), BillType.get(billtype), String.valueOf(order.getId()));
	}
	
	
}
