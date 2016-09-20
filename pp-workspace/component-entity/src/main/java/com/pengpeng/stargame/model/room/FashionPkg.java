package com.pengpeng.stargame.model.room;

import com.pengpeng.stargame.model.base.Item;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.util.Uid;

import java.util.*;


/**
 * 时装仓库
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午11:28
 */
public class FashionPkg {

	// 玩家ID
	private String pid;

	// 仓库等级
	private int level;

	// 仓库容量
	private int size;

    private Map<String,FashionItem> items = new HashMap<String, FashionItem>();

	public String getKey(){
		return this.pid;
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



	/**
	 * 新增物品
	 * @param itemId
	 * @param num
	 * @return grid
	 */
	private String addItemToGrid(String itemId, int num,Date validDete){
		return this.add(itemId,num,validDete);
	}

	private String add(String itemId , int num,Date validDete){
		FashionItem item = new FashionItem();
		item.setGrid(Uid.uuid());
		item.setPid(this.pid);
		item.setItemId(itemId);
		item.setNum(num);
		item.setStatus(0);
        item.setValidDete(validDete);
		this.items.put(item.getGrid(),item);
		return item.getGrid();
	}

	/**
	 * 是否有物品
	 * @return true:有
	 */
	public boolean existItems(){
		if(this.items !=null && !this.items.isEmpty()){
			return true;
		}
		return false;
	}

	/**
	 * 是否有物品
	 * @param itemId 物品ID
	 * @return true:存在
	 */
	public boolean existByItemId(String itemId){
		for(Map.Entry<String,FashionItem> entry : this.items.entrySet()){
			if(entry.getValue().getItemId().equals(itemId)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否存在物品
	 * @param grid Item的grid
	 * @return true:存在
	 */
	public boolean existByGrid(String grid){
		for(Map.Entry<String,FashionItem> entry : this.items.entrySet()){
			if(entry.getValue().getGrid().equals(grid)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 增加物品数量
	 * @param itemId itemId
	 * @param num 物品数量
	 * @param max 物品叠加上限
	 */
	public void addItem(String itemId,int num,int max,Date validDete){
		if(this.isMaxBySize(itemId,num,max)){
			return;
		}

        /**
         * 对之前算法就行修改 。。。。。。。。。。。。。。。。。。。。。。
         * 如果 物品 存在，并且 没有达到 叠加上限，那么 添加到那个格子上面
         * 如果达到了 叠加上限，那么 新开格子
         */
        /**
         * 先 添加到 没有 满的 格子 里面
         */
        for(Map.Entry<String,FashionItem> entry : this.items.entrySet()){
            if(entry.getValue().getItemId().equals(itemId)){
                if(num==0){
                    break;
                }
                if(entry.getValue().getNum()<max){
                    //如果 没满的 格子 刚好 合适
                    if(entry.getValue().getNum()+num<=max){
                        entry.getValue().setNum(entry.getValue().getNum()+num);
                        num=0;
                    }else {
                        int oneadd=max-entry.getValue().getNum();
                        num-=oneadd;
                        entry.getValue().setNum(max);

                    }
                }
            }
        }

        /**
         * 如果 没有添加 完 ，继续添加
         */
        if(num>0){
            int  surplusNum=num;

            // 需要新增多少个格子
            int addBulk = surplusNum / max ;

            //还剩多少
            int  mNum=surplusNum% max;

            for(int i=0;i<addBulk;i++){
                this.add(itemId,max,validDete);
            }

            if(mNum>0){
                this.add(itemId,mNum,validDete);
            }


        }

	}



	/**
	 * 移除物品
	 * @param grid Item的grid
	 */
	public void removeItem(String grid){
		this.items.remove(grid);
	}

	/**
	 * 已占用了多少格子 - 仓库已被使用了多少个格子
	 * @return int
	 */
	public int consumeByItemSize(){
		if(this.items == null || this.items.isEmpty()){
			return 0;
		}
		return this.items.size();
	}

	/**
	 * 仓库容量是否达到容量上限
	 * @param num 需加入物品总数
	 * @param max 物品叠加上限
	 * @return true : 超过容量
	 */
	public boolean isMaxBySize(String itemId,int num,int max){
		// 已使用了多少个格子
		int occupy = this.consumeByItemSize();
        //判断 仓库内 是否 存在添加的物品 ,并且没有占满格子，计算格子的num应该减去没占满格子还能添加的数量
        int hasNum = 0; //不需要占格子的数量
        for (Map.Entry<String, FashionItem> entry : this.items.entrySet()) {
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
	 * @param grid Item的grid
	 * @param num 物品数量
	 */
	public void subItem(String grid,int num){
		FashionItem item = this.getItemByGrid(grid);
		if(item.getNum()<=num){
			this.removeItem(item.getGrid());
		}else{
			item.subNum(num);
		}
	}

	/**
	 * 数量是否足够扣除
	 * @param grid Item的grid
	 * @param num 物品数量
	 * @return true : 不足
	 */
	public boolean checkByNum(String grid,int num){
		FashionItem item = this.items.get(grid);
		if(item==null){
			return true;
		}
		if(item.getNum() < num){
			return true;
		}
		return false;
	}

	/**
	 * 获取物品
	 * @param grid Item的grid
	 * @return FashionItem
	 */
	public FashionItem getItemByGrid(String grid){
		for(FashionItem item: this.items.values()){
			if(item.getGrid().equals(grid)){
				return item;
			}
		}
		return null;
	}

	/**
	 * 获取物品集合
	 * @param itemId 物品ID
	 * @return List<Item>
	 */
	public List<FashionItem> getItemByItemId(String itemId){
		List<FashionItem> list = new ArrayList<FashionItem>();

		for(Map.Entry<String,FashionItem> entry : this.items.entrySet()){
			FashionItem item = entry.getValue();
			if(item == null){
				continue;
			}
			if(item.getItemId().equals(itemId)){
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 获取所有物品
	 * @return List<FashionItem>
	 */
	public List<FashionItem> getItemAll(){
		List<FashionItem> list = new ArrayList<FashionItem>();
		for(Map.Entry<String,FashionItem> entry : this.items.entrySet()){
			list.add(entry.getValue());
		}
		return list;
	}

    /**
     * 扣除物品
     * @param itemId
     * @param num
     */
    public void deductByStatus(String itemId, int num) {
        List<FashionItem> list = this.getItemByItemId(itemId);
        for(FashionItem item : list){
            int n = item.getNum();
            this.subItem(item.getGrid(),num);

            num -= n;

            if(num <=0){
                break;
            }
        }
    }

	/**
	 * 扣除物品
	 * @param itemId
	 * @param num
	 */
	public void deduct(String itemId, int num) {
		List<FashionItem> list = this.getItemByItemId(itemId);
		for(FashionItem item : list){

            if(item.getStatus()==0){
                int n = item.getNum();


                this.subItem(item.getGrid(),num);

                num -= n;

                if(num <=0){
                    break;
                }
            }

		}
	}

	/**
	 * 扣除物品
	 * @param grid Item的grid
	 * @param num
	 */
	public void deductById(String grid, int num) {
		this.subItem(grid,num);
	}

	/**
	 * 物品是否为NULL - 有没有物品
	 * @param itemId
	 * @return true : 没有物品
	 */
	public boolean isEmpty(String itemId){
		List<FashionItem> farmItemList = getItemByItemId(itemId);
		if(farmItemList == null || farmItemList.isEmpty()){
			return true;
		}
		return false;
	}

    /**
     * 物品总数
     * @param itemId
     * @return sum(num)
     */
    public int getItemNumByStatus(String itemId) {
        List<FashionItem> itemList = getItemByItemId(itemId);

        if(this.isEmpty(itemId)){
            return 0;
        }

        int num = 0;
        for(FashionItem item : itemList){
            if(item.getStatus()==0){
                num += item.getNum();
            }

        }
        return num;
    }
	/**
	 * 物品总数
	 * @param itemId
	 * @return sum(num)
	 */
	public int getItemBySum(String itemId) {
		List<FashionItem> itemList = getItemByItemId(itemId);

		if(this.isEmpty(itemId)){
			return 0;
		}

		int num = 0;
		for(Item item : itemList){
			num += item.getNum();
		}
		return num;
	}

	/**
	 * 更新物品状态
	 * @param grid
	 */
	public void updateStatus(String grid) {
		FashionItem item = (FashionItem) getItemByGrid(grid);
		if(item ==null){
			return;
		}

		if(item.getStatus() == 0){
			item.setStatus(1);
		}else{
			item.setStatus(0);
		}
	}

	/**
	 * 随机一件物品
	 * @return
	 */
	public FashionItem getRandomItem(){
		if(!existItems()){
			return null;
		}
		List<FashionItem> list = getItemAll();
		int max = list.size() - 1;
		int idx = RandomUtil.range(0,max);
		return list.get(idx);
	}

    public List<String> checkTime(){
        List<String>  listkeys=new ArrayList<String>();
        Date date=new Date();
        for(Map.Entry<String,FashionItem> entry:this.items.entrySet()){
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
