package com.pengpeng.stargame.model.farm.decorate;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.*;

/**
 * User: mql
 * Date: 14-3-13
 * Time: 下午3:58
 * 农场装饰信息 包括小动物
 */
public class FarmDecorate extends BaseEntity<String> {
    private String pid; //玩家Id
    private Map<String, OneDecorate> items; //装饰信息
    private Map<String, Date> refreshTime; //每个动物 刷新时间 ，出现的时间 ，Key为 id ,Date 为上次刷新的时候
    private Map<String, Integer> animalEffect;//动物 效果
    private int buyFieldNum; //购买田地的次数
    private long idCounter; //添加装饰品的时候 里面唯一Id 生成

    public FarmDecorate() {

    }

    public long getIdCounter() {
        return idCounter;
    }

    public OneDecorate getOneDecorate(String id){
        OneDecorate oneDecorate=this.getItems().get(id);
        if(oneDecorate!=null){
            oneDecorate.setId(id);
        }
        return oneDecorate;
    }
    public void setIdCounter(long idCounter) {
        this.idCounter = idCounter;
    }

    public FarmDecorate(String pid) {
        this.pid = pid;
        items = new HashMap<String, OneDecorate>();
        animalEffect = new HashMap<String, Integer>();
        refreshTime = new HashMap<String, Date>();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Map<String, OneDecorate> getItems() {
        return items;
    }

    public void setItems(Map<String, OneDecorate> items) {
        this.items = items;
    }

    public Map<String, Date> getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Map<String, Date> refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getBuyFieldNum() {
        return buyFieldNum;
    }

    public void setBuyFieldNum(int buyFieldNum) {
        this.buyFieldNum = buyFieldNum;
    }

    public Map<String, Integer> getAnimalEffect() {
        return animalEffect;
    }

    public void setAnimalEffect(Map<String, Integer> animalEffect) {
        this.animalEffect = animalEffect;
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

    /**
     * 添加一个装饰
     *
     * @param position
     */
    public void add(OneDecorate position) {
        //Id 用累加
        String id= String.valueOf(idCounter++);
        items.put(id, position);
    }

    public List<String> getDecorateIds() {
        List<String> list = new ArrayList<String>();
        for (OneDecorate decoratePosition : this.getDecoratePositionList()) {
            list.add(decoratePosition.getiId());

        }
        return list;
    }

    /**
     * 获取 装饰列表
     *
     * @return
     */
    public List<OneDecorate> getDecoratePositionList() {
        List<OneDecorate> oneDecorateList = new ArrayList<OneDecorate>();
        for (Map.Entry<String, OneDecorate> entry : this.items.entrySet()) {
            OneDecorate oneDecorate = entry.getValue();//这里把Id 设置进去，保存的时候 不用保存，节省容量
            oneDecorate.setId(entry.getKey());
            oneDecorateList.add(oneDecorate);
        }
        return oneDecorateList;
    }

    /**
     * 更改
     *
     * @param id
     * @param decoratePosition
     */
    public void updateDecorate(String id, OneDecorate decoratePosition) {
        items.put(id, decoratePosition);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void remove(String id) {
        items.remove(id);
    }

    /**
     * 获取 农场内 某个物品的数量
     *
     * @param itemId
     * @return
     */
    public int getNumByItemId(String itemId) {
        int num = 0;
        for (Map.Entry<String, OneDecorate> entry : this.items.entrySet()) {
            if (entry.getValue().getiId().equals(itemId)) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取 某类型物品的数量
     *
     * @param itemId
     * @return
     */
    public List<OneDecorate> getList(String itemId) {
        List<OneDecorate> oneDecorateList = new ArrayList<OneDecorate>();
        for (Map.Entry<String, OneDecorate> entry : this.items.entrySet()) {
            if (entry.getValue().getiId().equals(itemId)) {
                OneDecorate oneDecorate = entry.getValue();//这里把Id 设置进去，保存的时候 不用保存，节省容量
                oneDecorate.setId(entry.getKey());
                oneDecorateList.add(oneDecorate);
            }
        }
        return oneDecorateList;
    }

    public void updateRefreshTime(String itemId, Date date) {
        refreshTime.put(itemId, date);
    }

    /**
     * 删除 某个 小动物
     *
     * @param itemId
     * @param num
     */
    public void deleteByItemId(String itemId, int num) {
        List<String> listkeys = new ArrayList<String>();
        for (Map.Entry<String, OneDecorate> entry : this.items.entrySet()) {
            if (entry.getValue().getiId().equals(itemId)) {
                listkeys.add(entry.getKey());
            }
        }
        if (listkeys.size() > num) {
            listkeys = listkeys.subList(0, num);
        }
        for (String key : listkeys) {
            items.remove(key);
        }

    }

    public void incrAnimalEffectNum(String itemId, int num) {
        if (animalEffect.containsKey(itemId)) {
            animalEffect.put(itemId, animalEffect.get(itemId) + num);
        } else {
            animalEffect.put(itemId, num);
        }
    }

    /**
     * 检测 删掉过期的 小动物
     *
     * @return
     */
    public List<String> checkTime() {
        List<String> listkeys = new ArrayList<String>();
        Date date = new Date();
        for (Map.Entry<String, OneDecorate> entry : this.items.entrySet()) {
            if (entry.getValue().geteTime() == null) {
                continue;
            }
            if (entry.getValue().geteTime().before(date)) {
                listkeys.add(entry.getKey());
            }
        }
        for (String key : listkeys) {
            items.remove(key);
        }

        return listkeys;
    }

}
