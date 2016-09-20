<%@page import="com.chitu.chess.payment.PaymentHolder.PayStatus"%>
<%@page import="com.chitu.chess.payment.PersistPayOrder"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.chitu.chess.payment.PaymentHolder"%>
<%@page import="org.apache.commons.codec.digest.DigestUtils"%>
<%
 	String spid = request.getParameter("spid");
   String md5 = request.getParameter("md5");
   String sid = request.getParameter("oid");
   String sporderid = request.getParameter("sporder");
   String money = request.getParameter("mz");
   String zdy = request.getParameter("zdy");
   String uid = request.getParameter("spuid");
   
   
   String spmd5 = StringUtils.upperCase(DigestUtils.md5Hex(sid + sporderid + PaymentHolder.MESSAGE_SPID + money + PaymentHolder.MESSAGE_SPPWD));
   if(!spmd5.equals(md5)){
  	 out.print("failydzf");
  	 return;
   }
   
   PersistPayOrder order = PersistPayOrder.get(Long.valueOf(sporderid));
   if(order.getPayStatus() == PayStatus.Complete.ordinal()){
  	 out.print("okydzf");
  	 return;
   }
   
   order.setSid(sid);
   order.setPayStatus(PayStatus.Complete.ordinal());
   order.setNotiflyTime(System.currentTimeMillis());
   order.update();
   
   PaymentHolder.addRmb(order);
   out.print("okydzf");
%>
