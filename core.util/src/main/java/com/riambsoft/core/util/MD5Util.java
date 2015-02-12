package com.riambsoft.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class MD5Util {

	/**
	 * MD5加密
	 */
	public final static String md5(String str) {
		// 用来将字节转换成16进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		byte[] btInput = str.getBytes();
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);

			// 获得密文 MD5 的计算结果是一个 128 位的长整数
			byte[] md = mdInst.digest();

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char[] s = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				s[k++] = hexDigits[b >>> 4 & 0xf];
				s[k++] = hexDigits[b & 0xf];
			}
			return new String(s);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}