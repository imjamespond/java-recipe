package com.pengpeng.stargame.vo.role;

import com.pengpeng.stargame.annotation.Desc;



/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-19下午4:58
 */
@Desc("网站的用户信息")
public class UserInfo {
    private Integer id;
    private String nickName;
    //网站数据:0:女,1:男
    //游戏早期:1:女,2:男
    //现在游戏:0:男,1:女
    private Integer sex;
    private Integer starId;

    //1 未超级粉丝
    private Integer  payMember;

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

    public UserInfo(){

    }
    public UserInfo(Integer id, String nickName, Integer sex) {
        this.id = id;
        this.nickName = nickName;
        this.sex = sex;
    }

    public Integer getStarId() {
        return starId;
    }

    public void setStarId(Integer starId) {
        this.starId = starId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getPayMember() {
        return payMember;
    }

    public void setPayMember(Integer payMember) {
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
