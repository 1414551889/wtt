package com.cmcc.report.service;


import com.cmcc.report.model.*;
import com.cmcc.report.util.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface OnlineTraceContentService {

	public int saveOnlineTraceContent(OnlineTraceContent onlineTraceContent);
	public void deleteOnlineTraceContent(List<Long> idList);
	public int updateOnlineTraceContent(OnlineTraceContent onlineTraceContent);
	public OnlineTraceContent getOnlineTraceContent(Long id);
	Page<OnlineTraceContent> getContentByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date date, Date date1, Integer centerId);

	Page<OnlineTraceContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Integer centerId, Long
            billId);

    ResultObject importProjectContent(HttpServletRequest request, Long billId, User user);
}
