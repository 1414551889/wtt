package com.cmcc.report.service;


import com.cmcc.report.model.OnlineTraceBill;
import com.cmcc.report.model.User;
import com.cmcc.report.util.Page;

import java.util.Date;

public interface OnLineTraceBillService {
	public int saveOnLineTraceBill(OnlineTraceBill onlineTraceBill);
	public void deleteOnLineTraceBill(Long id);
	public int updateOnLineTraceBill(OnlineTraceBill onlineTraceBill);
	public int publishOnLineTraceBill(Long id);
	public OnlineTraceBill getlateastOnLineTraceBill(Long id);

	void initializeFirtBill(User user);

	Page<OnlineTraceBill> getBillByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Integer centerId);

	Page<OnlineTraceBill> getBillBeforeTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date date, Date parse, Integer centerId);

	OnlineTraceBill getBillForLast();

	void updateCheckForBill(Long id, String saveCheckIds, String saveCheckNames);

}
