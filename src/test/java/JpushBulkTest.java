import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


public class JpushBulkTest {

	private static  final Logger logger =  LoggerFactory.getLogger(jpushController.class);
	//开发版android
	// private static final String appKey ="";
	//private static final String masterSecret = "";
	// 开发版ios
	 //private static final String appKey = "";
	 //private static final String masterSecret = "";
	 //正式版(推送)
	 private static final String appKey ="";
	 private static final String masterSecret = "";
	 
	 public static  String TITLE = "";
     public static  String ALERT = "";
     public static  String MSG_CONTENT = "msgContent";
     public static  String REGISTRATION_ID = "0900e8d85ef";
     public static  String TAG = "tag_api";
     
	public static void main(String[] args) {
		//pushTest1();
//		long time = System.currentTimeMillis();
//		getTestDate();
//		System.out.println("耗时："+(System.currentTimeMillis()-time));
		test2();
	}
	public static void pushTest1(){
		List<String> useridlist  = new ArrayList<String>();
		useridlist.add("");
		useridlist.add("");
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload=buildPushObject_all_all_alert(useridlist);
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
	
	public static void getTestDate(){
		String s = "";
		List<String> slist = new ArrayList<String>();
		for(int i = 1;i<200000;i++){
			s = String.format("%09d", i);
			slist.add(s);
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("TestUserids.txt");
			for(String sl : slist){
				pw.println(sl);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(pw!=null){
				pw.close();
			}
		}
	}
	/**
	 * 解码txt
	 */
	public static void test2(){
		File file = new File("/上传文件/JpushBulk/test2.txt");
		List<String> lists = readFileByLines(file);
		System.out.println("lists.size() "+lists.size());
		List<String> lists2 = new ArrayList<String>();
		for(String s : lists){
			lists2.add(URLDecoder.decode(s));
		}
		boolean flag = false;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
		fw = new FileWriter("/上传文件/JpushBulk/test23.txt", true);
		bw = new BufferedWriter(fw, 100);
		for (String s : lists2) {
		bw.write(s+"\r\n");
		}

		flag = true;
		} catch (IOException e) {
		System.out.println("写入文件出错");
		flag = false;
		} finally {
		if (bw != null) {
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}
		if (fw != null)
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	public static void uploadFile(InputStream filestream,String path,String filename){
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
