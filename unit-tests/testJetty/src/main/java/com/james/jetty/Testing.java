package com.james.jetty;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Md5PasswordEncoder md5=new Md5PasswordEncoder();

        /*
        * "a"   密码
        * "abc"    是key
        * 9af7268244164521c43624a92ea963ac  加密后的字节串
        */
        //String md5Password=md5.encodePassword("a", "abc");

        String md5Password=md5.encodePassword("1212", "");
        System.out.println("root: "+md5Password);
	}

}
