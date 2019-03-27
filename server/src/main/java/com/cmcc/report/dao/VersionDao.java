package com.cmcc.report.dao;

import java.util.List;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cmcc.report.common.BaseJdbcDaoImpl;
import com.cmcc.report.model.VersionInfo;
import com.cmcc.report.model.VersionInfoRowMapper;

@Repository
public class VersionDao extends BaseJdbcDaoImpl{
	
	final Logger logger = LoggerFactory.getLogger(VersionDao.class);

	static String SELECT_VERSION = "select versionCode,updateInfo,updateUrl,updateTime from versionInfo v where v.os=? order by v.updateTime desc";
    public VersionInfo selectVersion(Integer os) {
    	  Object[] args = new Object[]{os};
          logger.info(SELECT_VERSION.replaceAll("\\?", "{}"), args);
          try {
        	  List<VersionInfo> list=this.getJdbcTemplate().query(SELECT_VERSION, args, new VersionInfoRowMapper());
        	  if(null == list){
        		  return null;
        	  }
        	  return list.get(0);
          } catch (Exception e) {
              logger.error(e.getMessage(), e);
              return null;
          }  
    }
    
  
}
