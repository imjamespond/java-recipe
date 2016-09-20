package org.copycat.framework;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;

public class TableMap {
	private Map<String, String> map = new HashMap<String, String>();
	private Map<String, Class<?>> type = new HashMap<String, Class<?>>();
	private String sequnce;
	private String id;
	private String tableName;
	private String primaryKey;

	public String getId() {
		return id;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setSequnce(String fieldName, String sequnce) {
		this.id = fieldName;
		this.sequnce = sequnce;
		this.primaryKey = map.get(fieldName);
		map.remove(fieldName);
	}

	public void addColumn(String fieldName, String columnName) {
		map.put(fieldName, columnName);
	}

	public String getColumn(String fieldName) {
		if (id.equals(fieldName)) {
			return this.primaryKey;
		}
		return map.get(fieldName);
	}

	public Class<?> getType(String fieldName) {
		return type.get(fieldName);
	}

	public void toKey() {
		System.out.println(map.keySet());
	}

	public void toValue() {
		System.out.println(map.values());
	}

	public String toInsert() {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		sb.append(tableName);
		sb.append("(");
		sb.append(primaryKey);
		sb.append(",");
		sb.append(Joiner.on(',').join(map.values()));
		sb.append(") values (:");
		sb.append(id);
		for (String key : map.keySet()) {
			sb.append(",:");
			sb.append(key);
		}
		sb.append(")");
		return sb.toString();
	}

	public String toUpdate() {
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (String key : map.keySet()) {
			if (flag) {
				sb.append(", ");
			}
			sb.append(map.get(key));
			sb.append("= :");
			sb.append(key);
			flag = true;
		}
		return sb.toString();
	}

	public String toUpdateWidthId() {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");
		boolean flag = false;
		for (String key : map.keySet()) {
			if (flag) {
				sb.append(",");
			}
			sb.append(map.get(key));
			sb.append("= :");
			sb.append(key);
			flag = true;
		}

		sb.append(" where ");
		sb.append(primaryKey);
		sb.append("=:");
		sb.append(id);
		return sb.toString();
	}

	public String toDeleteWidthId() {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ");
		sb.append(tableName);
		sb.append(" where ");
		sb.append(primaryKey);
		sb.append("=?");
		return sb.toString();
	}

	public String toSelectWidthId() {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(primaryKey);
		sb.append(" as ");
		sb.append(id);
		for (String key : map.keySet()) {
			sb.append(",");
			sb.append(map.get(key));
			sb.append(" as ");
			sb.append(key);
		}
		sb.append(" from ");
		sb.append(tableName);
		sb.append(" where ");
		sb.append(primaryKey);
		sb.append(" = ?");
		return sb.toString();
	}

	public String toSelect() {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(primaryKey);
		sb.append(" as ");
		sb.append(id);
		for (String key : map.keySet()) {
			sb.append(",");
			sb.append(map.get(key));
			sb.append(" as ");
			sb.append(key);
		}
		sb.append(" from ");
		sb.append(tableName);
		return sb.toString();
	}

	public String toCount() {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) from ");
		sb.append(tableName);
		return sb.toString();
	}

	public String toList() {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append(primaryKey);
		sb.append(" as ");
		sb.append(id);
		for (String key : map.keySet()) {
			sb.append(",");
			sb.append(map.get(key));
			sb.append(" as ");
			sb.append(key);
		}
		sb.append(" from ");
		sb.append(tableName);
		return sb.toString();
	}

	public String toListWidthPage() {
		StringBuilder sb = new StringBuilder();
		sb.append(toList());
		sb.append(" order by ");
		sb.append(primaryKey);
		sb.append(" desc offset ? limit ?");
		return sb.toString();
	}

	public String toSequnce() {
		return "select nextval('" + sequnce + "')";
	}

	public String getTalbe() {
		return this.tableName;
	}

}
