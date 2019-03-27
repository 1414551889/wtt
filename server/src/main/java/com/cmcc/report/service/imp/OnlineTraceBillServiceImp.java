package com.cmcc.report.service.imp;

import com.cmcc.report.dao.OnlineBillDao;
import com.cmcc.report.dao.OnlineTraceBillDao;
import com.cmcc.report.model.OnlinePlanBill;
import com.cmcc.report.model.OnlineTraceBill;
import com.cmcc.report.model.User;
import com.cmcc.report.service.OnLineBillService;
import com.cmcc.report.service.OnLineTraceBillService;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("onlineTraceBillService")
public class OnlineTraceBillServiceImp implements OnLineTraceBillService {
	private static Logger logger = LoggerFactory.getLogger(OnlineTraceBillServiceImp.class);

	@Autowired
	private OnlineTraceBillDao onlineTraceBillDao;
	@Override
	public int saveOnLineTraceBill(OnlineTraceBill onlinePlanBill) {
		OnlineTraceBill save = null;
		try {
			save = onlineTraceBillDao.save(onlinePlanBill);
		} catch (Exception e) {
			logger.error("保存报错"+e.getMessage());
		}
		if(save != null){
			return 1;
		}
		return 0;
	}

	@Override
	public void deleteOnLineTraceBill(Long id) {

	}

	@Override
	public int updateOnLineTraceBill(OnlineTraceBill onlineTraceBill) {
		return 0;
	}

	@Override
	@Transactional
	public int publishOnLineTraceBill(Long id) {
		String UPDATE_FORCHECK_JPQL="update onlinetracebill o set o.isCheck=2 where o.billId=?1";
		List  params = new ArrayList<Object>();
		params.add(id);
		onlineTraceBillDao.update(UPDATE_FORCHECK_JPQL,params);
		return 0;
	}
	private final String SELECT_ONE_JPQL= "select t from OnlineTraceBill t WHERE t.id = ?1";
	@Override
	public OnlineTraceBill getlateastOnLineTraceBill(Long id) {
			List params = new ArrayList<Object>();
			params.add(id);
			List<OnlineTraceBill> list;
			try {
				list = onlineTraceBillDao.findAllList(SELECT_ONE_JPQL, params);
				if(null != list && !list.isEmpty()){
					return list.get(0);
				}
			} catch (Exception e) {
				logger.error("查询订单详情+billId="+id+"错误信息:"+e.getMessage());
				return null;
			}
			return null;

}
	@Override
	public void initializeFirtBill(User creater) {
		//判断本周是否有清单 没有则保存
		OnlineTraceBill billForInWeek = this.getBillForInWeek();
			if(null == billForInWeek){
				OnlineTraceBill bill = new OnlineTraceBill();
				try {
					//获取本周最后一天
					String lastDayForWeek = DateUtil.getLastDayForWeek();
					bill.setTitle("上线追踪"+lastDayForWeek);
					bill.setCreater(creater.getUserId());
					//bill.setCenterId(creater.getCenterId());
					//bill.setCenterName(creater.getCenter());
					bill.setIsCheck(0);
					bill.setIsDelete(0);
					bill.setSubTime(new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek));
					this.saveOnLineTraceBill(bill);
				} catch (Exception e) {
					logger.error("清单新增错误信息:" + e.getMessage());
				}
			}
		}

	@Override
	public Page<OnlineTraceBill> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId) {
		String SELECT_INWEEK_JPQL= "select t from OnlineTraceBill t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = " +
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
		return onlineTraceBillDao.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);

	}

	@Override
	public Page<OnlineTraceBill> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date beginTime,Date endTime,
											  Integer centerId) {
		String SELECT_BEFOREWEEK_JPQL= "select t from OnlineTraceBill t WHERE t.subTime between ?1 AND ?2 AND t.isDelete =" +
				" 0";
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
		return onlineTraceBillDao.getAllPage(SELECT_BEFOREWEEK_JPQL,params,pageSize,pageIndex);

	}

	@Override
	public OnlineTraceBill getBillForLast() {
		String SELECT_FORINWEEK_JPQL= "select t from OnlineTraceBill t WHERE t.isCheck in (1,2,3) Order by t.subTime desc";
		try {
			List  params = new ArrayList<Object>();
			List<OnlineTraceBill> findAllList = onlineTraceBillDao.findAllList(SELECT_FORINWEEK_JPQL, params);
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

	@Override
	public void updateCheckForBill(Long id, String checkIds, String checkNames) {
		String UPDATE_FORCHECK_JPQL="update onlinetracebill o set o.isCheck=3,o.checkerId=?1,o.checkerName=?2 where o.billId=?3";
		List  params = new ArrayList<Object>();
		params.add(checkIds);
		params.add(checkNames);
		params.add(id);
		onlineTraceBillDao.update(UPDATE_FORCHECK_JPQL,params);
	}

	public OnlineTraceBill getBillForInWeek() {
			String SELECT_FORINWEEK_JPQL= "select t from OnlineTraceBill t WHERE YEARWEEK(date_format(t.subTime,'%Y-%m-%d')) = YEARWEEK(now()) AND t.isDelete = 0";
			try {
				List  params = new ArrayList<Object>();
				List<OnlineTraceBill> findAllList = onlineTraceBillDao.findAllList(SELECT_FORINWEEK_JPQL, params);
				if(null != findAllList && !findAllList.isEmpty()){
					return findAllList.get(0);
				}else{
					return null;
				}
			} catch (Exception e) {
				//logger.error("错误信息:" + e.getMessage());
				return null;
			}
		}
}
