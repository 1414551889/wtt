package com.cmcc.report.dao;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.OnlineTraceContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineTraceContentDao extends JpaRepository<OnlineTraceContent, Long>,JpaSpecificationExecutor<OnlineTraceContent>,
        BaseDao<OnlineTraceContent, Long>{
}

