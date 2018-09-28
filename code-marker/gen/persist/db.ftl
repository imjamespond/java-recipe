<#--<#list vo.items as e>
${e.type} ${e.name} ${e.nameDesc}
</#list>-->

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

	//values

	//method

	//override method

	private static final int[] TYPE_UPDATE = new int[] {${getFields(0)}, java.sql.Types.BIGINT};
	private static final String SQL_UPDATE = "update ${vo.table} set <#assign i = 0><#list vo.items as e><#if i!=0>, </#if>${e.name}=?<#assign i=i+1></#list> where id = ?";
	
	private static final int[] TYPE_INSERT = new int[] {${getFields(0)}};
	private static final String SQL_INSERT = "insert into ${vo.table} (<#assign i = 0><#list vo.items as e><#if i!=0>, </#if>${e.name}<#assign i=i+1></#list>) values (<#assign i = 0><#list vo.items as e><#if i!=0>, </#if>?<#assign i=i+1></#list>)";
	private static final String SQL_DELETE = "delete from ${vo.table} where id = ?";

	public static ${vo.cls} getById(long id) {
		Object[] objs = {id};
		return PersistObject.get("select * from ${vo.table} where id=?",objs,TYPE_ID,new ${vo.cls}RowMapper());
	}
	
	public static List<${vo.cls}> getByName(String name) {
		Object[] objs = {name};
		return PersistObject.getList("select * from ${vo.table} where name=?",objs,TYPE_NAME,new ${vo.cls}RowMapper());
	}

	@Override
	public void delete() {
		JdbcTemplate jt = DaoFactory.getJdbcTemplate();
		if (null == jt) {
			return ;
		}
		
		Object[] params = new Object[]{id()};
		
		try {
			jt.update(SQL_DELETE, params, TYPE_ID);
		}  catch (DataAccessException e) {
			logger.warn(e.getMessage());
		}
	}

	@Override
	public void update() {

		JdbcTemplate jt = DaoFactory.getJdbcTemplate();
		if (null == jt) {
			return ;
		}
		
		Object[] params = new Object[]{<#assign i = 0><#list vo.items as e><#if i!=0>, </#if>${e.name}<#assign i=i+1></#list>, id()};
		
		try {
			jt.update(SQL_UPDATE, params, TYPE_UPDATE);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
		}
	}

	@Override
	public void save() {
		JdbcTemplate jt = DaoFactory.getJdbcTemplate();
		if (null == jt) {
			return ;
		}
		
		Object[] params = new Object[]{<#assign i = 0><#list vo.items as e><#if i!=0>, </#if>${e.name}<#assign i=i+1></#list> };
		
		try {
			jt.update(SQL_INSERT, params, TYPE_INSERT);
		} catch (DataAccessException e) {
			logger.warn(e.getMessage());
		}
	}
}
	
class ${vo.cls}RowMapper implements RowMapper<${vo.cls}>{

	@Override
	public ${vo.cls} mapRow(ResultSet rs, int arg1) throws SQLException {
		${vo.cls} vo = new ${vo.cls}();
		vo.setId( rs.getLong(1));//the first column is 1
		//vo.setName(rs.getString(2));//the same as field order
		//vo.setGender(rs.getInt(2));

		return vo;
	}
}

/*
DROP TABLE IF EXISTS `${vo.table}`;
CREATE TABLE `${vo.table}` (
<#list vo.items as e>
<#if e.type="java.lang.String">
`${e.name}` VARCHAR( 128 ) NOT NULL ,
<#elseif e.type="long">
`${e.name}` BIGINT NOT NULL ,
<#elseif e.type="double">
`${e.name}` DOUBLE NOT NULL ,
<#elseif e.type="float">
`${e.name}` FLOAT NOT NULL ,
<#elseif e.type="boolean">
`${e.name}` TINYINT NOT NULL ,
<#elseif e.type="int">
`${e.name}` INT NOT NULL ,
<#elseif e.nameDesc="blob">
`${e.name}` BLOB NOT NULL ,
<#else>
`${e.name}` ??? NOT NULL ,
</#if>
</#list>
PRIMARY KEY ( `id` ) 
) ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

=====POSTGESQL
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
