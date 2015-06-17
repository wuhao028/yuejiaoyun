package com.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5EncryptUtil {

	public MD5EncryptUtil() {
		// TODO Auto-generated constructor stub
	}
	public static String getMD5(String content) {
		  try {
		   MessageDigest digest = MessageDigest.getInstance("MD5");
		   digest.update(content.getBytes());
		   return getHashString(digest);
		   
		  } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
		  }
		  return null;
		 }
		 
	    private static String getHashString(MessageDigest digest) {
	        StringBuilder builder = new StringBuilder();
	        for (byte b : digest.digest()) {
	            builder.append(Integer.toHexString((b >> 4) & 0xf));
	            builder.append(Integer.toHexString(b & 0xf));
	        }
	        return builder.toString();
	    }
	
/*
	public static byte[] MD5(byte[] src) {
	    byte[] result = null;
	    try {
	         MessageDigest alg = MessageDigest.getInstance("MD5");
	         result = alg.digest(src);
	    } catch (NoSuchAlgorithmException ex) {
	    	ex.printStackTrace();
	    }
	    return result;
	}*/
	
}
