/**
 * Copyright (C) 2013 Tencent. All Rights Reserved
 *
 */
package com.youdao.dev.utils;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import android.util.Base64;

/**
 * <PRE>
 * 简介:
 * 
 *  标准RSA算法工具类， 支持公钥加密和解密，私钥加密和解密，私钥签名， 公钥验证签名等。
 *  默认公钥长度为1024位，加解密均支持超过117字节长度的输入。
 *     
 * 创建时间:  2013-1-23
 * </PRE>
 * 
 * @author erikchen
 */
public class RSAUtil {

	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

	/**
	 * 字符编码格式
	 */
	public static final String CHARSET = "utf-8";

	/**
	 * 私钥生成签名
	 * 
	 * @param src  签名源串
	 * @param privateKey  私钥
	 * @return  签名
	 */
	public static String sign(String src, String privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey, Base64.DEFAULT));
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			RSAPrivateKey privateK = (RSAPrivateKey) keyFactory
					.generatePrivate(pkcs8KeySpec);

			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateK);
			signature.update(src.getBytes(CHARSET));
			byte[] signRet = signature.sign();
			return new String(Base64.encode(signRet, Base64.NO_WRAP));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 公钥验证签名
	 * 
	 * @param src    签名源串
	 * @param sign   签名
	 * @param publicKey  公钥
	 * @return  验签是否成功
	 */
	public static boolean verify(String src, String sign, String publicKey) {
		try {
			byte[] encodedKey = Base64.decode(publicKey, Base64.DEFAULT);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			
			RSAPublicKey publicK = (RSAPublicKey) keyFactory
					.generatePublic(keySpec);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicK);
			signature.update(src.getBytes(CHARSET));
			
			return signature.verify(Base64.decode(sign, Base64.NO_WRAP));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToHexString(byte[] data) {
		final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };

		StringBuilder stringBuilder = new StringBuilder();
		if (data == null || data.length <= 0) {
			return null;
		}
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
		}
		return stringBuilder.toString();
	}

}
