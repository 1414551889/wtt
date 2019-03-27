package com.cmcc.report.util.excel.annotations;



import com.cmcc.report.util.excel.DbaHeaderInter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * sheet页签
  * @author
  * @date
  */
@Retention(value = RetentionPolicy.RUNTIME)
@Target( ElementType.TYPE)
public @interface ExcelExportSheet {
	/**
	 * sheet名称
	 * @return
	 */
	String name();

	/**
	 * 是否隐藏sheet
	 * 默认可见
	 * @return
	 */
	boolean visible() default true;
	/**
	 * 头实现
	 * 默认可见
	 * @return
	 */
	Class Header() default DbaHeaderInter.class;



}
