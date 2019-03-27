package com.cmcc.report.util;

import java.io.IOException;
import java.util.Properties;

/**
 * <b>类名称：</b>AppPropertiesUtil
 * <b>类描述：</b>加载app文件工具类
 * <b>创建人：</b>ChenSong
 * <b>修改人：</b>ChenSong
 * <b>修改时间：</b>2016-8-2 上午11:06:58
 * <b>修改备注：</b>
 * @version v1.0<br/>
 */
public class AppPropertiesUtil {
	    private static Properties p = new Properties();  
	    static{  
	        try {  
	            p.load(AppPropertiesUtil.class.getClassLoader().getResourceAsStream("app.properties"));  
	        } catch (IOException e) {  
	            e.printStackTrace();   
	        }  
	    }  
	    
	    public static String getValue(String key){  
	        return p.getProperty(key);  
	    }  
	    public static void main(String[] args) {
			System.out.println("---------------------------------------"+getValue("weibo.dbpath"));
		}
}
