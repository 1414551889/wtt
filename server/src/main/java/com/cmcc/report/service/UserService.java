package com.cmcc.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cmcc.report.dao.UserDao;
import com.cmcc.report.model.ResultObject;
import com.cmcc.report.model.User;
import com.cmcc.report.util.JwtToken;


@Service("UserService")
public class UserService {
	@Autowired
	private UserDao ud;
	
	//用户查询
	public User selectUserForLogin(String account,String password) {
		return ud.selectUserForLogin(account, password);	
	}
    //根据token获取用户对象
	public User getUser(Integer userId) {	
	    User userRes = ud.getUserByToken(userId);
		return userRes;	
	}
    //根据token获取用户信息
	@SuppressWarnings("rawtypes")
	public ResultObject<Object> getUserByToken(String token) {
		Map<String, Object> validToken = JwtToken.validToken(token);
		Integer state = Integer.valueOf(String.valueOf(validToken.get("state")));
		if(state != 0){
			return new ResultObject<Object>(-1, "token效验失败,请重新获取！");
		}else{
			Object object = validToken.get("data");
			Map mObj=JSON.parseObject(String.valueOf(object),Map.class);
			Integer userId = Integer.valueOf(String.valueOf(mObj.get("uid")));
			if(null != userId){
				User userRes = ud.getUserByToken(userId);
				if(null == userRes){
					return new ResultObject<Object>(-2, "无法获取用户信息,请重新获取！");
				}
				return new ResultObject<Object>(0, "获取用户成功！",userRes);
			}
			
		}	
		return new ResultObject<Object>(-3, "获取用户Id失败！");	
	}
	//修改密码
	public int updateUser(String newPass, Integer uid) {	
		return ud.updateUser(newPass, uid);		
	}
	//获取领导用户列表
	public List<User> getLdList() {
		return ud.getLdList();	
	}
}
