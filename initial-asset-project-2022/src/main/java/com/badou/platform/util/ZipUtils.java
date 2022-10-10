package com.badou.platform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:55:38
 * @todo 使用java.util.zip.ZipInputStream的压缩解压类
 */
public final class ZipUtils {

	protected static Logger logger = Logger.getLogger(ZipUtils.class);

	/**
	 * 设置缓冲值
	 */
	static final int BUFFER = 8192;

	private static final String ALGORITHM = "PBEWithMD5AndDES";

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:25:49
	 * @todo 压缩文件
	 * @param inputFile 文件路径
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void zip(String inputFile)
			throws Exception {
		zip(null, new File(inputFile), null);
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:26:39
	 * @todo 压缩文件（加密）
	 * @param zipFileName 文件名
	 * @param inputFile 文件路径
	 * @param pwd 密码
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void zip(String zipFileName, String inputFile, String pwd)
			throws Exception {
		zip(zipFileName, new File(inputFile), pwd);
	}

	/**
	 * 功能描述：压缩指定路径下的所有文件
	 * 
	 * @param zipFileName
	 *            压缩文件名(带有路径)
	 * @param inputFile
	 *            指定压缩文件夹
	 * @return
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void zip(String zipFileName, String inputFile)
			throws Exception {
		zip(zipFileName, new File(inputFile), null);
	}

	/**
	 * 功能描述：压缩文件对象
	 * 
	 * @param zipFileName
	 *            压缩文件名(带有路径)
	 * @param inputFile
	 *            文件对象
	 * @return
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void zip(String zipFileName, File inputFile, String pwd)
			throws Exception {
		if(StringUtils.isBlank(zipFileName)){
			zipFileName = inputFile.isDirectory() ? inputFile.getAbsolutePath() : inputFile.getParentFile().getAbsolutePath();
			zipFileName += ".zip";
		}
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		try{
			zip(out, inputFile, "", pwd);
		}finally{
			IOUtils.closeQuietly(out);
		}
		
	}

	/**
	 * 
	 * @param outputStream
	 *            压缩输出流对象
	 * @param file 要压缩的文件或目录
	 * @param base 压缩到目标路径
	 * @param pwd 密码
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void zip(ZipOutputStream outputStream, File file,
			String base, String pwd) throws Exception {
		if (file.isDirectory()) {
			File[] fl = file.listFiles();
			outputStream.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(outputStream, fl[i], base + fl[i].getName(), pwd);
			}
		} else {
			outputStream.putNextEntry(new ZipEntry(base));
			FileInputStream inputStream = new FileInputStream(file);
			try{
				// 普通压缩文件
				if (pwd == null || pwd.trim().equals("")) {
					int b;
					while ((b = inputStream.read()) != -1) {
						outputStream.write(b);
					}
				}else {// 给压缩文件加密
					PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
					SecretKeyFactory keyFactory = SecretKeyFactory
							.getInstance(ALGORITHM);
					SecretKey passwordKey = keyFactory.generateSecret(keySpec);
					byte[] salt = new byte[8];
					Random rnd = new Random();
					rnd.nextBytes(salt);
					int iterations = 100;
					PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,
							iterations);
					Cipher cipher = Cipher.getInstance(ALGORITHM);
					cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
					outputStream.write(salt);
					byte[] input = new byte[64];
					int bytesRead;
					while ((bytesRead = inputStream.read(input)) != -1) {
						byte[] output = cipher.update(input, 0, bytesRead);
						if (output != null) {
							outputStream.write(output);
						}
					}
					byte[] output = cipher.doFinal();
					if (output != null) {
						outputStream.write(output);
					}
					outputStream.flush();
				}
			}finally{
				IOUtils.closeQuietly(inputStream);
			}
		}
		file.delete();
	}
	/**
	 * 解压文件
	 * @param zipFileName　压缩文件全路径名称
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void unzip(String zipFileName)
			throws Exception {
		 unzip(zipFileName, null);
	}
	/**
	 * 解压文件
	 * @param zipFileName　压缩文件全路径名称
	 * @param outputDirectory 要解压的位置
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void unzip(String zipFileName, String outputDirectory)
			throws Exception {
		unzip(zipFileName, outputDirectory, null);
	}

	/**
	 * 功能描述：将压缩文件解压到指定的文件目录下
	 * 
	 * @param zipFileName
	 *            压缩文件名称(带路径)
	 * @param outputDirectory
	 *            指定解压目录
	 * @return
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void unzip(String zipFileName, String outputDirectory,
			String pwd) throws Exception {
		File zipFile = new File(zipFileName);
		unzip(zipFile, outputDirectory, pwd);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:28:17
	 * @todo 解压（有密码）
	 * @param zipFile 文件
	 * @param outputDirectory 解压输出路径
	 * @param pwd 密码
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void unzip(File zipFile, String outputDirectory, String pwd)
			throws Exception {
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(
				zipFile));
		if(StringUtils.isBlank(outputDirectory)){
			outputDirectory = zipFile.getParentFile().getAbsolutePath()+File.separator+zipFile.getName().substring(0, 
					zipFile.getName().lastIndexOf("."));
		}
		unzip(inputStream, outputDirectory, pwd);
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:28:55
	 * @todo 解压（有密码）
	 * @param inputStream 文件输入流
	 * @param outputDirectory 输出路径
	 * @param pwd 密码
	 * @throws Exception if has error(直接往外抛)
	 */
	public static void unzip(ZipInputStream inputStream,
			String outputDirectory, String pwd) throws Exception {
		ZipEntry zipEntry = null;
		FileOutputStream outputStream = null;
		try {
			while ((zipEntry = inputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File file = new File(outputDirectory + File.separator
							+ name);
					file.mkdir();
				} else {
					try{
						File file = new File(outputDirectory + File.separator
								+ zipEntry.getName());
						file.createNewFile();
						outputStream = new FileOutputStream(file);
						// 普通解压缩文件
						if (pwd == null || pwd.trim().equals("")) {
							int b;
							while ((b = inputStream.read()) != -1) {
								outputStream.write(b);
							}
						}
						// 解压缩加密文件
						else {
							PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
							SecretKeyFactory keyFactory = SecretKeyFactory
									.getInstance(ALGORITHM);
							SecretKey passwordKey = keyFactory
									.generateSecret(keySpec);
							byte[] salt = new byte[8];
							inputStream.read(salt);
							int iterations = 100;
							PBEParameterSpec parameterSpec = new PBEParameterSpec(
									salt, iterations);
							Cipher cipher = Cipher.getInstance(ALGORITHM);
							cipher.init(Cipher.DECRYPT_MODE, passwordKey,
									parameterSpec);
							byte[] input = new byte[64];
							int bytesRead;
							while ((bytesRead = inputStream.read(input)) != -1) {
								byte[] output = cipher.update(input, 0, bytesRead);
								if (output != null) {
									outputStream.write(output);
								}
							}
							byte[] output = cipher.doFinal();
							if (output != null) {
								outputStream.write(output);
							}
							outputStream.flush();
						}
					}finally{
						IOUtils.closeQuietly(outputStream);
					}
				}
			}
		} catch (IOException ex) {
			throw new Exception("解压读取文件失败");
		} catch (Exception ex) {
			throw new Exception("解压文件密码不正确");
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	/**
	 * 获取zip包里面的文件流
	 * @param zipFile
	 * @return zip包中的文件流集合
	 * @throws Exception if has error(直接往外抛)
	 */
	public static Map<String, InputStream> loadFiles(File zipFile) throws Exception {
		Charset charset = getZipCharset(zipFile); //获取zip的文件编码
		Map<String, InputStream> map = new HashMap<String, InputStream>();
		ZipEntry ze = null;
		try (ZipFile zip = new ZipFile(zipFile, charset);
			 	ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile), charset)) {

			do {
				if (ze != null && !ze.isDirectory()) {
					map.put(ze.getName(), zip.getInputStream(ze));
				}
			} while ((ze = zin.getNextEntry()) != null);
		}
		return map;
	}
	
	private static final String[] CHARSET_ARR = {"UTF-8", "GBK", "GB2312", "ISO-8859-1"};
	
	/**
	 * 获取zip文件的字符编码
	 * @author ztl
	 * @Date 2018年1月12日 下午3:10:32
	 * @param zipFile
	 * @return 字符集编码
	 * @throws Exception if has error(直接往外抛)
	 */
	public static Charset getZipCharset(File zipFile) throws Exception{
		Charset charset = null;
		ZipInputStream zin = null;
		for (String c : CHARSET_ARR) {
			try {
				charset = Charset.forName(c);
				zin = new ZipInputStream(new FileInputStream(zipFile), charset);
				zin.getNextEntry();
				break;
			} catch (Exception e) {
				logger.error(e);
			} finally {
				if (zin != null){
					zin.close();
				}
			}
		}
		return charset;
	}
}
