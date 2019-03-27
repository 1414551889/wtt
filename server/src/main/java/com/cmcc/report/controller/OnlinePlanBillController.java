package com.cmcc.report.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.*;
import com.cmcc.report.service.*;
import com.cmcc.report.service.imp.OnlinePlanContentServiceImp;
import com.cmcc.report.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class OnlinePlanBillController {
	final Logger logger = LoggerFactory.getLogger(OnlinePlanBillController.class);
	@Autowired
	private OnlinePlanContentService cs;
	@Autowired
	private OnLineBillService onLineBillService;
	@Autowired
	private UserService us;


	 /**
	    * @Title: publishOnlineBill
	    * @Description: 清单发布
	    * @param @param request
	    * @param @param response
	    * @param @param billId 清单Id
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value = "/publishOnlineBill", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> publishOnlineBill(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "onlineBillId", required = true) String onlineBillId) {
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
			onLineBillService.publishOnLineBill(Long.valueOf(onlineBillId));
			//推送清单发布消息
			try {
				List<User> ldList = us.getLdList();
				for (User resUser : ldList) {
					JpushUtil.TITLE = "新的清单";
					JpushUtil.ALERT = "您有一条新的上线计划清单没有查看！";
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
	    * @Title: getOnlineBills
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
	  *   @param  flag 1代表上线计划,2代表上线报告
	    * @param  参数说明  时间不传查迄今为止的数据
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value="/getOnlineBills",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getOnlineBills(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, @RequestParam(value = "flag", required =
			false) String flag) {
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
			Page<OnlinePlanBill> contentForWeekList = null;
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
			onLineBillService.initializeFirtBill(user,flag);
			//默认查找本周
			if(isWeek == 0){
				contentForWeekList = onLineBillService.getBillByTimeAndCenter(pageSize, pageIndex, isCheck, centerId,
						Integer.valueOf(flag));
				return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
			}
			String lastDayForWeek = DateUtil.getLastDayForWeek();
			if (null == startTime && null == endTime) {
				contentForWeekList = onLineBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek), centerId,Integer.valueOf(flag));
			} else if (null != startTime && null == endTime) {
				contentForWeekList = onLineBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd")
								.parse(lastDayForWeek),centerId,Integer.valueOf(flag));
			} else if (null == startTime && null != endTime) {
				contentForWeekList = onLineBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId,Integer.valueOf(flag));
			} else {
				contentForWeekList = onLineBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId,Integer.valueOf(flag));
			}
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + "isCheck=" + isCheck + "endTime=" + endTime + "startTime="
					+ startTime);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: getOnlineContentByBill
	 * @Description: 根据清单查询分页方法
	 * @param  request
	 * @param  response
	 * @param  isImportant是否重要0 重要 1 不重要 2 所有
	 * @param  pageIndex 当前页
	 * @param  pageSize  总页数
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getOnlineContentByBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getOnlineContentByBill(HttpServletRequest request, HttpServletResponse response,
													   @RequestParam(value = "centerId", required = false) Integer centerId
	) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		Long billId = Function.parseLong(request.getParameter("billId"), -1);// 默认为-1
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
			OnlinePlanBill onlinePlanBill = onLineBillService.getlateastOnLineBill(Long.valueOf(billId));
			Map<String,Object> resMap = new HashMap<>();
			resMap.put("billId", onlinePlanBill.getId());
			resMap.put("title", onlinePlanBill.getTitle());
			Page<OnlinePlanContent> contentForWeekList = null;
			if(null == centerId){
				switch (role) {
					case 1:
						contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex,
								user.getCenterId(), billId);
						break;
					case 2:
						contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, null, billId);
						break;
					default:
						contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, null, billId);
						break;
				}
			}else{
				contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, centerId, billId);
			}

			contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, null, billId);
			resMap.put("data", contentForWeekList);
			return new ResultObject<Object>(0, "获取数据成功！", resMap);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + billId + "isCheck=");
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: getLastOnlineBillContent(移动端)
	 * @Description: 根据清单查询分页方法
	 * @param  request
	 * @param  response
	 * @param  centerId
	 * @param  pageIndex 当前页
	 * @param  pageSize  总页数
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getLastOnlineBillContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getLastOnlineBillContent(HttpServletRequest request, HttpServletResponse response,
												   @RequestParam(value = "centerId", required = false) Integer
														   centerId,  @RequestParam(value = "flag", required = false)
																	 Integer flag
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
			//获取最后一条 已阅或已提交的bill
			OnlinePlanBill bill = onLineBillService.getBillForLast(flag);
			if(null == bill){
				return new ResultObject<Object>(1, "暂未符合要求的数据！");
			}
			Map<String,Object> resMap = new HashMap<>();
			resMap.put("title", bill.getTitle());
			resMap.put("billId", bill.getId());
			Page<OnlinePlanContent> contentForWeekList = null;
			contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex,centerId,Long.valueOf
					(String.valueOf(bill.getId())));
			resMap.put("data", contentForWeekList);
			return new ResultObject<Object>(0, "获取数据成功！", resMap);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: updateOnlineBillForCheck
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
	@RequestMapping(value="/updateOnlineBillForCheck",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateOnlineBillForCheck(HttpServletRequest request, HttpServletResponse response,
												   @RequestParam(value = "billId", required = true) String billId) {
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
		OnlinePlanBill resBill = onLineBillService.getlateastOnLineBill(Long.valueOf(billId));
		if(null != resBill){
			String checkIds = resBill.getCheckerId();
			if(null != checkIds){
				String [] ids = checkIds.split(",");
				boolean contains = Arrays.asList(ids).contains(String.valueOf(uid));
				if(!contains){
					String saveCheckIds = resBill.getCheckerId()+","+uid;
					String saveCheckNames = resBill.getCheckerName()+","+user.getName();
					try {
						onLineBillService.updateCheckForBill(Long.valueOf(billId),saveCheckIds,saveCheckNames);
						return new ResultObject<Object>(0, "修改成功！");
					} catch (Exception e) {
						return new ResultObject<Object>(-1, "修改异常");
					}
				}else{
					return new ResultObject<Object>(0, "已存在");
				}
			}else{
				onLineBillService.updateCheckForBill(Long.valueOf(billId),String.valueOf(user.getUserId()),user.getName());
				return new ResultObject<Object>(0, "修改成功！");
			}
		}
		return new ResultObject<Object>(0, "不存在该清单，无需修改");
	}
	/**
	 * @param request
	 * @author:huwl
	 * @time:
	 * @return:String
	 * @describe:导入
	 */
	@RequestMapping("/imporOnlineBatchRecharge")
	@ResponseBody
	public ResultObject imporOnlineBatchRecharge(HttpServletRequest request, @RequestParam(value = "billId", required =
			false)  Long billId) {
		String token = request.getHeader("jwtToken");// 获取用户登录凭证
		Integer uid = 0;
		//登陆效验
		if (null == token) {
			return new ResultObject<Object>(-2, "用户未登录！");
		} else {
			Map<String, Object> validToken = JwtToken.validToken(token);
			Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
			if(state != 0){
				return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
			}
			Object object = validToken.get("data");
			Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
			uid = Integer.valueOf(String.valueOf(mObj.get("uid")));
			User user = us.getUser(uid);
			ResultObject result = cs.importProjectContent(request, billId,user);
			return new ResultObject<Object>(0, "导入成功");

		}
	}
	/**
	 * @param
	 * @author:
	 * @time:
	 * @return:String
	 * @describe:导出
	 */
	@RequestMapping("/exportOnlineBatchResult")
	@ResponseBody
	public ResponseEntity<byte[]> exportOnlineBatchResult(HttpServletRequest request) {
		String path = this.getClass().getClassLoader().getResource("/").getPath().replace("WEB-INF/classes","");
		ResponseEntity<byte[]> download = null;
		try {
			download = DownloadUtil.download(path+"dist/onlineplan.xls");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return download;
	}
}
