package com.bdc.common.utils;

import lombok.NonNull;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lombok.NonNull;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zz.gmhelper.BCECUtil;
import org.zz.gmhelper.SM2Util;
import org.zz.gmhelper.SM4Util;

/**
 * 加解密相关安全工具类
 * 上链metadata加密逻辑：<br>
 * 使用统一社会信用代码调用generateKey生成加密数据的密钥<br>
 * 调用encryptMetaData(metadata)加密密钥，上传到区块链 .<br>
 *
 * 授权逻辑<br>
 * 在点击hash地址跳转区块链浏览器的之前，先生成签名<br>
 * 签名signAuth(data) data的数据规则：授权企业的统一社会信用代码|申请企业的企业名称|授权截至日期 eg:test01;test02;20190903 <br>
 *     //测试
        SecurityUtil security = InstanceFactory.getInstance(SecurityUtil.class);
        String orgCode ="123";
        String reqorgName ="申请企业";
        //模拟metedata
        String metadata="{name:123}";
        String enMeta =security.encryptMetaData(security.generateKey(orgCode), metadata);
        System.out.println("enMeta:"+enMeta);


        //模拟授权 new Date().getTime() 截至日期的time
        String orignalSign =orgCode+security.COMM_SEPERTOR+reqorgName+security.COMM_SEPERTOR+new Date().getTime()+security.COMM_SEPERTOR+id;
        String sign = security.signAuth(orignalSign);
        System.out.println("sign:"+sign);
        String hexOrginalSign=security.str2HexStr(orignalSign);
 		浏览器传参：
         sign:  sign+URL_COMM_SEPERTOR+hexOrginalSign

        //区块链浏览器跳转验证模拟
        String hexOrginalSign=security.str2HexStr(orignalSign);
        boolean flag = security.verifySign(hexOrginalSign,sign);
        //验证通过
        if(flag){
        	String signData = security.hexStr2Str(hexOrginalSign);
        	String[] datas = signData.split(";");
        	String orgCode1 = datas[0];
        	String reqorgName1 = datas[1];
        	String dataString = datas[2];
        	long time = System.currentTimeMillis()- Long.parseLong(dataString);
            //解密
        	System.out.println(time);
        	String deMeta = security.decrypteMetaData(security.generateKey(orgCode1), enMeta);
        	System.out.println("deMeta:"+deMeta);

        }

 * @author lps
 *
 */
@Component
public class SecurityUtil {
	/**
	 * 默认编码UTF-8
	 */
	public static final String DEFAULT_ENCODING ="UTF-8";

	/**
	 * 使用算法SM4
	 */
	public static final String ALGORITHM_NAME = "SM4";

	private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	/**
	 * 默认分割号 ;
	 */
	public static final String COMM_SEPERTOR=";";

	/**
	 * URL默认分割号 ;
	 */
	public static final String URL_COMM_SEPERTOR="&";

	/**
	 * 系统私钥
	 */
//	@Value("${server.security.sm2.pri}")
	private String priHex ="0094C6D7E02A1C008B8DFB6C452B76788C979D9EAFFD2D79C2CDCFDA8649BFE5E1";
	/**
	 * 系统公钥-x
	 */
//	@Value("${server.security.sm2.pub-x}")
	private String pubX="5D65B827FDD9C9DD9E135E013C26C4F4FD9708F73B87F82D5592A62DC226C9E7";
	/**
	 * 系统公钥-y
	 */
//	@Value("${server.security.sm2.pub-y}")
	private String pubY="B7D05B3D4EB0FE25B28437D9D530060F44C8F6514C4BB477DDE631066343C8DF";
	/**
	 * 系统公钥-point
	 */
//	@Value("${server.security.sm2.pub-point}")
	private String pubPoint="045D65B827FDD9C9DD9E135E013C26C4F4FD9708F73B87F82D5592A62DC226C9E7B7D05B3D4EB0FE25B28437D9D530060F44C8F6514C4BB477DDE631066343C8DF";
	/**
	 * 区块链浏览器参数
	 */
	public static final String EXPLORER_SIGN ="sign";
	public static final String EXPLORER_ORIGNAL ="orignal";


