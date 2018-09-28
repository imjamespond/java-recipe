package com.metasoft.empire.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class HashUtils {
	
	public static String sha256(String str){  	
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			
	        /*//convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        System.out.println("Hex format : " + sb.toString());*/
	        
	        //convert the byte to hex format method 2
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	//if(hex.length()==1) hexString.append('0');//?
	   	     	hexString.append(hex);
	    	}
	    	
	    	return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static String md5(String str){
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			String hex = (new HexBinaryAdapter()).marshal(md5.digest(str.getBytes()));
			return hex;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public static void main(String[] args)throws Exception
    {

    	for(Provider p : Security.getProviders()){
    		System.out.println(p.toString());
    	}
    	
    	String password = "123456";
    	
    	System.out.println("Hex format : " + sha256(password));
    	System.out.println("Hex format : " + md5(password));
    }
}
