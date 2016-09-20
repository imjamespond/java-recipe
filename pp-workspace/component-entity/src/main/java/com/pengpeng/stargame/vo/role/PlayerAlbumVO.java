package com.pengpeng.stargame.vo.role;


import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

@Desc("角色信息")
public class PlayerAlbumVO {
    @Desc("角色类型")
    private String[] picture;//角色类型
    @Desc("总数")
    private int total;//
    @Desc("页码")
    private int page;//


}
