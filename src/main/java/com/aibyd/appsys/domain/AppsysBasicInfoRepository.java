package com.aibyd.appsys.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AppsysBasicInfoRepository extends CrudRepository<AppsysBasicInfo, Long> {

	@Query("select t from appsys_basic_info t where t.infoType = ?1")
	public List<AppsysBasicInfo> findBasicInfosByType(String type);
	
	@Query("select t from appsys_basic_info t where t.pid = ?1")
	public List<AppsysBasicInfo> findBasicInfosByPid(long pid);

}
