package com.cmcc.report.controller;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.Center;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.service.BillV2Service;
import com.cmcc.report.service.CenterService;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.UserAndContentService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.CaptchaUtil;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Function;
import com.cmcc.report.util.JpushUtil;
import com.cmcc.report.util.JwtToken;
import com.cmcc.report.util.Page;
import com.cmcc.report.util.UpLoadPropertiesUtil;


/**
 * @author HF
 */
@Controller
@RequestMapping("/api")
public class ReportController {

	final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private ContentService cs;
	@Autowired
	private BillV2Service billV2Service;
	@Autowired
	private UserService us;
	@Autowired
	private CenterService centerService;
	@Autowired
	private UserAndContentService userAndContentService;
	String picUrl="";
	
	    /** 
	    * @Title: saveContent 保存信息
	    * @Description: 保存信息 用于信息的新增和修改
	    * @param request
	    * @param  response
	    * @param  contentId 存在此参数时 用于修改内容 不存在时用于新增
	    * @param  endTime 截止日期
	    * @param  projectName 项目名称
	    * @param  workProject 工作项目
	    * @param  workContent 工作内容
	    * @param  cooperator 配合人
	    * @param  responsible 责任人
	    * @param  timeLimit 完成时限
	    * @param  isRisk 是否有风险 0 有 1 没有
	    * @param  riskSituation 风险情况
	    * @param  department 所属部门
	    * @param  center 所属中心
	    * @param  updater 最后修改人
	    * @param  Remark 备注 可以为空
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/saveContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveContent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "projectName", required = true) String projectName,
			@RequestParam(value = "billId", required = false) Integer billId,
			@RequestParam(value = "isImportant", required = true) Integer isImportant,
			@RequestParam(value = "workProject", required = true) String workProject,
			@RequestParam(value = "workContent", required = true) String workContent,
			@RequestParam(value = "overState", required = true) String overState,
			@RequestParam(value = "cooperator", required = true) String cooperator,
			@RequestParam(value = "responsible", required = true) String responsible,
			@RequestParam(value = "timeLimit", required = true) String timeLimit,
			@RequestParam(value = "isRisk", required = true) Integer isRisk,
			@RequestParam(value = "riskSituation", required = true) String riskSituation,
			@RequestParam(value = "centerName", required = true) String centerName,
			@RequestParam(value = "centerId", required = true) Integer centerId,
			@RequestParam(value = "updater", required = true) Integer updater,
			@RequestParam(value = "leaders", required = false) String leaders,
			@RequestParam(value = "selecttime", required = true) String selecttime,
			@RequestParam(value = "picture", required = false) String picture,
			@RequestParam(value = "remark", required = false) String remark) {
		String contentId = request.getParameter("contentId");// 获取信息Id
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
		Content content = null;
		if (null == contentId) {
			//content = new Content(null,billId,isImportant, projectName, workProject, workContent, overState, responsible, cooperator, timeLimit, isRisk, riskSituation, remark, centerName, centerId, 0, updater, new Date(),null,null,null);
			content = new Content();
			content.setBillId(-1);//清单ID
			content.setCenterId(centerId);//中心ID
			content.setCenterName(centerName);//中心名称
			content.setCooperator(cooperator);//合作者
			content.setRemark(remark);//备注
			content.setResponsible(responsible);//责任人
			content.setRiskSituation(riskSituation);//风险情况
			content.setTimeLimit(timeLimit);//完成时限
			content.setIscheck(0);//是否审核 默认未审核
			content.setIsDelete(0);//是否删除
			content.setIsFocused(0);//是否关注  默认未关注
			content.setIsImportant(isImportant);//是否重要
			content.setIsPublish(0);//未发布
			content.setIsRisk(isRisk);//是否有风险 0 有 1 没有
			content.setOverState(overState);//完成状况
			content.setProjectName(projectName);//项目名称
			content.setWorkContent(workContent);//工作内容
			content.setWorkProject(workProject);//工作项目
			content.setUpdater(updater);//修改人
			content.setLeaders(leaders);//@领导人
			content.setProjectType(1);//类型：1推进
			content.setSelecttime(selecttime);
			Date date = new Date();
			content.setUpdateTime(date);
			content.setPublishTime(date);
			content.setPicUrl(picture); //缩略图地址
		} else {
//			content = new Content(Long.valueOf(contentId),billId, isImportant, projectName, workProject, workContent, overState, responsible, cooperator, timeLimit, isRisk, riskSituation, remark, centerName, centerId, 0, updater, new Date(),null,null,null);
			content = new Content();
			content.setContentId(Long.valueOf(contentId));
			content.setBillId(-1);//清单ID
			content.setCenterId(centerId);//中心ID
			content.setCenterName(centerName);//中心名称
			content.setCooperator(cooperator);//合作者
			content.setRemark(remark);//备注
			content.setResponsible(responsible);//责任人
			content.setRiskSituation(riskSituation);//风险情况
			content.setTimeLimit(timeLimit);//完成时限
			content.setIscheck(0);//是否审核 默认未审核
			content.setIsDelete(0);//是否删除
			content.setIsFocused(0);//是否关注  默认未关注
			content.setIsImportant(isImportant);//是否重要
			content.setIsPublish(0);//未发布
			content.setIsRisk(isRisk);//是否有风险 0 有 1 没有
			content.setOverState(overState);//完成状况
			content.setProjectName(projectName);//项目名称
			content.setWorkContent(workContent);//工作内容
			content.setWorkProject(workProject);//工作项目
			content.setUpdater(updater);//修改人
			content.setLeaders(leaders);//@领导人
			content.setProjectType(1);//类型：1推进
			Date date = new Date();
			content.setUpdateTime(date);
			content.setPublishTime(date);
			content.setPicUrl(picture); //缩略图地址
			content.setSelecttime(selecttime);
		}
		int saveRes = cs.saveContent(content);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}
	
	@RequestMapping(value="/saveContentTemplate",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveContentTemplate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "projectName", required = true) String projectName,
			@RequestParam(value = "billId", required = false) Integer billId,
			@RequestParam(value = "isImportant", required = true) Integer isImportant,
			@RequestParam(value = "workProject", required = true) String workProject,
			@RequestParam(value = "workContent", required = true) String workContent,
			@RequestParam(value = "overState", required = true) String overState,
			@RequestParam(value = "cooperator", required = true) String cooperator,
			@RequestParam(value = "responsible", required = true) String responsible,
			@RequestParam(value = "timeLimit", required = true) String timeLimit,
			@RequestParam(value = "isRisk", required = true) Integer isRisk,
			@RequestParam(value = "riskSituation", required = true) String riskSituation,
			@RequestParam(value = "centerName", required = true) String centerName,
			@RequestParam(value = "centerId", required = true) Integer centerId,
			@RequestParam(value = "updater", required = true) Integer updater,
			@RequestParam(value = "leaders", required = false) String leaders,
			@RequestParam(value = "selecttime", required = true) String selecttime,
			@RequestParam(value = "picture", required = false) String picture,
			@RequestParam(value = "remark", required = false) String remark) {
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
		Content content = new Content();
		content = new Content();
		content.setBillId(-1);//清单ID
		content.setCenterId(centerId);//中心ID
		content.setCenterName(centerName);//中心名称
		content.setCooperator(cooperator);//合作者
		content.setRemark(remark);//备注
		content.setResponsible(responsible);//责任人
		content.setRiskSituation(riskSituation);//风险情况
		content.setTimeLimit(timeLimit);//完成时限
		content.setIscheck(0);//是否审核 默认未审核
		content.setIsDelete(0);//是否删除
		content.setIsFocused(0);//是否关注  默认未关注
		content.setIsImportant(isImportant);//是否重要
		content.setIsPublish(0);//未发布
		content.setIsRisk(isRisk);//是否有风险 0 有 1 没有
		content.setOverState(overState);//完成状况
		content.setProjectName(projectName);//项目名称
		content.setWorkContent(workContent);//工作内容
		content.setWorkProject(workProject);//工作项目
		content.setUpdater(updater);//修改人
		content.setLeaders(leaders);//@领导人
		content.setProjectType(1);//类型：1推进
		content.setSelecttime(selecttime);
		Date date = new Date();
		content.setUpdateTime(date);
		content.setPublishTime(date);
		content.setPicUrl(picture); //缩略图地址
		int saveRes = cs.saveContent(content);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}
	
	
	    /** 
	    * @Title: selectContents 
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
	@RequestMapping(value="/selectContents",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> selectContents(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		int isWeek = Function.parseInt(request.getParameter("isWeek"), 0);// 默认查找本周
		int isCheck = Function.parseInt(request.getParameter("isCheck"), 0);// 默认未审核
		int isGroup = Function.parseInt(request.getParameter("isGroup"), 0);// 默认不分组
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
			Page<Content> contentForWeekList = null;
			if (null == startTime && null == endTime) {
				contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new Date(),isGroup);
			} else if (null != startTime && null == endTime) {
				contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new Date(),isGroup);
			} else if (null == startTime && null != endTime) {
				contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),isGroup);
			} else {
				contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),isGroup);
			}
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 isWeek=" + isWeek + "isCheck=" + isCheck + "endTime=" + endTime + "startTime="
					+ startTime);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	
	    /** 
	    * @Title: deleteContent 
	    * @Description: 信息删除 多个以逗号隔开
	    * @param request
	    * @param response
	    * @param contentId 信息Id
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/deleteContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deleteContent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "contentIds", required = true) String contentIds) {
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
			cs.deleteContent(contentIds);
		} catch (Exception e) {
				e.printStackTrace();
		return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(0, "删除成功！");
	}
	
	
	    /** 
	    * @Title: login 
	    * @Description: 登录
	    * @param @param request
	    * @param @param response
	    * @param @param account
	    * @param @param password
	    * @param @return  参数说明
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loginMobile",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> loginMobile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "account", required = true) String account,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "validateCode", required = true) String validateCode
			) {
		try {
			String key = request.getHeader("randomString");
			/*HttpSession session = request.getSession();
			String key = (String) session.getAttribute("key");*/

