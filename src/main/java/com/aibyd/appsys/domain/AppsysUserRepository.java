package com.aibyd.appsys.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aibyd.appsys.domain.AppsysUser;

@Repository
public interface AppsysUserRepository extends CrudRepository<AppsysUser, Long> {

	@Query("select o from appsys_user o where o.id = ?1")
	public AppsysUser findUserById(long userId);

	@Query("select o from appsys_user o where o.userName = ?1")
	public AppsysUser findUserByName(String userName);

	@Query(value = "select SQL_CALC_FOUND_ROWS t.id,t.user_name,t.valid from appsys_user t order by t.create_time desc limit ?1, ?2", nativeQuery = true)
	public Iterable<Object[]> findUsersByPage(long offset, long settings);

	@Query(value = "select count(*) from appsys_user t", nativeQuery = true)
	public long findUserNumber();

	@Query(value = "select t.id from appsys_user t where t.user_name = ?1", nativeQuery = true)
	public long findUserIdByUserName(String userName);

	@Query(value = "select t.user_name from appsys_user t", nativeQuery = true)
	public Iterable<String> findAllUsersName();

	@Query(value = "update appsys_user t set t.valid = ?1 where t.user_name = ?2", nativeQuery = true)
	@Modifying
	@Transactional
	public int updateValidByName(String valid, String userName);

	@Query(value = "delete from appsys_user where id = ?1", nativeQuery = true)
	@Modifying
	@Transactional
	public int deleteUserById(long id);

}
