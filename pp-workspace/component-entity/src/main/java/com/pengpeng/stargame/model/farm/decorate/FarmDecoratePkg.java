package com.pengpeng.stargame.model.farm.decorate;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.farm.FarmItem;
import com.pengpeng.stargame.util.Uid;

import java.util.*;

/**
 * User: mql
 * Date: 14-3-17
 * Time: 下午3:55
 */
public class FarmDecoratePkg extends BaseEntity<String> {
    // 玩家ID
    private String pid;

    // 仓库等级
    private int level;

    // 仓库容量
    private int size;

    // 仓库物品 key : id(FarmItem中的id) ,value : FarmItem
    private Map<String, FarmItem> items = new HashMap<String, FarmItem>();

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<String, FarmItem> getItems() {
        return items;
    }

    /**
     * 仓库内是否有物品
     *
     * @return true:有
     */
    public boolean existItems() {
        if (this.items != null && !this.items.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 是否存在指定的 物品，通过物品 Id 查找
     *
     * @param itemId
     * @return true:存在
     */
    public boolean existByItemId(String itemId) {
        for (Map.Entry<String, FarmItem> entry : this.items.entrySet()) {
            if (entry.getValue().getItemId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在物品   通过格子的唯一 Id
     *
     * @param id
     * @return true:存在
     */
    public boolean existById(String id) {
        for (Map.Entry<String, FarmItem> entry : this.items.entrySet()) {
            if (entry.getValue().getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 增加物品数量
     *
     * @param itemId itemId
     * @param num    物品数量
     * @param max    物品叠加上限
     */
    public void addItem(String itemId, int num, int max,Date validDete) {

        /**
         * 先 添加到 没有 满的 格子 里面
         */
        for (Map.Entry<String, FarmItem> entry : this.items.entrySet()) {
            if (entry.getValue().getItemId().equals(itemId)) {
                if (num == 0) {
                    break;
                }
                if (entry.getValue().getNum() < max) {
                    //如果 没满的 格子 刚好 合适
                    if (entry.getValue().getNum() + num <= max) {
                        entry.getValue().setNum(entry.getValue().getNum() + num);
                        num = 0;
                    } else {
                        int oneadd = max - entry.getValue().getNum();
                        num -= oneadd;
                        entry.getValue().setNum(max);

                    }
                }
            }
        }

        /**
         * 如果 没有添加 完 ，继续添加
         */
        if (num > 0) {
            int surplusNum = num;

            // 需要新增多少个格子
            int addBulk = surplusNum / max;

            //还剩多少
            int mNum = surplusNum % max;

            for (int i = 0; i < addBulk; i++) {
                this.add(itemId, max,validDete);
            }

            if (mNum > 0) {
                this.add(itemId, mNum,validDete);
            }


        }


    }

    /**
     * 增加物品数量
     *
     * @param itemId
     * @param num    物品数量
     */
    private void add(String itemId, int num,Date validDete) {
        FarmItem farmItem = new FarmItem();
        farmItem.setId(Uid.uuid());
        farmItem.setItemId(itemId);
        farmItem.setNum(num);
        if(validDete!=null){

            farmItem.setValidDete(validDete);
        }
        items.put(farmItem.getId(), farmItem);
    }

    /**
     * 整理物品
     *
     * @param itemId
     * @param max    物品叠加上限
     */
    public void arrangeItem(String itemId, int max) {
        if (this.isEmpty(itemId)) {
            return;
        }

        // 物品总数
        int sum = this.getSumByNum(itemId);

        //物品的有效期
        Date validDete=null;
        // 移除所有物品
        List<FarmItem> list = this.getItemByItemId(itemId);
        for (FarmItem farmItem : list) {
            if(farmItem.getValidDete()!=null){
                validDete=farmItem.getValidDete();
            }
            this.removeItem(farmItem.getId());
        }

        // 需要多少个格子
        int needCell = sum % max != 0 ? sum / max + 1 : sum / max;

        // 重新分配物品
        for (int i = 0; i < needCell; i++) {
            if (sum >= max) {
                this.add(itemId, max,validDete);
            } else {
                this.add(itemId, sum,validDete);
            }

            sum -= max;

            if (sum <= 0) {
                break;
            }
        }
    }

    /**
     * 移除物品
     *
     * @param id FarmItem的id
     */
    private void removeItem(String id) {
        this.items.remove(id);
    }

    /**
     * 已占用了多少格子 - 仓库已被使用了多少个格子
     *
     * @return int
     */
    public int outlayByItemSize() {
        if (this.items == null || this.items.isEmpty()) {
            return 0;
        }
        return this.items.size();
    }

    /**
     * 仓库容量是否达到容量上限
     *
     * @param itemId 添加的物品的Id
     * @param num    需加入物品总数
     * @param max    物品叠加上限
     * @return true : 超过容量
     */
    public boolean isMaxBySize(String itemId, int num, int max) {
        // 已使用了多少个格子
        int occupy = this.outlayByItemSize();

        //判断 仓库内 是否 存在添加的物品 ,并且没有占满格子，计算格子的num应该减去没占满格子还能添加的数量
        int hasNum = 0; //不需要占格子的数量
        for (Map.Entry<String, FarmItem> entry : this.items.entrySet()) {
            if (entry.getValue().getItemId().equals(itemId)) {
                if (entry.getValue().getNum() >= max) {
                    continue;
                }
                if (entry.getValue().getNum() < max) {
                    hasNum += (max - entry.getValue().getNum());
                }
            }
        }

        num = num - hasNum;

        int addBulk = 0;
        // 需要新增多少个格子
        if (num > 0) {
            addBulk = num % max != 0 ? num / max + 1 : num / max;
        }

        if ((occupy + addBulk) > this.size) {
            return true;
        }
        return false;
    }

    /**
     * 扣除物品
     *
     * @param id  FarmItem的id
     * @param num 物品数量
     */
    public void subItem(String id, int num) {
        FarmItem farmItem = this.items.get(id);
        if (farmItem.checkByNum(num)) {
            this.removeItem(farmItem.getId());
        } else {
            farmItem.subNum(num);
        }
    }

    /**
     * 数量是否足够扣除
     *
     * @param id  FarmItem的id
     * @param num 物品数量
     * @return true : 不足够
     */
    public boolean checkByNum(String id, int num) {
        FarmItem farmItem = this.items.get(id);
        if (farmItem == null) {
            return true;
        }
        if (farmItem.getNum() < num) {
            return true;
        }
        return false;
    }

    /**
     * 清空物品
     *
     * @param id FarmItem的id
     */
    public void clearItemByKey(String id) {
        this.removeItem(id);
    }

    /**
     * 获取物品
     *
     * @param id FarmItem的id
     * @return
     */
    public FarmItem getFarmItem(String id) {
        return this.items.get(id);
    }

    /**
     * 获取物品集合
     *
     * @param itemId 物品ID
     * @return List<FarmItem>
     */
    public List<FarmItem> getItemByItemId(String itemId) {
        List<FarmItem> list = new ArrayList<FarmItem>();

        for (Map.Entry<String, FarmItem> entry : this.items.entrySet()) {
            FarmItem farmItem = entry.getValue();
            if (farmItem.getItemId().equals(itemId)) {
                list.add(farmItem);
            }
        }
        return list;
    }

    /**
     * 获取所有物品
     *
     * @return List<FarmItem>
     */
    public List<FarmItem> getItemAll() {
        List<FarmItem> list = new ArrayList<FarmItem>();
        for (Map.Entry<String, FarmItem> entry : this.items.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * 通过 格子Id 扣除物品
     * @param gid
     * @param num
     */
    public void deductByGid(String gid,int num){
        FarmItem farmItem=items.get(gid);
        int itemnum=farmItem.getNum();
        itemnum-=num;
        if(itemnum<=0){
            items.remove(gid);
        }
    }

    /**
     * 扣除物品
     * 优先扣除 数量少的 格子的 物品
     *
     * @param itemId
     * @param num
     */
    public void deduct(String itemId, int num) {
        List<FarmItem> list = this.getItemByItemId(itemId);
        Collections.sort(list,new Comparator<FarmItem>() {
            @Override
            public int compare(FarmItem o1, FarmItem o2) {
                return o1.getNum()-o2.getNum();
            }
        });
        for (FarmItem farmItem : list) {
            int n = farmItem.getNum();
            this.subItem(farmItem.getId(), num);

            num -= n;

            if (num <= 0) {
                break;
            }
        }
    }

    /**
     * 扣除物品
     *
     * @param id  FarmItem的id
     * @param num
     */
    public void deductById(String id, int num) {
        this.subItem(id, num);
    }

    /**
     * 物品是否为NULL - 有没有物品
     *
     * @param itemId
     * @return true : 没有物品
     */
    public boolean isEmpty(String itemId) {
        List<FarmItem> farmItemList = getItemByItemId(itemId);
        if (farmItemList == null || farmItemList.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 物品总数
     *
     * @param itemId
     * @return
     */
    public int getSumByNum(String itemId) {
        List<FarmItem> farmItemList = getItemByItemId(itemId);

        if (this.isEmpty(itemId)) {
            return 0;
        }

        int num = 0;
        for (FarmItem farmItem : farmItemList) {
            num += farmItem.getNum();
        }
        return num;
    }

    /**
     * 检测过期物品
     * @return
     */
    public List<String> checkTime(){
        List<String>  listkeys=new ArrayList<String>();
        Date date=new Date();
        for(Map.Entry<String,FarmItem> entry:this.items.entrySet()){
            if(entry.getValue().getValidDete()==null){
                continue;
            }
            if(entry.getValue().getValidDete().before(date)){
                listkeys.add(entry.getKey());
            }
        }
        for(String key:listkeys){
            items.remove(key);
        }

        return listkeys;
    }
}
