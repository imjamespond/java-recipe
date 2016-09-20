package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-18上午11:17
 */
@Desc("创建角色")
public class CreatePlayerReq extends BaseReq {

    private String nickName;
    private int sex;
    private int type;
    private int userId;
    @Desc("明星Id")
    private int starId;
    @Desc("是否是超级粉丝 1 是")
    private int payMember;
    @Desc("生日")
    private long birthday;
    @Desc("所在省份")
    private java.lang.Integer province;
    @Desc("所在城市")
    private java.lang.Integer city;
    @Desc("出生省份")
    private java.lang.Integer birthProvince;
    @Desc("出生城市")
    private java.lang.Integer birthCity;
    @Desc("个性签名")
    private java.lang.String signature;

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPayMember() {
        return payMember;
    }

    public void setPayMember(int payMember) {
        this.payMember = payMember;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getBirthProvince() {
        return birthProvince;
    }

    public void setBirthProvince(Integer birthProvince) {
        this.birthProvince = birthProvince;
    }

    public Integer getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(Integer birthCity) {
        this.birthCity = birthCity;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
