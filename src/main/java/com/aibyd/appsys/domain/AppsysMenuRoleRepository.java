package com.aibyd.appsys.domain;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppsysMenuRoleRepository extends CrudRepository<AppsysMenuRole, Long> {

	@Query("select o from appsys_menu_role o where o.id = ?1")
	public AppsysMenuRole findSysMenuRoleById(long id);

	@Query("select o from appsys_menu_role o where o.roleId = ?1")
	public Set<AppsysMenuRole> findSysMenusByRoleId(long roleId);

	@Query("select o.menuId from appsys_menu_role o where o.roleId = ?1")
	public Set<Long> findMenuIdsByRoleId(long roleId);

	@Query("select o.roleId from appsys_menu_role o where o.menuId = ?1")
	public Set<Long> findRoleIdsByMenId(long menId);

	@Query(value = "select group_concat(t.sys_role_id) from appsys_menu_role t where t.sys_menu_id = ?1", nativeQuery = true)
	public String findRoleIdStrByMenuId(long menuId);

}
