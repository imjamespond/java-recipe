package com.pengpeng.stargame.task.rule;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.base.Item;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.vo.task.TaskConditionVO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 任务规则
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-11下午2:36
 */
@Entity
@Table(name = "sg_rule_task")
public class TaskRule extends BaseEntity<String> {
    //任务编号
    @Id
    private String id;
    //任务名称
    @Column
    private String name;
    //衔接任务编号
    @Column
    private String parentId;
    //任务类型
    @Column(nullable = false)
    private int type;
    //任务子类型
    @Column(nullable = false)
    private int subtype;

    //任务图标
    @Column
    private String icon;

    //农场等级
    @Column(nullable = false)
    private int farmLevel;

    //房间豪华度
    @Column(nullable = false)
    private int roomDegree;

    //时尚指数
    @Column(nullable = false)
    private int fashionIndex;

    //商业等级
    @Column(nullable = false)
    private int businessLevel;

    //任务条件描述
    @Column
    private String conditionsDesc;


    //条件类型
    @Column
    private int conditionsType;

    //条件编辑
    @Column
    private String conditions;

    //立即完成达人币
    @Column(nullable = false)
    private int gold;

    //任务说明
    @Column
    private String memo;

    //目的地链接
    @Column
    private String link;
    //目的地链接描述
    @Column
    private String linkDesc;

    //游戏币奖励
    @Column(nullable = false)
    private int gameCoin;
    //农场经验奖励
    @Column(nullable = false)
    private int farmExp;
    //商业经验奖励
    @Column(nullable = false)
    private int businessExp;
    //家族经费奖励
    @Column(nullable = false)
    private int familyFunds;
    //家族贡献奖励
    @Column(nullable = false)
    private int familyDevote;
    //积分奖励
    @Column(nullable = false)
    private int bonusScore;
    //免费赞次数的奖励奖励
    @Column(nullable = false)
    private int zanNum;

    //道具奖励
    @Column
    private String items;
    @Column
    private String chapters;
    @Column(nullable = false)
    private int newtTask;


    @Transient
    private List<TaskItem> taskItems;
    @Transient
    private List<TaskCondition> taskConditions;


    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getBusinessExp() {
        return businessExp;
    }

    public void setBusinessExp(int businessExp) {
        this.businessExp = businessExp;
    }

    public int getFamilyFunds() {
        return familyFunds;
    }

    public void setFamilyFunds(int familyFunds) {
        this.familyFunds = familyFunds;
    }

    public int getFamilyDevote() {
        return familyDevote;
    }

    public void setFamilyDevote(int familyDevote) {
        this.familyDevote = familyDevote;
    }

    public int getBonusScore() {
        return bonusScore;
    }

    public void setBonusScore(int bonusScore) {
        this.bonusScore = bonusScore;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getRoomDegree() {
        return roomDegree;
    }

    public void setRoomDegree(int roomDegree) {
        this.roomDegree = roomDegree;
    }

    public int getFashionIndex() {
        return fashionIndex;
    }

    public void setFashionIndex(int fashionIndex) {
        this.fashionIndex = fashionIndex;
    }

    public int getBusinessLevel() {
        return businessLevel;
    }

    public void setBusinessLevel(int businessLevel) {
        this.businessLevel = businessLevel;
    }

    public String getConditionsDesc() {
        return conditionsDesc;
    }

    public void setConditionsDesc(String conditionsDesc) {
        this.conditionsDesc = conditionsDesc;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public int getNewtTask() {
        return newtTask;
    }

    public void setNewtTask(int newtTask) {
        this.newtTask = newtTask;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskItem> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(List<TaskItem> taskItems) {
        this.taskItems = taskItems;
    }

    public List<TaskCondition> getTaskConditions() {
        return taskConditions;
    }

    public void setTaskConditions(List<TaskCondition> taskConditions) {
        this.taskConditions = taskConditions;
    }


    public int getConditionsType() {
        return conditionsType;
    }

    public void setConditionsType(int conditionsType) {
        this.conditionsType = conditionsType;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    @Override
    public String getKey() {
        return id;
    }

    private TaskItem newTaskItem(String[] items) {
        if (items.length >= 2) {
            TaskItem item = new TaskItem();
            item.itemId = items[0];
            item.num = Integer.parseInt(items[1]);
            return item;
        }
        return null;
    }

    //条件类型包括：1表示NPC对话、2表示收集道具、3表示满足农场等级、4表示满足房间豪华度、5表示满足时尚指数、6表示满足商业等级、……
    private TaskCondition newTaskCondition(String[] items) {
        if (items.length <= 1) {
            return null;
        }
        int type = Integer.parseInt(items[0]);
        if (TaskConstants.ID_TASK_TYPE.contains(type) && items.length >= 3) {
            //如果是物品
            TaskCondition item = new TaskCondition();
            item.type = Integer.parseInt(items[0]);
            item.id = items[1];
            item.num = Integer.parseInt(items[2]);
            return item;
        } else {
            //否则是数值
            if (items.length >= 2) {
                TaskCondition item = new TaskCondition();
                item.type = Integer.parseInt(items[0]);
                item.num = Integer.parseInt(items[1]);
                return item;
            }
        }
        return null;
    }

    public void init() {
        List<TaskItem> itemList = new ArrayList<TaskItem>();
        List<TaskCondition> condiList = new ArrayList<TaskCondition>();
        //初始化物品
        StringTokenizer itemToken = new StringTokenizer(items, ";");
        while (itemToken.hasMoreElements()) {
            String item = itemToken.nextToken();
            String[] its = item.split(",");
            TaskItem it = newTaskItem(its);
            if (it != null) {
                itemList.add(it);
            }
        }

        //初始化任务条件
        StringTokenizer condiToken = new StringTokenizer(conditions, ";");
        String[] dess = conditionsDesc.split(";");
        int i = 0;
        while (condiToken.hasMoreElements()) {
            String item = condiToken.nextToken();
            String[] its = item.split(",");
            TaskCondition it = newTaskCondition(its);
            if (it != null) {
                if (dess.length > i) {
                    it.setDes(dess[i]);
                }
                condiList.add(it);
                i++;
            }
        }
        taskItems = itemList;
        taskConditions = condiList;
    }

    public class TaskItem {
        //物品id
        public String itemId;
        //物品数量
        public int num;

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

    public class TaskCondition {
        //料件类型
        public int type;
        public String id;
        //条件值
        public int num;

        public String des;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}
