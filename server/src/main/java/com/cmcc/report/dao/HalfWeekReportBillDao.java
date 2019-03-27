package com.cmcc.report.dao;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.Bill;
import com.cmcc.report.model.HalfWeekReportBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HalfWeekReportBillDao extends JpaRepository<HalfWeekReportBill, Long>,JpaSpecificationExecutor<HalfWeekReportBill>,BaseDao<HalfWeekReportBill,
        Long>{
}

