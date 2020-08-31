package com.example.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {

  static public String Sha1(String str){
    MessageDigest crypt = null;
    try {
      crypt = MessageDigest.getInstance("SHA-1");

      crypt.reset();
      crypt.update(str.getBytes("UTF-8"));
      return new BigInteger(1, crypt.digest()).toString(16);
    } catch (Exception e) {
    }
    return null;
  }
}
