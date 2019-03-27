package com.cmcc.report.util.excel.parser;

import com.cmcc.report.controller.BillController;
import com.cmcc.report.util.excel.DbaHeaderInter;
import com.cmcc.report.util.excel.ExcelExportParseParam;
import com.cmcc.report.util.excel.ExcelUtil;
import com.cmcc.report.util.excel.annotations.ExcelAnnotationUtil;
import com.cmcc.report.util.excel.annotations.ExcelExportCell;
import com.cmcc.report.util.excel.annotations.ExcelExportSheet;
import com.cmcc.report.util.excel.annotations.ExcelExportValidate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * 2003excel文档导出解析类
 * @Title:
 * @Package
 * Company:
 * @author
 * @date
 */
public class HSSFExcelExportParse implements ExcelExportParseInter{
	final Logger logger = LoggerFactory.getLogger(BillController.class);
	private HSSFWorkbook workbook;
	@Override
	public Workbook parse( List<? extends Object> data, Class clazz,Object headData)  {
		/*if (data == null || data.isEmpty()) {
			return null;
		}*/
		workbook = new HSSFWorkbook();
		//解析sheet数据
		parseData( data,clazz, headData);
		return workbook;
	}
	@Override
	public Workbook parse( List<ExcelExportParseParam> parseParamlist )  {
		if( CollectionUtils.isNotEmpty( parseParamlist )){
			workbook = new HSSFWorkbook();
			for( ExcelExportParseParam param : parseParamlist ) {
				parseData( param.getData(),param.getClazz(),param.getHeaderData());
			}
			return workbook;
		}
		return null;
	}

	private void parseData(List<? extends Object> data, Class clazz,Object headData)  {
		HSSFSheet sheet = createHSSFSheet( clazz, workbook );
		//解析非头部信息
		ExcelExportSheet sheetAnnotation = (ExcelExportSheet) clazz.getAnnotation(ExcelExportSheet.class);
		List<Field> excelColumns = ExcelAnnotationUtil.getExcelExportCellFieldList( clazz );
		Class header = sheetAnnotation.Header();
		Integer rowCount = 0;
		DbaHeaderInter instant = createInstant(header);
		if(instant != null){
			rowCount =  instant.creatHeader(headData, sheet, workbook,excelColumns.size());
		}
		//行数，第一行是title，之后的数据
		rowCount = createHSSFTitle( ExcelUtil.creatTitleStyle( workbook ), sheet, excelColumns, rowCount,data);
		createHSSFData( data, ExcelUtil.creatDataStyle( workbook ), sheet, excelColumns, rowCount );
		List<Field> validateFieldList = ExcelAnnotationUtil.getExcelExcelExportValidateFieldList( clazz );
		if(data.size()>0){
			createHSSFValidate(sheet,data.get( 0 ),validateFieldList);
		}
	}

	private DbaHeaderInter createInstant(Class header) {
		try {
			DbaHeaderInter o = (DbaHeaderInter)header.newInstance();
			return o;

		} catch (InstantiationException e) {
			logger.error("生成实体错误",e);
		} catch (IllegalAccessException e) {
			logger.error("生成实体错误",e);
		}
		return null;

	}

	private void createHSSFValidate( Sheet sheet,Object obj,List<Field> validateFieldList ) {
		//关联下拉列表数据名称
		String nameName = "hidden";
		//隐藏索引
		int hIndex=0;
		for( Field field : validateFieldList ) {
			ExcelExportValidate validate = field.getAnnotation( ExcelExportValidate.class );
			if(null!=validate && validate.droplists() && null!=obj){
				try {
					Object oj = field.get( obj );
					if(oj instanceof List){
						List<String> dataoption = ( List<String> )oj;
						if(CollectionUtils.isNotEmpty( dataoption )){
							hIndex+=1;
							dataValidations( sheet,dataoption,validate.firstRow(),validate.endRow(),validate.firstCol(),validate.endCol() ,nameName+hIndex,hIndex);
						}
					}
				} catch( IllegalAccessException e ) {
					e.printStackTrace();
				}

			}
		}

	}

	private HSSFSheet createHSSFSheet( Class clazz, HSSFWorkbook workbook ) {
		ExcelExportSheet sheetAnnotation = (ExcelExportSheet ) clazz.getAnnotation(ExcelExportSheet.class);
		HSSFSheet sheet;
		if (sheetAnnotation == null){
			sheet = workbook.createSheet();
		}else{
			sheet = workbook.createSheet(sheetAnnotation.name());
			boolean visible = sheetAnnotation.visible();
			if(!visible){
				workbook.setSheetHidden( workbook.getSheetIndex( sheetAnnotation.name() ),true );
			}
		}
		return sheet;
	}

