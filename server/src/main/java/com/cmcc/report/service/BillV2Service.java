package com.cmcc.report.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.report.dao.CenterDao;
import com.cmcc.report.dao.ContentDao;
import com.cmcc.report.dao.UserAndContentDao;
import com.cmcc.report.model.Center;
import com.cmcc.report.model.Content;
import com.cmcc.report.model.LeaderIndex;
import com.cmcc.report.util.Page;


@Service
public class BillV2Service {
	
	@Autowired
	private ContentDao cod;
	
	@Autowired
	private CenterDao centerDao;
	
	@Autowired
	private UserAndContentDao userAndContentDao;
	
	private static Logger logger = LoggerFactory.getLogger(BillV2Service.class);
	
	//获取本周清单
	public List<LeaderIndex> getBillForInWeek(Integer uid, Date time1, Date time2) {
		List<LeaderIndex> leaderList = new ArrayList<LeaderIndex>();
		try {
			List<Center> allCenter = centerDao.getAllCenterSort();
			for (Center ct : allCenter) {
				List<Content> centerDetails = null;
				List params = new ArrayList<Object>();
				params.add(time1);
				params.add(time2);
				int cid = ct.getCenterId();
				List params2 = new ArrayList<Object>();
				params2.add(cid);
				String  jpql = "select bl from Content bl where bl.isPublish = 1 and bl.isDelete = 0 and bl.publishTime between ?1 AND ?2 and bl.projectType = 1 and (bl.isImportant*8+bl.isRisk*4+bl.isFocused*2) > 0 and  "
						+ "bl.centerId="+cid +" order by (bl.isImportant*8+bl.isRisk*4+bl.isFocused*2) desc";
				String  jpql2 = "select bl from Content bl where bl.isPublish = 1 and bl.projectType = 1 and bl.isDelete = 0 and  "
						+ "bl.centerId=?1 order by bl.publishTime desc";
				try {
					centerDetails = cod.findAllList(jpql, params);
					try {
						if(centerDetails.size() == 0){
							Page<Content> cd = cod.getAllPage(jpql2,params2,2,1);
							centerDetails = cd.getList();
						}
					} catch (Exception e) {}
				} catch (Exception e) {//当排序字段为0走保错sql
					/*Page<Content> cd = cod.getAllPage(jpql2,params2,2,1);
					centerDetails = cd.getList();*/
				}
				for (Content content : centerDetails) {
					String stripHtml = this.stripHtml(content.getOverState());
//					String firstImg = this.getFirstImg(content.getOverState());//暂时不用
					content.setCompletionText(stripHtml);
					if(content.getPicUrl() != null){
						content.setProjectImage(content.getPicUrl());
					}else{
						content.setPicUrl("");
						content.setProjectImage("");
					}
					if(content.getRemark() == null || ("").equals(content.getRemark())){
						content.setRemark("无");
					}
					if(content.getTimeLimit() != null && !("").equals(content.getTimeLimit())){
						if(content.getTimeLimit().length()>10){
							String substring = content.getTimeLimit().substring(0,10);
							content.setTimeLimit(substring);
						}
					}
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
				}
				LeaderIndex leaderIndex = new LeaderIndex();
				try {
					int size = userAndContentDao.selectByCenterId(cid, uid).size();
					if(size == 0){//已读
						leaderIndex.setHasUnRead(0);
						ct.setHasUnRead(0);
					}else{
						leaderIndex.setHasUnRead(1);
						ct.setHasUnRead(1);
					}
				} catch (Exception e) {
					leaderIndex.setHasUnRead(0);
					ct.setHasUnRead(0);
				}
				
				leaderIndex.setCenterId(ct.getCenterId());
				leaderIndex.setCenterName(ct.getCenterName());
				leaderIndex.setRecentTime(ct.getPublishTime());
				leaderIndex.setProjects(centerDetails);
				leaderList.add(leaderIndex);
			} 
			return leaderList;
		} catch (Exception e) {
			logger.error("错误信息:" + e.getMessage());
			return leaderList;
		}
	}
	
    /**
     * @return 获取第一个Img
     */
    private String getFirstImg(String content) {  
       try {
    	   String img = "";  
           Pattern p_image;  
           Matcher m_image;  
           String str = "";  
           String[] images = null;  
           String regEx_img = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";  
           p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);  
           m_image = p_image.matcher(content);  
           while (m_image.find()) {  
               img = m_image.group();  
               Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);  
               while (m.find()) {  
                   String tempSelected = m.group(1);  
         
                   if ("".equals(str)) {  
                       str = tempSelected;  
                   } else {  
                       String temp = tempSelected;  
                       str = str + "," + temp;  
                   }  
               }  
           }  
           if (!"".equals(str)) {  
               images = str.split(",");  
           }  
           String firstImg = images[0];
           return firstImg; 
		} catch (Exception e) {
			return "";
		} 
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
	
	/*//获取本周清单
		public List<List<Content>> getBillForInWeek() {
			//List<Content> list  = new ArrayList<Content>();
			//Map<Object, Object> map = new HashMap<Object, Object>();
			List<List<Content>> allList =new ArrayList<>();
			
			 try {
				List<Center> allCenter = centerDao.getAllCenterSort();
//				List<Center> allCenter = centerDao.getAllCenter();

				for (Center ct : allCenter) {
					List params = new ArrayList<Object>();
					int cid = ct.getCenterId();
					 String  jpql = "select bl,(bl.isImportant*8+bl.isRisk*4+bl.isFocused*2) as saa  from Content bl where YEARWEEK(date_format(bl.updateTime,'%Y-%m-%d')) = YEARWEEK(NOW())  and  bl.isDelete = 0 and  "
							+ "bl.centerId="+cid +" order by saa desc";
					 
						List<Content> centerDetails = cod.findAllList(jpql, params);
					    allList.add(centerDetails);
				} 
			} catch (Exception e) {
				logger.error("错误信息:" + e.getMessage());
			  return null;
			}
			return allList;	
		}*/
	
};