package com.pengpeng.stargame.farm.rule;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 14-3-17
 * Time: 上午10:36
 */
//@Entity
//@Table(name = "sg_rule_farm_animal")
//@PrimaryKeyJoinColumn(name="itemsId")
public class FarmAnimalRule extends BaseItemRule{
    private String name;

}
