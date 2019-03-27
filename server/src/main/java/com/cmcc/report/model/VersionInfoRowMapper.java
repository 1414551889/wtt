package com.cmcc.report.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class VersionInfoRowMapper implements RowMapper<VersionInfo> {

	@Override
	public VersionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		VersionInfo versionInfo = new VersionInfo();
		//versionInfo.setId(rs.getInt("Id"));
		//versionInfo.setUpdate(rs.getString("update"));
		versionInfo.setUpdateInfo(rs.getString("updateInfo"));
		versionInfo.setUpdateTime(rs.getTimestamp("updateTime"));
		versionInfo.setUpdateUrl(rs.getString("updateUrl"));
		versionInfo.setVersionCode(rs.getString("versionCode"));
		//versionInfo.setOs(rs.getInt("os"));
		return versionInfo;
		
	}

}
