package com.cmcc.report.util.excel.parser;

import com.cmcc.report.util.excel.ExcelExportParseParam;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 *
 * @Title:
 * @Package
 * Company:
 * @author huwl
 * @date
 */
public interface ExcelExportParseInter {

	/**
	 *  excel导出解析，会生成文件
	  * @param		data sheet数据
	  * @param		clazz 解析的类别
	  * @param
	  * @return
	  * @throws		
	  * @author
	  * @date
	  */
	Workbook parse(List<? extends Object> data, Class clazz, Object headData) ;

	/**
	 * 多个sheet调用此接口
	 * @param parseParamlist
	 * @return
	 */
	Workbook parse(List<ExcelExportParseParam> parseParamlist) ;

}
