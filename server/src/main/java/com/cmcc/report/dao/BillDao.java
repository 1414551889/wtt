package com.cmcc.report.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cmcc.report.common.BaseDao;
import com.cmcc.report.model.Bill;
import com.cmcc.report.util.Page;

@Repository
public interface BillDao extends JpaRepository<Bill, Long>,JpaSpecificationExecutor<Bill>,BaseDao<Bill, Long>{
}

