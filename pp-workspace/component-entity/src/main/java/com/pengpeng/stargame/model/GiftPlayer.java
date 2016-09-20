package com.pengpeng.stargame.model;

import com.pengpeng.stargame.util.DateUtil;

import java.util.*;

/**
 * 好友礼物模块
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午5:15
 */
public class GiftPlayer extends BaseEntity<String> {
    private String pid;
    //key=好友pid,value=礼物物品id
    private Map<String,Gift> items;
    //记录每日赠送过的好友
    private Set<String> dayGifts;
    private List<Gift> giftList;

    /**
     * 每日赠送次数,到0点自动清零
     */
    private int giveCount;
    private Date giveDate;

    public GiftPlayer(){
        dayGifts = new HashSet<String>();
        items = new HashMap<String, Gift>();
        giveDate = new Date();
        giftList = new ArrayList<Gift>();
    }

    public GiftPlayer(String pid){
        dayGifts = new HashSet<String>();
        items = new HashMap<String, Gift>();
        this.pid = pid;
        giveDate = new Date();
        giftList = new ArrayList<Gift>();
    }


    public void incGive(){
        giveCount++;
    }
    public int getGiveCount(){
        return giveCount;
    }

    /**
     * 清理无效的礼物数据
     * @param date
     */
    public void calc(Date date){
        if (items==null||items.isEmpty()){
            return ;
        }
        List<Gift> list = new ArrayList<Gift>();
        for(Gift gift:items.values()){
            if(gift.getValidityDate().getTime()>date.getTime()){
                list.add(gift);
            }
        }
        items.clear();
        for(Gift gift:list){
            items.put(gift.getFid(),gift);
        }
    }
    /**
     * 如果是新的一天
     * @return
     */
    public boolean isNewDay(Date date){
        return DateUtil.getDayOfYear(giveDate)!=DateUtil.getDayOfYear(date);
    }

    public void clean(){
        this.giveCount = 0;
        dayGifts.clear();
        giveDate = new Date();
    }

    public void addGift(Gift gift){
        items.put(gift.getFid(),gift);
        giveDate = new Date();
    }

    public void addDayGift(String fid){
        dayGifts.add(fid);
    }
    public boolean hasGift(String fid){
        return items.containsKey(fid)||dayGifts.contains(fid);
    }

    public void removeGift(String fid){
        items.remove(fid);
    }

    public Gift getGift(String fid){
        return items.get(fid);
    }

    public int size(){
        return items.size();
    }

    public Gift getFromGiftList(int i){
        return giftList.get(i);
    }
    public void add2GiftList(Gift gift){
        giftList.add(gift);
    }
    public void set2GiftList(int i,Gift gift){
        giftList.set(i,gift);
    }
    public void removeFromGiftList(int i){
        giftList.remove(i);
    }
    public void cleanGift(){
        Iterator<Gift> it = giftList.iterator();
        while (it.hasNext()){
            Gift gift = it.next();
            if(gift == null){
                it.remove();
            }
        }
    }

    public int getGiftListSize() {
        return giftList.size();
    }



    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        this.pid = id;
    }

    @Override
    public String getKey() {
        return pid;
    }

    public List<Gift> getAll(){
        List<Gift> list = new ArrayList<Gift>(items.values());
        return list;
    }

	/**
	 * 未处理的礼物总数
	 * @return
	 */
	public int getUntreatedGift(){
		int size = this.size();
		if(size > 0){
			return size;
		}
		return 0;
	}

}
