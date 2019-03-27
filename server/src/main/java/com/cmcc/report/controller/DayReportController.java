package com.cmcc.report.controller;


import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.*;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.DayReportService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author hwl
 */
@Controller
@RequestMapping("/api")
public class DayReportController {

	final Logger logger = LoggerFactory.getLogger(DayReportController.class);
	
	@Autowired
	private DayReportService re;
	@Autowired
	private UserService us;
	
	    /** 
	    * @Title: saveDayReportContent 保存信息1
	    * @Description: 保存信息 用于信息的新增和修改
	    * @param request
	    * @param  response
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/saveDayReportContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveDayReportContent(HttpServletRequest request, HttpServletResponse response,@RequestBody
			DayReport dayReport) {
		String token = request.getHeader("jwtToken");// 获取用户登录凭证
		Integer uid = 0;
		//登陆效验
		if(null == token){
			return new ResultObject<Object>(-2, "用户未登录！");
		}else{
			Map<String, Object> validToken = JwtToken.validToken(token);
			Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
			if(state != 0){
				return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
			}
 			Object object = validToken.get("data");
			Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
 			uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
		}
		dayReport.setCreater(uid);
		int saveRes = re.saveDayReportInfo(dayReport);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}
	/**
	 * @Title: updateReportContent 更新信息2
	 * @Description: 保存信息 用于信息的新增和修改
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/updateReportContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateReportContent(HttpServletRequest request,@RequestBody DayReport dayReport) {
		String token = request.getHeader("jwtToken");// 获取用户登录凭证
		Integer uid = 0;
		//登陆效验
		if(null == token){
			return new ResultObject<Object>(-2, "用户未登录！");
		}else{
			Map<String, Object> validToken = JwtToken.validToken(token);
			Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
			if(state != 0){
				return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
			}
			Object object = validToken.get("data");
			Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
			uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
		}
		DayReport reportById = re.getReportById(dayReport.getId());
		if(uid != Integer.valueOf(reportById.getCreater())){
				return new ResultObject<Object>(0, "权限不足，请联系创建人进行修改！");
			}
		reportById.setContent(dayReport.getContent());
		reportById.setTitle(dayReport.getTitle());
		int saveRes = re.updateDayReportInfo(reportById);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "更新成功！");
		} else {
			return new ResultObject<Object>(-1, "更新失败！");
		}
	}
	/**
	 * @Title: deleteReportInfo
	 * @Description: 信息删除 多个以逗号隔开
	 * @param request
	 * @param response
	 * @param contentId 信息Id
	 * @param  参数说明
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/deleteReportInfo",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deleteReportInfo(HttpServletRequest request,@RequestBody List<Long> ids) {
		try {
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			//登陆效验
			if(null == token){
				return new ResultObject<Object>(-2, "用户未登录！");
			}else{
				Map<String, Object> validToken = JwtToken.validToken(token);
				Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
				if(state != 0){
					return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
				}
			}
			re.deleteDayReportInfo(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(0, "删除成功！");
	}

	/**
	 * @Title: getDayReportList
	 * @Description: 内容查询分页方法
	 * @param  request
	 * @param  response
	 * @param  isCheck 清单审核状态  0 草稿  1 提交 2 发布  3 已阅  4 全部  5(查找已发布和已审阅的) 6 (除草稿之外)
	 * @param  pageIndex 当前页
	 * @param  pageSize  总条数
	 * @param  centerId  传 按中心Id查 不传 查所有
	 * @param  startTime  开始时间
	 * @param  endTime  结束时间
	 * @param  isWeek 获取本周 0 获取本周 其他获取历史
	 * @param  参数说明  时间不传查迄今为止的数据
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getDayReportList",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getDayReportList(HttpServletRequest request, HttpServletResponse response,
											@RequestParam(value = "startTime", required = false) String startTime,
											@RequestParam(value = "endTime", required = false) String endTime) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		Integer centerId = Function.parseInt(request.getParameter("centerId"), -1);// 默认为-1
		int isCheck = Function.parseInt(request.getParameter("isCheck"), -1);// 默认所有
		int isWeek = Function.parseInt(request.getParameter("isWeek"), 0);// 默认查找本周
		try {
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			//登陆效验
			Integer uid = 0;
			Integer role = 0;
			if(null == token){
				return new ResultObject<Object>(-2, "用户未登录！");
			}else{
				Map<String, Object> validToken = JwtToken.validToken(token);
				Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
				if(state != 0){
					return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
				}
				Object object = validToken.get("data");
				Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
				uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
				role = Integer.valueOf(String.valueOf(mObj.get("role")));
			}
			Page<DayReport> contentForWeekList = null;
			User user = us.getUser(uid);
			//确定isCheck参数
			if(-1 == isCheck){
				switch (role) {
					case 1:
						isCheck = 4;
						centerId = user.getCenterId();
						break;
					case 2:
						isCheck = 4;
						break;
					case 3:
						isCheck = 5;
						break;
					default:
						isCheck = 4;
						break;
				}
			}
			//默认查找本周
			if(isWeek == 0){
				contentForWeekList = re.getBillByTimeAndCenter(pageSize, pageIndex, isCheck, centerId);
				return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
			}
			String lastDayForWeek = DateUtil.getLastDayForWeek();
			if (null == startTime && null == endTime) {
				contentForWeekList = re.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek), centerId);
			} else if (null != startTime && null == endTime) {
				contentForWeekList = re.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek),centerId);
			} else if (null == startTime && null != endTime) {
				contentForWeekList = re.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId);
			} else {
				contentForWeekList =re.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId);
			}
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + "isCheck=" + isCheck + "endTime=" + endTime + "startTime="
					+ startTime);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: getDayReportById
	 * @Description:
	 * @return ResultObject<Object>    返回类型
				* @throws
	 */
		@RequestMapping(value="/getDayReportById",method = RequestMethod.POST)
		@ResponseBody
		public ResultObject<Object> getDayReportById(HttpServletRequest request,
				@RequestParam(value = "id", required = false) Long id) {
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
			DayReport dayReport = re.getReportById(id);
			if(dayReport!=null){
				return new ResultObject<Object>(0, "获取数据成功！",dayReport );

			}else{
				return new ResultObject<Object>(0, "获取数据为空！", null);

			}
		}catch (Exception e) {
				logger.error("获取数据列表异常");
				return new ResultObject<Object>(-99, "服务器异常！");
			}

	}
	/**
	 * @Title: getlateastReportInfo
	 * @Description: 内容查询分页方法
	 * @param  request
	 * @param  response
	 * @param  ischeck 是否审核 0 未审核 1 审核 2 所有
	 * @param  pageIndex 当前页
	 * @param  pageSize  总页数
	 * @param  isWeek  0 本周 1 历史
	 * @param  startTime  开始时间
	 * @param  endTime  结束时间
	 * @param  参数说明
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getlateastReportInfo",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getlateastReportInfo(HttpServletRequest request) {

		try {
			Integer uid = 0;
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			//登陆效验
			if(null == token){
				return new ResultObject<Object>(-2, "用户未登录！");
			}else{
				Map<String, Object> validToken = JwtToken.validToken(token);
				Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
				if(state != 0){
					return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
				}
				Object object = validToken.get("data");
				Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
				uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
			}
			List<DayReport> reportDetail = re.getDayReportInfoByNow();
			//String dayReportIds="";
		/*	Map<String,Object> resMap = new HashMap<>();
			for(DayReport dayReport:reportDetail){
				dayReportIds=dayReportIds+dayReport.getId()+",";
			}*/
			//dayReportIds=dayReportIds.substring(0,dayReportIds.length()-1);
			//resMap.put("dayReportIds", dayReportIds);
			return new ResultObject<Object>(0, "获取数据成功！", reportDetail);
		}catch (Exception e) {
			logger.error("获取数据列表异常");
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}

	/**
	 * @Title: getLastDayReport(移动端)
	 * @Description: 根据清单查询分页方法
	 * @param  request
	 * @param  response
	 * @param  centerId
	 * @param  pageIndex 当前页
	 * @param  pageSize  总页数
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getLastDayReport",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getLastDayReport(HttpServletRequest request, HttpServletResponse response,
														 @RequestParam(value = "centerId", required = false) Integer
																 centerId
	) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		try {
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			//登陆效验
			if(null == token){
				return new ResultObject<Object>(-2, "用户未登录！");
			}else{
				Map<String, Object> validToken = JwtToken.validToken(token);
				Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
				if(state != 0){
					return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
				}
			}
			Page<DayReport> contentForWeekList = null;
			contentForWeekList = re.selectContentsByCenterId(pageSize, pageIndex,centerId);
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: updatestatusStatusForCheck
	 * @Description: 修改状态
	 * @param @param request
	 * @param @param response
	 * @param @param userId 审阅人Id
	 * @param @param billId 清单Id
	 * @param @param userName 审阅人名称
	 * @param @return  参数说明
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/updatestatusStatusForCheck",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updatestatusStatusForCheck(HttpServletRequest request, HttpServletResponse response,
												   @RequestParam(value = "dayReportIds", required = true) String dayReportIds) {
		String token = request.getHeader("jwtToken");// 获取用户登录凭证
		//登陆效验
		Integer uid = 0;
		if(null == token){
			return new ResultObject<Object>(-2, "用户未登录！");
		}else{
			Map<String, Object> validToken = JwtToken.validToken(token);
			Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
			if(state != 0){
				return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
			}
			Object object = validToken.get("data");
			Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
			uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
		}
		User user = us.getUser(uid);
		String[] idArray = dayReportIds.split(",");

		for(int i=0;i<idArray.length;i++){
			Long id=Long.valueOf(idArray[i]);
			DayReport report=re.getReportById(id);
			if(null != report){
				String checkIds = report.getCheckerId();
				if(null != checkIds){
					String [] ids = checkIds.split(",");
					boolean contains = Arrays.asList(ids).contains(String.valueOf(uid));
					if(!contains){
						String saveCheckIds = report.getCheckerId()+","+uid;
						String saveCheckNames = report.getCheckerName()+","+user.getName();
						try {
							re.updateCheckForBill(id,saveCheckIds,saveCheckNames);
							return new ResultObject<Object>(0, "修改成功！");
						} catch (Exception e) {
							e.printStackTrace();
							return new ResultObject<Object>(-1, "修改异常");
						}
					}
				}else{
					re.updateCheckForBill(id,String.valueOf(user.getUserId()),user.getName());
					return new ResultObject<Object>(0, "修改成功！");
				}
			}
		}

		return new ResultObject<Object>(0, "不存在该清单，无需修改");
	}

	/**
	 * @Title: publishDayReport
	 * @Description: 清单发布
	 * @param @param request
	 * @param @param response
	 * @param @param billId 清单Id
	 * @param @return  参数说明
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value = "/publishDayReport", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> publishDayReport(HttpServletRequest request, HttpServletResponse response,
											@RequestBody List<Long> dayReportIdList ) {
		String token = request.getHeader("jwtToken");// 获取用户登录凭证
		// 登陆效验
		if (null == token) {
			return new ResultObject<Object>(-2, "用户未登录！");
		} else {
			Map<String, Object> validToken = JwtToken.validToken(token);
			Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
			if (state != 0) {
				return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
			}
		}
		try {
			re.publishDayReport(dayReportIdList);
			//推送清单发布消息
			try {
				List<User> ldList = us.getLdList();
				for (User resUser : ldList) {
					JpushUtil.TITLE = "新的日报";
					JpushUtil.ALERT = "您有新的日报详情没有查看！";
					JpushUtil.MSG_CONTENT = "";
					JpushUtil.TAG = "user_"+resUser.getUserId();
					JpushUtil.sendPush(JpushUtil.buildPushObject_android_and_iosByTag(JpushUtil.TAG,JpushUtil.TITLE,JpushUtil.MSG_CONTENT,JpushUtil.ALERT,null));
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("极光推送失败，错误信息："+e.getMessage());
			}
			return new ResultObject<Object>(0, "修改成功！");
		} catch (Exception e) {
			return new ResultObject<Object>(-1, "修改异常"+e.getMessage());
		}
	}
}
