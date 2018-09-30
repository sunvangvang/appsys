package com.aibyd.appsys.domain;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppsysUserRoleRepository extends CrudRepository<AppsysUserRole, Long> {

	@Query("select o.roleId from appsys_user_role o where o.userId = ?1")
	public Set<Long> findRoleIdsByUserId(long userId);

	@Query(value = "select group_concat(t.sys_role_id) from appsys_user_role t where t.sys_user_id = ?1", nativeQuery = true)
	public String findRoleIdStrByUserId(long userId);

}
