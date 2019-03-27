package com.cmcc.report.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.report.dao.BillDao;
import com.cmcc.report.dao.VersionDao;
import com.cmcc.report.model.Bill;
import com.cmcc.report.model.User;
import com.cmcc.report.model.VersionInfo;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Page;



@Service("BillService")
public class BillService {
	@Autowired
	private BillDao bd;
	
	@Autowired
	private VersionDao vd;
	
	private static Logger logger = LoggerFactory.getLogger(BillService.class);
	
	    //信息保存	
		@Transactional
		public int saveBill(Bill bill){
			Bill save = null;
			try {
				save = bd.save(bill);
			} catch (Exception e) {
				logger.error("保存报错"+e.getMessage());
			}
			if(save != null){
				return 1;
			}
			return 0;
		}
		//查询本周清单
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Bill> getBillByTimeAndCenter(int pageSize,int pageIndex,Integer isCheck,Integer centerId){
			String SELECT_INWEEK_JPQL= "select t from Bill t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = YEARWEEK(now()) AND t.isDelete = 0";
			List params = new ArrayList<Object>();
				if(isCheck != 4){
					if(isCheck == 5){
						SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.isCheck in (2,3)";
					}else if(isCheck == 6){
						SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.isCheck in (1,2,3)";
					}else{
					    SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.isCheck ="+isCheck;
					}
				}
				if(centerId != -1){
					params.add(centerId);
					SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.centerId=?1";
				}
				SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" order by t.subTime DESC";
				return bd.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);	
		}
		//查询历史清单
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Page<Bill> getBillBeforeTimeAndCenter(int pageSize,int pageIndex,Integer isCheck,Date beginTime,Date endTime,Integer centerId){
			String SELECT_BEFOREWEEK_JPQL= "select t from Bill t WHERE t.subTime between ?1 AND ?2 AND t.isDelete = 0";
			List params = new ArrayList<Object>();
				/*if(isCheck == 0){
					params.add(0);
					params.add(0);
				}else if(isCheck == 1){
					params.add(1);
					params.add(1);
				}else{
					params.add(0);
					params.add(1);
				}*/
			if(isCheck != 4){
				if(isCheck == 5){
					SELECT_BEFOREWEEK_JPQL = SELECT_BEFOREWEEK_JPQL+" AND t.isCheck in (2,3)";
				}else if(isCheck == 6){
					SELECT_BEFOREWEEK_JPQL = SELECT_BEFOREWEEK_JPQL+" AND t.isCheck in (1,2,3)";
				}else{
					SELECT_BEFOREWEEK_JPQL = SELECT_BEFOREWEEK_JPQL+" AND t.isCheck ="+isCheck;
				}
			}
				params.add(beginTime);
				params.add(endTime);
				if(centerId != -1){
					params.add(centerId);
					SELECT_BEFOREWEEK_JPQL = SELECT_BEFOREWEEK_JPQL+" AND t.centerId=?3";
				}
				SELECT_BEFOREWEEK_JPQL = SELECT_BEFOREWEEK_JPQL+" order by t.subTime DESC";
				return bd.getAllPage(SELECT_BEFOREWEEK_JPQL,params,pageSize,pageIndex);	
		}
		//清单删除
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Transactional
		public void deleteBill(String ids){	
			String DELETE_JPQL="update Bill o set o.isDelete=1 where o.billId=?1";
			String[] contentIds = ids.split(",");
			for (String string : contentIds) {
				List  params = new ArrayList<Object>();
				params.add(Long.valueOf(string));
				bd.update(DELETE_JPQL,params);
			}	
		}
		//清单删除真
		@Transactional
		public void deleteTrue(String ids){	
		/*	String DELETE_JPQL="update Bill o set o.isDelete=1 where o.billId=?1";*/
			String[] contentIds = ids.split(",");
			for (String string : contentIds) {
				bd.delete(Long.valueOf(string));
			}	
		}
		//清单状态修改
		private String UPDATE_JPQL="update bill o set o.isCheck=1 where o.billId=?1";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Transactional
		public void updateBill(String ids){	
			String[] contentIds = ids.split(",");
			for (String string : contentIds) {
				List  params = new ArrayList<Object>();
				params.add(Long.valueOf(string));
				bd.batchDelete(UPDATE_JPQL,params);
			}	
		}
		//查询订单详情
		private final String SELECT_ONE_JPQL= "select t from Bill t WHERE t.billId = ?1";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Bill getBill(Long billId) {
			List params = new ArrayList<Object>();
			params.add(billId);
			List<Bill> list;
			try {
				list = bd.findAllList(SELECT_ONE_JPQL, params);
				if(null != list && !list.isEmpty()){
					return list.get(0);
				}
			} catch (Exception e) {
				logger.error("查询订单详情+billId="+billId+"错误信息:"+e.getMessage());
				return null;
			}	
			return null;
		}
		//清单状态修改
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Transactional
		public void updateCheckForBill(Long id,String checkIds,String checkNames){	
			    String UPDATE_FORCHECK_JPQL="update bill o set o.isCheck=3,o.checkerId=?1,o.checkerName=?2 where o.billId=?3";
				List  params = new ArrayList<Object>();
				params.add(checkIds);
				params.add(checkNames);
				params.add(id);
				bd.update(UPDATE_FORCHECK_JPQL,params);	
		} 
	//获取重点项目
	private final String SELECT_IMPORTANT_JPQL = "select t from Bill t WHERE (select COUNT(*) from Content o where o.isImportant = 0)>0  AND t.isCheck in(2,3)order by t.subTime DESC";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Bill> getImportantBill(int pageSize,int pageIndex) {
		List params = new ArrayList<Object>();
		try {
			Page<Bill> allPage = bd.getAllPage(SELECT_IMPORTANT_JPQL, params, pageSize, pageIndex);
			return allPage;
		} catch (Exception e) {
			logger.error("错误信息:" + e.getMessage());
			return null;
		}
	}
	//清单提交
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void commitBill(Long id) {
		 String UPDATE_FORCHECK_JPQL="update bill o set o.isCheck=1 where o.billId=?1";
		 List  params = new ArrayList<Object>();
		 params.add(id);
		 bd.update(UPDATE_FORCHECK_JPQL,params);		
	}
	//清单发布
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void publishBill(Long id) {
		 String UPDATE_FORCHECK_JPQL="update bill o set o.isCheck=2 where o.billId=?1";
		 List  params = new ArrayList<Object>();
		 params.add(id);
		 bd.update(UPDATE_FORCHECK_JPQL,params);		
	}
	//获取本周清单
	@SuppressWarnings("rawtypes")
	public Bill getBillForInWeek() {
		String SELECT_FORINWEEK_JPQL= "select t from Bill t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = YEARWEEK(now()) AND t.isDelete = 0";
		 try {
			List  params = new ArrayList<Object>();
			List<Bill> findAllList = bd.findAllList(SELECT_FORINWEEK_JPQL, params);
			if(null != findAllList && !findAllList.isEmpty()){
				return findAllList.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e.getMessage());
		  return null;
		}	
	}
   //判断本周是否有清单 没有则保存
	public void initializeFirtBill(User creater) {
		Bill billForInWeek = this.getBillForInWeek();
		if(null == billForInWeek){
		Bill bill = new Bill();
		try {
			//获取本周最后一天
			String lastDayForWeek = DateUtil.getLastDayForWeek();
			bill.setTitle("项目推进表"+lastDayForWeek);
			bill.setCreater(creater.getUserId());
			//bill.setCenterId(creater.getCenterId());
			//bill.setCenterName(creater.getCenter());
			bill.setIsCheck(0);
			bill.setIsDelete(0);
			bill.setSubTime(new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek));
			this.saveBill(bill);
		} catch (Exception e) {
			logger.error("清单新增错误信息:" + e.getMessage());
		}
		}
	}
	
	//获取最后一条
	@SuppressWarnings("rawtypes")
	public Bill getBillForLast() {
		String SELECT_FORINWEEK_JPQL= "select t from Bill t WHERE t.isCheck in (1,2,3) Order by t.subTime desc";
		 try {
			List  params = new ArrayList<Object>();
			List<Bill> findAllList = bd.findAllList(SELECT_FORINWEEK_JPQL, params);
			if(null != findAllList && !findAllList.isEmpty()){
				return findAllList.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e.getMessage());
		  return null;
		}	
	}
	
	//获取最新版本信息
	public VersionInfo getVersionInf(Integer os) {
		return vd.selectVersion(os);
	}
	
	
}
