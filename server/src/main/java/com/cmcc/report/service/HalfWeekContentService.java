package com.cmcc.report.service;

import com.cmcc.report.model.*;
import com.cmcc.report.util.Page;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public interface HalfWeekContentService {
	int saveHalfWeekContent(HalfWeekContent halfWeekContent);
	int updateHalfWeekContentInfo(HalfWeekContent halfWeekContent);
	HalfWeekContent getHalfWeekContentById(Long aLong);
	void deleteHalfWeekContent(List<String> idList);
	Page<HalfWeekContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Long aLong) throws ParseException;

	ResultObject importHalfWeekContent(HttpServletRequest request, Long billId,User user);
}
