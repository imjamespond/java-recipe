package com.qianxun.service;

import java.util.List;

import org.copycat.framework.Page;
import org.copycat.framework.annotation.Table;
import org.copycat.framework.sql.Query;
import org.copycat.framework.sql.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.BaseExchangeProtos.BaseExchange;
import com.qianxun.model.BaseExchangeProtos.GemExchange;
import com.qianxun.model.PaymentPersist;
import com.qianxun.model.UserWealthPersist;

@Service
@Transactional
public class PaymentPersistService extends SqlService<PaymentPersist, Long> {
	private static final String TABLE = PaymentPersist.class.getAnnotation(Table.class).value();
	private static final String SQL_SEL = String.format("select * from %s ORDER BY id DESC LIMIT ? OFFSET ?",TABLE);
	private static final String SQL_SEL_UID = String.format("select * from %s WHERE uid=? ORDER BY id DESC LIMIT ? OFFSET ?",TABLE);
	private static final String SQL_SEL_UID_COUNT = " WHERE uid=?";
	private static final String SQL_SEL_ORDER = String.format("select * from %s where order_id=?",TABLE);
	
	public static final int FINISH = 1;
	public static final int FAIL = 2;
	
	@Autowired
	private UserWealthService wealthService;
	@Autowired
	private ExchangeToFlyingService exchangeToFlyingService;
	@Autowired
	private ExchangeToWebService exchangeToWebService;
	
	public PaymentPersist getByOrder(String orderId) {
		
		Object[] objs = {orderId};
		List<PaymentPersist> list = this.sqlTemplate.queryForList(SQL_SEL_ORDER, PaymentPersist.class, objs);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}	
	
	public List<PaymentPersist> listPay(Page page) {
		Object[] objs = { page.getLimit(), page.getOffset() };
		Query<PaymentPersist> query = this.query();
		query.count();
		int total = query.toInt(new Object[]{});
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_SEL, PaymentPersist.class, objs);
	}
	
	public PaymentPersist payForGems(String orderId, String discription){
		PaymentPersist pp = getByOrder(orderId);

		if(null != pp){
			//some logs
			BaseExchange.Builder bbuilder = BaseExchange.newBuilder();
			bbuilder.setType(ExchangeToWebService.CMD_GEM_EXCHANGE);
			GemExchange.Builder gbuilder = GemExchange.newBuilder();
			gbuilder.setCount((int) pp.getPayment());
			gbuilder.setTrend("");
			gbuilder.setUid(pp.getUid());			
			
			//订单是否已经执行
			if(pp.getState() == PaymentPersistService.FINISH){
				gbuilder.setDiscription(String.format("订单已执行,%s", pp.getOrderId()));
			}else{
				if(wealthService.addGems(pp.getUid(), ((int) pp.getPayment())*10)>0){
					pp.setState(PaymentPersistService.FINISH);
					pp.setFinish_time(System.currentTimeMillis());
					UserWealthPersist fp = wealthService.get(pp.getUid());
					gbuilder.setDiscription(String.format("充值记录,%s gems:%d", pp.getOrderId(), fp.getGems()));
				}else{
					pp.setState(PaymentPersistService.FAIL);
					gbuilder.setDiscription(String.format("充值失败,%s", pp.getOrderId()));
				}			
			}
			
			bbuilder.setExtension(GemExchange.gemExchange, gbuilder.build());
			BaseExchange be = bbuilder.build();		
			exchangeToWebService.put(be);
			exchangeToFlyingService.put(be);
		}
	
		if(null == pp){
			pp = new PaymentPersist();
			pp.setState(PaymentPersistService.FAIL);
			pp.setOrderId(orderId);
			pp.setDescription(discription);
			save(pp);
		}else{			
			pp.setDescription(discription);
			update(pp);					
		}
		
		return pp;
	}

	public List<PaymentPersist> listByUid(long uid, Page page) {
		Object[] objs = {uid, page.getLimit(), page.getOffset() };
		Query<PaymentPersist> query = this.query();
		query.count().set(SQL_SEL_UID_COUNT);
		int total = query.toInt(uid);
		page.setTotal(total);
		return this.sqlTemplate.queryForList(SQL_SEL_UID, PaymentPersist.class, objs);
	}
	
}