	private int createHSSFTitle( CellStyle headerStyle, HSSFSheet sheet, List<Field> excelColumns, int rowCount ,Object
			dataTemp) {
		int colCount = 0;
		HSSFRow headerRow = sheet.createRow(rowCount++);
		for (Field header : excelColumns) {
			ExcelExportCell cellAnnotation = header.getAnnotation(ExcelExportCell.class);
			if(cellAnnotation.dynamic()){
				List<? extends Object> dataList=(List<? extends Object>)dataTemp;
				if(dataList.size()>0) {
					Object data = dataList.get(0);
					try {
						Object map = header.get(data);
						;
						if (map instanceof LinkedHashMap) {
							LinkedHashMap<String, String> head = (LinkedHashMap<String, String>) map;
							Set<String> strings = head.keySet();
							for (String string : strings) {
								HSSFCell cell = headerRow.createCell(colCount++);
								cell.setCellValue(string);
								cell.setCellStyle(headerStyle);
							}
						}
					} catch (IllegalAccessException e) {
						logger.error("反射获取注解对应值时参数无法被访问,动态列头问题", e);
						e.printStackTrace();
					}
				}

			}else{
				String headerLabel = cellAnnotation.headerName();
				HSSFCell cell = headerRow.createCell(colCount++);
				cell.setCellValue(headerLabel);
				cell.setCellStyle(headerStyle);
			}

		}
		return rowCount;
	}

	private void createHSSFData( List<? extends Object> data, HSSFCellStyle dataStyle, HSSFSheet sheet, List<Field> excelColumns, int rowCount ) {
		try {
			int colCount;
			Map<String,Integer> macMap=new HashMap<>();
			for (int i = 0; i <excelColumns.size() ; i++) {
				macMap.put("max"+i,20);
			}
			for (Object dataObject : data) {
				HSSFRow dataRow = sheet.createRow(rowCount++);
				colCount = 0;
				for (int i = 0; i <excelColumns.size() ; i++) {
					Integer maxValue = macMap.get("max" + i);
					Field field =excelColumns.get(i);
					Object obj = field.get( dataObject );
					ExcelExportCell cellAnnotation = field.getAnnotation(ExcelExportCell.class);
					if(cellAnnotation.dynamic()){
						if(obj instanceof LinkedHashMap){
							LinkedHashMap<String,String> head = ( LinkedHashMap<String, String> )obj;
							Collection<String> values = head.values();
							for( String value : values ) {
								HSSFCell cell = dataRow.createCell(colCount++);
								cell.setCellValue(new HSSFRichTextString(value));
								cell.setCellStyle(dataStyle);
							}
						}
					}else {
						String value = "";
						if(obj != null){
							value = String.valueOf( obj );
						}
						HSSFCell cell = dataRow.createCell(colCount++);
						cell.setCellValue(new HSSFRichTextString(value));
						if (maxValue<value.length()){
							maxValue=value.length();
							macMap.put("max"+i,maxValue);
						}
						cell.setCellStyle(dataStyle);
					}
				}
			}
			for (int i = 0; i <excelColumns.size() ; i++) {
				Integer maxValue = macMap.get("max" + i);
				sheet.setColumnWidth(i,  (int)((maxValue + 0.72) * 256));
			}
		} catch( IllegalAccessException e ) {
			logger.error( "反射获取注解对应值时参数无法被访问",e );
			e.printStackTrace();
		}

	}

	private void dataValidations(Sheet sheet,List<String> options,int firstRow, int endRow, int firstCol, int endCol ,String nameName,int hindex){
		if(CollectionUtils.isNotEmpty( options )){
			HSSFSheet hidden = workbook.createSheet(nameName);
			for (int i = 0, length= options.size(); i < length; i++) {
				String name = options.get( i );
				HSSFRow row = hidden.createRow(i);
				HSSFCell cell = row.createCell(0);
				cell.setCellValue(name);
			}
			Name namedCell = workbook.createName();
			namedCell.setNameName(nameName);
			namedCell.setRefersToFormula(nameName+"!$A$1:$A$" + options.size());
			DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameName);
			CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
			HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);
			workbook.setSheetHidden(hindex, true);
			sheet.addValidationData(validation);
		}




	}



}
