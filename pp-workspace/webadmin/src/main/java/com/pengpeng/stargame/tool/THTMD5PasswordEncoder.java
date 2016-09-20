package com.pengpeng.stargame.tool;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * User: mql
 * Date: 13-10-8
 * Time: 下午4:34
 */
public class THTMD5PasswordEncoder {
    public static void main(String[] args) {
        Md5PasswordEncoder md5=new Md5PasswordEncoder();

        /*
        * "a"   密码
        * "abc"    是key
        * 9af7268244164521c43624a92ea963ac  加密后的字节串
        */
        //String md5Password=md5.encodePassword("a", "abc");

        String md5Password=md5.encodePassword("lanhaifeng", "");
        System.out.println("root: "+md5Password);
    }
}
