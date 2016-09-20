package com.pengpeng.stargame.fashion;

import com.pengpeng.stargame.model.room.FashionPlayer;
import org.springframework.stereotype.Component;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:11
 */
@Component
public class FashionBuilder {

    public FashionPlayer newFashionPlayer(String pId){
        FashionPlayer fashionPlayer=new FashionPlayer(pId);
        return  fashionPlayer;
    }
}
