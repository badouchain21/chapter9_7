package com.badou.platform.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.badou.brms.attach.model.Attach;
import com.badou.brms.fileupload.AttachFactory;
import com.badou.platform.model.ResourceOperEntity;
import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:56:06
 * @todo 上传zip压缩包工厂类
 */
public abstract class UploadZipFactory extends ZipFactory{

	protected static Logger logger = Logger.getLogger(UploadZipFactory.class);

	/**
	 * 压缩文件
	 * 
	 * @param file
	 *            需要压缩的文件
	 * @param savePath
	 *            压缩文件路径
	 * @param pwd
	 *            压缩文件的密码
	 * @return 文件路径
	 * @throws Exception if has error(直接往外抛)
	 */
	public static String zip(File file, String savePath, String pwd)
			throws Exception {
		String zipFilePath = savePath + ".zip";
		ZipUtils.zip(zipFilePath, savePath, pwd);
		// 删除已经压缩的文件
		if (file.exists()){
			file.deleteOnExit();
		}
		return zipFilePath;
	}

	/**
	 * 根据对象上传文件
	 * @param oper
	 * @throws Exception if has error(直接往外抛)
	 */
	public abstract void uploadFile(ResourceOperEntity oper,String resourceId,Map<String,String> dataMap) throws Exception;

	/**
	 * 保存到附件
	 * @param filePath
	 * @param resourceId
	 * @return 附件实体对象
	 * @throws Exception if has error(直接往外抛)
	 */
	public Attach saveAttach(String filePath, String resourceId) throws Exception {
		File file = new File(filePath);
		return AttachFactory.upload(resourceId, file);
	}

	/**
	 * 上传压缩包
	 * @param uploadUrl 上传路径
	 * @param filePath 文件路径
	 * @param dataMap 请求参数
	 * @return 结果信息
	 */
	public String uploadZip(String uploadUrl, String filePath, Map<String,String> dataMap) {
		StringBuffer msg = new StringBuffer();
		// 创建HttpClient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//0)显示传递参数
		if(null != dataMap){
 			for(String key : dataMap.keySet()){
				/*StringBody stringBody = new StringBody(dataMap.get(key), ContentType.TEXT_PLAIN);
				builder.addPart(key, stringBody);*/
			//	builder.addTextBody(key, dataMap.get(key), ContentType.TEXT_PLAIN);
				uploadUrl+="&"+key+"="+dataMap.get(key);
			}
		}
		
		// 1)构建POST请求
		HttpPost post = new HttpPost(uploadUrl);
		InputStream inputStream = null;
		try {
			// 2)获取文件流
			inputStream = new FileInputStream(filePath);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			// 3）第一个参数为 相当于 Form表单提交的file框的name值 第二个参数就是我们要发送的InputStream对象了 第三个参数是文件名
			builder.addBinaryBody("uploadFile", inputStream, ContentType.create("multipart/form-data"), filePath);
			// 4)构建请求参数 普通表单项
			/*if(null != dataMap){
				ContentType contentType = ContentType.create("text/plain",Charset.forName("UTF-8"));
				for(String key : dataMap.keySet()){
					StringBody stringBody = new StringBody(dataMap.get(key), ContentType.TEXT_PLAIN);
					builder.addPart(key, stringBody);
				//	builder.addTextBody(key, dataMap.get(key), ContentType.TEXT_PLAIN);
					builder.addTextBody(key, dataMap.get(key),contentType); 
				}
			}*/
			HttpEntity entity = builder.build();
			post.setEntity(entity);
			
			// 4)发送请求
			HttpResponse response = client.execute(post);
			entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
				// 转换为字节输入流
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Consts.UTF_8));
				String body = null;
				while ((body = br.readLine()) != null) {
					msg.append(body);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			msg.append(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return msg.toString();
	}
}
