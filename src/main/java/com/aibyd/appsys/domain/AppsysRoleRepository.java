package com.aibyd.appsys.domain;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AppsysRoleRepository extends CrudRepository<AppsysRole, Long> {

	@Query(value = "INSERT INTO appsys_role (role_code,role_name,role_desc,sys_flag,create_time,VERSION)VALUES(?1,?2,?3,?4,NOW(),1);", nativeQuery = true)
	@Modifying
	@Transactional
	public int addRole(String roleCode, String roleName, String roleDesc, String sysFlag);

	@Query(value = "UPDATE appsys_role SET role_code = ?2, role_name = ?3, role_desc = ?4, sys_flag = ?5, VERSION = VERSION + 1 WHERE id = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	public int updateRole(long roleId, String roleCode, String roleName, String roleDesc, String sysFlag);

	@Query(value = "DELETE FROM appsys_role WHERE id = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	public int deleteRoleById(long id);

	@Query("select o from appsys_role o where o.id = ?1")
	public AppsysRole findSysRoleById(long id);

	@Query("select o.id from appsys_role o ")
	public Set<Long> findRoleIds();

	@Query(value = "SELECT COUNT(*) FROM appsys_role t WHERE IF(?1 = '',TRUE,t.role_code LIKE CONCAT('%',?1,'%')) AND IF(?2 = '',TRUE,t.role_name LIKE CONCAT('%',?2,'%')) AND IF(?3 = '',TRUE,t.role_desc LIKE CONCAT('%',?3,'%')) AND IF(?4 = '',TRUE,t.sys_flag = ?4)", nativeQuery = true)
	public long findRoleNumber(String roleCode, String roleName, String roleDesc, String sysFlag);

	@Query(value = "select SQL_CALC_FOUND_ROWS t.id, t.role_name, t.role_code, t.role_desc, t.sys_flag, t.create_time from appsys_role t  WHERE IF(?1 = '',TRUE,t.role_code LIKE CONCAT('%',?1,'%')) AND IF(?2 = '',TRUE,t.role_name LIKE CONCAT('%',?2,'%')) AND IF(?3 = '',TRUE,t.role_desc LIKE CONCAT('%',?3,'%')) AND IF(?4 = '',TRUE,t.sys_flag = ?4) order by t.create_time desc limit ?5, ?6", nativeQuery = true)
	public Iterable<Object[]> findRolesByPage(String roleCode, String roleName, String roleDesc, String sysFlag,
			long offset, long settings);

}
