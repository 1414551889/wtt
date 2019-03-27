package com.cmcc.report.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.report.dao.UserAndContentDao;
import com.cmcc.report.dao.UserDao;
import com.cmcc.report.model.User;
import com.cmcc.report.model.UserAndContent;

@Service("UserAndContentService")
public class UserAndContentService {

	private static Logger logger = LoggerFactory.getLogger(UserAndContentService.class);
	@Autowired
	private UserAndContentDao userAndContentDao;
	
	@Autowired
	private UserDao ud;
	
	/**
	 * @return 获取所有用户
	 */
	public List<User> getAllUser(){
		try {
			List<User> allUserList = ud.getAllUserList();
			return allUserList;
		} catch (Exception e) {
			logger.error("获取所有用户出错"+e+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过项目ID删除数据
	 * @param projectId 项目ID
	 * @param userId 用户ID
	 */
	public void deleteUACByContentId(int projectId, int userId) {
		try {
			userAndContentDao.deleteUACByContentId(projectId,userId);
		} catch (Exception e) {
			logger.error("通过项目ID删除数据（projectId="+projectId+"_userId="+userId+"）"+e+e.getMessage());
			e.printStackTrace();
		}
	}
	

	/**
	 * 通过中心ID删除数据
	 * @param centerId 中心ID
	 * @param userId 用户ID
	 */
	public void deleteUACByCenterId(int centerId, Integer userId) {
		try {
			userAndContentDao.deleteUACByCenterId(centerId, userId);
		} catch (Exception e) {
			logger.error("通过中心ID删除数据（centerId="+centerId+"_userId="+userId+"）"+e+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @param uac 用户-项目关系对象
	 * @return 保存结果
	 */
	@Transactional
	public int addUAC(UserAndContent uac){
		try {
			UserAndContent save = userAndContentDao.save(uac);
			if(save == null){
				return 0;//保存失败
			}else{
				return 1;
			}
		} catch (Exception e) {
			logger.error("用户-项目关系对象保存报错"+e+e.getMessage());
			e.printStackTrace();
			return -90;
		}
	
	}
	
	
	/**
	 * 用户-项目关系保存
	 * @param centerId  中心ID
	 * @param contentId 项目ID（内容ID）
	 */
	@Transactional
	public void addUACMany(int centerId,int contentId){
		try {
			//获取所有用户
			List<User> allUser = this.getAllUser();
//			List<UserAndContent> uacs = new ArrayList<UserAndContent>();
			//保存已读未读标记
			for (User user : allUser) {
				UserAndContent uac = new UserAndContent();
				uac.setUserId(user.getUserId());
				uac.setCenterId(centerId);
				uac.setContentId(contentId);
				uac.setIsRead(0);
				userAndContentDao.save(uac);
//				uacs.add(uac);
			}
//			userAndContentDao.batchAdd(uacs,1000);
		} catch (Exception e) {
			logger.error("用户-项目关系对象保存报错(centerId="+centerId+"_contentId="+contentId+")"+e+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @param centerId 中心ID
	 * @param userId   用户ID
	 * @return 查询关系数据
	 */
	public int getUACCountByCenterId(int centerId,int userId){
		try {
			List<UserAndContent> uacs = userAndContentDao.selectByCenterId(centerId,userId);
			return uacs.size();
		} catch (Exception e) {
			return 0;
		}
	
	}
	
	/**
	 * @param contentId 项目（内容）ID
	 * @param userId   用户ID
	 * @return 查询关系数据
	 */
	public int getUACCountByContentId(int contentId,int userId){
		try {
			List<UserAndContent> uacs = userAndContentDao.selectByContentId(contentId,userId);
			return uacs.size();
		} catch (Exception e) {
			return 0;
		}
		
	}
	
	
}
