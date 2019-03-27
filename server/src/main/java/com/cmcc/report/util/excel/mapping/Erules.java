package com.cmcc.report.util.excel.mapping;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 导入导出excel xml
 * @Title: Erules
 * @author huwl
 * @date 2017/10/30 16:54
 */
@XmlRootElement(name = "erules")
@XmlAccessorType(value = javax.xml.bind.annotation.XmlAccessType.NONE)
public class Erules implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 对应的mapping
	 */
	private List<Erule> eruleList;
	@XmlElement(name = "erule")
	public List<Erule> getEruleList() {
		return eruleList;
	}
	public void setEruleList(List<Erule> eruleList) {
		this.eruleList = eruleList;
	}
	

}
