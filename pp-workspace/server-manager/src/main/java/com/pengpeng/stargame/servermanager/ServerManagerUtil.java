package com.pengpeng.stargame.servermanager;

import com.pengpeng.framework.utils.MD5Encoder;
import com.pengpeng.stargame.managed.NodeInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-18 下午3:25
 */
public class ServerManagerUtil {

    /**
     * 根据服务器节点信息生成服务器标识。
     * @param nodeinfo
     * @return
     */
    public static String buildInstanceId(NodeInfo nodeinfo) {
        String instanceId = nodeinfo.getId() + "-" + ((nodeinfo.getHost().replace(".","-"))) + "-" + nodeinfo.getPort();
        return instanceId;
    }

    /**
     * 计算两个时间之间的秒差，“结束时间”减去“开始时间”之间的分钟数.
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return int 分钟数。小于0：开始时间大于结束时间  大于0：开始时间小于结束时间
     */
    public static long evalSeconds(Date beginTime, Date endTime) {
        if (beginTime == null || endTime == null) {
            return 0;
        }
        long diff = endTime.getTime() - beginTime.getTime();
        return Math.abs(diff / 1000);
    }

    /**
     * 计算两个时间之间的分钟差.
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return int 分钟数。小于0：开始时间大于结束时间  大于0：开始时间小于结束时间
     */
    public static long evalMinutes(Date beginTime, Date endTime) {
        if (beginTime == null || endTime == null) {
            return 0;
        }
        long diff = endTime.getTime() - beginTime.getTime();
        return Math.abs(diff / 1000 / 60);
    }

    public static void main(String[] args) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = format.parse("2013-04-18 15:49:10");
            Date date2 = format.parse("2013-04-18 15:50:50");
            System.out.println(evalMinutes(date1,date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
