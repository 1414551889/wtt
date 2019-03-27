package com.cmcc.report.service;


import com.cmcc.report.model.DayReport;
import com.cmcc.report.util.Page;

import java.util.Date;
import java.util.List;

public interface DayReportService {
	public int saveDayReportInfo(DayReport dayReport);
	public void deleteDayReportInfo(List<Long> ids);
	public int updateDayReportInfo(DayReport dayReport);
	public List<DayReport> getDayReportInfoByNow();
	public void publishDayReport(List<Long>dayReportIdList);
	Page<DayReport> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId);
	Page<DayReport> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date date, Date parse, Integer centerId);

	List<DayReport> getBillForLast();

	Page<DayReport> selectContentsByCenterId(int pageSize, int pageIndex, Integer centerId);

	DayReport getReportById(Long id);

	void updateCheckForBill(Long id, String saveCheckIds, String saveCheckNames);
}
