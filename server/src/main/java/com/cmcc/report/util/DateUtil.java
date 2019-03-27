package com.cmcc.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


/**
 * 实现Date类型和String类型相互转换
 *
 * @author zhouzhihao
 *         2014年4月29日
 */
public class DateUtil {

    /**
     * 将Date类型数据转化为字符串
     * zhouzhihao
     *
     * @param date 日期类型数据
     * @return
     */
    public static String date2String(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    //返回两个时间的相差天数
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }  

    /**
     * 将字符串转化为Date类型
     * zhouzhihao
     *
     * @param str 字符串 格式为 yyyy-MM-dd
     * @return
     * @throws Exception
     */
    public static Date string2Date(String str) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(str);
    }

    /**
     * 返回字符型日期
     *
     * @param date 日期
     * @return 返回字符型日期
     */
    public static String getDate(java.util.Date date) {
        return format(date, "yyyy/MM/dd");
    }

    /**
     * 格式化输出日期
     *
     * @param date   日期
     * @param format 格式
     * @return 返回字符型日期
     */
    public static String format(java.util.Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                java.text.DateFormat df = new java.text.SimpleDateFormat(format);
                result = df.format(date);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }

    /**
     * 日期相减
     *
     * @param date 日期
     * @param day  天数
     * @return 返回相减后的日期
     */
    public static Date diffDate(java.util.Date date, int day) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 格式化日期
     *
     * @param dateStr 字符型日期
     * @param format  格式
     * @return 返回日期
     */
    public static java.util.Date parseDate(String dateStr, String format) {
        java.util.Date date = null;
        try {
            java.text.DateFormat df = new java.text.SimpleDateFormat(format);
            String dt = dateStr.replaceAll("-", "/");
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]", "0");
            }
            date = df.parse(dt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取两个日期之间的时间间隔
     *
     * @param currentDate
     * @param dateEnd
     * @return 1秒  1分钟  1小时 1天
     */
    public static String getTwoDay(Object currentDate, Object dateEnd) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long day = 0;
        String days = "";
        try {
            java.util.Date dateS = null;
            if (currentDate instanceof String) {
                dateS = myFormatter.parse((String) currentDate);
            } else if (currentDate instanceof Date) {
                dateS = (Date) currentDate;
            } else {
                return "";
            }
            java.util.Date dateE = null;
            if (dateEnd instanceof String) {
                dateE = myFormatter.parse((String) dateEnd);
            } else if (dateEnd instanceof Date) {
                dateE = (Date) dateEnd;
            } else {
                return "";
            }
            day = (dateS.getTime() - dateE.getTime()) / (24 * 60 * 60 * 1000); //天
            if (day == 0) {
                day = (dateS.getTime() - dateE.getTime()) / (60 * 60 * 1000); //时
                if (day == 0) {
                    day = (dateS.getTime() - dateE.getTime()) / (60 * 1000); //分
                    if (day == 0) {
                        day = (dateS.getTime() - dateE.getTime()) / (1000); //秒
                        days = day + " 秒";
                    } else {
                        days = day + " 分钟";
                    }
                } else {
                    days = day + " 小时";
                }
            } else {
                days = day + " 天";
            }
        } catch (Exception e) {
            return "";
        }
        return days + "";
    }

    /**
     * 返回年份
     *
     * @param date 日期
     * @return 返回年份
     */
    public static int getYear(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.YEAR);
    }

    /**
     * 返回月份
     *
     * @param date 日期
     * @return 返回月份
     */
    public static int getMonth(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MONTH) + 1;
    }

    /**
     * 返回日份
     *
     * @param date 日期
     * @return 返回日份
     */
    public static int getDay(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回小时
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回分钟
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.MINUTE);
    }
    
    /**
     * 
     * @Title: dateToTimestamp 
     * @Description: 将Date日期转换成日期戳 
     * @param     
     * @return String
     */
	public static String dateToTimestamp(Date date,String format) {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strDate = sdf.format(date);
		Date d;
		try {
			d = sdf.parse(strDate);
			long l = d.getTime();
			String str = String.valueOf(l);
			result = str.substring(0, 10);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

    /**
     * 返回秒钟
     *
     * @param date 日期
     * @return 返回秒钟
     */
    public static int getSecond(java.util.Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.get(java.util.Calendar.SECOND);
    }

    public static boolean compareTime(String lastDate) {
        boolean flag = false;
        try {
            java.util.Date nowdate = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date d = sdf.parse(lastDate);
            flag = d.before(nowdate);
            if (flag) {
                System.out.print("早于今天");
            } else {
                System.out.print("晚于今天");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static  void main(String[] args) throws ParseException{
    	/*String DateStr = DateUtil.dateToTimestamp(new Date(), DateUtil.YMDHMS);
		String openId = UUID.randomUUID().toString();
		openId = openId+DateStr;
		System.out.println(new Date());
        System.out.println(new Date().getTime());	
        System.out.println(DateUtil.stampToDate("1517296954305"));*/
//        compareTime("2015-12-31 23:59:59");
    	getLastDayForWeek();
    }
    /**
     * Str ==> date
     * @param args
     */
    public static java.util.Date strParseDate(String dt, String format) {
        java.util.Date date = null;
        try {
            java.text.DateFormat df = new java.text.SimpleDateFormat(format);
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]", "0");
            }
            date = df.parse(dt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public final static String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 比较时间与当前时间相差多少秒以内，返回boolean值,若当前时间与之前时间差大于给的相差秒数则返回true(需要重新调接口),否则反之
     * @param startDateStr
     * @return
     */
    public static boolean calLastedTime(Long startDateTime, int ms) {
    	long a = new Date().getTime();
    	int c = (int)((a - startDateTime) / 1000);
    	if(c >= ms){
    		return true;
    	}
    	return false;
	}
    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    /* 
     * 将时间转换为时间戳
     */    
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    /*
     * 返回本周最后一天
     */
    public static String getLastDayForWeek() throws ParseException{
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());

        int d = 0;
        if(cal.get(Calendar.DAY_OF_WEEK)==1){
            d = +1;
        }else{
            d = 2-cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        //所在周开始日期
        // System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 5);
        //所在周结束日期
        //System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /*
     * 返回周几
     */
    public static int getDayForWeek(Date date) throws ParseException{
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int d =  cal.get(Calendar.DAY_OF_WEEK)-1;
        return d;
    }/*
     * 返回本周的周三或者周五的日期
     */
    public static String getEveryDay(int code)
            throws ParseException
    {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());

        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = +1;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        if (code == 3) {
            cal.add(Calendar.DAY_OF_WEEK, 2);
        }
        if (code == 5) {
            cal.add(Calendar.DAY_OF_WEEK, 4);
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

}
