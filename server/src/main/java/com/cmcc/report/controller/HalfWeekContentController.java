package com.cmcc.report.controller;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.model.*;
import com.cmcc.report.service.HalfWeekContentService;
import com.cmcc.report.service.HalfWeekReportBillService;
import com.cmcc.report.service.UserService;
import com.cmcc.report.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class HalfWeekContentController {
	final Logger logger = LoggerFactory.getLogger(HalfWeekContentController.class);
	@Autowired
	private HalfWeekContentService halfWeekContentService;
	@Autowired
	private UserService us;


	/**
	 * @Title: saveHalfWapieekContent 保存信息
	 * @Description: 保存信息 用于信息的新增和修改
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/saveHalfWeekContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> saveHalfWeekContent(HttpServletRequest request, @RequestBody HalfWeekContent halfWeekContent) {
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
		halfWeekContent.setCreater(uid);
		halfWeekContent.setIscheck(0);
		halfWeekContent.setSubTime(new Date());
		int saveRes = halfWeekContentService.saveHalfWeekContent(halfWeekContent);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "保存成功！");
		} else {
			return new ResultObject<Object>(-1, "保存失败！");
		}
	}

	/**
	 * @Title: updateHalfWeekContent 更新信息
	 * @Description: 保存信息 用于信息的新增和修改
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/updateHalfWeekContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> updateHalfWeekContent(HttpServletRequest request,@RequestBody HalfWeekContent halfWeekContent) {
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
		Long id = halfWeekContent.getId();
		HalfWeekContent halfWeekContentById = halfWeekContentService.getHalfWeekContentById(halfWeekContent.getId());
		if(uid != Integer.valueOf(halfWeekContentById.getCreater())){
			return new ResultObject<Object>(0, "权限不足，请联系创建人进行修改！");
		}
		halfWeekContentById.setWorkContent(halfWeekContent.getWorkContent());
		halfWeekContentById.setSubProject(halfWeekContent.getSubProject());
		halfWeekContentById.setProjectName(halfWeekContent.getProjectName());
		int saveRes = halfWeekContentService.updateHalfWeekContentInfo(halfWeekContentById);
		if (saveRes > 0) {
			return new ResultObject<Object>(0, "更新成功！");
		} else {
			return new ResultObject<Object>(-1, "更新失败！");
		}
	}
	/**
	 * @Title: getHalfWeekContentById 获取信息
	 * @Description: 根据id获取数据
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getHalfWeekContentById",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> getHalfWeekContentById(HttpServletRequest request,@RequestParam(value = "contentId", required = true) Long contentId) {
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

		HalfWeekContent halfWeekContent = halfWeekContentService.getHalfWeekContentById(Long.valueOf(contentId));
		if (halfWeekContent!=null) {
			return new ResultObject<Object>(0, "获取数据成功！", halfWeekContent);
		} else {
			return new ResultObject<Object>(-1, "获取数据失败");
		}
	}
	/**
	 * @Title:  deleteHalfWeekContent删除信息
	 * @Description: 根据id获取数据
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/deleteHalfWeekContent",method = RequestMethod.POST)
	@ResponseBody
	public ResultObject<Object> deleteHalfWeekContent(HttpServletRequest request,@RequestBody List<String> idList) {
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

		halfWeekContentService.deleteHalfWeekContent(idList);
		return new ResultObject<Object>(0, "获取数据成功！", null);
	}
	/**
	 * @Title: getHalfContentBycenterId根据中心ID获取数据,最新的两周数据
	 * @Description: 根据id获取数据
	 * @param request
	 * @param  response
	 * @return ResultObject<Object>    返回类型
	 * @throws
	 */
	@RequestMapping(value="/getHalfContentBycenterId/{centerId}",method = RequestMethod.GET)
	@ResponseBody
	public ResultObject<Object>  getHalfContentBycenterId(HttpServletRequest request,@PathVariable("centerId")
			String centerId) {
		int pageIndex=1;//默认第一页
		int pageSize=4;
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

		Page<HalfWeekContent> halfWeekContentList = null;
		try {
			halfWeekContentList = halfWeekContentService.selectContentsByBillIdAndCenterId(pageSize,
                    pageIndex, Long.valueOf(centerId));
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResultObject<Object>(-1, "获取数据失败");
		}
		if (halfWeekContentList!=null) {
			return new ResultObject<Object>(0, "获取数据成功！", halfWeekContentList);
		} else {
			return new ResultObject<Object>(-1, "获取数据失败");
		}
	}
	/**
	 * @param request
	 * @author:huwl
	 * @time:
	 * @return:String
	 * @describe:导入
	 */
	@RequestMapping("/imporHalfBatchRecharge")
	@ResponseBody
	public ResultObject imporHalfBatchRecharge(HttpServletRequest request, @RequestParam(value = "billId", required =
			false)  Long  billId) {
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
			ResultObject result = halfWeekContentService.importHalfWeekContent(request, billId,user);
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
	@RequestMapping("/exportHalfBatchResult")
	@ResponseBody
	public ResponseEntity<byte[]> exportHalfBatchResult(HttpServletRequest request) {
		String path = this.getClass().getClassLoader().getResource("/").getPath().replace("WEB-INF/classes","");
		ResponseEntity<byte[]> download = null;
		try {
			download = DownloadUtil.download(path+"dist/halfweektemplate.xls");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return download;
	}
}
