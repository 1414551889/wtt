package com.cmcc.report.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.UserAndContent;

@Repository
public interface UserAndContentDao extends JpaRepository<UserAndContent, Integer>,JpaSpecificationExecutor<UserAndContent>,BaseDao<UserAndContent, Integer>{

	//通过项目ID（内容ID）删除关系数据
	@Transactional
	@Modifying
	@Query("delete UserAndContent t where t.contentId = ?1 and t.userId = ?2")
	public int deleteUACByContentId(int projectId, int userId);

	//通过中心ID 删除关系数据
	@Transactional
	@Modifying
	@Query("delete UserAndContent t where t.centerId = ?1 and t.userId = ?2")
	public void deleteUACByCenterId(int centerId, Integer userId);

	//通过中心ID 查询关系数据
	@Query("select t from UserAndContent t where t.centerId = ?1 and t.userId = ?2")
	public List<UserAndContent> selectByCenterId(int centerId, int userId);

	//通过项目ID（内容ID） 查询关系数据
	@Query("select t from UserAndContent t where t.contentId = ?1 and t.userId = ?2")
	public List<UserAndContent> selectByContentId(int contentId, int userId);
}
