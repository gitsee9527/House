package com.mooc.house.common.utils;

import java.nio.charset.Charset;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

//密码加盐，防止明文出现 
public class HashUtils {
	
	private static final HashFunction FUNCTION = Hashing.md5();
	//防止密码被暴力破解，将用户密码和一个字符串同时进行md5
	private static final String SALT = "mooc.com";
	
	public static String encyPassword(String password) {
		HashCode hashCode = FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
		return hashCode.toString();
	}
}
