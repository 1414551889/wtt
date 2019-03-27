package com.cmcc.report.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.Content;

@Repository
public interface ContentDao extends JpaRepository<Content, Long>,JpaSpecificationExecutor<Content>,BaseDao<Content, Long>{
}
