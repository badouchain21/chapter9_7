package com.badou.platform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.badou.brms.attach.model.Attach;
import com.badou.brms.fileupload.AttachFactory;
import com.badou.platform.model.ResourceOperEntity;
import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:59:14
 * @todo 下载zip包抽象工厂类
 */
public abstract class DownloadZipFactory extends ZipFactory{

	protected static Logger logger = Logger.getLogger(DownloadZipFactory.class);
	
	/**
	 * 根据对象下载文件
	 * @param oper
	 * @throws Exception if has error(直接往外抛)
	 */
	public abstract void downloadFile(ResourceOperEntity oper) throws Exception;

	/**
	 * 根据url下载文件到指定路径
	 * @param url
	 * @param filePath
	 * @param dataMap
	 * @throws Exception if has error(直接往外抛)
	 */
	public void downloadFile(String url, String filePath, Map<String,Object> dataMap) throws Exception{
        try {  
        	Iterator<Map.Entry<String,Object>> it = dataMap.entrySet().iterator();
        	StringBuffer buffer = new StringBuffer(url+"&");
            while (it.hasNext())  
            {  
                Map.Entry<String,Object> entry = it.next();  
                Object key = entry.getKey();  
                buffer.append(key);  
                buffer.append('=');  
                Object value = entry.getValue();  
                buffer.append(value);  
                if (it.hasNext())  
                {  
                    buffer.append("&");  
                }  
            }
            // 创建HttpClient对象
    		CloseableHttpClient client = HttpClients.createDefault(); 
            HttpGet get = new HttpGet(buffer.toString());
            HttpResponse response = client.execute(get);  
            
            // response对象的getEntity()方法，返回HttpEntity对象。该对象中包含了服务器页面body体之内的内容。  
            HttpEntity entity = response.getEntity(); 
            FileOutputStream downloadFile = null;
            InputStream is = null;
            int index;  
			byte[] bytes = new byte[1024]; 
            try {
				is = entity.getContent(); 
				downloadFile = new FileOutputStream(filePath);  
				while ((index = is.read(bytes)) != -1) {  
				    downloadFile.write(bytes, 0, index);  
				    downloadFile.flush();  
				}
			} catch (Exception e) {
				logger.error(e);
			}  finally{
				if(downloadFile != null){
					downloadFile.close();  
				}
				if(is != null){
					is.close();
				}
			}
        } catch (Exception e) {
			logger.error(e);
        }  
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:37:33
	 * @todo 解压
	 * @param zipFilePath
	 * @param pwd
	 * @return 临时文件存储路径
	 * @throws Exception if has error(直接往外抛)
	 */
	public String unzip(String zipFilePath, String pwd) throws Exception {
		String tempPath = getTempPath();
		File tempFile = new File(tempPath);
		if(!tempFile.exists()){
			tempFile.mkdir();
		}
		ZipUtils.unzip(zipFilePath, tempPath, pwd);
		return tempPath;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:38:03
	 * @todo 将文件转为字节流并返回
	 * @param fileName
	 * @return 文件字节流 
	 */
	public String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName); 
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(filecontent);
        } catch (IOException e) {
			logger.error(e);
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {
			logger.error(e);
            return null;  
        }  
    }

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
}
