package com.pengpeng.stargame.vo.piazza;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午2:56
 */
@Desc("明星礼物 请求")
public class StarGiftReq extends BaseReq{
    @Desc("类型  1 精美礼物  2 礼物")
    private int type;
    @Desc("礼物的Id")
    private String  id;
    @Desc("赠言")
    private String words;
    @Desc("数量")
    private int num;
    @Desc("请求的页数")
    private int pageNo;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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
}
