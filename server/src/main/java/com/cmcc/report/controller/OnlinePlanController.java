package com.cmcc.report.controller;


import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.OnlinePlanContent;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.OnlinePlanContentService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author HF
 */
@Controller
@RequestMapping("/api")
public class OnlinePlanController {

	final Logger logger = LoggerFactory.getLogger(OnlinePlanController.class);
	
	@Autowired
	private OnlinePlanContentService onlinePlanContentService;
	@Autowired
	private UserService us;
	
	    /** 
	    * @Title: saveOnlinePlanContent 保存信息
	    * @Description: 保存信息 用于信息的新增和修改
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/saveOnlinePlanContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveOnlinePlanContent(HttpServletRequest request,@RequestBody OnlinePlanContent onlinePlanContent) {
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
		onlinePlanContent.setUpdater(uid);
		int saveRes = onlinePlanContentService.saveOnlinePlanContent(onlinePlanContent);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}
	/**
	 * @Title: updateOnlinePlanContent 更新信息
	 * @Description: 保存信息 用于信息的新增和修改
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/updateOnlinePlanContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateOnlinePlanContent(HttpServletRequest request,@RequestBody OnlinePlanContent onlinePlanContent) {
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
		Long id = onlinePlanContent.getId();
		OnlinePlanContent onlinePlanContent1 = onlinePlanContentService.getOnlinePlanContent(onlinePlanContent.getId());
		if(uid != Integer.valueOf(onlinePlanContent1.getUpdater())){
			return new ResultObject<Object>(0, "权限不足，请联系创建人进行修改！");
		}
		onlinePlanContent.setUpdateTime(new Date());
		onlinePlanContent.setIscheck(onlinePlanContent1.getIscheck());
		int saveRes = onlinePlanContentService.updateOnlinePlanContent(onlinePlanContent);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "更新成功！");
		} else {
			return new ResultObject<Object>(-1, "更新失败！");
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
	@RequestMapping(value="/selectOnlineContents",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> selectOnlineContents(HttpServletRequest request, HttpServletResponse response,
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
				//contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
					//	DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new Date(),isGroup);
			} else if (null==startTime && null == endTime) {
				//contentForWeekList = onlinePlanContentService.getContentListForWeek(pageSize, pageIndex, isCheck,
					//	isWeek
						///DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new Date(),isGroup);
			} else if (null == startTime && null != endTime) {
				//contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
						//DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						//DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),isGroup);
			} else {
				//contentForWeekList = cs.getContentListForWeek(pageSize, pageIndex, isCheck, isWeek,
						//DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"),
					//	DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),isGroup);
			}
			return new ResultObject<Object>(0, "获取数据成功！", contentForWeekList);
		} catch (Exception e) {
			logger.error("获取数据列表异常 isWeek=" + isWeek + "isCheck=" + isCheck + "endTime=" + endTime + "startTime="
					+ startTime);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	
	    /** 
	    * @Title: deleteOlineContent
	    * @Description: 信息删除 多个以逗号隔开
	    * @param request
	    * @param response
	    * @param contentId 信息Id
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/deleteOnlineContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deleteOnlineContent(HttpServletRequest request, HttpServletResponse response,@RequestBody
			List<Long> contentIdList) {
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
			onlinePlanContentService.deleteOnlineContent(contentIdList);
		} catch (Exception e) {
				e.printStackTrace();
		return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(0, "删除成功！");
	}
	 /** 
	    * @Title: getOnlineContents
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
	@RequestMapping(value="/getOnlineContents",method = RequestMethod.POST)
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
			Page<Content> contentForWeekList = null;
			if (null == startTime && null == endTime) {
				contentForWeekList = onlinePlanContentService.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,DateUtil.parseDate
						("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"), new Date(), centerId);
			} else if (null != startTime && null == endTime) {
				contentForWeekList = onlinePlanContentService.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate(startTime, "yyyy/MM/dd HH:mm:ss"), new Date(),centerId);
			} else if (null == startTime && null != endTime) {
				contentForWeekList = onlinePlanContentService.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
						DateUtil.parseDate("1970/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss"),
						DateUtil.parseDate(endTime, "yyyy/MM/dd HH:mm:ss"),centerId);
			} else {
				contentForWeekList = onlinePlanContentService.getContentByTimeAndCenter(pageSize, pageIndex, isCheck,
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
	    * @Title: getOnlineContentByContentId
	    * @Description:根据内容Id获取内容
	    * @param  request
	    * @param  response
	    * @param  contentId
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/getOnlineContentByContentId",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getOnlineContentByContentId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "contentId", required = true) Long contentId) {
		try {
			OnlinePlanContent content = onlinePlanContentService.getOnlinePlanContent(contentId);
			if(null == content){
				return new ResultObject<Object>(0, "该内容不存在，请检查contenId="+contentId);
			}
			return new ResultObject<Object>(0, "获取数据成功！",content);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
}
