package com.cmcc.report.service;

import com.cmcc.report.model.*;
import com.cmcc.report.util.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


public interface HalfWeekReportBillService {
	public int saveHalfWeekReportBill(HalfWeekReportBill halfWeekReportBill);
		//查询订单详情
	public HalfWeekReportBill getHalfWeekReportBill(Long billId);
		//清单发布
	public void publishHalfBill(List<Long> idList) ;

	void initializeFirtBill(User user) throws ParseException;

	Page<HalfWeekReportBill> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date date, Date parse, Integer
			centerId);

	Page<HalfWeekReportBill> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId);

    List<HalfWeekReportBill> getHalfBillByCheck();

	Page<HalfWeekContent> selectContentsByBillListAndCenterId(int pageSize, int pageIndex, Integer centerId, List<Long>
			billList);

	Page<HalfWeekContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Integer centerId, Long billId);

	void updateCheckForBill(Long id, String s, String name);

}
