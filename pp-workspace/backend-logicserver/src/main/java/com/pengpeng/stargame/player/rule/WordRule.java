package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-29上午11:48
 */
@Entity()
@Table(name="sg_rule_word")
public class WordRule extends BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    private int id;

    @Column(length = 255)
    private String words;

    @Column(length = 10)
    private String type;

    @Column(length = 50)
    private String memo;

    @Column()
    private int state;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getKey() {
        return id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
