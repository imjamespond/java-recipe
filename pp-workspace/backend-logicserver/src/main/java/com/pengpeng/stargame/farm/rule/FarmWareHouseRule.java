package com.pengpeng.stargame.farm.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.farm.FarmPackage;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * User: mql
 * Date: 13-4-27
 * Time: 下午7:10
 */
@Entity
@Table(name = "sg_rule_warehouse")
public class FarmWareHouseRule extends BaseEntity<Integer> {

	@Id
    private int warehouseLevel;

	@Column
    private int capacity;

	@Column
    private String props;

    @Transient
	private List<Items> items;

    public int getWarehouseLevel() {
        return warehouseLevel;
    }

    public void setWarehouseLevel(int warehouseLevel) {
        this.warehouseLevel = warehouseLevel;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    @Override
    public Integer getId() {
        return this.warehouseLevel;
    }

    @Override
    public void setId(Integer id) {
        this.warehouseLevel=id;
    }

	public void init(){
		if(this.props == null || (this.props.trim()).length() ==0){
			return ;
		}

		StringTokenizer stk = new StringTokenizer(this.props,";");
		if(stk == null){
			return ;
		}

		List<Items> list = new ArrayList<Items>();
		while (stk.hasMoreElements()){
			String obj = (String) stk.nextElement();
			if(obj == null || (obj.trim()).length() == 0){
				continue;
			}

			String [] itemArr = obj.split(",");
			if(itemArr == null || itemArr.length !=2){
				continue;
			}

			Items items = new Items();
			items.setItemId(itemArr[0]);
			items.setNum(Integer.parseInt(itemArr[1]));
			list.add(items);
		}

		this.items = list;
	}

	public List<Items> getItems() {
		return items;
	}

	@Override
    public Integer getKey() {
        return this.warehouseLevel;
    }

	/**
	 * 检查是否可以升级
	 * @param farmPackage
	 * @return true : 可升级
	 */
	public boolean check(FarmPackage farmPackage){
		// 检查是否符合条件
		for(Items item : items){
			final String itemId = item.getItemId();
			final int num = item.getNum();

			int _num = farmPackage.getSumByNum(itemId);
			if(_num < num){
				return false;
			}
		}
		return true;
	}

	/**
	 * 扣除物品
	 * @param farmPackage
	 */
	public void deduct(FarmPackage farmPackage){
		for(Items item : items){
			final String itemId = item.getItemId();
			final int num = item.getNum();

			farmPackage.deduct(itemId,num);
		}
	}

	public class Items {
		private String itemId;
		private int num;

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}
	}

}
