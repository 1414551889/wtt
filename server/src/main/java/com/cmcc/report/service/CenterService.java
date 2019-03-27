package com.cmcc.report.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.report.dao.CenterDao;
import com.cmcc.report.model.Center;

@Service("CenterService")
public class CenterService {

	@Autowired
	private CenterDao centerDao;
	
	/**
	 * @param centerId 中心ID
	 * @param date 当前时间
	 *   更新中心表中发布时间
	 */
	@Transactional
	public void updateCenterPublishTime(Integer centerId, Date date) {
		centerDao.updateCenterTime(centerId,date);
	}

	
	/**
	 * @return 查询所有的中心
	 */
	public List<Center> getAllCenter() {
		List<Center> allCenterSort = centerDao.getAllCenterSort();
		return allCenterSort;
	}


	public Center getCenterByCenterId(Integer centerId) {
		Center ct = centerDao.getCenterByCenterId(centerId);
		return ct;
	}

}
