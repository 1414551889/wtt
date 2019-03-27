package com.cmcc.report.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.report.dao.ProjectConterDao;
import com.cmcc.report.model.Content;
import com.cmcc.report.service.ProjectConterService;
import com.cmcc.report.util.Page;

@Service(value="projectConterService")
public class ProjectConterServiceImp implements ProjectConterService{
	
	@Autowired
	private ProjectConterDao projectConterDao;

	//一般员工项目
	@Override
	public List<Content> ordinaryStaffObject(int centerId) {
		String SELECT_STAFF_JPQL="select o from Content o where o.centerId=?1  ORDER BY o.updateTime desc ";
		List<Object> params = new ArrayList<Object>();
		params.add(centerId);
		Page<Content> page= projectConterDao.getAllPage(SELECT_STAFF_JPQL, params, 10, 1);
		System.out.println(page);
		return page.getList();
	}

	@Override
	public Content prijectBoost(int contentId) {
		return projectConterDao.findOne((long)contentId);
	}

	//点击关注
	private final String UPDATE_ISFOCUSED_JPQL="UPDATE content set isFocused = 1 where contentId=?1 ";
	public void isFocused(int contentId) {
		List params = new ArrayList<Object>();
		params.add(contentId);
		projectConterDao.update(UPDATE_ISFOCUSED_JPQL, params);
	}

	//取消关注
	private final String UPDATE_CANCELFOCUSED_JPQL="UPDATE content set isFocused = 0 where contentId=?1 ";
	public void cancelFocused( int contentId) {
		List params = new ArrayList<Object>();
		params.add(contentId);
		projectConterDao.update(UPDATE_CANCELFOCUSED_JPQL, params);
		
	}
}
