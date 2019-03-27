package com.cmcc.report.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.Bill;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.model.VersionInfo;
import com.cmcc.report.service.BillService;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.AppPropertiesUtil;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.DownloadUtil;
import com.cmcc.report.util.Function;
import com.cmcc.report.util.JpushUtil;
import com.cmcc.report.util.JwtToken;
import com.cmcc.report.util.Page;

@Controller
@RequestMapping("/api")
public class BillController {
	final Logger logger = LoggerFactory.getLogger(BillController.class);
	@Autowired
	private ContentService cs;
	@Autowired
	private BillService bs;
	@Autowired
	private UserService us;


	    /**
	    * @Title: saveBill
	    * @Description: TODO(这里用一句话描述这个方法的作用)
	    * @param @param request
	    * @param @param response
	    * @param @param title 清单标题
	    * @param @param centerName 中心名称
	    * @param @param subTime 提交时间
	    * @param @param centerId 中心Id
	    * @param @param userId 审阅人Id
	    * @param @param userName 审阅名称
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/saveBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveBill(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "centerName", required = false) String centerName,
			@RequestParam(value = "subTime", required = true) String subTime,
			@RequestParam(value = "centerId", required = false) Integer centerId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "userName", required = false) String userName) {
		String billId = request.getParameter("billId");// 获取信息Id
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
		Bill bill = null;
		if (null == billId) {
			bill = new Bill(null, title, centerId, centerName, 0, DateUtil.parseDate(subTime, "yyyy/MM/dd HH:mm:ss"), userId, userName, 0,uid);
		} else {
			Bill resBill = bs.getBill(Long.valueOf(billId));
			if(uid != resBill.getCreater()){
				return new ResultObject<Object>(0, "权限不足，请联系创建人进行修改！");
			}
			bill = new Bill(Long.valueOf(billId), title, centerId, centerName, 0, DateUtil.parseDate(subTime, "yyyy/MM/dd HH:mm:ss"), userId, userName, 0,uid);
		}
		int saveRes = bs.saveBill(bill);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}


	    /**
	    * @Title: updateBillForCheck
	    * @Description: 修改清单状态
	    * @param @param request
	    * @param @param response
	    * @param @param userId 审阅人Id
	    * @param @param billId 清单Id
	    * @param @param userName 审阅人名称
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/updateBillForCheck",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateBillForCheck(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "billId", required = true) String billId,
			@RequestParam(value = "userName", required = false) String userName) {
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
		Bill resBill = bs.getBill(Long.valueOf(billId));
		if(null != resBill){
			String checkIds = resBill.getCheckerId();
			if(null != checkIds){
				String [] ids = checkIds.split(",");
				boolean contains = Arrays.asList(ids).contains(String.valueOf(uid));
				if(!contains){
					String saveCheckIds = resBill.getCheckerId()+","+uid;
					String saveCheckNames = resBill.getCheckerName()+","+user.getName();
					try {
						bs.updateCheckForBill(Long.valueOf(billId),saveCheckIds,saveCheckNames);
						return new ResultObject<Object>(0, "修改成功！");
					} catch (Exception e) {
						return new ResultObject<Object>(-1, "修改异常");
					}
				}else{
					return new ResultObject<Object>(0, "已存在");
				}
			}else{
				bs.updateCheckForBill(Long.valueOf(billId),String.valueOf(user.getUserId()),user.getName());
				return new ResultObject<Object>(0, "修改成功！");
			}
		}
		return new ResultObject<Object>(0, "不存在该清单，无需修改");
	}

    /**
	    * @Title: commitBill
	    * @Description: 清单提交
	    * @param @param request
	    * @param @param response
	    * @param @param billId 清单Id
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value = "/commitBill", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> coammitBill(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "billId", required = true) String billId) {
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
			bs.commitBill(Long.valueOf(billId));
			return new ResultObject<Object>(0, "修改成功！");
		} catch (Exception e) {
			return new ResultObject<Object>(-1, "修改异常");
		}
	}

	 /**
	    * @Title: publishBill
	    * @Description: 清单发布
	    * @param @param request
	    * @param @param response
	    * @param @param billId 清单Id
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value = "/publishBill", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> publishBill(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "billId", required = true) String billId) {
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
			bs.publishBill(Long.valueOf(billId));
			//推送清单发布消息
			try {
				List<User> ldList = us.getLdList();
				for (User resUser : ldList) {
					JpushUtil.TITLE = "新的清单";
					JpushUtil.ALERT = "您有一条新的清单没有查看！";
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
			return new ResultObject<Object>(-1, "修改异常");
		}
	}
	 /**
	    * @Title: selectBills
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
	@RequestMapping(value="/getBills",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> selectBills(HttpServletRequest request, HttpServletResponse response,
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
			Page<Bill> contentForWeekList = null;
			User user = us.getUser(uid);
			//确定isCheck参数
			if(-1 == isCheck){
				switch (role) {
				case 1:
					isCheck = 4;
					//centerId = user.getCenterId();
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
			//效验本周是否已生成清单，未生成则生成
			bs.initializeFirtBill(user);
			//默认查找本周
			if(isWeek == 0){
				contentForWeekList = bs.getBillByTimeAndCenter(pageSize, pageIndex, isCheck, centerId);
				return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
			}
			String lastDayForWeek = DateUtil.getLastDayForWeek();
			if (null == startTime && null == endTime) {
				contentForWeekList = bs.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek), centerId);
			} else if (null != startTime && null == endTime) {
				contentForWeekList = bs.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek),centerId);
			} else if (null == startTime && null != endTime) {
				contentForWeekList = bs.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId);
			} else {
				contentForWeekList = bs.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
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
	    * @Title: getContentByBill
	    * @Description: 根据清单查询分页方法
	    * @param  request
	    * @param  response
	    * @param  isImportant是否重要0 重要 1 不重要 2 所有
	    * @param  pageIndex 当前页
	    * @param  pageSize  总页数
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/getContentByBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> selectContentsByBillId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "centerId", required = false) Long centerId
			) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		Integer billId = Function.parseInt(request.getParameter("billId"), -1);// 默认为-1
		Integer isImportant = Function.parseInt(request.getParameter("isImportant"), 2);// 默认为2
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
			if(-1 == billId){
				return new ResultObject<Object>(-1, "billId参数缺失！");
			}
			User user = us.getUser(uid);
			Bill bill = bs.getBill(Long.valueOf(billId));
			Map<String,Object> resMap = new HashMap<>();
			resMap.put("billId", bill.getBillId());
			resMap.put("title", bill.getTitle());
			Page<Content> contentForWeekList = null;
			if(null == centerId){
				switch (role) {
				case 1:
					contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, isImportant, Long.valueOf(user.getCenterId()), billId);
					break;
	            case 2:
	            	contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, isImportant, null, billId);
					break;
				default:
					contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, isImportant, null, billId);
					break;
				}
			}else{
				contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, isImportant, centerId, billId);
			}

			//contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, isImportant, null, billId);
			resMap.put("data", contentForWeekList);
			return new ResultObject<Object>(0, "获取数据成功！", resMap);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + billId + "isCheck=");
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	    * @Title: getImportantBill 获取重点项目清单
	    * @Description: 内容查询分页方法
	    * @param  request
	    * @param  response
	    * @param  pageIndex 当前页
	    * @param  pageSize  总页数
	    * @param  参数说明  时间不传查迄今为止的数据
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */

