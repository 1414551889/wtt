package com.cmcc.report.util;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * 调用接口公共方法
 * @author HF
 *
 */
public class ApiUtil {
	
	static final Logger logger = LoggerFactory.getLogger(ApiUtil.class);
	public static final String REQUEST_TYPE_POST = "post";
	public static final String REQUEST_TYPE_GET = "get";
	//请求方法
	static String getDnSendStateUrl = AppPropertiesUtil.getValue("getDnSendStateUrl");
	//请求根路径
	static String baseUrl = AppPropertiesUtil.getValue("baseUrl");
	/** 
     * @Title: getDnSendStateUrl 
     * @Description: TODO(用于发送获取用户参与活动资格的请求) 
     * @param @return  参数说明  1 dn 手机号 2 startTime 3 endTime  
     * @return Map    返回类型 
     * @throws 
     * @version 创建时间：2018年1月25日 上午9:51:19
    */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getDnSendStateUrl(String type,MultiValueMap<String, Object> map){
		String exchange = "";
    	if(REQUEST_TYPE_POST.equalsIgnoreCase(type)){
    		exchange = new RestTemplate().postForObject(baseUrl+getDnSendStateUrl, map, String.class);
    	}else{
    		exchange = new RestTemplate().getForObject(baseUrl+getDnSendStateUrl, String.class);
    	}
    	Map<String, Object> resultMap = JSON.parseObject(exchange, Map.class);
		logger.info("调用订单请求接口：返回结果：" + exchange);
		return resultMap;
	}

}
