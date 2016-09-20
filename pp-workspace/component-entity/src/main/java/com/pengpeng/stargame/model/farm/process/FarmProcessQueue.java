package com.pengpeng.stargame.model.farm.process;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.Uid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 下午1:55
 */
public class FarmProcessQueue extends BaseEntity<String> {
    /**
     * 玩家id
     */
    private String pid;
    /**
     * 拥有的数量   这里记录玩家 花费达人币开启了几个，预留超级粉丝功能
     */
    private int num;
    /**
     * 队列信息
     */
    private List<OneQueue> oneQueueList;
    /**
     * 总队列的开始时间
     */
    private Date startTime;
    /**
     * 总队列的结束时间
     */
    private Date endTime;
    /**
     * 加速结束 时间
     */
    private Date speedTime;
    /**
     * 加速开始时间
     */
    private Date speedStartTime;


    public FarmProcessQueue() {
    }

    public FarmProcessQueue(String pid, int num) {
        this.pid = pid;
        this.num = num;
        this.oneQueueList = new ArrayList<OneQueue>();
    }



    /**
     * 每次获取的时候 调用  时间的计算
     */
    public boolean calculate() {
        Date now = new Date();

        if (startTime == null) {
            return false;
        }

        boolean ischange = false;
        if (hasProceed()) { //如果有正在进行的队列
//            if (endTime.before(now)) {//所有队列已经完成
            Date endTime=getAfterSpeedEndTime();
            if (endTime.before(now)) {//所有队列已经完成
                for (OneQueue oneQueue : oneQueueList) {
                    oneQueue.setStatus(3);
                }
                ischange = true;
                return ischange;
            }
            long secods = (now.getTime() - startTime.getTime()) / 1000;//已经进行了多长时间
            /**
             * 长时间未登录，登录之后 如果有正在进行的队列，那么要计算
             */
            for (int i = 0; i < oneQueueList.size(); i++) {
                OneQueue oneQueue = oneQueueList.get(i);
                if (oneQueue.getStatus() == 3) {
                    continue;
                }
                /**
                 * 计算的时候用加速的时间 计算
                 */
                if (oneQueue.getStatus() == 2) { //正在进行的队列
                    if (secods >= oneQueue.getSpeedTime()) { //进行的队列已经完成了，继续下个队列的计算
                        oneQueue.setStatus(3);
                        secods -= oneQueue.getSpeedTime();
                        ischange = true;
                        continue;
                    } else {
                        /**
                         * 设置修正值
                         */
                        if(speedTime!=null){
                            //如果   startTime<speedTime<now
                            if(speedTime.after(startTime)&&speedTime.before(now)){
                                int already=0;
                                if(speedStartTime.after(startTime)){
                                    already = (int) ((speedTime.getTime() - speedStartTime.getTime()) / 1000);
                                }   else {
                                    already = (int) ((speedTime.getTime() - startTime.getTime()) / 1000);
                                }
                                oneQueue.setAlreadyTime(oneQueue.getAlreadyTime()+already);
                                speedTime=null;//设置为Null 防止下次 重复设置
                                ischange = true;
                            }
                        }
                        break;
                    }
                }
                if (oneQueue.getStatus() == 1) {
                    if (secods >= oneQueue.getSpeedTime()) {
                        oneQueue.setStatus(3);
                        secods -= oneQueue.getSpeedTime();
                        ischange = true;
                        continue;
                    } else {
                        oneQueue.setStatus(2);//设为正在进行
                        int qs = (int) secods;
                        startTime = DateUtil.addSecond(now, -qs);//开始时间设置成已经进行了多久时间
                        ischange = true;
                        /**
                         * 设置修正值
                         */
                        if(speedTime!=null){
                            //如果   startTime<speedTime<now
                            if(speedTime.after(startTime)&&speedTime.before(now)){
                                int already=0;
                                if(speedStartTime.after(startTime)){
                                    already = (int) ((speedTime.getTime() - speedStartTime.getTime()) / 1000);
                                }   else {
                                    already = (int) ((speedTime.getTime() - startTime.getTime()) / 1000);
                                }
                                oneQueue.setAlreadyTime(already);
                                speedTime=null;//设置为Null 防止下次 重复设置
                                ischange = true;
                            }
                        }
                        break;
                    }
                }
            }

        }

        return ischange;

    }

