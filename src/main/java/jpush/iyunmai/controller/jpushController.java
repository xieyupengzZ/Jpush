
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
@Controller
public class jpushController {
	
		private static  final Logger logger =  LoggerFactory.getLogger(jpushController.class);
		//开发版android
		// private static final String appKey ="***";
		//private static final String masterSecret = "***";
		// 开发版ios
		 //private static final String appKey = "***";
		 //private static final String masterSecret = "***";
		 //正式版(推送)
		 private static final String appKey ="***";
		 private static final String masterSecret = "***";
		 
		 public static  String TITLE = "";
	     public static  String ALERT = "";
	     public static  String MSG_CONTENT = "msgContent";
	     public static  String REGISTRATION_ID = "0900e8d85ef";
	     public static  String TAG = "tag_api";
	     
	@RequestMapping(value = "submitForm.do",method = { RequestMethod.POST}, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String submitForm(@RequestParam("title") String title,@RequestParam("context") String alert,@RequestParam("platform") String platform,@RequestParam("file") MultipartFile file){
			JSONObject result = new JSONObject();
			JSONObject info = new JSONObject();
			result.put("info", info);
			TITLE = title;
			ALERT = alert;
			
			String parentPath = "/上传文件/JpushBulk/";
			if(file!=null&&!"".equals(platform)){
				try {
						uploadFile(file.getInputStream(), parentPath, file.getOriginalFilename());//获取到的文件名包括后缀
				} catch (IOException e) {
					setInfo(info,false);
					e.printStackTrace();
					logger.info("uploadFile error:{}",e);
				}
				
				doPush(platform,new File(parentPath+file.getOriginalFilename()));
				setInfo(info,true);
			}else{
				setInfo(info,false);
			}
			logger.info("result:{}",JSON.toJSONString(result));
			return JSON.toJSONString(result);
		}
	
	public void setInfo(JSONObject info,boolean result){
		
		if(result){
			info.put("code", 0);
			info.put("mes", "操作成功！");
		}else{
			info.put("code",	 -1);
			info.put("mes", "操作失败！");
		}
	}
	/**
	 *  >>>>>>>>>>>>>>>>>>>>>功能函数>>>>>>>>>>>>>>>>>>>>>>>
	 * @return
	 */
	public static void doPush(String platform,File file){
		List<String>useridlist = new ArrayList<String>();
		useridlist = readFileByLines(file);
		System.out.println(platform);
		String[] platforms = platform.split(",");
		for(String s : platforms){
			System.out.println(s);
		}
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		/**
		 * >>>>>>>>>>>>>>>>>>>>>>>>设备判断>>>>>>>>>>>>>>>>>>>>
		 */
		if(platforms!=null&&platforms.length == 2)
		{
			System.out.println("ios and android : title"+TITLE+" context :"+ALERT);
			JpushBulkImportAndroidAndIos(useridlist,jpushClient);
		}
		else if(platforms!=null&&platforms.length == 1)
		{
			if(platforms[0].equals("I"))
			{
				System.out.println("ios : title"+TITLE+" context :"+ALERT);
				JpushBulkImportIos(useridlist,jpushClient);
			}
			if(platforms[0].equals("A"))
			{
				System.out.println("android : title"+TITLE+" context :"+ALERT);
				JpushBulkImportAndroid(useridlist,jpushClient);
			}
		}
	}
	
	public static void  JpushBulkImportAndroid(List<String> useridlist,JPushClient jpushClient)
	{
		List<String>useridlist2 = new ArrayList<String>();
		int m = 0;
		for(int i = 0 ; i <useridlist.size();i+=1000)
		{
			if(i+1000>useridlist.size())
			{
				useridlist2 = useridlist.subList(i, useridlist.size());
			}
			else{
				useridlist2 = useridlist.subList(i, i+1000);
			}
			 m++;
			PushPayload payload=buildPushObject_android_all_alertWithTitle(useridlist2);
			
			try {
	            PushResult result = jpushClient.sendPush(payload);
	            logger.info("Got result - " + result);
	            
	        } catch (APIConnectionException e) {
	            logger.error("Connection error. Should retry later. ", e);
	            
	        } catch (APIRequestException e) {
	            logger.error("Error response from JPush server. Should review and fix it. ", e);
	            logger.info("HTTP Status: " + e.getStatus());
	            logger.info("Error Code: " + e.getErrorCode());
	            logger.info("Error Message: " + e.getErrorMessage());
	            logger.info("Msg ID: " + e.getMsgId());
	        }
		}
	}
	public static void  JpushBulkImportIos(List<String> useridlist,JPushClient jpushClient)
	{
		List<String>useridlist2 = new ArrayList<String>();
		for(int i = 0 ; i <useridlist.size();i+=1000)
		{
			if(i+1000>useridlist.size())
			{
				useridlist2 = useridlist.subList(i, useridlist.size());
			}
			else{
				useridlist2 = useridlist.subList(i, i+1000);
			}
			PushPayload payload=buildPushObject_ios_all_alertWithTitle(useridlist2);
			
			try {
	            PushResult result = jpushClient.sendPush(payload);
	            logger.info("Got result - " + result);
	            
	        } catch (APIConnectionException e) {
	            logger.error("Connection error. Should retry later. ", e);
	            
	        } catch (APIRequestException e) {
	            logger.error("Error response from JPush server. Should review and fix it. ", e);
	            logger.info("HTTP Status: " + e.getStatus());
	            logger.info("Error Code: " + e.getErrorCode());
	            logger.info("Error Message: " + e.getErrorMessage());
	            logger.info("Msg ID: " + e.getMsgId());
	        }
		}
	}
	public static void  JpushBulkImportAndroidAndIos(List<String> useridlist,JPushClient jpushClient)
	{
		List<String>useridlist2 = new ArrayList<String>();
		for(int i = 0 ; i <useridlist.size();i+=1000)
		{
			if(i+1000>useridlist.size())
			{
				useridlist2 = useridlist.subList(i, useridlist.size());
			}
			else{
				useridlist2 = useridlist.subList(i, i+1000);
			}
			PushPayload payloadAll=buildPushObject_all_all_alert(useridlist2);
			try {
	            PushResult resultAll = jpushClient.sendPush(payloadAll);
	            logger.info("Got resultIOS - " + resultAll);
	            
	        } catch (APIConnectionException e) {
	            logger.error("Connection error. Should retry later. ", e);
	            
	        } catch (APIRequestException e) {
	            logger.error("Error response from JPush server. Should review and fix it. ", e);
	            logger.info("HTTP Status: " + e.getStatus());
	            logger.info("Error Code: " + e.getErrorCode());
	            logger.info("Error Message: " + e.getErrorMessage());
	            logger.info("Msg ID: " + e.getMsgId());
	        }
		}
	}
	
	public static PushPayload buildPushObject_android_all_alertWithTitle(List<String> useridlist)
	{
		return PushPayload.newBuilder()
				.setPlatform(Platform.android())
				.setAudience(Audience.newBuilder()
											.addAudienceTarget(AudienceTarget.alias(useridlist))
											.build())
				.setNotification(Notification.android(ALERT, TITLE, null))
				.build();
	}
	public static PushPayload buildPushObject_ios_all_alertWithTitle(List<String> useridlist)
	{
		return PushPayload.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.newBuilder()
											.addAudienceTarget(AudienceTarget.alias(useridlist))
											.build())
				.setNotification(Notification.newBuilder()
												.addPlatformNotification(IosNotification.newBuilder()
														.setAlert(ALERT)
														.setBadge(1)
														.build())
												.build())
				.setOptions(Options.newBuilder()
									.setApnsProduction(true)
									.build())
				
				.build();
	}
	public static PushPayload buildPushObject_all_all_alert(List<String> useridlist) {
		  return new Builder()
        .setPlatform(Platform.all())
        .setAudience(Audience.newBuilder()
					.addAudienceTarget(AudienceTarget.alias(useridlist))
					.build())
		  .setOptions(Options.newBuilder()
								.setApnsProduction(true)
								.build())
        .setNotification(Notification.alert(ALERT)).build();
  }
	/**
     * 以行为单位读取文件
     */
    public static List<String> readFileByLines(File file) {
    	
    	List<String> useridlist = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                //System.out.println("line : " + line + ": " + tempString);
                if(!"".equals(tempString))
            		useridlist.add(tempString.trim());
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	
                }
            }
        }
		return useridlist;
    }

	/**
	 * 文件输出
	 * @param filestream
	 * @param path
	 * @param filename
	 */
	public void uploadFile(InputStream filestream,String path,String filename){
		FileOutputStream outputStream = null;
		File file = createFileWithParentPath(path,filename);
		try {
			 outputStream = new FileOutputStream(file);
			byte[] bytes = new byte[1024*1024];
			int byteread;
			while((byteread = filestream.read(bytes))!=-1){
				outputStream.write(bytes,0,byteread);
				outputStream.flush();
			}
			outputStream.close();
			filestream.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("upload error : "+e);
		}
	}
	/**
	 * 创建新文件，包括父路径
	 */
	public static File createFileWithParentPath(String parentPath,String fileName){
		File file = new File(parentPath+fileName);
		if(!file.getParentFile().exists()){//判断父路径（parentPath/file.getParentFile）是否存在
			new File(parentPath).mkdirs();//不存在的父文件全部创建
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
