package com.cmcc.report.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.util.Map;

/**
 * Created by huwl .
 */

public class DbaHeaderImp implements  DbaHeaderInter {
    @Override
    public Integer creatHeader(Object data, HSSFSheet sheet, HSSFWorkbook wb, int size) {
        if (data==null){
            return 0;
        }
        /**标题样式,占一行，字体加粗，字体为20号字体**/
        Map<Integer,String > dataMap=(Map<Integer,String >)data;
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 800);
        HSSFCell cell = row.createCell((short)0);
        cell.setCellValue(dataMap.get(0));
        Region region=new Region( 0,  (short)0,  0, (short)(size-1));
        sheet.addMergedRegion(region);
        ExcelUtil.setRegionStyle(sheet,region,ExcelUtil.creatHeaderTitleStyle(wb));
        /**主题样式,占一行，字体加粗，字体为12号字体**/
        HSSFRow row1 = sheet.createRow(1);
        HSSFCell cell1 = row1.createCell((short)0);
        cell1.setCellStyle(ExcelUtil.creatHeaderContentStyle(wb));
        cell1.setCellValue(dataMap.get(1));
        region=new Region( 1,  (short)0,  1, (short)(size-1));
        sheet.addMergedRegion(region);
        ExcelUtil.setRegionStyle(sheet,region,ExcelUtil.creatHeaderContentStyle(wb));


        /**主题内容,占一行，字体为12号字体**/
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeight((short) 1900);
        HSSFCell cell2 = row2.createCell((short)0);
        cell2.setCellValue(dataMap.get(2));
        region = new Region(2, (short) 0, 2, (short) (size - 1));
        sheet.addMergedRegion(region);
        ExcelUtil.setRegionStyle(sheet,region,ExcelUtil.creatHeaderContentStyle(wb));


        /**主题空行,占一行，字体为12号字体**/
        HSSFRow row3 = sheet.createRow(3);
        HSSFCell cell3 = row3.createCell((short)0);
        cell3.setCellValue("");
        region =new Region( 3,  (short)0,  3, (short)(size-1));
        sheet.addMergedRegion(region);
        ExcelUtil.setRegionStyle(sheet,region,ExcelUtil.creatHeaderContentStyle(wb));

        cell3.setCellStyle(ExcelUtil.creatHeaderContentStyle(wb));
        return 4;
    }



    @Override
    public HSSFCellStyle getCellStyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);//字体大小
        font.setFontName("Calibri");
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight((short) 40);

        //2.生成样式对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFont(font); //调用字体样式对象
        style.setWrapText(true);
        return style;
    }


}
