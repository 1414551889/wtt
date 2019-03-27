package com.cmcc.report.common;
import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.cmcc.report.util.Page;






@NoRepositoryBean
public interface BaseDao <T,ID extends Serializable> extends JpaRepository<T, ID>{
	
	Page<T> getAllPage(String jpql,List<Object> params,int pageSize,int pageIndex);
	
	List<T> findAllList(String jpql,List<Object> params);
	
	void batchDelete(List<T> entity);
	
	void batchDelete(String jpql,List<Object> params);
	
	void batchUpdate(List<T> entity,int leap);
	
	void batchAddorUpdate(List<T> entity, List<String> params,String keyName,int leap)throws ClassNotFoundException, Exception;
	
	void batchAdd(List<T> entity,int leap);
	
	Object findForMap(String jpql,Object[] params);
	
	Object[] findForObject(String sql,Object[] params);
	
	Object update(Object o);

	//总数
	public Integer getCount(String jpql, List<Object> params) ;
	
	//平均数
	public double getAvg(String jpql, List<Object> params);
	
	//及格率
	public int getAvgPass(String jpql, List<Object> params);
	
	//击败人数
	public int getBeatNum(String jpql,List<Object> obj);
	
	public int update(String jpql,List<Object> params);
}
