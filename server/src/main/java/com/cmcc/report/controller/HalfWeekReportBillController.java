package com.cmcc.report.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.*;
import com.cmcc.report.service.BillService;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.HalfWeekReportBillService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api")
public class HalfWeekReportBillController {
	final Logger logger = LoggerFactory.getLogger(HalfWeekReportBillController.class);
	@Autowired
	private HalfWeekReportBillService halfWeekReportBillService;
	@Autowired
	private UserService us;


	 /**
	    * @Title: publishHalfBill
	    * @Description: 清单发布
	    * @param @param request
	    * @param @param response
	    * @param @param billId 清单Id
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型
	    * @throws
	    */
	@RequestMapping(value = "/publishHalfBill", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> publishHalfBill(HttpServletRequest request, HttpServletResponse response, @RequestBody
											List<Long> billList) {
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
			halfWeekReportBillService.publishHalfBill(billList);
			//推送清单发布消息
			try {
				List<User> ldList = us.getLdList();
				for (User resUser : ldList) {
					JpushUtil.TITLE = "新的清单";
					JpushUtil.ALERT = "您新的半周报没有查看！";
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
	 /**
	    * @Title: getHalfWeekReportBill获取所有清单,如果没有清单则自动生成
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
	@RequestMapping(value="/getHalfWeekReportBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getHalfWeekReportBill(HttpServletRequest request, HttpServletResponse response,
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
			Page<HalfWeekReportBill> contentForWeekList = null;
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
			halfWeekReportBillService.initializeFirtBill(user);
			//默认查找本周
			if(isWeek == 0){
				contentForWeekList = halfWeekReportBillService.getBillByTimeAndCenter(pageSize, pageIndex, isCheck, centerId);
				return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
			}
			String lastDayForWeek = DateUtil.getLastDayForWeek();
			if (null == startTime && null == endTime) {
				contentForWeekList = halfWeekReportBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek), centerId);
			} else if (null != startTime && null == endTime) {
				contentForWeekList = halfWeekReportBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd").parse(lastDayForWeek),centerId);
			} else if (null == startTime && null != endTime) {
				contentForWeekList = halfWeekReportBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId);
			} else {
				contentForWeekList = halfWeekReportBillService.getBillBeforeTimeAndCenter(pageSize, pageIndex, isCheck,
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
	 * @Title: getContentByBill,根据清单ID获取内容,查看功能
	 * @Description: 根据清单查询分页方法
	 * @param  request
	 * @param  response
	 * @param  pageIndex 当前页
	 * @param  pageSize  总页数
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getContentByHalfBill",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getContentByHalfBill(HttpServletRequest request, HttpServletResponse response,
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
			HalfWeekReportBill bill = halfWeekReportBillService.getHalfWeekReportBill(Long.valueOf(billId));
			Map<String,Object> resMap = new HashMap<>();
			resMap.put("billId", bill.getId());
			resMap.put("title", bill.getTitle());
			Page<HalfWeekContent> contentForWeekList = null;
			if(null == centerId){
				switch (role) {
					case 1:
						contentForWeekList = halfWeekReportBillService.selectContentsByBillIdAndCenterId(pageSize, pageIndex, user.getCenterId(), billId);
						break;
					case 2:
						contentForWeekList = halfWeekReportBillService.selectContentsByBillIdAndCenterId(pageSize, pageIndex, null, billId);
						break;
					default:
						contentForWeekList = halfWeekReportBillService.selectContentsByBillIdAndCenterId(pageSize, pageIndex, null, billId);
						break;
				}
			}else{
				contentForWeekList = halfWeekReportBillService.selectContentsByBillIdAndCenterId(pageSize, pageIndex, centerId,billId);
			}

			//contentForWeekList = cs.selectContentsByBillIdAndCenterId(pageSize, pageIndex, isImportant, null, billId);
			resMap.put("data", contentForWeekList);
			return new ResultObject<Object>(0, "获取数据成功！", resMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取数据列表异常 centerId=" + billId + "isCheck=");
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: getLastHalfBillContent(移动端)
	 * @Description: 根据清单查询分页方法
	 * @param  request
	 * @param  response
	 * @param  centerId
	 * @param  pageIndex 当前页
	 * @param  pageSize  总页数
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getLastHalfBillContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getLastHalfBillContent(HttpServletRequest request, HttpServletResponse response,
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
			//获取最后一条 已阅或已提交的bill
			List<HalfWeekReportBill> halfWeekReportBills = halfWeekReportBillService.getHalfBillByCheck();
			if(CollectionUtils.isEmpty(halfWeekReportBills)){
				return new ResultObject<Object>(1, "暂未符合要求的数据！");
			}
			List<Map<String,Object>> list=new ArrayList<>();
			List<Long>billList =new ArrayList<>();
			//String bills="";
			/*for(HalfWeekReportBill halfWeekReportBill:halfWeekReportBills){
				Map<String,Object> map = new HashMap<>();
				map.put("billId",halfWeekReportBill.getId());
				map.put("title",halfWeekReportBill.getTitle());
				contentForWeekList = halfWeekReportBillService.selectContentsByBillListAndCenterId(pageSize, pageIndex,
						centerId,billList);
				bills=bills+halfWeekReportBill.getId()+",";
				billList.add(halfWeekReportBill.getId());
			}*/
			//bills=bills.substring(0,bills.length()-1);
			HalfWeekReportBill halfWeekReportBill=halfWeekReportBills.get(0);
			billList.add(halfWeekReportBill.getId());
			Map<String,Object> resMap = new HashMap<>();
			resMap.put("billId", halfWeekReportBill.getId());
			resMap.put("title", halfWeekReportBill.getTitle());
			Page<HalfWeekContent> contentForWeekList = null;
			contentForWeekList = halfWeekReportBillService.selectContentsByBillListAndCenterId(pageSize, pageIndex,
					centerId,billList);
			resMap.put("data", contentForWeekList);
			return new ResultObject<Object>(0, "获取数据成功！", resMap);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 * @Title: updateHalfForCheck
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
	@RequestMapping(value="/updateHalfForCheck",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateHalfForCheck(HttpServletRequest request, HttpServletResponse response,
												   @RequestParam(value = "billIds", required = true) String billIds
												  ) {
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
		String[] idArray = billIds.split(",");

		for(int i=0;i<idArray.length;i++){
			Long id=Long.valueOf(idArray[0]);
			HalfWeekReportBill resBill = halfWeekReportBillService.getHalfWeekReportBill(Long.valueOf(id));
			if(null != resBill){
				String checkIds = resBill.getCheckerId();
				if(null != checkIds){
					String [] ids = checkIds.split(",");
					boolean contains = Arrays.asList(ids).contains(String.valueOf(uid));
					if(!contains){
						String saveCheckIds = resBill.getCheckerId()+","+uid;
						String saveCheckNames = resBill.getCheckerName()+","+user.getName();
						try {
							halfWeekReportBillService.updateCheckForBill(id,saveCheckIds,saveCheckNames);
							return new ResultObject<Object>(0, "修改成功！");
						} catch (Exception e) {
							return new ResultObject<Object>(-1, "修改异常");
						}
					}else{
						return new ResultObject<Object>(0, "已存在");
					}
				}else{
					halfWeekReportBillService.updateCheckForBill(id,String.valueOf(user.getUserId()),user.getName());
					return new ResultObject<Object>(0, "修改成功！");
				}
			}
		}

		return new ResultObject<Object>(0, "不存在该清单，无需修改");
	}
}
