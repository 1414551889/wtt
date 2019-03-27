package com.cmcc.report.dao;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.OnlinePlanContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlinePlanContentDao extends JpaRepository<OnlinePlanContent, Long>,JpaSpecificationExecutor<OnlinePlanContent>,
        BaseDao<OnlinePlanContent, Long>{
}