    /**
     * 获取已经加速了多少秒
     * @return
     */
    public int alreadySpeedTime(){
        Date now=new Date();
        int already=0;
        if (hasSpeedTime()){
            if(speedStartTime.after(startTime)){
                 already = (int) ((now.getTime() - speedStartTime.getTime()) / 1000);
            }   else {
                 already = (int) ((now.getTime() - startTime.getTime()) / 1000);
            }
        }
        return already;
    }

    /**
     * 返回可以领取的队列
     * @return
     */
    public List<OneQueue> getFinish(){

        List<OneQueue> oneQueues=new ArrayList<OneQueue>();
        for(OneQueue oneQueue:oneQueueList){
            if(oneQueue.getStatus()==3){
                oneQueues.add(oneQueue);
            }
        }
       return oneQueues;
    }

    /**
     * 返回队列中是否 有正在进行的队列
     *
     * @return
     */
    public boolean hasProceed() {

        for (OneQueue oneQueue : oneQueueList) {
            if (oneQueue.getStatus() == 2) {
                return true;
            }
        }
        return false;

    }

    /**
     * 获取队列的结束时间 没有buffer 效果的
     * @return
     */
    public Date getQueueEndTime(){
        Date rDate=this.getStartTime();
        if(hasProceed()){
            for(OneQueue oneQueue:oneQueueList){
                if(oneQueue.getStatus()==3){ //队列已经完成的 不算时间
                    continue;
                }
                rDate=DateUtil.addSecond(rDate, oneQueue.getTime());
            }
            return rDate;
        }
        return new Date();
    }

    /**
     * 获取 实际的 队列结束时间  (有Buffer效果的)
     * @return
     */
    public Date getAfterSpeedEndTime(){
        Date rDate=this.getStartTime();
       if(hasProceed()){
           for(OneQueue oneQueue:oneQueueList){
               if(oneQueue.getStatus()==3){ //队列已经完成的 不算时间
                   continue;
               }
               rDate=DateUtil.addSecond(rDate, oneQueue.getSpeedTime());
           }
           return rDate;
       }
        return new Date();

    }

    /**
     * 玩家使用加速Buffer 有队列的时候 调用
     */
    public void speedAll(){
        Date endTime=this.getStartTime();
        Date nextStartTime=this.getStartTime();
        Date now=new Date();
        for(OneQueue oneQueue:oneQueueList){
            if(oneQueue.getStatus()==3){
                continue;
            }
            endTime=DateUtil.addSecond(endTime,oneQueue.getTime());//当前队列的 结束时间
            if(speedTime.before(nextStartTime)){ //如果 加速结束时间 在 当前队列的开始时间之前，那么以后的队列 不用加速
                break;
            }
            if(speedTime.after(nextStartTime)&&speedTime.before(endTime)){//如果加速时间 介于 当前队列的开始时间 和 结束时间之间。
               int sTime=0;
                if(oneQueue.getStatus()==2){
                    int a= (int) ((now.getTime()-nextStartTime.getTime())/1000);// a 表示正在进行的队列 进行了 多长时间
                    sTime= (int) ((speedTime.getTime()-now.getTime())/1000);// 这个队列可以加速多久
                    int b= (int) ((endTime.getTime()-speedTime.getTime())/1000);// a 表示这个队列 从加速结束到 队列结束 还剩多久
                    oneQueue.setSpeedTime(a+sTime/2+b);
                }
                if(oneQueue.getStatus()==1){//等待的队列
                    int a= (int) ((endTime.getTime()-speedTime.getTime())/1000);// a 表示这个队列 从加速结束到 队列结束 还剩多久
                    sTime= (int) ((speedTime.getTime()-nextStartTime.getTime())/1000);// 这个队列可以加速多久
                    oneQueue.setSpeedTime(a+sTime/2);
                }

            }
            nextStartTime=endTime;//设置下个队列的 开始时间
            //如果到达这个单个队列的结束时间，在 队列Buffer结束直接之前，那么这个队列需要加速
            if(this.getSpeedTime().after(endTime)){

                if(oneQueue.getStatus()==2){
                    int a= (int) ((now.getTime()-startTime.getTime())/1000);// a 表示正在进行的队列 进行了 多长时间
                    int sTime= (int) ((endTime.getTime()-now.getTime())/1000);// 这个队列可以加速多久
                    oneQueue.setSpeedTime(a+sTime/2);
                    int b=oneQueue.getTime()-a;  // b 表示正在进行的队列 剩余多长时间
                }else{
                    oneQueue.setSpeedTime(oneQueue.getTime()/2);
                }


            }
        }
    }

