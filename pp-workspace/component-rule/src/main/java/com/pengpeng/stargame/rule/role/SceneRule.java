package com.pengpeng.stargame.rule.role;

import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.model.Player;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: fang
 * Date: 12-12-21
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="sg_rule_scene")
public class SceneRule extends BaseEntity<String> {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private int type;
    @Column
    private int withTheScreen;

    @Column
    private int pkType;
    @Column
    @Lob
    private String monster;//

    @Column
    private String npc;//npc_101,100,200;npc_102,120,200;
    @Column
    private String imagePath;
    @Column
    private String musicPath;


    @Override
    public String getId() {
        return id;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWithTheScreen() {
        return withTheScreen;
    }

    public void setWithTheScreen(int withTheScreen) {
        this.withTheScreen = withTheScreen;
    }

    public int getPkType() {
        return pkType;
    }

    public void setPkType(int pkType) {
        this.pkType = pkType;
    }

    public String getMonster() {
        return monster;
    }

    public void setMonster(String monster) {
        this.monster = monster;
    }

    public String getNpc() {
        return npc;
    }

    public void setNpc(String npc) {
        this.npc = npc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }


}
