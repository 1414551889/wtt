package com.cmcc.report.util.excel.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * excel单元格
  * @author
  */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD)
public @interface ExcelExportCell {
	/**
	 * 列顺序
	 * @return
	 */
	int columnOrder();
	/**
	 * excel 表头名称
	 * @return
	 */
	String headerName();

	/**
	 * 动态表头
	 * @return
	 */
	boolean dynamic() default false;

}