    /**
     * 玩家使用加速Buffer 有队列的时候 调用
     */
    public void speedAll1(){
        Date now =new Date();
        int allSpeedSecods= (int) ((speedTime.getTime()-now.getTime())/1000);// a 表示从现在开始到 加速队列结束 一共可以加速多少秒
        for(OneQueue oneQueue:oneQueueList){
            if(allSpeedSecods<=0){
                break;
            }
            if(oneQueue.getStatus()==3){
                continue;
            }
            if(oneQueue.getStatus()==2){
                int a= (int) ((now.getTime()-startTime.getTime())/1000);// a 表示正在进行的队列 进行了 多长时间
                int b= oneQueue.getTime()-a;//正在进行的队列 还剩下多少时间
                int c=b/2;//c 表示 次队列 还可以加速的最大时间。每个队列最多加速2倍
                if(allSpeedSecods>c){
                    oneQueue.setSpeedTime(oneQueue.getTime()-c);
                    allSpeedSecods-=c;
                }else {
                    oneQueue.setSpeedTime(oneQueue.getTime()-allSpeedSecods);
                    allSpeedSecods=0;
                }
            }
            if(oneQueue.getStatus()==1){
                int c=oneQueue.getTime()/2;//c 表示 次队列 还可以加速的最大时间。每个队列最多加速2倍
                if(allSpeedSecods>c){
                    oneQueue.setSpeedTime(oneQueue.getTime()-c);
                    allSpeedSecods-=c;
                } else {
                    oneQueue.setSpeedTime(oneQueue.getTime()-allSpeedSecods);
                    allSpeedSecods=0;
                }
            }

        }
    }
    public void speedAllEnd(){
        for(OneQueue oneQueue:oneQueueList){
            if(oneQueue.getStatus()==3){
                continue;
            }
//            if(oneQueue.getStatus()==2){
//                oneQueue.setAlreadyTime( oneQueue.getAlreadyTime()+(oneQueue.getTime()-oneQueue.getSpeedTime()));
//            }
            oneQueue.setTime(oneQueue.getSpeedTime());
        }
    }

    /**
     * 添加一个队列
     *
     * @param processId
     * @param time
     */
    public void addOneQueue(String processId, int time) {
        Date now = new Date();
        OneQueue oneQueue = new OneQueue();
        oneQueue.setId(Uid.uuid());
        oneQueue.setProcessid(processId);
        oneQueue.setTime(time);
        oneQueue.setSpeedTime(time);
        Date afterSendDate=getAfterSpeedEndTime();//当前队列的实际结束时间
        if (hasProceed()) {
            oneQueue.setStatus(1);
            endTime = DateUtil.addSecond(endTime, oneQueue.getTime());
            if(speedTime!=null&&speedTime.after(afterSendDate)){//此队列 享受 加速
                int sTime= (int) ((speedTime.getTime()-afterSendDate.getTime())/1000);// 表示此队列可以加速多久
                int c=oneQueue.getSpeedTime()/2;//c 表示 次队列 还可以加速的最大时间。每个队列最多加速2倍
                if(sTime>c){
                    oneQueue.setSpeedTime(oneQueue.getTime()-c);
                } else {
                    oneQueue.setSpeedTime(oneQueue.getTime()-sTime);
                }
            }
        } else {
            oneQueue.setStatus(2);
            startTime = now;
            endTime = DateUtil.addSecond(now, oneQueue.getTime());

            if(speedTime!=null&&speedTime.after(now)){//此队列 享受 加速
                int sTime= (int) ((speedTime.getTime()-now.getTime())/1000);// 表示此队列可以加速多久
                int c=oneQueue.getSpeedTime()/2;//c 表示 次队列 还可以加速的最大时间。每个队列最多加速2倍
                if(sTime>c){
                    oneQueue.setSpeedTime(oneQueue.getTime()-c);
                } else {
                    oneQueue.setSpeedTime(oneQueue.getTime()-sTime);
                }
            }
        }
        oneQueueList.add(oneQueue);
    }

