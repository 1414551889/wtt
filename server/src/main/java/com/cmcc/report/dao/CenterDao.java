package com.cmcc.report.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.Center;

@Repository
public interface CenterDao extends JpaRepository<Center, Integer>,JpaSpecificationExecutor<Center>,BaseDao<Center, Integer> {
	
		
	@Query(value="select c.* from  center c ",nativeQuery = true)
	public List<Center> getAllCenter();
	
	@Query(value="select c.* from  center c order by c.publishTime desc",nativeQuery = true)
	public List<Center> getAllCenterSort();
	
	@Query(value = "update center e set e.publishTime = ?2 where e.centerId=?1", nativeQuery = true)
	@Modifying
	public void updateCenterTime(Integer id, Date date);

	@Query(value="select c.* from  center c  where c.centerId=?1",nativeQuery = true)
	public Center getCenterByCenterId(Integer centerId);

	@Query(value="select c.* from  center c  where c.centerName=?1",nativeQuery = true)
	public Center getCenterBycenterName(String centerName);
	
}
