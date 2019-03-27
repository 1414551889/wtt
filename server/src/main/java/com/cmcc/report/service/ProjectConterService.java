package com.cmcc.report.service;

import java.util.List;

import com.cmcc.report.model.Content;

public interface ProjectConterService {

	List<Content> ordinaryStaffObject(int centerId);

	Content prijectBoost(int contentId);

	void isFocused(int isFocusedId);

	void cancelFocused(int isFocusedId);

}
