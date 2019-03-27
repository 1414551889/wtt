package com.cmcc.report.dao;


import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DayReportDetailDao extends JpaRepository<DayReport, Long>,JpaSpecificationExecutor<DayReport>,BaseDao<DayReport, Long> {
}

