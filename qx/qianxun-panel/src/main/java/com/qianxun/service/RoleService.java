package com.qianxun.service;

import java.util.ArrayList;
import java.util.List;

import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.Manager;
import com.qianxun.model.Role;

@Service
@Transactional
public class RoleService extends SqlService<Role, Long> {

	public List<Role> findByManager(Manager manager) {
		List<Role> roleList = new ArrayList<Role>();
		String roles = manager.getRoles();
		if (roles == null || roles.isEmpty()) {
			return roleList;
		}

		String[] keyArray = roles.split(",");
		for (String key : keyArray) {
			Role role = this.findBykey(key);
			roleList.add(role);
		}

		return roleList;
	}

	public Role findBykey(String key) {
		String sql = "select * from role where key = ?";
		return sqlTemplate.queryForObject(sql, Role.class, key);
	}
}
