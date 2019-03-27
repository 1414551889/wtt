package com.cmcc.report.util.excel.mapping;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 封装那些没有字段的导入数据
 * @Title: Other
 * @author huwl
 * @date 2017/10/30 16:54
 */
@XmlRootElement(name = "other")
@XmlAccessorType(value = javax.xml.bind.annotation.XmlAccessType.NONE)
public class Other  implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String column;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	
}
