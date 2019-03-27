package com.cmcc.report.service.imp;

import com.cmcc.report.dao.BillDao;
import com.cmcc.report.dao.HalfWeekContentDao;
import com.cmcc.report.dao.HalfWeekReportBillDao;
import com.cmcc.report.dao.VersionDao;
import com.cmcc.report.model.*;
import com.cmcc.report.service.HalfWeekReportBillService;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Page;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("halfWeekReportBillService")
public class HalfWeekReportBillServiceImp implements HalfWeekReportBillService {
	@Autowired
	private HalfWeekReportBillDao halfWeekReportBillDao;
	@Autowired
	private HalfWeekContentDao contentDao;
	
	private static Logger logger = LoggerFactory.getLogger(HalfWeekReportBillServiceImp.class);
	
	    //信息保存
	    @Override
		@Transactional
		public int saveHalfWeekReportBill(HalfWeekReportBill halfWeekReportBill){
			HalfWeekReportBill save = null;
			try {
				save = halfWeekReportBillDao.save(halfWeekReportBill);
			} catch (Exception e) {
				logger.error("保存报错"+e.getMessage());
			}
			if(save != null){
				return 1;
			}
			return 0;
		}
		//查询订单详情
		private final String SELECT_ONE_JPQL= "select t from HalfWeekReportBill t WHERE t.id = ?1";
		@Override
		public HalfWeekReportBill getHalfWeekReportBill(Long billId) {
			List params = new ArrayList<Object>();
			params.add(billId);
			List<HalfWeekReportBill> list;
			try {
				list =halfWeekReportBillDao .findAllList(SELECT_ONE_JPQL, params);
				if(null != list && !list.isEmpty()){
					return list.get(0);
				}
			} catch (Exception e) {
				logger.error("查询订单详情+billId="+billId+"错误信息:"+e.getMessage());
				return null;
			}	
			return null;
		}

	//清单发布
	@Override
	@Transactional
	public void publishHalfBill(List<Long> idList) {
		 String UPDATE_FORCHECK_JPQL="update reportBill o set o.isCheck=2 where o.id in (";
		int size = idList.size();
		List  params = new ArrayList<Object>();
		if(CollectionUtils.isEmpty(idList)){
			return;
		}else{
			for(int i=1;i<=idList.size();i++){
				UPDATE_FORCHECK_JPQL=UPDATE_FORCHECK_JPQL+"?"+i+",";
				params.add(idList.get(i-1));
			}
		}
		UPDATE_FORCHECK_JPQL=UPDATE_FORCHECK_JPQL.substring(0,UPDATE_FORCHECK_JPQL.length()-1)+")";
		halfWeekReportBillDao.update(UPDATE_FORCHECK_JPQL,params);
	}
	//获取本周清单
	@SuppressWarnings("rawtypes")
	public List<HalfWeekReportBill> getBillForInWeek() {
		String SELECT_FORINWEEK_JPQL= "select t from HalfWeekReportBill t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = YEARWEEK(now()) AND t.isDelete = 0";
		 try {
			List  params = new ArrayList<Object>();
			List<HalfWeekReportBill> findAllList = halfWeekReportBillDao.findAllList(SELECT_FORINWEEK_JPQL, params);
			if(null != findAllList && !findAllList.isEmpty()){
				return findAllList;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e.getMessage());
		  return null;
		}	
	}
   //判断本周是否有清单 没有则保存
	@Override
	public void initializeFirtBill(User creater) throws ParseException {
		List<HalfWeekReportBill> billForInWeek = this.getBillForInWeek();
		String  lastDayForWeek=null;
		HalfWeekReportBill halfWeekReportBill=null;
		if(!CollectionUtils.isEmpty(billForInWeek)&&billForInWeek.size()==1){
			Date subTime = billForInWeek.get(0).getSubTime();
			int dayForWeek = DateUtil.getDayForWeek(subTime);
			int dayForWeek1 = DateUtil.getDayForWeek(new Date());
			if(dayForWeek==3&&(dayForWeek1==4||dayForWeek1==5)){
				 halfWeekReportBill = new HalfWeekReportBill();
				halfWeekReportBill.setCreater(creater.getUserId());
				lastDayForWeek = DateUtil.getEveryDay(5);

			}
		}
		if(CollectionUtils.isEmpty(billForInWeek)){
			 halfWeekReportBill = new HalfWeekReportBill();
			halfWeekReportBill.setCreater(creater.getUserId());
			int dayForWeek = DateUtil.getDayForWeek(new Date());
			if(dayForWeek==1||dayForWeek==2||dayForWeek==3){
				 lastDayForWeek = DateUtil.getEveryDay(3);

			}else if(dayForWeek==4||dayForWeek==5){
				 lastDayForWeek = DateUtil.getEveryDay(5);
			}else {
				return ;
			}
		}
		if(halfWeekReportBill!=null){
			try {
				saveHalfBill(halfWeekReportBill,lastDayForWeek);
			} catch (Exception e) {
				logger.error("清单新增错误信息:" + e.getMessage());
			}
		}
	}

