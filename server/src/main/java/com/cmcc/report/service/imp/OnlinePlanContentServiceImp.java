package com.cmcc.report.service.imp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.cmcc.report.dao.OnlinePlanContentDao;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.OnlinePlanContent;
import com.cmcc.report.model.OnlinePlanContentImport;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.service.OnlinePlanContentService;
import com.cmcc.report.util.Page;
import com.cmcc.report.util.UploadFileUtil;
import com.cmcc.report.util.excel.parser.SheetParser;


@Service("onlinePlanContentService")
public class OnlinePlanContentServiceImp implements OnlinePlanContentService {
	@Autowired
	private OnlinePlanContentDao onlinePlanContentDao;
	private static Logger logger = LoggerFactory.getLogger(OnlinePlanContentServiceImp.class);
	
	    //信息保存	
		@Transactional
		public int saveContent(OnlinePlanContent content){
			OnlinePlanContent save = null;
			try {
				save = onlinePlanContentDao.save(content);
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
				return onlinePlanContentDao.getAllPage(SELECT_INWEEK_JPQL,params,pageSize,pageIndex);
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
					return onlinePlanContentDao.getAllPage(SELECT_BEFOREWEEK_JPQL,params,pageSize,pageIndex);
				}else{
					return onlinePlanContentDao.getAllPage(SELECT_BEFOREWEEK_GROUPBYCENENTER_JPQL,params,pageSize,pageIndex);
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
				onlinePlanContentDao.delete(Long.valueOf(string));
			}	
		}
		
