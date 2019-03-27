package com.cmcc.report.dao;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.OnlineTraceBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineTraceBillDao extends JpaRepository<OnlineTraceBill, Long>,JpaSpecificationExecutor<OnlineTraceBill>,
        BaseDao<OnlineTraceBill, Long>{
}

