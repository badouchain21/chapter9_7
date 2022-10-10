package com.bdc.common.utils;

import com.badou.brms.attach.AttachFactory;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 *
 * @ClassName FileUtils
 * @author zzl
 * @Date 2018年2月8日 下午3:35:15
 * @version 1.0.0
 */
public class FileUtils {

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 *
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public static void inputStreamToFile(InputStream is, File file) {
		BufferedInputStream bis = new BufferedInputStream(is);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write((byte) c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 *
	 * 描述：获取文件名后缀
	 *
	 * @param fileName
	 * @return
	 * @author qd
	 * @throws Exception
	 */
	public static String getFileSuffix(String fileName) throws Exception {
		int index = fileName.lastIndexOf(".");
		if (index == -1) {
			return "";
		}
		return fileName.substring(index + 1, fileName.length());
	}

	/**
	 *
	 * 描述：获取文件名后缀
	 *
	 * @param fileName
	 * @return
	 * @author qd
	 * @throws Exception
	 */
	public static String getFileSuffix(File file) throws Exception {
		if (file.isDirectory()) {
			return "";
		}
		return getFileSuffix(file.getName());
	}

	/**
	 * @Description (获取文件md5值)
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByFile(File file) throws Exception {
		String value = null;
		FileInputStream in = null;
		FileChannel channel = null;
		try {
			in = new FileInputStream(file);
			channel = in.getChannel();
			MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (channel != null) {
					channel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return value;
	}

	/**
	 * @Description (获取文件md5值)
	 * @throws FileNotFoundException
	 */
	public static String getMd5ByInputStream(FileInputStream in, long fileLength) throws Exception {
		String value = null;
		FileChannel channel = null;
		try {
			channel = in.getChannel();
			MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (channel != null) {
				channel.close();
			}
		}
		return value;
	}

	public static void inputstreamtofile(InputStream ins, File file) throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		try {
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (os != null) {
				os.close();
			}
			if (ins != null) {
				ins.close();
			}

		}
	}

	/**
	 *
	 * 描述：根据文件与文件名获取一个Attach
	 *
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 * @author qd
	 */
	public static Attach getAttach(File file, String fileName) throws Exception {
		byte content[] = null;
		Attach attach = new Attach();
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			content = new byte[is.available()];
			is.read(content);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LogonCertificate logon = LogonCertificateHolder.getLogonCertificate();
		String suffix = "";
		suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		attach.setName(fileName);
		attach.setFileName(fileName);
		attach.setFileContent(content);
		attach.setFileSize(content.length);
		attach.setExtendName(suffix);
		attach.setGenPersonId(logon.getUserId());
		attach.setGenPersonName(logon.getLoginId());
		return attach;
	}

	/**
	 *
	 * 描述：根据attachId跟文件名获取一个attach
	 *
	 * @param attachId
	 * @param fileName
	 * @return
	 * @throws Exception
	 * @author qd
	 */
	public static Attach getAttach(String attachId, String fileName) throws Exception {
		Attach attach = new Attach();
		byte content[] = null;
		InputStream is = null;
		try {
			AttachFactory attachFactory = InstanceFactory.getInstance(AttachFactory.class);
			is = attachFactory.downloadFile(attachId);
			content = new byte[is.available()];
			is.read(content);
			LogonCertificate logon = LogonCertificateHolder.getLogonCertificate();
			String suffix = "";
			suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			attach.setName(fileName);
			attach.setFileName(fileName);
			attach.setFileContent(content);
			attach.setFileSize(content.length);
			attach.setExtendName(suffix);
			if (EmptyUtil.isNotEmpty(attach)) {
				attach.setGenPersonId(logon.getUserId());
				attach.setGenPersonName(logon.getLoginId());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return attach;
	}

	/**
	 *
	 * 描述：获取一个attach
	 *
	 * @param is
	 * @param fileName
	 * @return
	 * @throws Exception
	 * @author qd
	 */
	public static Attach getAttach(InputStream is, String fileName) throws Exception {
		byte content[] = null;
		Attach attach = new Attach();
		try {
			content = new byte[is.available()];
			is.read(content);
			LogonCertificate logon = LogonCertificateHolder.getLogonCertificate();
			String suffix = "";
			suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			attach.setName(fileName);
			attach.setFileName(fileName);
			attach.setFileContent(content);
			attach.setFileSize(content.length);
			attach.setExtendName(suffix);
			if (EmptyUtil.isNotEmpty(attach)) {
				attach.setGenPersonId(logon.getUserId());
				attach.setGenPersonName(logon.getLoginId());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return attach;
	}

	public static void closeInputStream(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeOutputStream(OutputStream outputStream) {
		if (outputStream != null) {
			try {
				outputStream.close();
				outputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * downloadFromUrl
	 * @param url
	 * @param dir
	 * @return
	 */
	 public static File downloadFromUrl(String url,String dir) {

		 try {
			 String fileName = getFileNameFromUrl(url);
			 File f = new File(dir+"/" + fileName);
			 download(url,dir);
			 return f;
		 } catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
	 }
	 /**
	  * getFileNameFromUrl
	  * @param url
	  * @return
	  */
	 public static String getFileNameFromUrl(String url){
	        String name = new Long(System.currentTimeMillis()).toString() + ".X";
	        int index = url.lastIndexOf("/");
	        if(index > 0){
	            name = url.substring(index + 1);
	            if(name.trim().length()>0){
	                return name;
	            }
	        }
	        return name;
	    }

/**
	 * description: 使用url 下载远程文件
	 * @param urlPath  --- url资源
	 * @param targetDirectory --- 目标文件夹
	 * @throws Exception
	 * @return void
	 * @version v1.0
	 * @author w
	 * @date 2019年9月3日 下午8:29:01
	 */
	public static void download(String urlPath , String targetDirectory) throws Exception {
		// 解决url中可能有中文情况
		System.out.println("url:"+ urlPath);
		HttpURLConnection http =null;
		InputStream inputStream=null;
		OutputStream out=null;
		try {
			URL url = new URL(urlPath);
			http = (HttpURLConnection)url.openConnection();
			http.setConnectTimeout(3000);
			// 设置 User-Agent 避免被拦截
			http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
			String contentType = http.getContentType();
			System.out.println("contentType: "+ contentType);
			// 获取文件大小
			long length = http.getContentLengthLong();
			System.out.println("文件大小："+(length / 1024)+"KB");
			// 获取文件名
			String fileName = getFileName(http , urlPath);
			inputStream = http.getInputStream();
			byte[] buff = new byte[1024*10];
			out = new FileOutputStream(new File(targetDirectory,fileName));
			int len ;
			int count = 0; // 计数
			while((len = inputStream.read(buff)) != -1) {
				out.write(buff, 0, len);
				out.flush();
				++count ;
			}
			System.out.println("count:"+ count);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			// 关闭资源
			if (out!=null)
				out.close();
			if(inputStream!=null)
				inputStream.close();
			if(http!=null)
				http.disconnect();
		}

	}
	/**
	 * description: 获取文件名
	 * @param http
	 * @param urlPath
	 * @throws UnsupportedEncodingException
	 * @return String
	 * @version v1.0
	 * @author w
	 * @date 2019年9月3日 下午8:25:55
	 */
	private static String getFileName(HttpURLConnection http , String urlPath) throws UnsupportedEncodingException {
		String headerField = http.getHeaderField("Content-Disposition");
		String fileName = null ;
		if(null != headerField) {
			String decode = URLDecoder.decode(headerField, "UTF-8");
			fileName = decode.split(";")[1].split("=")[1].replaceAll("\"", "");
			System.out.println("文件名是： "+ fileName);
		}
		if(null == fileName) {
			// 尝试从url中获取文件名
			String[] arr  = urlPath.split("/");
			fileName = arr[arr.length - 1];
			System.out.println("url中获取文件名："+ fileName);
		}
		return fileName;
	}
}
