package com.cmcc.report.controller;


import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.OnlinePlanContent;
import com.cmcc.report.model.OnlineTraceContent;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.service.OnlinePlanContentService;
import com.cmcc.report.service.OnlineTraceContentService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Function;
import com.cmcc.report.util.JwtToken;
import com.cmcc.report.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author HF
 */
@Controller
@RequestMapping("/api")
public class OnlineTraceContentController {

	final Logger logger = LoggerFactory.getLogger(OnlineTraceContentController.class);
	
	@Autowired
	private OnlineTraceContentService onlineTraceContentService;
	@Autowired
	private UserService us;
	
	    /** 
	    * @Title: saveOnlineTraceContent 保存信息
	    * @Description: 保存信息 用于信息的新增和修改
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/saveOnlineTraceContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveOnlineTraceContent(HttpServletRequest request,@RequestBody OnlineTraceContent onlineTraceContent) {
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
		onlineTraceContent.setUpdater(uid);
		int saveRes = onlineTraceContentService.saveOnlineTraceContent(onlineTraceContent);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}
	/**
	 * @Title: updateOnlineTraceContent 更新信息
	 * @Description: 保存信息 用于信息的新增和修改
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/updateOnlineTraceContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateOnlineTraceContent(HttpServletRequest request,@RequestBody OnlineTraceContent onlineTraceContent) {
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
		Long id = onlineTraceContent.getId();
		OnlineTraceContent onlineTraceContent1 = onlineTraceContentService.getOnlineTraceContent(onlineTraceContent
				.getId());
		if(uid != Integer.valueOf(onlineTraceContent1.getUpdater())){
			return new ResultObject<Object>(0, "权限不足，请联系创建人进行修改！");
		}
		onlineTraceContent1.setUpdateTime(new Date());
		onlineTraceContent.setIscheck(onlineTraceContent1.getIscheck());
		int saveRes = onlineTraceContentService.updateOnlineTraceContent(onlineTraceContent);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "更新成功！");
		} else {
			return new ResultObject<Object>(-1, "更新失败！");
		}
	}
	
	    /** 
	    * @Title: deleteOlineTraceContent
	    * @Description: 信息删除 多个以逗号隔开
	    * @param request
	    * @param response
	    * @param contentId 信息Id
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/deleteOnlineTraceContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deleteOnlineTraceContent(HttpServletRequest request, HttpServletResponse response,@RequestBody
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
			onlineTraceContentService.deleteOnlineTraceContent(contentIdList);
		} catch (Exception e) {
				e.printStackTrace();
		return new ResultObject<Object>(-99, "服务器异常！");
		}
		return new ResultObject<Object>(0, "删除成功！");
	}
	   /** 
	    * @Title: getOnlineTraceContentByContentId
	    * @Description:根据内容Id获取内容
	    * @param  request
	    * @param  response
	    * @param  contentId
	    * @param  参数说明 
	    * @return ResultObject<Object>    返回类型 
	    * @throws 
	    */
	@RequestMapping(value="/getOnlineTraceContentByContentId",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getOnlineTraceContentByContentId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "contentId", required = true) Long contentId) {
		try {
			OnlineTraceContent content = onlineTraceContentService.getOnlineTraceContent(contentId);
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
