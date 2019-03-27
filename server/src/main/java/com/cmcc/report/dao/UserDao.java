package com.cmcc.report.dao;

import java.util.List;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cmcc.report.common.BaseJdbcDaoImpl;
import com.cmcc.report.model.User;
import com.cmcc.report.model.UserRowMapper;

@Repository
public class UserDao extends BaseJdbcDaoImpl{
	
	final Logger logger = LoggerFactory.getLogger(UserDao.class);

	    /** 
	    * @Title: addUser 
	    * @Description: TODO(这里用一句话描述这个方法的作用) 
	    * @param @param account
	    * @param @param passWord
	    * @param @param center
	    * @param @return  参数说明 
	    * @return int    返回类型 
	    * @throws 
	    */
	static String ADD_USER = "INSERT INTO userinfo(account,passWord,center,role) VALUES (?,?,?,1)";
	public int addUser(String account,String passWord, String center) {
		return this.getJdbcTemplate().update(ADD_USER, new Object[]{account,passWord,center});
	}
	
	static String SELECT_USER = "select * from userinfo t limit ?,?";
        /** 
        * @Title: selectUser 
        * @Description: 用户信息分页查询
        * @param actionId
        * @param  pageSize
        * @param  pageIndex
        * @param  参数说明 
        * @return PaginationSupport    返回类型 
        * @throws 
        */
    public List<User> selectUser(int pageIndex, int pageSize) {
    	  Object[] args = new Object[]{(pageIndex-1)*pageSize,(pageIndex-1)*pageSize+pageSize};
          logger.info(SELECT_USER.replaceAll("\\?", "{}"), args);
          try {
        	  List<User> list=this.getJdbcTemplate().query(SELECT_USER, args, new UserRowMapper());
        	  return list;
          } catch (Exception e) {
              logger.error(e.getMessage(), e);
              return null;
          }  
    }
    
    static String SELECT_USER_FORLOGIN = "select * from userinfo t where t.account = ? and t.password = ?";
    
        /** 
        * @Title: selectUserForLogin 
        * @Description: TODO 用于登录
        * @param @param account
        * @param @param password
        * @param @return  参数说明 
        * @return User 返回类型 
        * @throws 
        */
    public User selectUserForLogin(String account,String password) {
  	    Object[] args = new Object[]{account,password};
        logger.info(SELECT_USER.replaceAll("\\?", "{}"), args);
        try {
      	  List<User> list=this.getJdbcTemplate().query(SELECT_USER_FORLOGIN, args, new UserRowMapper());
      	  if(null != list){
      		return list.get(0); 
      	  }else{
      		return null;  
      	  }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }  
  }
       static String SELECT_USER_BYUSERID = "select * from userinfo t where t.userId = ?";
		
		    /** 
		    * @Title: getUserByToken 
		    * @Description: 根据userId查询用户信息
		    * @param @param userId
		    * @param @return  参数说明
		    * @return User    返回类型 
		    * @throws 
		    */
		public User getUserByToken(Integer userId) {
			    Object[] args = new Object[]{userId};
		        logger.info(SELECT_USER.replaceAll("\\?", "{}"), args);
		        try {
		      	  List<User> list=this.getJdbcTemplate().query(SELECT_USER_BYUSERID, args, new UserRowMapper());
		      	  if(null != list){
		      		return list.get(0); 
		      	  }else{
		      		return null;  
		      	  }
		        } catch (Exception e) {
		            logger.error(e.getMessage(), e);
		            return null;
		        }  
		}
		  /** 
		    * @Title: updateUser 
		    * @Description: TODO(这里用一句话描述这个方法的作用) 
		    * @param @param password
		    * @param @param userId
		    * @param @return  参数说明 
		    * @return int    返回类型 
		    * @throws 
		    */
		static String UPDATE_USER = "update userinfo u set u.password = ? where u.userId = ?";
		public int updateUser(String passWord, Integer userId) {
			return this.getJdbcTemplate().update(UPDATE_USER, new Object[]{passWord,userId});
		}
		
		 //获取领导列表
		 static String SELECT_USER_BYUSERROLE = "select * from userinfo t where t.role = 3";
		 public List<User> getLdList() {
			    Object[] args = new Object[]{};
		        logger.info(SELECT_USER.replaceAll("\\?", "{}"), args);
		        try {
		      	  List<User> list=this.getJdbcTemplate().query(SELECT_USER_BYUSERROLE, args, new UserRowMapper());
		      	  return list;
		        } catch (Exception e) {
		            logger.error(e.getMessage(), e);
		            return null;
		        }
		}
		 
		//获取所有用户列表
		 static String SELECT_USER_AllUser = "select * from userinfo t";
		 public List<User> getAllUserList() {
			    Object[] args = new Object[]{};
		        logger.info(SELECT_USER.replaceAll("\\?", "{}"), args);
		        try {
		      	  List<User> list=this.getJdbcTemplate().query(SELECT_USER_AllUser, args, new UserRowMapper());
		      	  return list;
		        } catch (Exception e) {
		            logger.error(e.getMessage(), e);
		            return null;
		        }
		}
}
