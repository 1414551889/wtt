package com.cmcc.report.service.imp;


import java.io.InputStream;
import java.text.ParseException;
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

import com.cmcc.report.dao.HalfWeekContentDao;
import com.cmcc.report.dao.HalfWeekReportBillDao;
import com.cmcc.report.model.HalfWeekContent;
import com.cmcc.report.model.HalfWeekContentImport;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.service.ContentService;
import com.cmcc.report.service.HalfWeekContentService;
import com.cmcc.report.util.DateUtil;
import com.cmcc.report.util.Page;
import com.cmcc.report.util.StringUtil;
import com.cmcc.report.util.UploadFileUtil;
import com.cmcc.report.util.excel.parser.SheetParser;

@Service("halfWeekContentService")
public class HalfWeekContentServiceImp implements HalfWeekContentService {
	private static Logger logger = LoggerFactory.getLogger(ContentService.class);

	@Autowired
	private HalfWeekContentDao contentDao;
	@Autowired
	private HalfWeekReportBillDao halfWeekReportBillDao;

	@Override
	@Transactional
	public int saveHalfWeekContent(HalfWeekContent halfWeekContent) {
		HalfWeekContent save = null;
		try {
			save = contentDao.save(halfWeekContent);
		} catch (Exception e) {
			logger.error("保存报错" + e.getMessage());
		}
		if (save != null) {
			return 1;
		}
		return 0;
	}

	@Override
	@Transactional
	public int updateHalfWeekContentInfo(HalfWeekContent halfWeekContent) {
		Object update = null;
		try {
			update = contentDao.update(halfWeekContent);
		} catch (Exception e) {
			logger.error("更新报错" + e.getMessage());
		}
		if (update != null) {
			return 1;
		}
		return 0;
	}
	private final String SELECT_ONE_JPQL= "select t from HalfWeekContent t WHERE t.id = ?1";
	@Override
	public HalfWeekContent getHalfWeekContentById(Long id) {
			List params = new ArrayList<Object>();
			params.add(id);
			List<HalfWeekContent> list;
			try {
				list = contentDao.findAllList(SELECT_ONE_JPQL, params);
				if(null != list && !list.isEmpty()){
					return list.get(0);
				}
			} catch (Exception e) {
				logger.error("查询详情+id="+id+"错误信息:"+e.getMessage());
				return null;
			}
			return null;
	}

	@Override
	public void deleteHalfWeekContent(List<String> idList) {
		for (String id : idList) {
			contentDao.delete(Long.valueOf(id));
		}
	}

	@Override
	public Page<HalfWeekContent> selectContentsByBillIdAndCenterId(int pageSize, int pageIndex, Long centerId)
			throws ParseException {
		String SELECT_BYBILLID_JPQL = "select t from HalfWeekContent t WHERE t.centerId=?1 and t.isCheck in (1,2,3)";
		List params = new ArrayList<Object>();
		params.add(centerId);
		SELECT_BYBILLID_JPQL = SELECT_BYBILLID_JPQL + " order by subTime DESC";
		Page<HalfWeekContent> page = contentDao.getAllPage(SELECT_BYBILLID_JPQL, params, pageSize, pageIndex);
		List<HalfWeekContent> list = page.getList();
		List<HalfWeekContent> listTemp = new ArrayList<>();
		HalfWeekContent halfWeekContent0 = list.get(0);
		Date subTime = halfWeekContent0.getSubTime();
		listTemp.add(halfWeekContent0);
		for (HalfWeekContent content : list) {
			Date subTime1 = content.getSubTime();
			int i = DateUtil.daysBetween(subTime, subTime1);
			if (i == 2 || i == 6 || i == 8|| i == 5) {
				listTemp.add(content);
			}
		}
		page.setList(listTemp);
		return page;
	}

	@Override
	public ResultObject importHalfWeekContent(HttpServletRequest request, Long billId,User user) {
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
			List<HalfWeekContentImport> imports = parser.createEntity(sheet, HalfWeekContentImport.class);
			// 数据校验
			List<HalfWeekContent> dataDist = transferImportData(imports);
			for(HalfWeekContent content:dataDist){
				content.setBillId(billId);
				content.setCenterId(user.getCenterId());
				content.setCenterName(user.getCenter());
				content.setSubTime(new Date());
				content.setCreater(user.getUserId());
                content.setIscheck(0);
			}
			contentDao.batchAdd(dataDist,1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private List<HalfWeekContent> transferImportData(List<HalfWeekContentImport> anImportList) {
		Map<String,Object> importDataMap=new HashMap<>();
		List<HalfWeekContent>contentList=new ArrayList<>();
		for (HalfWeekContentImport anImport : anImportList) {
			HalfWeekContent content=new HalfWeekContent();
			String projectName1 = anImport.getProjectName();
			if(StringUtil.isNotEmpty(projectName1)){
				content.setProjectName(projectName1);
			}
			String subProject = anImport.getSubProject();
			if(StringUtil.isNotEmpty(subProject)){
				content.setSubProject(subProject);
			}

			String workContent = anImport.getWorkContent();
			if(workContent!=null){
				content.setWorkContent(workContent);
			}
			contentList.add(content);
		}
		return contentList;
	}

}