	@Override
	public Page<HalfWeekReportBill> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date beginTime, Date endTime, Integer centerId) {
		String SELECT_BEFOREWEEK_JPQL= "select t from HalfWeekReportBill t WHERE t.subTime between ?1 AND ?2 AND t.isDelete = 0";
		List params = new ArrayList<Object>();
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
		return halfWeekReportBillDao.getAllPage(SELECT_BEFOREWEEK_JPQL,params,pageSize,pageIndex);
	}

	@Override
	public Page<HalfWeekReportBill> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId) {
		String SELECT_INWEEK_JPQL= "select t from HalfWeekReportBill t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = YEARWEEK(now()) AND t.isDelete = 0";
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
		return halfWeekReportBillDao.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);
	}


	@Override
	public List<HalfWeekReportBill> getHalfBillByCheck() {
		String SELECT_FORINWEEK_JPQL= "select t from HalfWeekReportBill t WHERE t.isCheck in (1,2,3) Order by t.subTime desc";
		try {
			List<HalfWeekReportBill> result=new ArrayList<>();
			List  params = new ArrayList<Object>();
			List<HalfWeekReportBill> findAllList = halfWeekReportBillDao.findAllList(SELECT_FORINWEEK_JPQL, params);
			if(null != findAllList && !findAllList.isEmpty()){
				result.add(findAllList.get(0));
				/*if(findAllList.size()>1){
					result.add(findAllList.get(1));
				}*/
				return result;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("错误信息:" + e.getMessage());
			return null;
		}
	}

	@Override
	public Page<HalfWeekContent> selectContentsByBillListAndCenterId(int pageSize, int pageIndex, Integer centerId,
																	 List<Long> billList) {
		String SELECT_INWEEK_JPQL= "select t from HalfWeekContent t ";
		List params = new ArrayList<Object>();

		if(centerId != -1){
			params.add(centerId);
			SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" where t.centerId=?1";
		}
		if(!CollectionUtils.isEmpty(billList)){
			if(billList.size()==1){
				Long id = billList.get(0);
				params.add(id);
				SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.billId =?2";

			}else{
				for(int i=0;i<billList.size();i++){
					params.add(billList.get(i));
					int j=i+2;
					SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.billId in(?"+j+",";
					SELECT_INWEEK_JPQL=SELECT_INWEEK_JPQL.substring(0,SELECT_INWEEK_JPQL.length()-1)+")";

				}
			}
		}
		SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" order by t.subTime DESC";
		return contentDao.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);
	}

	@Override
	public Page<HalfWeekContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Integer centerId, Long
			billId) {
		String SELECT_BYBILLID_JPQL = "select t from HalfWeekContent t WHERE t.billId=?1";
		List params = new ArrayList<Object>();
		params.add(billId);

		if(null != centerId){
			SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL+" And t.centerId ="+centerId;
		}
		SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL + " order by t.id,t.centerId,t.updateTime DESC";
		return halfWeekReportBillDao.getAllPage(SELECT_BYBILLID_JPQL, params, pageSize, pageIndex);
	}

	@Override
	public void updateCheckForBill(Long id, String checkIds, String checkNames) {
		String UPDATE_FORCHECK_JPQL="update reportBill o set o.isCheck=3,o.checkerId=?1,o.checkerName=?2 where o.id=?3";
		List  params = new ArrayList<Object>();
		params.add(checkIds);
		params.add(checkNames);
		params.add(id);
		halfWeekReportBillDao.update(UPDATE_FORCHECK_JPQL,params);
	}


	private void saveHalfBill(HalfWeekReportBill halfWeekReportBill,String lastDayForWeek) {
		//获取本周最后一天
		halfWeekReportBill.setTitle("半周报"+lastDayForWeek);
		//bill.setCenterId(creater.getCenterId());
		//bill.setCenterName(creater.getCenter());
		halfWeekReportBill.setIsCheck(0);
		halfWeekReportBill.setIsDelete(0);
		try {
			halfWeekReportBill.setSubTime(new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.saveHalfWeekReportBill(halfWeekReportBill);
	}
}
