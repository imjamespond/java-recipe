<#function getFields dummy>
<#local fields="">
<#local i=0>
    <#list vo.items as e>
    <#if i!=0><#local fields=fields+", "></#if>
    <#local i=1>
        <#if e.type="java.lang.String">
        <#local fields=fields+"java.sql.Types.VARCHAR">
        <#elseif e.type="long">
        <#local fields=fields+"java.sql.Types.BIGINT">
        <#elseif e.type="double">
        <#local fields=fields+"java.sql.Types.BIGINT">
        <#elseif e.type="float">
        <#local fields=fields+"java.sql.Types.FLOAT">
        <#elseif e.type="boolean">
        <#local fields=fields+"java.sql.Types.BOOLEAN">
        <#elseif e.type="int">
        <#local fields=fields+"java.sql.Types.INTEGER">
        <#elseif e.nameDesc="blob">
        <#local fields=fields+"java.sql.Types.BINARY">
        <#else>
        <#local fields=fields+e.type+" "+e.name>
        </#if>
    </#list>
<#return fields>
</#function>
package com.metasoft.flying.service;

import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;

public class UserPersistenceService extends SqlService<UserPersist, Long> {
	private static final int[] TYPE_NAME = new int[] {java.sql.Types.VARCHAR};

	public List<UserPersist> getByName(String name) {
		
		Object[] objs = {name};
		return this.sqlTemplate.queryForList("select * from ${vo.table} where name=?", objs);
	}

}

/*
=====POSTGESQL
CREATE SEQUENCE ${vo.idSeq} START 101
DROP TABLE IF EXISTS ${vo.table};
CREATE TABLE ${vo.table} (
<#list vo.items as e>
<#if e.type="java.lang.String">
${e.name} VARCHAR( 128 ) NOT NULL ,
<#elseif e.type="long">
${e.name} BIGINT NOT NULL ,
<#elseif e.type="double">
${e.name} bigint NOT NULL ,
<#elseif e.type="float">
${e.name} FLOAT NOT NULL ,
<#elseif e.type="boolean">
${e.name} TINYINT NOT NULL ,
<#elseif e.type="int">
${e.name} INT NOT NULL ,
<#elseif e.nameDesc="blob">
${e.name} bytea NOT NULL ,
<#else>
${e.name} ??? NOT NULL ,
</#if>
</#list>
PRIMARY KEY ( id ) 
)
*/