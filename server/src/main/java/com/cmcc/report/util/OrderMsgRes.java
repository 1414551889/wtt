package com.cmcc.report.util;



public enum OrderMsgRes {
	WEIDINGGOU("N","-1"),//订单未生成
    DINGGOUING("a","0"),//订单处理中
    DINGGOUED("D","1"),//订单已完成
    MOREN("g","2");//退订
	/** 
	   * 库中状态名称。 
	   */
	  private String value; 
	  /** 
	   * 需要转化的
	   */
	  private String text; 
	  /** 
	   * @param status 库中状态名称
	   * @param desc 需要获取的状态值
	   */
	  private OrderMsgRes(String status, String desc) { 
	    value = status; 
	    text = desc; 
	  } 
	  /** 
	   * @return 当前枚举对象的值。 
	   */
	  public String getValue() {
			return value;
		}
	  /** 
	   * @return 当前状态的中文描述。 
	   */
	  public String getText() { 
	    return text; 
	  } 
	 

	/** 
	   * 根据活动状态的值获取枚举对象。 
	   * 
	   * @param res 结果值
	   * @return 枚举对象 
	   */
	  public static String getInstance(String res) { 
		  OrderMsgRes[] allStatus = OrderMsgRes.values(); 
	    for (OrderMsgRes ws : allStatus) { 
	      if (ws.getValue().equals(res)) { 
	        return ws.getText(); 
	      }
	    }
		return "1";     
	  } 
	}
