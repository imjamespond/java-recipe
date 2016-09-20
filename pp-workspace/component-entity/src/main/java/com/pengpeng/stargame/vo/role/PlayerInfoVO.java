package com.pengpeng.stargame.vo.role;


import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.smallgame.SmallGameListVO;

@Desc("角色信息")
public class PlayerInfoVO {
    @Desc("id")
    public String id;

    //属性信息
	@Desc("用户主站头像")
	private String portrait;// 用户主站头像
    @Desc("角色名")
    private String nickName;// 角色名
    @Desc("性别")
    private int sex;//性别

    @Desc("家族Id ，如果是 空字符串 表示目前没有加入家族")
    private String family;
    @Desc("家族身份 ，如果是 空字符串 表示目前没有加入家族")
    private int familyIdentity;

    @Desc("称号")
    private String title;// 称号

    @Desc("农场等级")
    private int farmLevel;// 农场等级
    @Desc("时尚指数")
    private int fashionIndex;// 时尚指数
    @Desc("房间的豪华度")
    private int roomIndex;// 房间的豪华度

    @Desc("小游戏排行")
    private SmallGameListVO gameRank;

    //个人信息
	@Desc("用户主站ID")
	private int userId;
    @Desc("粉丝身份")
    private int payMember;
    @Desc("生日")
    private long birth;
    @Desc("省")
    private int province;
    @Desc("市")
    private int city;
    @Desc("个性签名")
    private String signature;

    //相册
    @Desc("相册")
    private String album;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public int getFamilyIdentity() {
        return familyIdentity;
    }

    public void setFamilyIdentity(int familyIdentity) {
        this.familyIdentity = familyIdentity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFarmLevel() {
        return farmLevel;
    }

    public void setFarmLevel(int farmLevel) {
        this.farmLevel = farmLevel;
    }

    public int getFashionIndex() {
        return fashionIndex;
    }

    public void setFashionIndex(int fashionIndex) {
        this.fashionIndex = fashionIndex;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public SmallGameListVO getGameRank() {
        return gameRank;
    }

    public void setGameRank(SmallGameListVO gameRank) {
        this.gameRank = gameRank;
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

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
