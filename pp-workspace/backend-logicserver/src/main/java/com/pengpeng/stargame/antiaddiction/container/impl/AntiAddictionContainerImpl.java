package com.pengpeng.stargame.antiaddiction.container.impl;

import com.pengpeng.stargame.antiaddiction.container.IAntiAddictionContainer;
import com.pengpeng.stargame.antiaddiction.dao.IPlayerAntiAddictionDao;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.model.antiaddiction.PlayerAntiAddiction;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-11
 * Time: 下午5:15
 */

@Component
public class AntiAddictionContainerImpl implements IAntiAddictionContainer {
    @Autowired
    public IOtherPlayerDao otherPlayerDao;
    @Autowired
    public IPlayerAntiAddictionDao antiAddictionDao;
    @Autowired
    private IExceptionFactory exceptionFactory;

    private static Pattern p18 = Pattern.compile("\\d{17}[x,X]|\\d{18}");

    private static Pattern p15 = Pattern.compile("\\d{15}");

    private static Pattern pLeap = Pattern.compile("((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))");

    private static Pattern pNLeap = Pattern.compile("((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))");

    private static String PATTERN = "10X98765432";

    private static int[] AREA={11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91};

    private static long EIGHTEEN = 18l*365l*24l*60l*60l*1000l;

    private static long HOUR5 = 3600l*5l;
    private static long HOUR3 = 3600l*3l;

    public long check(String identity) throws AlertException{
        long birth = 0l;
         if(identity.length()==18){
             checkLength18(identity);
             birth = checkDate18(identity);
             checkLocation(identity);
             checkSum(identity);
         }else if(identity.length()==15){
             checkLength15(identity);
             birth = checkDate15(identity);
             checkLocation(identity);
         } else{
             exceptionFactory.throwAlertException("anti.addiction.wrong.format");
         }
         return birth;
     }

    private void checkLength18(String identity) throws AlertException{
     //验证长度
     //String identity = "610203198505022927".toUpperCase();
     identity = identity.toUpperCase();

     Matcher m = p18.matcher(identity);
     if(!m.matches()){
         exceptionFactory.throwAlertException("anti.addiction.wrong.format");
     }
    }

    private void checkLength15(String identity) throws AlertException{
        //验证长度
        identity = identity.toUpperCase();
        Matcher m = p15.matcher(identity);
        if(!m.matches()){
            exceptionFactory.throwAlertException("anti.addiction.wrong.format");
        }
    }

    private long checkDate18(String identity) throws AlertException{
        //验证年
        String year = identity.substring(6,10);
        String month = identity.substring(10,12);
        String day = identity.substring(12,14);
        Pattern p = null;
        int y = Integer.valueOf(year);
        int m = Integer.valueOf(month);
        int d = Integer.valueOf(day);
        if(y%4 == 0){
            p = pLeap;
        }else{
            p = pNLeap;
        }
        Matcher dm = p.matcher(identity.substring(10,14));
        if(!dm.matches()){
            exceptionFactory.throwAlertException("anti.addiction.wrong.date");
        }
        return getDate(y,m,d);
    }

    private long checkDate15(String identity) throws AlertException{
        //验证长度
        String year = identity.substring(6,8);
        String month = identity.substring(8,10);
        String day = identity.substring(10,12);
        Pattern p = null;
        int y = Integer.valueOf(year)+1900;
        int m = Integer.valueOf(month);
        int d = Integer.valueOf(day);
        if(y%4 == 0){
            p = pLeap;
        }else{
            p = pNLeap;
        }
        Matcher dm = p.matcher(identity.substring(8,12));
        if(!dm.matches()){
            exceptionFactory.throwAlertException("anti.addiction.wrong.date");
        }
        return getDate(y,m,d);
    }

    private Long getDate(int y,int m,int d){
        Calendar ca = Calendar.getInstance();
        ca.set(y,m,d);
        return ca.getTimeInMillis();
    }

    private void checkLocation(String identity) throws AlertException{
        //验证区域
        if(Arrays.binarySearch(AREA, Integer.valueOf(identity.substring(0, 2)))<0){
            exceptionFactory.throwAlertException("anti.addiction.wrong.area");
        }
    }

    private void checkSum(String identity) throws AlertException{
        //验证尾数
        int mod = ((identity.charAt(0) -'0')*7+
                (identity.charAt(1) -'0')*9+
                (identity.charAt(2) -'0')*10+
                (identity.charAt(3) -'0')*5+
                (identity.charAt(4) -'0')*8+
                (identity.charAt(5) -'0')*4+
                (identity.charAt(6) -'0')*2+
                (identity.charAt(7) -'0')*1+
                (identity.charAt(8) -'0')*6+
                (identity.charAt(9) -'0')*3+
                (identity.charAt(10) -'0')*7+
                (identity.charAt(11) -'0')*9+
                (identity.charAt(12) -'0')*10+
                (identity.charAt(13) -'0')*5+
                (identity.charAt(14) -'0')*8+
                (identity.charAt(15) -'0')*4+
                (identity.charAt(16) -'0')*2)%11;

        if(PATTERN.charAt(mod)!=identity.charAt(17)){
            exceptionFactory.throwAlertException("anti.addiction.wrong.checksum");
        }
    }

    public boolean isEighteen(long birth){
        if(birth == 0l)
            return false;
        long d = System.currentTimeMillis() - birth;
        return d >= EIGHTEEN;
    }

    public int decline(String pid,int val){
        PlayerAntiAddiction pa = antiAddictionDao.getPlayerAntiAddiction(pid);
        OtherPlayer op = otherPlayerDao.getBean(pid);
        if(isEighteen(pa.getBirth())){
            return val;
        }
        long ol =  op.getAccumulateOnlineTime(new Date());
        if(ol>HOUR5){
            return 0;
        }
        if(ol>HOUR3){
            return val>>1;
        }
        return val;
    }

}