		//按照中心和时间查找的数据
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Page<Content> getContentByTimeAndCenter(int pageSize,int pageIndex,Integer isCheck,Date beginTime,Date endTime,Integer centerId){
			String SELECT_BYTIMEANDCENTER_JPQL= "select t from Content t WHERE t.ischeck in (?1,?2) AND t.endTime between ?3 AND ?4";
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
					params.add(centerId);
					SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" AND t.centerId=?5";
				}
				SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" order by t.updateTime DESC";
				return onlinePlanContentDao.getAllPage(SELECT_BYTIMEANDCENTER_JPQL,params,pageSize,pageIndex);
			
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
			return onlinePlanContentDao.getAllPage(SELECT_BYBILLID_JPQL,params,pageSize,pageIndex);
		}

		//查询详情
		private final String SELECT_ONE_JPQL= "select t from Content t WHERE t.contentId = ?1";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Content getContentByContentId(Long contentId) {
			List params = new ArrayList<Object>();
			params.add(contentId);
			List<Content> list;
			try {
				list = onlinePlanContentDao.findAllList(SELECT_ONE_JPQL, params);
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
		return onlinePlanContentDao.getAllPage(SELECT_BYBILLID_JPQL, params, pageSize, pageIndex);
	}
	//信息导入
	@Transactional
	@Override
	public ResultObject importProjectContent(HttpServletRequest request, Long billId, User user){
		ResultObject result=null;
		try {
			// 返回上传文件流
			Map<String, InputStream> map = UploadFileUtil.uploadFile(request);
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
					result = importData(workbook,billId,user);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("导入数据异常，创建HSSFWorkbook或XSSFWorkbook有问题", e);
		}
		return result;
	}
	public ResultObject importData(Workbook workbook,Long billId,User user) {
		ResultObject result=null;
		try {
			int successnum = 0;
			int failnum = 0;

			Sheet sheet = workbook.getSheet("sheet1");
			SheetParser parser = new SheetParser();
			List<OnlinePlanContentImport> imports = parser.createEntity(sheet, OnlinePlanContentImport.class);
			// 数据校验
			List<OnlinePlanContent> dataDist = transferImportData(imports);
			for(OnlinePlanContent content:dataDist){
				content.setOnlinePlanBillId(billId);
				content.setCenterId(user.getCenterId());
				content.setCenterName(user.getCenter());
				content.setIscheck(0);
				content.setUpdater(user.getUserId());
				content.setUpdateTime(new Date());
			}
			onlinePlanContentDao.batchAdd(dataDist,1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<OnlinePlanContent> transferImportData(List<OnlinePlanContentImport> anImportList) {
		Map<String,Object> importDataMap=new HashMap<>();
		List<OnlinePlanContent>contentList=new ArrayList<>();
		for (OnlinePlanContentImport anImport : anImportList) {
			OnlinePlanContent content=new OnlinePlanContent();
			String type = anImport.getType();
			if(type!=null){
				content.setType(type);
			}
			String content1 = anImport.getContent();
			if(content1!=null){
				content.setContent(content1);
			}
			String developStaff = anImport.getDevelopStaff();
			if(developStaff!=null){
				content.setDevelopStaff(developStaff);
			}
			String productStaff = anImport.getProductStaff();
			if(productStaff!=null){
				content.setProductStaff(productStaff);
			}
			String remark = anImport.getRemark();
			if(remark!=null){
				content.setRemark(remark);
			}
			String project = anImport.getProject();
			if(project!=null){
				content.setProject(project);
			}
			String testStaff = anImport.getTestStaff();
			if(testStaff!=null){
				content.setTestStaff(testStaff);
			}
			String testTime = anImport.getTestTime();
			if(testTime!=null){
				content.setTestTime(testTime);
			}
			String onlineTime = anImport.getOnlineTime();
			if(onlineTime!=null){
				content.setOnlineTime(onlineTime);
			}
			String projectNumber = anImport.getProjectNumber();
			if(projectNumber!=null){
				content.setProjectNumber(projectNumber);
			}
			contentList.add(content);
		}
		return contentList;
	}

	@Override
	public int saveOnlinePlanContent(OnlinePlanContent onlinePlanContent) {
		onlinePlanContent.setIscheck(0);
		onlinePlanContent.setUpdateTime(new Date());
		OnlinePlanContent save=null;
		try {
			save= onlinePlanContentDao.save(onlinePlanContent);
		} catch (Exception e) {
			logger.error("保存报错"+e.getMessage());
		}
		if(save != null){
			return 1;
		}
		return 0;
	}

	@Override
	public void deleteOnlineContent(List<Long> idList) {
			for(Long id:idList){
				onlinePlanContentDao.delete(id);
			}
	}

	@Override
	public int updateOnlinePlanContent(OnlinePlanContent onlinePlanContent) {
		Object update = null;
		try {
			update = onlinePlanContentDao.update(onlinePlanContent);
		} catch (Exception e) {
			logger.error("更新报错" + e.getMessage());
		}
		if (update != null) {
			return 1;
		}
		return 0;
	}

	@Override
	public int deplayOnlinePlanContent(OnlinePlanContent onlinePlanContent) {
		return 0;
	}

	@Override
	public OnlinePlanContent getOnlinePlanContent(Long id) {
		String SELECT_ONE_JPQL = "select t from OnlinePlanContent t WHERE t.id = ?1";
		List params = new ArrayList<Object>();
		params.add(id);
		List<OnlinePlanContent> list;
		try {
			list = onlinePlanContentDao.findAllList(SELECT_ONE_JPQL, params);
			if (null != list && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询详情+id=" + id + "错误信息:" + e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public Page<Content> getContentByTimeAndCenter(int pageSize, int pageIndex, int isCheck, Date beginTime, Date endTime, Integer centerId) {
		String SELECT_BYTIMEANDCENTER_JPQL= "select t from onlineplancontent t WHERE t.ischeck in (?1,?2) AND t.endTime between ?3 AND ?4";
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
			params.add(centerId);
			SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" AND t.centerId=?5";
		}
		SELECT_BYTIMEANDCENTER_JPQL = SELECT_BYTIMEANDCENTER_JPQL+" order by t.updateTime DESC";
		return onlinePlanContentDao.getAllPage(SELECT_BYTIMEANDCENTER_JPQL,params,pageSize,pageIndex);
	}

	@Override
	public Page<OnlinePlanContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Integer centerId, Long
			billId) {
		String SELECT_BYBILLID_JPQL = "select t from OnlinePlanContent t WHERE t.onlinePlanBillId=?1";
		List params = new ArrayList<Object>();
		params.add(billId);
		if(null != centerId){
			SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL+" And t.centerId ="+centerId;
		}
		SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL + " order by t.id,t.centerId,t.updateTime DESC";
		return onlinePlanContentDao.getAllPage(SELECT_BYBILLID_JPQL, params, pageSize, pageIndex);
	}

}
