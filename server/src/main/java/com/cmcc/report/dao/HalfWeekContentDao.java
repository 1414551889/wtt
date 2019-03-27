package com.cmcc.report.dao;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.HalfWeekContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HalfWeekContentDao extends JpaRepository<HalfWeekContent, Long>,JpaSpecificationExecutor<HalfWeekContent>,BaseDao<HalfWeekContent,
        Long>{
}

