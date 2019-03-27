package com.cmcc.report.dao;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.OnlinePlanBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineBillDao extends JpaRepository<OnlinePlanBill, Long>,JpaSpecificationExecutor<OnlinePlanBill>,BaseDao<OnlinePlanBill, Long>{
}

