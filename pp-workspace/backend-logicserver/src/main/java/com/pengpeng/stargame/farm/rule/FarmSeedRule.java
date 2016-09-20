package com.pengpeng.stargame.farm.rule;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.util.RandomUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 农场种子
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-3-6下午12:17
 */
@Entity
@Table(name = "sg_rule_farm_seed")
@PrimaryKeyJoinColumn(name="itemsId")
public class FarmSeedRule extends BaseItemRule {

    @Column
    private int growthTime;
    @Column
    private String output;
    @Column
    private int production;
    @Column
    private String harvestEditor;
    @Column
    private int cropsReward;
    @Column
    private int expReward;
    @Column
    private String growthImage;
    @Column
    private String matureImage;
    @Column
    private int seedsExp;

    @Column//掉落信息
    private String dropInfo;
    @Column//掉落信息
    private int fProbability;
    //几熟
    @Transient
    List<Product> productList;
    //掉落
    @Transient
    List<DropItem> dropItemList;
    //成熟
    @Transient
   List<DropItem> ripeItemList;

    public List<DropItem> getDropItemList() {
        return dropItemList;
    }

    public void setDropItemList(List<DropItem> dropItemList) {
        this.dropItemList = dropItemList;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(int growthTime) {
        this.growthTime = growthTime;
    }

    public String getOutput() {
        if(ripeItemList.size()>0){
            int total=0;
            for(DropItem dropItem:ripeItemList){
                 total+=dropItem.getProbability();
            }
            int randomNum = RandomUtil.range(0, total);
            int temp=0;
            for(DropItem dropItem:ripeItemList){
                temp+=dropItem.getProbability();
                if(randomNum<temp){
                    return dropItem.getItemId();
                }
            }

        }
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public String getHarvestEditor() {
        return harvestEditor;
    }

    public void setHarvestEditor(String harvestEditor) {
        this.harvestEditor = harvestEditor;
    }

    public int getCropsReward() {
        return cropsReward;
    }

    public void setCropsReward(int cropsReward) {
        this.cropsReward = cropsReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public String getGrowthImage() {
        return growthImage;
    }

    public void setGrowthImage(String growthImage) {
        this.growthImage = growthImage;
    }

    public String getMatureImage() {
        return matureImage;
    }

    public void setMatureImage(String matureImage) {
        this.matureImage = matureImage;
    }

    public int getSeedsExp() {
        return seedsExp;
    }

    public void setSeedsExp(int seedsExp) {
        this.seedsExp = seedsExp;
    }



    @Override
    public String getKey() {
        return super.getItemsId();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public String getDropInfo() {
        return dropInfo;
    }

    public void setDropInfo(String dropInfo) {
        this.dropInfo = dropInfo;
    }

    public int getfProbability() {
        return fProbability;
    }

    public void setfProbability(int fProbability) {
        this.fProbability = fProbability;
    }

    public List<DropItem> getRipeItemList() {
        return ripeItemList;
    }

    public void setRipeItemList(List<DropItem> ripeItemList) {
        this.ripeItemList = ripeItemList;
    }

    public  void init(){

        /**
         * 初始化 成熟信息
         */
        String [] line=this.harvestEditor.split(";");

        List<Product> list=new ArrayList<Product>();
        for (int i=0;i<line.length;i++){
            Product p=new Product();
            String[] items=line[i].split(",");
            p.setId(i+1);
            p.setTime(Integer.parseInt(items[0]));
            p.setNum(Integer.parseInt(items[1]));
            list.add(p);
        }
        productList = list;

        /**
         * 初始化 掉落 信息
         */
        String [] line1=this.dropInfo.split(";");

        List<DropItem> list1=new ArrayList<DropItem>();
        for (int i=0;i<line1.length;i++){
           DropItem dropItem=new DropItem();
            String[] items=line1[i].split(",");
            if(items.length!=3){
                continue;
            }
            dropItem.setItemId(items[0]);
            dropItem.setNum(Integer.parseInt(items[1]));
            dropItem.setProbability(Integer.parseInt(items[2]));
            list1.add(dropItem);
        }
        dropItemList = list1;
        /**
         * 初始化成熟  随机收获 信息
         */
        List<DropItem> ripList=new ArrayList<DropItem>();
        String [] ripLine=this.output.split(";");
        for (int i=0;i<ripLine.length;i++){
            DropItem dropItem=new DropItem();
            String[] items=ripLine[i].split(",");
            if(items.length!=2){
                continue;
            }
            dropItem.setItemId(items[0]);
//            dropItem.setNum(Integer.parseInt(items[1]));
            dropItem.setProbability(Integer.parseInt(items[1]));
            ripList.add(dropItem);
        }
        ripeItemList = ripList;
    }
    public Product getOneProduct(int id){
        for (int i=0;i<this.getProductList().size();i++){
            if(this.getProductList().get(i).getId()==id){
                return this.getProductList().get(i);
            }
        }
        return null;
    }

    /**
     * 等级是否可种
     * @param level
     * @return
     */
    @Override
    public void checkLevel(int level) throws AlertException{
        if(this.getFarmLevel()>level){
            throw new AlertException("等级未达到");
        }
    }


}
