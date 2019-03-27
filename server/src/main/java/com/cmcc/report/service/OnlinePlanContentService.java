package com.cmcc.report.service;


import com.cmcc.report.model.Content;
import com.cmcc.report.model.OnlinePlanContent;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.util.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface OnlinePlanContentService {

	public int saveOnlinePlanContent(OnlinePlanContent onlinePlanContent);
	public void deleteOnlineContent(List<Long> idList);
	public int updateOnlinePlanContent(OnlinePlanContent onlinePlanContent);
	public int deplayOnlinePlanContent(OnlinePlanContent onlinePlanContent);
	public OnlinePlanContent getOnlinePlanContent(Long id);
	Page<Content> getContentByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date date, Date date1, Integer centerId);

	Page<OnlinePlanContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Integer centerId, Long
			billId);

    ResultObject importProjectContent(HttpServletRequest request, Long billId, User user);
}