	/**
	 * 根据原key值按照md5生成16位key
	 * useage: SecurityUtil.generateKey("123")
	 * @param orignalKey
	 * @return
	 */
	public  String  generateKey(@NonNull String orignalKey){
		String hexKey = encrypt16(orignalKey);
		return hexKey;
	}

	/**
	 * 加密数据
	 * @param key 加密密钥 16位密钥
	 * @param metaData 需要加密的数据
	 * @return 加密后数据
	 * useage: SecurityUtil.encryptMetaData("d3a8986bbd7f36a8","data");
	 * @throws Exception
	 */
	public  String encryptMetaData(@NonNull String key ,@NonNull String metaData) throws Exception{
		byte[] cipherText = null;
		try {
			cipherText = SM4Util.encrypt_Ecb_Padding(key.getBytes(DEFAULT_ENCODING),metaData.getBytes(DEFAULT_ENCODING));
			String hexCipherText = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.toHexString(cipherText);
		    return hexCipherText;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * 签名数据
	 * @param orignalData 签名元数据
	 * @return 签名后数据
	 * @throws Exception
	 */
	public  String signAuth(String orignalData) throws Exception{
		 try {
			ECPrivateKeyParameters priKey = new ECPrivateKeyParameters( new BigInteger(ByteUtils.fromHexString(priHex)), SM2Util.DOMAIN_PARAMS);
			byte[] sign = SM2Util.sign(priKey, str2HexStr(orignalData).getBytes(DEFAULT_ENCODING));
			return  ByteUtils.toHexString(sign);
		} catch (UnsupportedEncodingException | CryptoException e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * 验证签名
	 * @param orignalData 签名元数据
	 * @param sign 签名数据
	 * @return 签名后数据
	 * @throws Exception
	 */
	public  Boolean verifySign(String orignalData,String sign) throws Exception{
		 try {
			   ECPublicKeyParameters pubKey = BCECUtil.createECPublicKeyParameters(pubX, pubY, SM2Util.CURVE, SM2Util.DOMAIN_PARAMS);
	           if (SM2Util.verify(pubKey, orignalData.getBytes(DEFAULT_ENCODING), ByteUtils.fromHexString(sign))) {
	        	   	return true;
	            }
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * 解密数据
	 * @param key 加密密钥 16位密钥
	 * @param encryptMetaData 加密后数据
	 * @return 解密后数据
	 * useage: SecurityUtil.decrypteMetaData("d3a8986bbd7f36a8","encryptdata");
	 * @throws Exception
	 */
	public static String decrypteMetaData(@NonNull String key ,@NonNull String encryptMetaData) throws Exception{
		byte[] decryptedData = null;
		try {
			decryptedData = SM4Util.decrypt_Ecb_Padding(key.getBytes(DEFAULT_ENCODING),org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(encryptMetaData));
			return new String(decryptedData,DEFAULT_ENCODING);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchProviderException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	/**
	 * 字符串转换成为16进制(无需Unicode编码)
	 *
	 * @param str
	 * @return
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	/**
	 * 16进制直接转换成为字符串(无需Unicode解码)
	 *
	 * @param hexStr
	 * @return
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * @Description:加密-32位小写
	 * @author:liuyc
	 * @time:2016年5月23日 上午11:15:33
	 */
	public static String encrypt32(String encryptStr) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md5.digest(encryptStr.getBytes());
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			encryptStr = hexValue.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return encryptStr;
	}

	/**
	 * @Description:加密-16位小写
	 * @author:liuyc
	 * @time:2016年5月23日 上午11:15:33
	 */
	public static String encrypt16(String encryptStr) {
		return encrypt32(encryptStr).substring(6, 22);

	}

	/**
	 * 生成请求浏览器参数
	 * @param sign
	 * @param orignal
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String generateSignParam(String sign ,String orignal) throws UnsupportedEncodingException{
		String signParam="";
		Base64.Encoder encoder = Base64.getEncoder();
	   try {
		signParam =encoder.encodeToString((sign+URL_COMM_SEPERTOR+orignal+URL_COMM_SEPERTOR+System.currentTimeMillis()).getBytes(DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return signParam;
	}


	/**
	 * 解析请求浏览器参数
	 * @param sign
	 * @param orignal
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Map<String,String> decodeSignParam(String signParam ) throws UnsupportedEncodingException{
		Map<String,String> map = new HashMap<String,String>();
		Base64.Decoder decoder = Base64.getDecoder();
		try {
			byte[] decode = decoder.decode(signParam.getBytes(DEFAULT_ENCODING));
			String decodeString = new String(decode,DEFAULT_ENCODING);
			String[] params = decodeString.split(URL_COMM_SEPERTOR);
			if(params!=null && params.length==3){
				map.put("sign", params[0]);
				map.put("orignal", params[1]);
				return map;
			}else{
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}




	public String getPriHex() {
		return priHex;
	}

	public void setPriHex(String priHex) {
		this.priHex = priHex;
	}

	public String getPubX() {
		return pubX;
	}

	public void setPubX(String pubX) {
		this.pubX = pubX;
	}

	public String getPubY() {
		return pubY;
	}

	public void setPubY(String pubY) {
		this.pubY = pubY;
	}

	public String getPubPoint() {
		return pubPoint;
	}

	public void setPubPoint(String pubPoint) {
		this.pubPoint = pubPoint;
	}

	@SuppressWarnings("all")
	public static void main(String[] args) throws UnsupportedEncodingException {
		String key = "tonyStark";
		String originalData = "i am ironman123!@#!";

		String customsKey ="customs";
		try {
/*			String key1 =generateKey(key);
			String encryptMetaData = encryptMetaData(key1, originalData);
			String decryptMetaData = decrypteMetaData(key1, encryptMetaData);
			System.out.println(originalData.equals(decryptMetaData));

			String signOrignal = "企业名称|企业统一社会信用代码|"+new Date();*/
			AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPairParameter();
			ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
			ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

			System.out.println("Pri Hex:"
					+ ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
			System.out.println("Pub X Hex:"
	                + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
			System.out.println("Pub X Hex:"
	                + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
			System.out.println("Pub Point Hex:"
					  + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());
		/*	byte[] sign = SM2Util.sign(priKey, str2HexStr(signOrignal).getBytes(DEFAULT_ENCODING));
			System.out.println("SM2 sign without withId result:\n" + ByteUtils.toHexString(sign));

	        boolean flag = SM2Util.verify(pubKey, str2HexStr(signOrignal).getBytes(DEFAULT_ENCODING), sign);*/

//	        System.out.println(flag);


	       /* String pubXHex = ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase();
	        String pubYHex = ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase();
	        String pointHex = "04CA12B69E2EB098CD104908602D1A1B2A4C7663CB8650E332ED117ECD8A07D32F29FDBC50E17DB2AD27D3BC9F5F8BAD623A2C74AE0F2CB0537C8C191790DBD194";
	        ECPublicKeyParameters pubKey2 = BCECUtil.createECPublicKeyParameters(pubXHex, pubYHex, SM2Util.CURVE, SM2Util.DOMAIN_PARAMS);
	        System.out.println(str2HexStr(signOrignal));
            if (!SM2Util.verify(pubKey2, str2HexStr(signOrignal).getBytes(DEFAULT_ENCODING), sign)) {
               System.out.println("fail");
            }


	        System.out.println(hexStr2Str(str2HexStr(signOrignal)));*/


		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
