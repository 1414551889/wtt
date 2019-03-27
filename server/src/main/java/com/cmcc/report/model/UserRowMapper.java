package com.cmcc.report.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUserId(rs.getInt("userId"));
		user.setAccount(rs.getString("account"));
		user.setPassword(rs.getString("passWord"));
		user.setCenterId(rs.getInt("centerId"));
		user.setCenter(rs.getString("center"));
		user.setRole(rs.getInt("role"));
		user.setName(rs.getString("name"));
		return user;
		
	}

}
