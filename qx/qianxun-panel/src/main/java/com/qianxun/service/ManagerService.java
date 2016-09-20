package com.qianxun.service;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.copycat.framework.sql.SqlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianxun.model.Manager;

@Service
@Transactional
public class ManagerService extends SqlService<Manager, Long> {

	public Manager findByEmail(String email) {
		String sql = "select * from manager where email = ?";
		return sqlTemplate.queryForObject(sql, Manager.class, email);
	}

	/**
	 * 认证成功返回用户ID，失败返回0。
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public long authenticate(String email, String password) {
		Manager manager = this.findByEmail(email);
		if (DigestUtils
				.md5Hex(DigestUtils.md5Hex(password) + manager.getSalt())
				.equals(manager.getPassword())) {
			return manager.getId();
		}
		return 0;
	}

	public Long save(Manager manager) {
		String salt = UUID.randomUUID().toString().substring(0, 6);
		String password = manager.getPassword();
		password = DigestUtils.md5Hex(DigestUtils.md5Hex(password) + salt);
		manager.setPassword(password);
		manager.setSalt(salt);
		return super.save(manager);
	}

	public void update(Manager manager) {
		Manager oldManager = this.get(manager.getId());
		oldManager.setUsername(manager.getUsername());
		oldManager.setRoles(manager.getRoles());
		oldManager.setState(manager.getState());
		oldManager.setEmail(manager.getEmail());
		super.update(oldManager);
	}

	public void updatePasswored(long id, String password) {
		Manager manager = this.get(id);
		String salt = manager.getSalt();
		password = DigestUtils.md5Hex(DigestUtils.md5Hex(password) + salt);
		manager.setPassword(password);
		super.update(manager);
	}
}
