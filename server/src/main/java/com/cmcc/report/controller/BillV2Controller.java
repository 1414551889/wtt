package com.cmcc.report.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.dao.BillDao;
import com.cmcc.report.model.Bill;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.DayReport;
import com.cmcc.report.model.LeaderIndex;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.service.BillService;
import com.cmcc.report.service.BillV2Service;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.JwtToken;

/**
 * 项目清单
 * 
 * @author liukesen
 *
 */
@Controller
@RequestMapping("/homepageListForLeader")
public class BillV2Controller {
	final Logger logger = LoggerFactory.getLogger(BillV2Controller.class);
	
	@Autowired
	private BillV2Service  billV2Service;
	
	
	@RequestMapping("/leadPage")
	@ResponseBody
	public ResultObject<Object> getLeadPage(HttpServletRequest request) {
		try {
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			//登陆效
			Integer uid = 0;
			Integer role = 0;
			if (null == token) {
				return new ResultObject<Object>(-2, "用户未登录！");
			} else {
				Map<String, Object> validToken = JwtToken.validToken(token);
				Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
				if (state != 0) {
					return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
				}
				Object object = validToken.get("data");
				Map mObj = JSON.parseObject(String.valueOf(object), Map.class);
				uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
				role = Integer.valueOf(String.valueOf(mObj.get("role")));
				}	
//				List<List<Content>> billForInWeek = billV2Service.getBillForInWeek();
				String preWeekMonday = this.preWeekMonday();
				List<LeaderIndex> billForInWeek = billV2Service.getBillForInWeek(uid,DateUtil.parseDate(preWeekMonday, "yyyy/MM/dd HH:mm:ss"),new Date());
				return new ResultObject<Object>(0, "获取数据成功！",billForInWeek);
			}catch (Exception e) {
				logger.error("获取数据列表异常");
				return new ResultObject<Object>(-99, "服务器异常！");
				}
		}
	
	
	/**
	 * @return 微头条二期_上周一时间字符串
	 */
	public String preWeekMonday(){
		Calendar preWeekMondayCal = Calendar.getInstance();
		//设置时间成本周第一天(周日)
		preWeekMondayCal.set(Calendar.DAY_OF_WEEK,1);
		//上周一时间
		preWeekMondayCal.add(Calendar.DATE, -6);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//转化为日期
		Date preWeekMonday = preWeekMondayCal.getTime();
		return sdf.format(preWeekMonday);
	}

}
