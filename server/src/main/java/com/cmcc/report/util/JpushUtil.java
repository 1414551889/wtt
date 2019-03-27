package com.cmcc.report.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


public class JpushUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(JpushUtil.class);  
	  
	
     // demo App defined in resources/jpush-api.conf   
	public static  String TITLE;  
    public static  String ALERT;  
    public static  String MSG_CONTENT;  
    public static  String REGISTRATION_ID = "0900e8d85ef";   
    public static  String TAG = "USER_001";  
    public static final String appKey="b8d9c942f749292cec56347b";//appKey
    public static final String masterSecret="d09a1200e66a12fcca61838e";//masterSecret
    public  static JPushClient jpushClient=null;  
      
    
    
    public static String getTITLE() {
		return TITLE;
	}

	public static void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public static String getALERT() {
		return ALERT;
	}

	public static void setALERT(String aLERT) {
		ALERT = aLERT;
	}

	public static String getMSG_CONTENT() {
		return MSG_CONTENT;
	}

	public static void setMSG_CONTENT(String mSG_CONTENT) {
		MSG_CONTENT = mSG_CONTENT;
	}

	public static String getREGISTRATION_ID() {
		return REGISTRATION_ID;
	}

	public static void setREGISTRATION_ID(String rEGISTRATION_ID) {
		REGISTRATION_ID = rEGISTRATION_ID;
	}

	public static String getTAG() {
		return TAG;
	}

	public static void setTAG(String tAG) {
		TAG = tAG;
	}

	public static JPushClient getJpushClient() {
		return jpushClient;
	}

	public static void setJpushClient(JPushClient jpushClient) {
		JpushUtil.jpushClient = jpushClient;
	}

	public static Logger getLog() {
		return LOG;
	}

	public static String getAppkey() {
		return appKey;
	}

	public static String getMastersecret() {
		return masterSecret;
	}

	public static void sendPush(PushPayload payload) {          
    	 jpushClient = new JPushClient(masterSecret, appKey, 3);  
         
         // HttpProxy proxy = new HttpProxy("localhost", 3128);  
         // Can use this https proxy: https://github.com/Exa-Networks/exaproxy  
          
           
         // For push, all you need do is to build PushPayload object.  
         //PushPayload payload = buildPushObject_all_all_alert();  
          //生成推送的内容，这里我们先测试全部推送  
        
           
           
         try {  
             System.out.println(payload.toString());  
             PushResult result = jpushClient.sendPush(payload);  
             System.out.println(result+"................................");  
               
             LOG.info("Got result - " + result);  
               
         } catch (APIConnectionException e) {  
             LOG.error("Connection error. Should retry later. ", e);  
               
         } catch (APIRequestException e) {  
             LOG.error("Error response from JPush server. Should review and fix it. ", e);  
             LOG.info("HTTP Status: " + e.getStatus());  
             LOG.info("Error Code: " + e.getErrorCode());  
             LOG.info("Error Message: " + e.getErrorMessage());  
             LOG.info("Msg ID: " + e.getMsgId());  
         }  
     }  
       
     public static PushPayload buildPushObject_all_all_alert() {  
         return PushPayload.alertAll(ALERT);  
     }  
       
     public static PushPayload buildPushObject_all_alias_alert() {  
         return PushPayload.newBuilder()  
                 .setPlatform(Platform.all())//设置接受的平台  
                 .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到  
                 .setNotification(Notification.alert(ALERT))  
                 .build();  
     }  
       
     public static PushPayload buildPushObject_android_tag_alertWithTitle() {  
         return PushPayload.newBuilder()  
                 .setPlatform(Platform.android())  
                 .setAudience(Audience.all())  
                 .setNotification(Notification.android(ALERT, TITLE, null))  
                 .build();  
     }  
       
     public static PushPayload buildPushObject_android_and_ios() {  
         return PushPayload.newBuilder()  
                 .setPlatform(Platform.android_ios())  
                 .setAudience(Audience.tag(TAG))  
                 .setNotification(Notification.newBuilder()  
                         .setAlert(ALERT)  
                         .addPlatformNotification(AndroidNotification.newBuilder()  
                                 .setTitle(TITLE).build())  
                         .addPlatformNotification(IosNotification.newBuilder()  
                                 .incrBadge(1)  
                                 .addExtra("from", "jPush").build())  
                         .build())  
                 .build();  
     }  
       
     public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {  
         return PushPayload.newBuilder()  
                 .setPlatform(Platform.ios())  
                 .setAudience(Audience.tag_and(TAG, "tag_all"))  
                 .setNotification(Notification.newBuilder()  
                         .addPlatformNotification(IosNotification.newBuilder()  
                                 .setAlert(ALERT)  
                                 .setBadge(5)  
                                 .setSound("")  
                                 .addExtra("from", "JPush")  
                                 .build())  
                         .build())  
                  .setMessage(Message.content(MSG_CONTENT))  
                  .setOptions(Options.newBuilder()  
                          .setApnsProduction(true)  
                          .build())  
                  .build();  
     }  
       
     public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {  
         return PushPayload.newBuilder()  
                 .setPlatform(Platform.android_ios())  
                 .setAudience(Audience.newBuilder()  
                         .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))  
                         .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))  
                         .build())  
                 .setMessage(Message.newBuilder()  
                         .setMsgContent(MSG_CONTENT)  
                         .addExtra("from", "JPush")  
                         .build())  
                 .build();  
     }  
     
     
     //description：自定义推送函数--分组推送
     //createTime： 2016.12.10
     //author： HF
     public static PushPayload buildPushObject_android_and_iosByTag(String tag,String title,String content,String ALERT,String type) {
     return PushPayload.newBuilder()
     .setPlatform(Platform.android_ios())
     .setAudience(Audience.tag(tag))
     .setOptions(Options.newBuilder().setApnsProduction(true).build())
     .setNotification(Notification.newBuilder()
     .setAlert(ALERT)
     .addPlatformNotification(AndroidNotification.newBuilder()
	 .addExtra("alertTitle", title)
	 .addExtra("alertMessage", ALERT)
	 .addExtra("content", content)
	 .addExtra("type", type)
     .setTitle(title).build())
     .addPlatformNotification(IosNotification.newBuilder()
     .incrBadge(1)
     .addExtra("alertTitle", title)
     .addExtra("alertMessage", ALERT)
     .addExtra("type", type)
     .addExtra("content", content).build())
     .build())
     .build();
     }

   
   
public static void main(String[] args) {
	JpushUtil.TITLE = "22";
	JpushUtil.ALERT = "test 推送1111";
	JpushUtil.MSG_CONTENT = "hefang";
	JpushUtil.TAG = "user_7";
  // Pushclients.testSendPush(Pushclients.buildPushObject_all_alias_alert());
	JpushUtil.sendPush(JpushUtil.buildPushObject_android_and_iosByTag(JpushUtil.TAG,JpushUtil.TITLE,JpushUtil.MSG_CONTENT,JpushUtil.ALERT,null));
}
}
