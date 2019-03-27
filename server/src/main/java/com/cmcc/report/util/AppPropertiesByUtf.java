package com.cmcc.report.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * <b>类名称：</b>AppPropertiesUtil
 * <b>类描述：</b>加载app文件工具类 用于加载配置文件中的中文 
 * <b>创建人：</b>HF
 * <b>修改人：</b>HF
 * <b>修改时间：</b>2017-1-12 上午9:06:58
 * <b>修改备注：</b>
 * @version v1.0<br/>
 */
public class AppPropertiesByUtf {
	    private static Properties prop=new Properties();   
	    static{  
	        try {       
				prop.load(new InputStreamReader(AppPropertiesByUtf.class.getClassLoader().getResourceAsStream("app.properties"), "UTF-8"));    
	        } catch (IOException e) {  
	            e.printStackTrace();   
	        }  
	    }  
	    
	    public static String getValue(String key){  
	        return prop.getProperty(key);  
	    }  
	    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
			Properties prop=new Properties();         
			prop.load(new InputStreamReader(AppPropertiesByUtf.class.getClassLoader().getResourceAsStream("app.properties"), "UTF-8"));    
		
			System.out.println(prop.getProperty("exam_msgs"));
			
		}
}