	@RequestMapping(value="/getImportantBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getImportantBill(HttpServletRequest request, HttpServletResponse response
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
			Page<Bill> contentForWeekList = null;
			contentForWeekList = bs.getImportantBill(pageSize, pageIndex);
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常"+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}

	   /**
	    * @Title: deleteBill
	    * @Description: 信息删除 多个以逗号隔开
	    * @param request
	    * @param response
	    * @param contentId 信息Id
	    * @param  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/deleteBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deleteBill(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "billIds", required = true) String billIds) {
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
			bs.deleteTrue(billIds);
		} catch (Exception e) {
				e.printStackTrace();
		return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(0, "删除成功！");
	}

	 /**
	    * @Title:  updatePassWord
	    * @Description: 修改密码
	    * @param request
	    * @param response
	    * @param oldPass 老密码 newPass 新密码
	    * @param  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/updatePassWord",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updatePassWord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "oldPass", required = true) String oldPass,
			@RequestParam(value = "newPass", required = true) String newPass
			) {
		try {
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
			User user = us.getUser(uid);
			if(!oldPass.equals(user.getPassword())){
				return new ResultObject<Object>(1, "原始密码错误！");
			}
			if(newPass.equals(user.getPassword())){
				return new ResultObject<Object>(2, "新密码和原始不能相同");
			}
			int res = us.updateUser(newPass,uid);
			if(res > 0){
				return new ResultObject<Object>(0, "修改成功！");
			}
		} catch (Exception e) {
				e.printStackTrace();
		return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(3, "无法获取用户信息，请重试！");
	}
	//初始化用户密码

	 /**
	    * @Title:  updatePassWord
	    * @Description: 修改密码
	    * @param request
	    * @param response
	    * @param oldPass 老密码 newPass 新密码
	    * @param  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/initializePassWord",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> initializePassWord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "oldPass", required = false) String oldPass
			) {
		try {
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
			User user = us.getUser(uid);
			String account = user.getAccount();
			String newpass = "123456";//新密码默认为123456
			if(account.length() >= 6){
				newpass = account.substring(account.length()-6, account.length());
			}
			int res = us.updateUser(newpass,uid);
			if(res > 0){
				return new ResultObject<Object>(0, "初始化成功！");
			}
		} catch (Exception e) {
				e.printStackTrace();
		return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(3, "无法获取用户信息，请重试！");
	}
	/**
	    * @Title: getLastBillContent
	    * @Description: 根据清单查询分页方法
	    * @param  request
	    * @param  response
	    * @param  isImportant是否重要0 重要 1 不重要 2 所有
	    * @param  centerId 是否重要 0 重要 1 不重要 2 所有
	    * @param  pageIndex 当前页
	    * @param  pageSize  总页数
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/getLastBillContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getLastBillContent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "centerId", required = false) Long centerId
			) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		Integer isImportant = Function.parseInt(request.getParameter("isImportant"), 2);// 默认为2
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
			//获取最后一条 已阅或已提交的bill
			Bill bill = bs.getBillForLast();
			if(null == bill){
				return new ResultObject<Object>(1, "暂未符合要求的数据！");
			}
			Map<String,Object> resMap = new HashMap<>();
			resMap.put("title", bill.getTitle());
			resMap.put("billId", bill.getBillId());
			Page<Content> contentForWeekList = null;
			contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex,isImportant,centerId,Integer.valueOf(String.valueOf(bill.getBillId())));
			resMap.put("data", contentForWeekList);
			return new ResultObject<Object>(0, "获取数据成功！", resMap);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}

	@RequestMapping(value="/validateVersionInfo",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> validateVersionInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "platform", required = true) String platform,
			@RequestParam(value = "currentVersionCode", required = true) String currentVersionCode
			) {
		try {
			VersionInfo versionInf = null;
			String forcedVersion = "";
			if("a".equalsIgnoreCase(platform)){
				//Andriod
				forcedVersion = AppPropertiesUtil.getValue("androidForcedVersion");
				versionInf = bs.getVersionInf(0);
			}else{
				//查找ios
				forcedVersion = AppPropertiesUtil.getValue("iosForcedVersion");
				versionInf = bs.getVersionInf(1);
			}
	        //更新版本信息
			if (currentVersionCode.equals(versionInf.getVersionCode())) {
				versionInf.setUpdate("0");// 无需更新
				return new ResultObject<Object>(1, "获取数据成功！", versionInf);
			}
			int compareRes = forcedVersion.compareTo(currentVersionCode);
			if (compareRes >= 0) {
				versionInf.setUpdate("2");// 强制更新
			} else {
				versionInf.setUpdate("1");// 更新
			}
				return new ResultObject<Object>(1, "获取数据成功！", versionInf);

		} catch (Exception e) {
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @param request
	 * @author:huwl
	 * @time:
	 * @return:String
	 * @describe:导入
	 */
	@RequestMapping("/imporBatchRecharge")
	@ResponseBody
	public ResultObject imporBatchRecharge(HttpServletRequest request) {
//		String token = request.getHeader("jwtToken");// 获取用户登录凭证
//		Integer billId = 0;
//		Integer uid = 0;
		Integer centerId = 7;
		//登陆效验
		/*if (null == token) {
			return new ResultObject<Object>(-2, "用户未登录！");
		} else {
			Map<String, Object> validToken = JwtToken.validToken(token);
			Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
			if (state != 0) {
				return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
			}
			Object object = validToken.get("data");
			Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
			centerId = Integer.valueOf(String.valueOf(mObj.get("centerId")));
		}*/
		try {
			ResultObject<Object> a = cs.importProjectContent(request, centerId);
			if(a.getCode() == 0){
				return new ResultObject<Object>(0, "导入成功");
			}else{
				return a;
			}
		} catch (Exception e) {
			logger.error("导入数据报错imporBatchRecharge："+e+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
		
	}
	/**
	 * @param
	 * @author:
	 * @time:
	 * @return:String
	 * @describe:导出
	 */
	/*@RequestMapping("/exportBatchResult2")
	@ResponseBody
	public ResponseEntity<byte[]> exportBatchResult2(HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("dist/projectNew.xls");
		//String realPath = this.getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes","")+"dist/projectNew.xls";
		logger.error("文件下载路径realPath:"+realPath);
		ResponseEntity<byte[]> download = null;
		try {
			download = DownloadUtil.download(realPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return download;
	}*/
	
	/**
	 * @param
	 * @author:
	 * @time:
	 * @return:String
	 * @describe:导出
	 */
	@RequestMapping("/exportBatchResult")
	@ResponseBody
	public ResponseEntity<byte[]> exportBatchResult(HttpServletRequest request) {
		try {
			String filepath = this.getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes","")+"dist/projectNew.xls";
			String agent=request.getHeader("User-Agent").toLowerCase();
			if (request.getHeader("User-Agent").toUpperCase().indexOf("msie") > 0){//IE 10以及10以下
				ResponseEntity<byte[]> responseEntity = null;
				File downloadFile= new File(filepath);
				if(downloadFile.exists()){
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
					String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
					headers.setContentDispositionFormData("attachment", fileName);
					responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray( downloadFile), headers, HttpStatus.OK);
				}
				return responseEntity;
			}else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
				ResponseEntity<byte[]> responseEntity = null;
				File downloadFile= new File(filepath);
				if(downloadFile.exists()){
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
					String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
					headers.setContentDispositionFormData("attachment", fileName);
					responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray( downloadFile), headers, HttpStatus.OK);
				}
				return responseEntity;
			}else{//其他浏览器
				ResponseEntity<byte[]> download = null;
				try {
					download = DownloadUtil.download(filepath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return download;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping("/exportBatchResultAddress")
	@ResponseBody
	public String exportBatchResultAddress(HttpServletRequest request) {
		String realPath = this.getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes","")+"dist/projectNew.xls";
		return realPath;
	}
	
	
	@RequestMapping("/testDown")
	@ResponseBody
	public ResponseEntity<byte[]> testDown(HttpServletRequest request){
		try {
			String filepath = this.getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes","")+"dist/projectNew.xls";
			String agent=request.getHeader("User-Agent").toLowerCase();
			if (request.getHeader("User-Agent").toUpperCase().indexOf("msie") > 0){//IE 10以及10以下
				ResponseEntity<byte[]> responseEntity = null;
				File downloadFile= new File(filepath);
				if(downloadFile.exists()){
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
					String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
					headers.setContentDispositionFormData("attachment", fileName);
					responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray( downloadFile), headers, HttpStatus.OK);
				}
				return responseEntity;
			}else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
				ResponseEntity<byte[]> responseEntity = null;
				File downloadFile= new File(filepath);
				if(downloadFile.exists()){
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
					String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
					headers.setContentDispositionFormData("attachment", fileName);
					responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray( downloadFile), headers, HttpStatus.OK);
				}
				return responseEntity;
			}else{//其他浏览器
				ResponseEntity<byte[]> download = null;
				try {
					download = DownloadUtil.download(filepath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return download;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
