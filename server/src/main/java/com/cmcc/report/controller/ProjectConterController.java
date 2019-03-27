package com.cmcc.report.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.EmployeeIndex;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.ProjectConterService;
import com.cmcc.report.service.UserAndContentService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.Function;
import com.cmcc.report.util.JpushUtil;
import com.cmcc.report.util.JwtToken;
import com.cmcc.report.util.Page;

/**
 * 一般员工，项目推进
 * @author youzhiqing
 * @ClassName: ProjectConterController 
 * @Description: TODO
 * @date: 2018年6月5日 上午11:16:50
 */
@Controller
@RequestMapping(value="/projectConterController")
public class ProjectConterController {

	final Logger logger = LoggerFactory.getLogger(ProjectConterController.class);
	@Autowired
	private ProjectConterService projectConterService;
	@Autowired
	private UserService us;
	@Autowired
	private ContentService cs;
	@Autowired
	private UserAndContentService uacs;
	
	/**
	 * 一般员工项目
	 * @author youzhiqing
	 * @date 2018年6月5日上午11:28:59
	 * @param request
	 * @param response
	 * @param onlineBillId
	 * @return
	 */
	@RequestMapping(value = "homepageListForStaff", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> ordinaryStaffObject(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "centerId", required = true) int centerId) {
		try {
			String token = request.getHeader("jwtToken");// 获取用户登录凭证
			// 登陆效验
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
			//一般员工项目
			int pageIndex = Function.parseInt(request.getParameter("page"), 1);// 默认第一页
			int pageSize = Function.parseInt(request.getParameter("pageSize"), 10);// 默认每页10条
			Page<Content> contentPage = cs.getContentByCenternew(pageSize, pageIndex, centerId);
			//page转换为需要的格式
			EmployeeIndex contents = cs.changeFormat(contentPage,uid);
			//request.setAttribute("contents", contents);
			//List<Content> contents = projectConterService.ordinaryStaffObject(centerId);
			//推送清单发布消息
			return new ResultObject<Object>(0, "获取数据成功！", contentPage);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + centerId);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	
	/**
	 * 项目推进
	 * @author youzhiqing
	 * @date 2018年6月5日上午11:28:59
	 * @param request
	 * @param response
	 * @param onlineBillId
	 * @return
	 */
	@RequestMapping(value = "projectInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> prijectBoost(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "projectId", required = true) int projectId) {
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
			//查询单个项目
			Content content = (Content) projectConterService.prijectBoost(projectId);
			if(content.getTimeLimit() != null && !("").equals(content.getTimeLimit())){
				if(content.getTimeLimit().length()>10){
					String substring = content.getTimeLimit().substring(0,10);
					content.setTimeLimit(substring);
				}
			}
			if(content.getRemark() == null || ("").equals(content.getRemark())){
				content.setRemark("无");
			}
			request.setAttribute("content", content);
			return new ResultObject<Object>(0, "获取数据成功！", content);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + projectId);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	/**
	 * 点击关注
	 * @author youzhiqing
	 * @date 2018年6月5日上午11:28:59
	 * @param request
	 * @param response
	 * @param option	是否关注
	 * @param projectId 项目ID（内容ID）
	 * @return
	 */
	@RequestMapping(value = "projectFocus", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> focused(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "option", required = true) int option,
			@RequestParam(value = "projectId", required = true) int projectId) {
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
			//点击关注改变状态
			if(option==1){
				//关注
				projectConterService.isFocused(projectId);
				return new ResultObject<Object>(0, "关注成功！", null);
			}else if(option==0){
				//取消关注
				projectConterService.cancelFocused(projectId);
				return new ResultObject<Object>(0, "取消关注操作成功！", null);
			}else{
				return new ResultObject<Object>(-1, "option参数错误！", null);
			}
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + null);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @param centerId 中心ID
	 * @param token    权限认证（用户信息）
	 * @param type     标记已读 0:根据项目ID标记单条  1：标记中心下的所有项目
	 * @param origin   客户端身份
	 * @param projectId项目ID（内容ID）
	 * @return 标记已读
	 */
	@RequestMapping(value = "projectReadTag", method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> projectReadTag(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "centerId", required = true) int centerId,
			@RequestParam(value = "jwtToken", required = true) String token,
			@RequestParam(value = "type", required = true) int type,
			@RequestParam(value = "origin", required = true) String origin,
			@RequestParam(value = "projectId", required = true) int projectId) {
		try {
			// 登陆效验
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
			if(type == 0){//标记单条已读（删除关系表中相应数据）
				uacs.deleteUACByContentId(projectId,uid);
			}else{//标记多条已读
				uacs.deleteUACByCenterId(centerId,uid);
			}
			return new ResultObject<Object>(0, "操作成功！", null);
		} catch (Exception e) {
			logger.error("获取数据列表异常 centerId=" + null);
			return new ResultObject<Object>(-99, "服务器异常！");
		}
	}
	
}
