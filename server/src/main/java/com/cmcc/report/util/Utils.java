package com.cmcc.report.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.MimetypesFileTypeMap;

/**
 * Created by LY on 14-3-27 上午11:37
 */
public class Utils {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";

    //模糊查询时特殊字符的转义
    public static String escapeForSpecialChar(String content) {
        if (StringUtils.isBlank(content)) {//为空，返回空
            return "";
        }
        content = content.replace("\\", "\\\\");// 转义前的字符串不为空，执行替换操作
        content = content.replace("%", "\\%");
        content = content.replace("_", "\\_");
        return content;
    }

    /*按字节长度截取字符串 
        * @param str 将要截取的字符串参数
		* @param toCount 截取的字节长度 
		* @param more 字符串末尾补上的字符串 
		* @return 返回截取后的字符串 
	*/
    public static String substring(String str, int toCount, String more) {
        if (StringUtils.isBlank(str)) {//若字符串为空，直接返回
            return "";
        }
        if (str.trim().getBytes().length <= toCount) {//截取的字节长度大于字符串字节长度，直接返回
            return str;
        }
        int loop = 0;
        StringBuffer stringBuffer = new StringBuffer();
        char[] tempChar = str.toCharArray();
        for (int i = 0; i < tempChar.length && toCount > loop; i++) {//根据字符串字节获取截取后字符串
            String tmp = str.valueOf(tempChar[i]);
            byte[] b = tmp.getBytes();
            loop += b.length;
            stringBuffer.append(tempChar[i]);//
        }
        if (toCount == loop || (toCount == loop - 1)) {//添加末尾字符串
            stringBuffer.append(more);
        }
        return stringBuffer.toString();
    }

    //获取访问者IP
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int getStrlength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static <T> T getJsonObject(String json, Class<T> beanClass) throws Exception {
        return new ObjectMapper().readValue(json, beanClass);
    }

