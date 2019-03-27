package com.cmcc.report.service.imp;


import com.cmcc.report.dao.DayReportDetailDao;
import com.cmcc.report.model.DayReport;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.DayReportService;
import com.cmcc.report.util.Page;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("dayReportServiceImp")
public class DayReportServiceImp implements DayReportService {
	private static Logger logger = LoggerFactory.getLogger(ContentService.class);

	@Autowired
	private DayReportDetailDao re;

	@Override
	@Transactional
	public int saveDayReportInfo(DayReport dayReport) {
		dayReport.setIsDelete(0);
		dayReport.setIsCheck(0);

		dayReport.setSubTime(new Date());
		DayReport save=null;
		try {
			save= re.save(dayReport);
		} catch (Exception e) {
			logger.error("保存报错"+e.getMessage());
		}
		if(save != null){
			return 1;
		}
		return 0;
	}

	@Override
	@Transactional
	public void deleteDayReportInfo(List<Long>ids) {
		for(Long id :ids){
			re.delete(id);
		}
	}

	@Override
	@Transactional
	public int updateDayReportInfo(DayReport dayReport) {
		Object update=null;
		try {
			update = re.update(dayReport);
		} catch (Exception e) {
			logger.error("更新报错"+e.getMessage());
		}
		if(update != null){
			return 1;
		}
		return 0;
	}

	@Override
	public List<DayReport> getDayReportInfoByNow() {
		String sql= "select * from DayReport where to_days(subTime) = to_days(now());";
		List<DayReport> allList = re.findAllList(sql, null);
		if(allList!=null&&allList.size()>0){
			return allList;
		}
		//如果获取不,则新增
		return null;
	}

	@Override
	@Transactional
	public void publishDayReport(List<Long> dayReportIdList) {
		String UPDATE_FORCHECK_JPQL="update dayReport o set o.isCheck=2 where o.id in (";
		List  params = new ArrayList<Object>();
		if(CollectionUtils.isEmpty(dayReportIdList)){
			return;
		}else{
			for(int i=1;i<=dayReportIdList.size();i++){
				UPDATE_FORCHECK_JPQL=UPDATE_FORCHECK_JPQL+"?"+i+",";
				params.add(dayReportIdList.get(i-1));
			}
		}
		UPDATE_FORCHECK_JPQL=UPDATE_FORCHECK_JPQL.substring(0,UPDATE_FORCHECK_JPQL.length()-1)+")";
		re.update(UPDATE_FORCHECK_JPQL,params);
	}

	@Override
	public Page<DayReport> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId) {
		String SELECT_INWEEK_JPQL= "select t from DayReport t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = " +
				"YEARWEEK(now()) AND t.isDelete = 0";
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
		return re.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);
	}

	@Override
	public Page<DayReport> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date beginTime, Date endTime, Integer centerId) {
		String SELECT_BEFOREWEEK_JPQL= "select t from DayReport t WHERE t.subTime between ?1 AND ?2 AND t.isDelete = 0";
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
		return re.getAllPage(SELECT_BEFOREWEEK_JPQL,params,pageSize,pageIndex);
	}

	@Override
	public List<DayReport> getBillForLast() {
		String SELECT_FORINWEEK_JPQL= "select t from DayReport t WHERE t.isCheck in (1,2,3) Order by t.subTime desc";
		try {
			List  params = new ArrayList<Object>();
			List<DayReport> findAllList = re.findAllList(SELECT_FORINWEEK_JPQL, params);
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

	@Override
	public Page<DayReport> selectContentsByCenterId(int pageSize, int pageIndex, Integer centerId) {
		String SELECT_INWEEK_JPQL= "select t from DayReport t WHERE t.isCheck in (1,2,3)";
		List params = new ArrayList<Object>();
		if(centerId != -1){
			params.add(centerId);
			SELECT_INWEEK_JPQL = SELECT_INWEEK_JPQL+" AND t.centerId=?1";
		}
		SELECT_INWEEK_JPQL=SELECT_INWEEK_JPQL+" Order by t.subTime desc";
		List<DayReport> result=new ArrayList<>();

		Page allPage = re.getAllPage(SELECT_INWEEK_JPQL, params, pageSize, pageIndex);
		List<DayReport> findAllList = allPage.getList();
		if(null != findAllList && !findAllList.isEmpty()){
			result.add(findAllList.get(0));
			if(findAllList.size()>1){
				result.add(findAllList.get(1));
			}
			allPage.setList(result);
		}else{
			return null;
		}
		return allPage;
	}
	private final String SELECT_ONE_JPQL= "select t from DayReport t WHERE t.id = ?1";
	@Override
	public DayReport getReportById(Long id) {
		List params = new ArrayList<Object>();
		params.add(id);
		List<DayReport> list;
		try {
			list = re.findAllList(SELECT_ONE_JPQL, params);
			if(null != list && !list.isEmpty()){
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询详情+id="+id+"错误信息:"+e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public void updateCheckForBill(Long id, String checkIds, String checkNames) {
		String UPDATE_FORCHECK_JPQL="update dayReport o set o.isCheck=3,o.checkerId=?1,o.checkerName=?2 where o.id=?3";
		List  params = new ArrayList<Object>();
		params.add(checkIds);
		params.add(checkNames);
		params.add(id);
		re.update(UPDATE_FORCHECK_JPQL,params);
	}


}
