package com.cmcc.report.service;


import com.cmcc.report.model.OnlinePlanBill;
import com.cmcc.report.model.User;
import com.cmcc.report.util.Page;

import java.util.Date;

public interface OnLineBillService {
	public int saveOnLineBill(OnlinePlanBill onlinePlanBill);
	public void deleteOnLineBill(Long id);
	public int updateOnLineBill(OnlinePlanBill onlinePlanBill);
	public int publishOnLineBill(Long id);
	public OnlinePlanBill getlateastOnLineBill(Long id);

	void initializeFirtBill(User user,String flag);

	Page<OnlinePlanBill> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId, Integer type);

	Page<OnlinePlanBill> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date date, Date parse,
													Integer centerId,Integer type);

	OnlinePlanBill getBillForLast(Integer type);

	void updateCheckForBill(Long id, String saveCheckIds, String saveCheckNames);

}
