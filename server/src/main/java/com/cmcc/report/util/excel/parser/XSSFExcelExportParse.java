package com.cmcc.report.util.excel.parser;
import com.cmcc.report.util.excel.ExcelExportParseParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 *
 * @Title: suninfo
 * @Package com.suninfo.util.excel.parser
 * Company:suninfo
 * @author alfredo
 * @date 2016/8/24
 */
public class XSSFExcelExportParse implements ExcelExportParseInter{

	@Override
	public Workbook parse( List<? extends Object> data, Class clazz,Object headData) {
		return new XSSFWorkbook();
	}

	@Override
	public Workbook parse( List<ExcelExportParseParam> parseParamlist ) {
		return null;
	}
}
