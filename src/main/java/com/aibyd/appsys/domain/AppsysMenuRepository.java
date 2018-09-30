package com.aibyd.appsys.domain;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppsysMenuRepository extends CrudRepository<AppsysMenu, Long> {

	@Query("select o from appsys_menu o where o.id = ?1")
	public AppsysMenu findSysMenuById(long id);

	@Query("select o.id from appsys_menu o ")
	public Set<Long> findMenuIds();

	@Query(value = "select t.id from appsys_menu t where t.page_url = ?1", nativeQuery = true)
	public long findMenuIdByUri(String pageUri);

	@Query(value = "select t.menu_name, t.page_url, t.level, t.sort, t.pid from appsys_menu t where t.id = ?1", nativeQuery = true)
	public Iterable<Object[]> findMenuNameUriLevelSortById(long menuId);

	@Query(value = "select t.id, t.pid, t.menu_name, t.page_url, t.level, t.sort from appsys_menu t where t.level <> '0'", nativeQuery = true)
	public Iterable<Object[]> findFuncMenus();
	
	@Query(value = "SELECT COUNT(*) FROM appsys_menu t WHERE t.PAGE_URL = ?1", nativeQuery = true)
	public int findCountNumByUri(String uri);
}
