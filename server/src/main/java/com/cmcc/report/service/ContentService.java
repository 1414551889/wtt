package com.cmcc.report.service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.report.dao.CenterDao;
import com.cmcc.report.dao.ContentDao;
import com.cmcc.report.dao.UserAndContentDao;
import com.cmcc.report.model.Center;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.EmployeeIndex;
import com.cmcc.report.model.ProjectImport;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Page;
import com.cmcc.report.util.UploadFileUtil;
import com.cmcc.report.util.excel.parser.SheetParser;



@Service("ContentService")
public class ContentService {
	
	@Autowired
	private CenterDao centerDao;
	@Autowired
	private ContentDao cd;
	@Autowired
	private UserAndContentDao userAndContentDao;
	private static Logger logger = LoggerFactory.getLogger(ContentService.class);
	
	    //信息保存	
		@Transactional
		public int saveContent(Content content){
			Content save = null;
			try {
				save = cd.save(content);
			} catch (Exception e) {
				logger.error("保存报错"+e.getMessage());
			}
			if(save != null){
				return 1;
			}
			return 0;
		}
		
		//查找本周
		private final String SELECT_INWEEK_JPQL= "select t from Content t WHERE YEARWEEK(date_format(t.updateTime,'%Y-%m-%d')) = YEARWEEK(now()) AND t.ischeck in (?1,?2) order by t.updateTime DESC";
		//查找本周之前
		private final String SELECT_BEFOREWEEK_JPQL= "select t from Content t WHERE YEARWEEK(date_format(t.updateTime,'%Y-%m-%d')) < YEARWEEK(now()) AND t.ischeck in (?1,?2) AND t.endTime between ?3 AND ?4 order by t.updateTime DESC";
		//查找本周之前按照中心分组的数据
		private final String SELECT_BEFOREWEEK_GROUPBYCENENTER_JPQL= "select t from Content t WHERE YEARWEEK(date_format(t.updateTime,'%Y-%m-%d')) < YEARWEEK(now()) AND t.ischeck in (?1,?2) AND t.endTime between ?3 AND ?4 GROUP BY t.department,t.center order by t.updateTime DESC";	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Content> getContentListForWeek(int pageSize,int pageIndex,int isCheck,int isweek,Date beginTime,Date endTime,int isGroup){
			List params = new ArrayList<Object>();
			if(isweek == 0){
				if(isCheck == 0){
					params.add(0);
					params.add(0);
				}else if(isCheck == 1){
					params.add(1);
					params.add(1);
				}else{
					params.add(0);
					params.add(1);
				}
				return cd.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);
			}else{
				if(isCheck == 0){
					params.add(0);
					params.add(0);
				}else if(isCheck == 1){
					params.add(1);
					params.add(1);
				}else{
					params.add(0);
					params.add(1);
				}
				if(isGroup == 0){
					return cd.getAllPage(SELECT_BEFOREWEEK_JPQL,params,pageSize,pageIndex);
				}else{
					return cd.getAllPage(SELECT_BEFOREWEEK_GROUPBYCENENTER_JPQL,params,pageSize,pageIndex);
				}
				
			}
			
		}
		
		 // 删除多个
		//private final String DELETE_JPQL= "delete Content o where o.contentId in (?1)";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Transactional
		public void deleteContent(String ids){
			List  params = new ArrayList<Object>();
			params.add(ids);
			String[] contentIds = ids.split(",");
			for (String string : contentIds) {
				cd.delete(Long.valueOf(string));
			}	
		}
		
		
		//按照中心和时间查找的数据
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Content> getContentByTimeAndCenter(int pageSize,int pageIndex,Integer isCheck,Date beginTime,Date endTime,Integer centerId, String centerName){
			String SELECT_BYTIMEANDCENTER_JPQL= "select t from Content t WHERE t.ischeck in (?1,?2) AND t.isDelete = 0 AND t.projectType = 1 AND t.publishTime between ?3 AND ?4";
			List params = new ArrayList<Object>();
				if(isCheck == 0){
					params.add(0);
					params.add(0);
				}else if(isCheck == 1){
					params.add(1);
					params.add(1);
				}else{
					params.add(0);
					params.add(1);
				}
				params.add(beginTime);
				params.add(endTime);
				if(centerId != -1){
					params.add(centerName);
					SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" AND t.centerName=?5";
					/*params.add(centerId);
					SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" AND t.centerId=?5";*/
				}
				SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" order by t.publishTime DESC";
				return cd.getAllPage(SELECT_BYTIMEANDCENTER_JPQL,params,pageSize,pageIndex);
			
		}
		
		//按照清单id查找数据
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Content> selectContentsByBillId(int pageSize,int pageIndex,Integer billId,Integer isImportant) {
			String SELECT_BYBILLID_JPQL= "select t from Content t WHERE t.isImportant in (?1,?2) AND t.billId = ?3";
			List params = new ArrayList<Object>();
			if(isImportant == 0){
				params.add(0);
				params.add(0);
			}else if(isImportant == 1){
				params.add(1);
				params.add(1);
			}else{
				params.add(0);
				params.add(1);
			}
			params.add(billId);
			SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL+" order by t.updateTime DESC";
			return cd.getAllPage(SELECT_BYBILLID_JPQL,params,pageSize,pageIndex);
		}

		//查询详情
		private final String SELECT_ONE_JPQL= "select t from Content t WHERE t.contentId = ?1";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Content getContentByContentId(Long contentId) {
			List params = new ArrayList<Object>();
			params.add(contentId);
			List<Content> list;
			try {
				list = cd.findAllList(SELECT_ONE_JPQL, params);
				if(null != list && !list.isEmpty()){
					return list.get(0);
				}
			} catch (Exception e) {
				logger.error("查询清单详情+contentId="+contentId+"错误信息:"+e.getMessage());
				return null;
			}	
			return null;
		}
		
		//按照清单id和中心查找数据
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Content> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex,
				Integer isImportant,Long centerId,Integer billId) {
			String SELECT_BYBILLID_JPQL = "select t from Content t WHERE t.billId=?1";
			List params = new ArrayList<Object>();
			params.add(billId);
			if (isImportant != 2) {
				SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL+" And t.isImportant ="+isImportant;
			}
			if(null != centerId){
				SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL+" And t.centerId ="+centerId;
			}
			SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL + " order by t.contentId,t.centerId,t.updateTime DESC";
			return cd.getAllPage(SELECT_BYBILLID_JPQL, params, pageSize, pageIndex);
		}
		
		
		//信息导入
		@Transactional
		public ResultObject<Object> importProjectContent(HttpServletRequest request,Integer centerId){
			ResultObject<Object> result=null;
			try {
				// 返回上传文件流
				Map<String, InputStream> map = UploadFileUtil.uploadFile(request);
				logger.error("imporBatchRecharge上传ExcelMap:"+map.toString());
				if (map.size() > 0) {
					Iterator<Map.Entry<String, InputStream>> iterator = map.entrySet().iterator();
					// 每次导入只有一个文件
					while (iterator.hasNext()) {
						Map.Entry<String, InputStream> next = iterator.next();
						Workbook workbook;
						if (next.getKey().endsWith("xls")) {
							workbook = new HSSFWorkbook(next.getValue());
						} else {
							workbook = new XSSFWorkbook(next.getValue());
						}
						result = importData(workbook,centerId);
						break;
					}
				}else{
					return new ResultObject<Object>(-1, "文件为空");
				}
			} catch (Exception e) {
				logger.error("导入数据异常imporBatchRecharge，创建HSSFWorkbook或XSSFWorkbook有问题", e);
				return new ResultObject<Object>(-2, "请按照下载的模板填写内容");
			}
			return result;
		}
		
		
		public ResultObject<Object> importData(Workbook workbook,Integer centerId) {
			ResultObject<Object> result = new ResultObject<Object>(0, "成功");
			try {
				int successnum = 0;
				int failnum = 0;
	
				Sheet sheet = workbook.getSheet("dddd");
				SheetParser parser = new SheetParser();
				List<ProjectImport> imports = parser.createEntity(sheet, ProjectImport.class);
				// 数据校验
				List<Content> dataDist = transferImportData(imports,centerId);
				cd.batchAdd(dataDist,1000);
			} catch (Exception e) {
				logger.error("imporBatchRecharge数据检验出错"+e+e.getMessage());
				e.printStackTrace();
				return new ResultObject<Object>(-2, "请按照下载的模板填写内容");
			}
			return result;
		}

		
		private List<Content> transferImportData(List<ProjectImport> anImportList,Integer centerId) {
			Map<String,Object> importDataMap=new HashMap<>();
			List<Content>contentList=new ArrayList<>();
			for (ProjectImport anImport : anImportList) {
				Content content=new Content();
				String isImportant = anImport.getIsImportant();
				if(isImportant!=null){
					if(isImportant.equals("是")){
						content.setIsImportant(1);
					}else {
						content.setIsImportant(0);
					}
				}
				String centerName = anImport.getCenterName();
				if(centerName!=null){
					content.setCenterName(centerName);
					try {
						Center centerBycenterName = centerDao.getCenterBycenterName(centerName);
						content.setCenterId(centerBycenterName.getCenterId());
					} catch (Exception e) {
						content.setCenterId(centerId);
					}
				}else{
					content.setCenterId(centerId);
				}
				String cooperator = anImport.getCooperator();
				if(cooperator!=null){
					content.setCooperator(cooperator);
				}
				String projectName = anImport.getProjectName();
				if(projectName!=null){
					content.setProjectName(projectName);
				}
				String responsible = anImport.getResponsible();
				if(responsible!=null){
					content.setResponsible(responsible);
				}
				String reMark = anImport.getRemark();
				if(reMark!=null){
					content.setRemark(reMark);
				}
				String workContent = anImport.getWorkContent();
				if(workContent!=null){
					content.setWorkContent(workContent);
				}
				String workProject = anImport.getWorkProject();
				if(workProject!=null){
					content.setWorkProject(workProject);
				}
				String timeLimit = anImport.getTimeLimit();
				if(timeLimit!=null){
					content.setTimeLimit(timeLimit);
				}
				String isRisk = anImport.getIsRisk();
				if(isRisk!=null){
					if(isRisk.equals("是")){
						content.setIsRisk(1);
					}else {
						content.setIsRisk(0);
					}
				}
				String riskSituation = anImport.getRiskSituation();
				if(riskSituation!=null){
					content.setRiskSituation(riskSituation);
				}
				String responsible1 = anImport.getResponsible();
				if(responsible1!=null){
					content.setResponsible(responsible1);
				}
				String overState = anImport.getOverState();
				if(overState!=null){
					content.setOverState(overState);
				}
				content.setIscheck(0);//是否审核 默认未审核
				content.setIsDelete(0);//是否删除
				content.setIsFocused(0);//是否关注  默认未关注
				content.setIsPublish(0);//未发布
				content.setBillId(-1);//清单ID
				content.setProjectType(1);//类型：1推进
				content.setUpdater(1);
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
				String selecttime = sdf.format(date) + "T12:00:00.000Z";
				content.setSelecttime(selecttime);
				content.setUpdateTime(date);
				content.setPublishTime(date);
				contentList.add(content);
			}
			return contentList;
		}

		//按照中心查找的数据
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Content> getContentByCenternew(int pageSize,int pageIndex,Integer centerId){
			String SELECT_BYTIMEANDCENTER_JPQL= "select t from Content t WHERE t.isPublish = 1 and t.isDelete = 0";
			List params = new ArrayList<Object>();
			if(centerId != -1){
				params.add(centerId);
				SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" AND t.centerId=?1";
			}
			SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" order by t.publishTime DESC";
			return cd.getAllPage(SELECT_BYTIMEANDCENTER_JPQL,params,pageSize,pageIndex);
		}
		
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Transactional
		public void updateContent(){
			
		}

		
		/**
		 * @param contentPage Page分页格式
		 * @return 微头条二期_客户端普通首页（中心项目列表）格式
		 */
		public EmployeeIndex changeFormat(Page<Content> page,int uid) {
			List<Content> list = page.getList();
			for (Content content : list) {
				String overState = content.getOverState();
				String stripHtml = this.stripHtml(overState);
				content.setCompletionText(stripHtml);
				try {
					Integer contentId = Integer.parseInt(content.getContentId()+"");
					int num = userAndContentDao.selectByContentId(contentId, uid).size();
					if(num == 0){//已读
						content.setIsRead(1);
					}else{//未读
						content.setIsRead(0);
					}
				} catch (Exception e) {
					content.setIsRead(1);
				}
				if(content.getRemark() == null || ("").equals(content.getRemark())){
					content.setRemark("无");
				}
				if(content.getPicUrl() != null){
					content.setProjectImage(content.getPicUrl());
				}else{
					content.setPicUrl("");
					content.setProjectImage("");
				}
				if(content.getTimeLimit() != null && !("").equals(content.getTimeLimit())){
					if(content.getTimeLimit().length()>10){
						String substring = content.getTimeLimit().substring(0,10);
						content.setTimeLimit(substring);
					}
				}
			}
			EmployeeIndex employeeIndex = new EmployeeIndex();
			employeeIndex.setPageCount(page.getPageCount());
			employeeIndex.setPageIndex(page.getPageIndex());
			employeeIndex.setPageSize(page.getPageSize());
			employeeIndex.setProjects(list);
			employeeIndex.setReultAllCount(page.getReultAllCount());
			employeeIndex.setReultCount(page.getReultCount());
			return employeeIndex;
		}
		
		
		/**
		 * @return 去掉HTML
		 */
		public String stripHtml(String content) { 
		    // <p>段落替换为换行 
		    content = content.replaceAll("<p .*?>", "\r\n"); 
		    // <br><br/>替换为换行 
		    content = content.replaceAll("<br\\s*/?>", "\r\n"); 
		    // 去掉其它的<>之间的东西 
		    content = content.replaceAll("\\<.*?>", ""); 
		    // 去掉空格 
		    content = content.replaceAll(" ", "");
		    return content;   
		}
		
}