    public int getNumQueue(){
        return oneQueueList.size();
    }

    public OneQueue getOneQueue(String id) {
        for (OneQueue oneQueue1 : oneQueueList) {
            if (id.equals(oneQueue1.getId())) {
                return oneQueue1;
            }
        }
        return null;
    }

    public void finish(){
        for(OneQueue oneQueue:oneQueueList){
            if(oneQueue.getStatus()==2||oneQueue.getStatus()==1){
                oneQueue.setStatus(3);
            }

        }
    }
    /**
     * 立即完成一个队列
     */
    public void  finishOne(String id) {
        OneQueue oneQueue = getOneQueue(id);

        int status=oneQueue.getStatus();
        oneQueue.setStatus(3);//改变队列的状态 设置成完成

        /**
         * 正在等待的队列
         */
        if (status == 1) {
            //总队列的完成时间 减去 这个队列的时间
            endTime = DateUtil.addSecond(endTime, -oneQueue.getTime());
        }
        /**
         * 正在进行的队列   ,把等待的队列设置为 开始进行，设置 总队列的时间
         */
        if (status == 2) {
            Date now = new Date();
            //从新计算时间
            int allTime = 0;
            for (int i = 0; i < oneQueueList.size(); i++) {
                OneQueue temp = oneQueueList.get(i);
                if (temp.getStatus() == 1) {
                    allTime += temp.getTime();
                }
            }
            //把第一个 等待的 队列 状态 改为 正在进行
            for (int i = 0; i < oneQueueList.size(); i++) {
                OneQueue temp = oneQueueList.get(i);
                if (temp.getStatus() == 1) {
                    temp.setStatus(2);
                    startTime=now;
                    endTime=DateUtil.addSecond(now,allTime);
                    break;
                }
            }

        }

    }
    /**
     * 取消队列
     *
     * @param id
     */
    public void cancelQueue(String id) {
        OneQueue oneQueue = getOneQueue(id);
        /**
         * 正在等待的队列
         */
        if (oneQueue.getStatus() == 1) {
            endTime = DateUtil.addSecond(endTime, -oneQueue.getTime());
            oneQueueList.remove(oneQueue);
        }
        /**
         * 正在进行的队列   ,把等待的队列设置为 开始进行，设置 总队列的时间
         */
        if (oneQueue.getStatus() == 2) {
            Date now = new Date();
            oneQueueList.remove(oneQueue);
            //从新计算时间
            int allTime = 0;
            for (int i = 0; i < oneQueueList.size(); i++) {
                OneQueue temp = oneQueueList.get(i);
                if (temp.getStatus() == 1) {
                    allTime += temp.getTime();
                }
            }
            //把第一个 等待的 队列 状态 改为 正在进行
            for (int i = 0; i < oneQueueList.size(); i++) {
                OneQueue temp = oneQueueList.get(i);
                if (temp.getStatus() == 1) {
                    temp.setStatus(2);
                    startTime=now;
                    endTime=DateUtil.addSecond(now,allTime);
                    break;
                }
            }

        }

    }

    public void deleteFinish(List<OneQueue> finishList){
        oneQueueList.removeAll(finishList);
    }

    public boolean hasSpeedTime(){
        if(speedTime==null){
            return false;
        }
        if(speedTime.before(new Date())){
            return false;
        }
        return true;
    }
    public int getAllsurplusesTime(){
        Date now=new Date();
        int apt=0;
        if(hasSpeedTime()){
            apt= (int) ((getQueueEndTime().getTime()-now.getTime())/1000);
            apt-=alreadySpeedTime();
        }else{
            apt= (int) ((getAfterSpeedEndTime().getTime()-now.getTime())/1000);
        }
        return apt;

    }


    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<OneQueue> getOneQueueList() {
        return oneQueueList;
    }

    public void setOneQueueList(List<OneQueue> oneQueueList) {
        this.oneQueueList = oneQueueList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSpeedTime() {
        return speedTime;
    }

    public void setSpeedTime(Date speedTime) {
        this.speedTime = speedTime;
    }

    public Date getSpeedStartTime() {
        return speedStartTime;
    }

    public void setSpeedStartTime(Date speedStartTime) {
        this.speedStartTime = speedStartTime;
    }
}