			if(!key.equals(validateCode)){
				return new ResultObject<Object>(2, "验证码错误！");
			}
			User selectUser = us.selectUserForLogin(account, password);
			if(null == selectUser){
				return new ResultObject<Object>(1, "用户不存在或密码错误！");
			}
			ResultObject res=new ResultObject();
			//生成token
			String token = null;
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("uid", selectUser.getUserId());
			payload.put("role", selectUser.getRole());
			payload.put("centerId", selectUser.getCenterId());
			payload.put("centerName", selectUser.getCenter());
			token = JwtToken.createToken(payload);
			//JwtToken.validToken(token);
			res.setCode(0);
			res.setDesc("登录成功！");
			res.setObject(selectUser);
			res.setToken(token);
			return res;
		} catch (Exception e) {
				e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}

	}
	/**
	 * @Title: login
	 * @Description: 登录
	 * @param @param request
	 * @param @param response
	 * @param @param account
	 * @param @param password
	 * @param @return  参数说明
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> login(HttpServletRequest request, HttpServletResponse response,
									  @RequestParam(value = "account", required = true) String account,
									  @RequestParam(value = "password", required = true) String password,
									  @RequestParam(value = "validateCode", required = true) String validateCode
	) {
		try {
			HttpSession session = request.getSession();
			String key = (String) session.getAttribute("key");
			if(!key.equals(validateCode)){
				return new ResultObject<Object>(2, "验证码错误！");
			}
			User selectUser = us.selectUserForLogin(account, password);
			if(null == selectUser){
				return new ResultObject<Object>(1, "用户不存在或密码错误！");
			}
			ResultObject res=new ResultObject();
			//生成token
			String token = null;
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("uid", selectUser.getUserId());
			payload.put("role", selectUser.getRole());
			payload.put("centerId", selectUser.getCenterId());
			payload.put("centerName", selectUser.getCenter());
			token = JwtToken.createToken(payload);
			//JwtToken.validToken(token);
			res.setCode(0);
			res.setDesc("登录成功！");
			res.setObject(selectUser);
			res.setToken(token);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}

	}




	/**
	    * @Title: getUserByToken 
	    * @Description:根据token获取用户信息
	    * @param  request
	    * @param  response
	    * @param  token
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/getUserByToken",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getUserByToken(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(value = "jwtToken", required = true) String token) {
		try {
			return us.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	@RequestMapping(value="/getUserByTokenNew")
	@ResponseBody
	public ResultObject<Object> getUserByTokenNew(HttpServletRequest request, HttpServletResponse response) {
		try {
			String token = request.getParameter("jwtToken");
			return us.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	
	 /** 
	    * @Title: selectContents 
	    * @Description: 内容查询分页方法(新增去除按照周查询)
	    * @param  request
	    * @param  response
	    * @param  isCheck 是否审核 0 未审核 1 审核 2 所有
	    * @param  pageIndex 当前页
	    * @param  pageSize  总页数
	    * @param  centerId  传 按中心Id查 不传 查所有
	    * @param  startTime  开始时间
	    * @param  endTime  结束时间
	    * @param  参数说明  时间不传查迄今为止的数据
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/getContents",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> selectContentsNew(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		Integer centerId = Function.parseInt(request.getParameter("centerId"), -1);// 默认为-1
		int isCheck = Function.parseInt(request.getParameter("isCheck"), 2);// 默认未审核
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
			String centerName = "";
			try {
				if(centerId == -1){
					ResultObject<Object> userByToken = us.getUserByToken(token);
					User user = (User) userByToken.getObject();
					centerId = user.getCenterId();
					centerName = user.getCenter();
				}else{
					Center ct = centerService.getCenterByCenterId(centerId);
					centerName = ct.getCenterName();
				}
			} catch (Exception e) {
				centerId = -1;
			}
			Page<Content> contentForWeekList = null;
			if (null == startTime && null == endTime) {
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new Date(), centerId,centerName);
			} else if (null != startTime && null == endTime) {
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new Date(),centerId,centerName);
			} else if (null == startTime && null != endTime) {
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId,centerName);
			} else {
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId,centerName);
			}
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + "isCheck=" + isCheck + "endTime=" + endTime + "startTime="
					+ startTime);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	   /** 
	    * @Title: getContentByContentId 
	    * @Description:根据内容Id获取内容
	    * @param  request
	    * @param  response
	    * @param  contentId
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/getContentByContentId",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getContentByContentId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "contentId", required = true) Long contentId) {
		try {
			Content content = cs.getContentByContentId(contentId);
			if(null == content){
				return new ResultObject<Object>(0, "该内容不存在，请检查contenId="+contentId);
			}else{
				/*String stripHtml = billV2Service.stripHtml(content.getOverState());
				content.setOverState(stripHtml);*/
				//picurl
				if(content.getPicUrl() != null){
					content.setProjectImage(content.getPicUrl());
					content.setPicture(content.getPicUrl());
				}else{
					content.setPicUrl("");
					content.setProjectImage("");
					content.setPicture("");
				}
				if(content.getRemark() == null || ("").equals(content.getRemark())){
					content.setRemark("无");
				}
			}
			return new ResultObject<Object>(0, "获取数据成功！",content);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	/**
	 *获取验证码
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/captcha/{radomValue}")
	@ResponseBody
	public void  captcha(HttpServletRequest request, HttpServletResponse response, @PathVariable("radomValue") String radomValue)
			throws ServletException, IOException
	{
		CaptchaUtil.outputCaptcha(request, response,radomValue);
	}
	/**
	 *获取验证码
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/radomValue")
	@ResponseBody
	public Map<String,String>  radomValue(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String,String> map=new HashMap<>();
		String randomString = CaptchaUtil.getRandomString();
		map.put("randomString",randomString);
		return map;
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
	
	
	/** 
	    * @Title: selectContents 
	    * @Description: 微头条二期_推进清单分页查询
	    * @param  request
	    * @param  response
	    * @param  isCheck 是否审核 0 未审核 1 审核 2 所有
	    * @param  pageIndex 当前页
	    * @param  pageSize  总页数
	    * @param  centerId  传 按中心Id查 不传 查所有
	    * @param  startTime  开始时间
	    * @param  endTime  结束时间
	    * @param  参数说明  时间不传查迄今为止的数据
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/getContentsNew",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getContentsNew(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime) {
		int pageIndex = Function.parseInt(request.getParameter("pageIndex"), 1);// 默认第一页
		int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
		int isCheck = Function.parseInt(request.getParameter("isCheck"), 2);// 默认未审核
		Integer centerId = Function.parseInt(request.getParameter("centerId"), -1);// 默认为-1
		try {
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			String centerName = "";
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
			try {
				if(centerId == -1){
					ResultObject<Object> userByToken = us.getUserByToken(token);
					User user = (User) userByToken.getObject();
					centerId = user.getCenterId();
					centerName = user.getCenter();
				}else{
					Center ct = centerService.getCenterByCenterId(centerId);
					centerName = ct.getCenterName();
				}
			} catch (Exception e) {
				centerId = -1;
			}
			Page<Content> contentForWeekList = null;
			if (null == startTime && null == endTime) {
				startTime = this.preWeekMonday();
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new Date(), centerId, centerName);
			} else if (null != startTime && null == endTime) {
//				startTime = startTime + " 00:00:00";
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new Date(),centerId, centerName);
			} else if (null == startTime && null != endTime) {
				endTime = endTime.substring(0,10) + " 23:59:00";
				startTime = this.preWeekMonday();
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId, centerName);
			} else {
//				startTime = startTime  + " 00:00:00";
				endTime = endTime.substring(0,10) + " 23:59:00";
				contentForWeekList = cs.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId, centerName);
			}
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId + "isCheck=" + isCheck + "endTime=" + endTime + "startTime="
					+ startTime);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	public static Integer centerIdy = -9;
	public static Integer contentIdy = -9;
	public static Date datey = null;
	
	/**
	 * @param response
	 * @param request
	 * @param billId 发布内容ID
	 * @return 微头条二期_推进清单发布
	 */
	@RequestMapping(value="/publishContentsNew",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> publishContentsNew(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "contentId", required = true) String billId){
		try {
			long begintime = System.currentTimeMillis();
			if(billId.indexOf(",") != -1){
				billId = billId.substring(0,billId.length()-1);
			}
			String[] contentIds = billId.split(",");
			for (String contentId : contentIds) {
				datey = new Date();
				Content contentByContentId = cs.getContentByContentId(Long.parseLong(contentId));
				contentByContentId.setIsPublish(1);//修改状态为1
				contentByContentId.setPublishTime(datey);
				centerIdy = contentByContentId.getCenterId();//获取中心ID
				cs.saveContent(contentByContentId);//修改内容状态
				contentIdy = Integer.parseInt(contentId);
				//标记未读已读标记（添加）
				new Thread(){
					public void run(){
						try {
							//更新发布时间
							if(datey != null){
								centerService.updateCenterPublishTime(centerIdy,datey);//更新中心表中的发布时间
							}
							//标记是否已读
							if(centerIdy != -9 && contentIdy != -9){
								userAndContentService.addUACMany(centerIdy, contentIdy);
							}
						} catch (Exception e) {}
						//推送清单发布消息
						try {
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
						} catch (Exception e) {}
						System.out.println("标记完成");
					}
				}.start();
			}
			long endtime = System.currentTimeMillis();
			System.out.println("总发布时间"+(begintime-endtime));
			return new ResultObject<Object>(0, "发布成功！");
		} catch (Exception e) {
			logger.error("微头条二期_推进清单发布");
			return new ResultObject<Object>(-99, "服务器异常！");
		} 
	}
	
	/**
	 * @param response
	 * @param request
	 * @param billId 发布内容ID
	 * @return 微头条二期_推进清单发布(是否可以)
	 */
	@RequestMapping(value="/ispublishContentsNew",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> ispublishContentsNew(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "contentId", required = true) String contentId){
		try {
			int sum = 0;
			if(contentId.indexOf(",") != -1){
				contentId = contentId.substring(0,contentId.length()-1);
			}
			String[] contentIds = contentId.split(",");
			for (String cid : contentIds) {
				Content contentByContentId = cs.getContentByContentId(Long.parseLong(cid));
				Integer isPublish = contentByContentId.getIsPublish();
				sum = sum + isPublish;
			}
			if(sum == 0){
				return new ResultObject<Object>(0, "可以发布！");
			}else{
				return new ResultObject<Object>(1, "不可以发布");
			}
		} catch (Exception e) {
			logger.error("微头条二期_推进清单发布");
			return new ResultObject<Object>(-99, "服务器异常！");
		} 
	}
	
	/**
	 * @param response
	 * @param request
	 * @param billId 删除内容ID
	 * @return 微头条二期_推进清单批量删除
	 */
	@RequestMapping(value="/deletContentsManyNew",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deletContentsManyNew(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "contentId", required = true) String contentId){
		try {
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
			if(contentId.indexOf(",") != -1){
				contentId = contentId.substring(0,contentId.length()-1);
			}
			String[] contentIds = contentId.split(",");
			for (String cid : contentIds) {
				Content contentByContentId = cs.getContentByContentId(Long.parseLong(cid));
				contentByContentId.setIsDelete(1);//修改删除状态为1
				cs.saveContent(contentByContentId);//修改内容状态
			}
			return new ResultObject<Object>(0, "删除成功！");
		} catch (Exception e) {
			logger.error("微头条二期_推进清单批量删除报错contentId="+contentId+e+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		} 
	}
	
	
	/**
	 * @return 微头条二期_获取中心列表
	 */
	@RequestMapping(value="/getAllCenterNew",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getAllCenterNew(HttpServletResponse response,HttpServletRequest request){
		try {
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
			List<Center> centers = centerService.getAllCenter();
			return new ResultObject<Object>(0, "获取数据成功！", centers);
		} catch (Exception e) {
			logger.error("微头条二期_获取中心列表错误信息："+e+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	
	/**
	 * @param response
	 * @param request
	 * @param contentId 内容ID
	 * @return 微头条二期_推进清单查看
	 */
	@RequestMapping(value="/getContentsByIdNew",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getContentsByIdNew(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "contentId", required = true) String contentId){
		try {
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
			Content contentByContentId = cs.getContentByContentId(Long.parseLong(contentId));
			return new ResultObject<Object>(0, "获取数据成功！", contentByContentId);
		} catch (Exception e) {
			logger.error(" 微头条二期_推进清单查看(contentId="+contentId+")错误信息："+e+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	/**
	 * @param response
	 * @param request
	 * @param contentId
	 * @return 微头条二期_推进清单删除
	 */
	@RequestMapping(value="/deleteContentsByIdNew")
	@ResponseBody
	public ResultObject <Object> deleteContentsByIdNew(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "contentId",required = true) String contentId){
		try {
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
			Content contentByContentId = cs.getContentByContentId(Long.parseLong(contentId));
			contentByContentId.setIsDelete(1);
			int saveRes = cs.saveContent(contentByContentId);
			if (saveRes > 0) {
				return new ResultObject<Object>(0, "删除成功！");
			} else {
				return new ResultObject<Object>(-1, "删除失败！");
			}
		} catch (Exception e) {
			logger.error("微头条二期_推进清单删除(contentId="+contentId+")错误信息："+e+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	/**
	 * 微头条二期_项目推进清单图片上传
	 * 
	 * @param response
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/pictureUpLoad")
	@ResponseBody
	public ResultObject <Object> pictureUpLoad(HttpServletResponse response,HttpServletRequest request
			){
//		@RequestParam(value = "picture",required = false) MultipartFile filedata
		try {
			/*String token = request.getHeader("jwtToken");// 获取用户登录凭证
			// 登陆效验
			if (null == token) {
				return new ResultObject<Object>(-2, "用户未登录！");
			} else {
				Map<String, Object> validToken = JwtToken.validToken(token);
				Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
				if (state != 0) {
					return new ResultObject<Object>(-3, "用户效验失败,请重新登录！");
				}
			}*/
			MultipartHttpServletRequest multipart = (MultipartHttpServletRequest)request;
			MultipartFile filedata = multipart.getFile("file");
			
			//String  geturl ="";
			String filename="";
			UpLoadPropertiesUtil upu = new UpLoadPropertiesUtil("upload.properties");
			String filePath = (String) upu.getProperties().get("savePicUrl");
			String getPicUrl=(String) upu.getProperties().get("getPicUrl");
			
	            if(filedata != null && !filedata.isEmpty()){ 
	                //生成uuid作为文件名称  
	                String uuid = UUID.randomUUID().toString().replaceAll("-","");  
	                //获得文件类型（可以判断如果不是图片，禁止上传）  
	                String contentType=filedata.getContentType();  
	                //获得文件后缀名称  
	                String imageName=contentType.substring(contentType.indexOf("/")+1);  
	                //path="/uploadimg/"+uuid+"."+imageName; 
	                filename="uploadimg/"+uuid+"."+imageName;
	                File floder = new File(filePath+filename);
	                if (!floder.exists()) { //如果不存在 则创建   
	                	floder.mkdirs();  
	                } 
	                filedata.transferTo(floder);
	                picUrl=filename;  
	                
	            }  
				return new ResultObject<Object>(0, "上传成功！",getPicUrl+filename);
		} catch (Exception e) {
			logger.error("微头条二期_项目推进清单图片上传错误信息："+e+e.getMessage());
			return new ResultObject<Object>(-99, "服务器异常！");
		}
		
	}	
	
}
