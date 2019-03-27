package com.cmcc.report.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by huwl
 */
public interface DbaHeaderInter {

	public Integer creatHeader(Object data, HSSFSheet sheet, HSSFWorkbook wb, int size);

	public HSSFCellStyle getCellStyle(HSSFWorkbook wb);


}
