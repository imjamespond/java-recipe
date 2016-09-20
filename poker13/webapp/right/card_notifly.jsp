<%@page import="com.chitu.chess.payment.PaymentHolder.PayStatus"%>
<%@page import="com.chitu.chess.payment.PersistPayOrder"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.chitu.chess.payment.PaymentHolder"%>
<%@page import="org.apache.commons.codec.digest.DigestUtils"%>

 <%
 	String spid = request.getParameter("spid");
   String md5 = request.getParameter("md5");
   String sid = request.getParameter("sid");
   String sporderid = request.getParameter("sporderid");
   String money = request.getParameter("money");
   String ntime = request.getParameter("ntime");
   String flag = request.getParameter("flag");
   String uid = request.getParameter("uid");
   
   
   String spmd5 = StringUtils.upperCase(DigestUtils.md5Hex(PaymentHolder.CARD_SPID + PaymentHolder.CARD_SPPWD + sid + sporderid + money + flag));
   if(!spmd5.equals(md5)){
  	 out.print(0);
  	 return;
   }
   
   PersistPayOrder order = PersistPayOrder.get(Long.valueOf(sporderid));
   if(order.getPayStatus() == PayStatus.Complete.ordinal()){
  	 out.print(1);
  	 return;
   }
   
   order.setSid(sid);
   order.setPayStatus(PayStatus.Complete.ordinal());
   order.setNotiflyTime(System.currentTimeMillis());
   order.update();
   
   PaymentHolder.addRmb(order);
   out.print(1);
 %>