    /*
     * 时分、昨天 前天、日期
	 */
    public static String findTimeDiff(String time) {
        try {
            Calendar c = Calendar.getInstance();// 带时分秒的值
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df3 = new SimpleDateFormat("yy-MM-dd");
            long s = df1.parse(time).getTime();
            long e = df1.parse(df1.format(c.getTime())).getTime();
            if (e == s) {// 今天
                SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
                return df2.format(df.parse(time));
            } else if ((e - s) / (3600000 * 24) == 1) {// 昨天
                return "昨天 ";
            } else if ((e - s) / (3600000 * 24) == 2) {// 前天
                return "前天";
            } else {
                return df3.format(df1.parse(time));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未知时间";
        }
    }

    // 获取时间差方法  by liuyang
	/*
	 * 昨天 刚刚 1分钟内 分钟 1小时内 几小时 今天 日期
	 */
    public static String changeTimeDiff(String time) {
        try {
            Calendar c = Calendar.getInstance();// 带时分秒的值
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            long s = df1.parse(time).getTime();
            long e = df1.parse(df1.format(c.getTime())).getTime();
            if (e == s) {// 今天
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long timeLong = c.getTimeInMillis() - df2.parse(time).getTime();
                int b = (int) timeLong / 1000;
                if (b < 1) {
                    return "刚刚";
                } else if (1 <= b && b < 60) {
                    return b + "秒之前";
                } else if (b < 3600 && b >= 60) {
                    // //精确到分钟
                    return ((int) b / 60) + "分钟之前";
                } else if (b < 3600 * 24 && b >= 3600) {
                    // //精确到小时
                    return ((int) b / 3600) + "小时之前";
                } else {
                    return "未知时间";
                }
            } else if ((e - s) / (3600000 * 24) == 1) {// 昨天
                return "昨天 ";
            } else if ((e - s) / (3600000 * 24) == 2) {// 前天
                return "前天";
            } else {// 前天
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return df1.format(df2.parse(time));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "未知时间";
        }
    }

    public static Random _random = new Random();
    /**
     * 生成随机数 param length 随机数长度，长度不能大于10
     * @return String
     */
    public static String randomNum() {
        int x = _random.nextInt(999999);
        String wholeNum = ("000000" + x);
        return wholeNum.substring(wholeNum.length() - 6, wholeNum.length());
    }
    
    /**
     * 获取ip地址的信息，以json字符串的形式返回
     * 结合convertJsonStringToMap(json)方法使用可返回json对应的map
     * 例如：{"ret":1,"start":-1,"end":-1,"country":"\u4e2d\u56fd","province":"\u5e7f\u4e1c","city":"\u5e7f\u5dde","district":"","isp":"","type":"","desc":""};
     * @param ip
     * @return
     */
    public static String getIpInfo(String ip){
		if (Utils.isIP(ip)) {
			URL url = null;
			InputStream is = null;
			try {
				url = new URL(
						"http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="
								+ ip);
				is = url.openStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;
				line = br.readLine();
				return line.split("=")[1];
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;	
    }
    /**
     * 将字符串类型的json数据转换成为map对象并返回
     * @param json
     * @return
     */
//    public static Map convertJsonStringToMap(String json){
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	JSONObject object = JSONObject.fromObject(json);
//	    for (Object k : object.keySet()) {
//	        Object v = object.get(k);
//	        map.put(k.toString(), v);
//	    }
//	    return map;
//    }
    
    /**
     * 判断ip格式是否正确
     * @param ip
     * @return
     */
    public static  boolean isIP(String ip)
	 {
		 Pattern p = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
		 Matcher m = p.matcher(ip);
		 return m.matches();
	 }

    /*
    * 文件下载，因为下载时，需要显示原文件名，所以这里进行操作
    * @param request
    * @param response
    * @param filePathWithName :文件的物理路径，包含文件名：home/a/b/c/d/abc.txt
    * @param fileName：文件名，提示用户的文件名（实际的文件名）
    * @return
    * @throws UnsupportedEncodingException
    */
    public static HttpServletResponse fileDownload(HttpServletRequest request, HttpServletResponse response, String filePathWithName,String fileName) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType(CONTENT_TYPE);
        File file = new File(filePathWithName);// filePath是指欲下载的文件的路径。
        InputStream fis = new BufferedInputStream(new FileInputStream(file));// 以流的形式下载文件。
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        response.reset();// 清空response
        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){// IE浏览器
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        } else if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){// firefox浏览器
        	fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0){// chrome浏览器
            fileName = new String(fileName.getBytes("UTF-8"), "ISO_8859_1");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        } else if (request.getHeader("User-Agent").toLowerCase().indexOf("rv") > 0){// IE 11 浏览器
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        } else {
        	fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        	response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//        response.setContentType("application/x-msdownload");
//        response.setContentType("applicatoin/octet-stream");
        
        
        	Path path = Paths.get(filePathWithName);
            String contentType = null;
            try {
                contentType = Files.probeContentType(path);
            } catch (IOException e) {
           
                e.printStackTrace();
            }
        	 response.setContentType(contentType);
        	 if(contentType==null){
//        	 response.setContentType("multipart/form-data");
        	 response.setContentType("application/x-msdownload");
             }
        
       
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        return response;
    }
    
    // 移动、联通、电信手机号码判断
    public static int matchesPhoneNumber(String phone_number) {
        String cm = "^((13[4-9])|(147)|(15[0-2,7-9])|(18[2-4,7-8])|(178))\\d{8}$";//移动
        String cu = "^((13[0-2])|(145)|(15[5-6])|(18[5-6])|(176))\\d{8}$";//联通
        String ct = "^((133)|(153)|(18[0,1,9])|(177))\\d{8}$";//电信
        int flag;
        if (phone_number.matches(cm)) {
            flag = 1;
        } else if (phone_number.matches(cu)) {
            flag = 2;
        } else if (phone_number.matches(ct)) {
            flag = 3;
        } else {
            flag = 4;
        }
        return flag;

    }

    public static String secondToTimeString(long seconds) {
        String ret = "00:00:00";
        if(seconds == 0) return ret;
        String hour = String.format("%02d", (int)seconds/(60*60));
        String minute = String.format("%02d", (int)(seconds/60)%60);
        String second = String.format("%02d", (int)seconds%60);
        ret = hour+":"+minute+":"+second;
        return ret;
    }
public static void main(String[] args) {
	try {
		String usr = URLEncoder.encode("http://toupiao.hebpe.com/wxanswer/activity/index","UTF-8");
		System.out.println(usr);
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
