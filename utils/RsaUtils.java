package com.metasoft.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

public class RsaUtils {
	/**
	 * RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * RSA
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 
	 * @param data
	 *           
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, BigInteger modulus, BigInteger publicExponent) throws Exception {
		RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey pKey = keyFactory.generatePublic(rsaPublicKeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pKey);

		int inputLen = data.length;System.out.println("inputLen:"+inputLen);
		if(MAX_ENCRYPT_BLOCK < inputLen){
			return null;
		}
		int offSet = 0;
		byte[] encryptedData;
		encryptedData = cipher.doFinal(data, offSet, inputLen - offSet);
		return encryptedData;
	}

	/**
	 * 
	 * @param encryptedData
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData,  BigInteger modulus, BigInteger privateExponent) throws Exception {
		RSAPrivateKeySpec rsaPrivateKey = new RSAPrivateKeySpec(modulus, privateExponent);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(rsaPrivateKey);
        
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;System.out.println("inputLen:"+inputLen);
		if(MAX_DECRYPT_BLOCK < inputLen){
			return null;
		}
        int offSet = 0;
        byte[] cache;
        cache = cipher.doFinal(encryptedData, offSet, inputLen);
        return cache;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	static BigInteger MODULUS;
	static BigInteger PUBLIC_EXPONENT;
	static BigInteger PRIVATE_EXPONENT;
	static{
		MODULUS = new BigInteger("00cf70c160647e1e7812372fafca5dee5e91a1801aea08884e2dd48e634039f1b21dac05667485d07e3622267b2783dec80776abcd4004c1b6053c7362edc6625d2ce1e90f9adb55c0e845b1728d3ca0f1bf859546f7680d50a405b6a491188b57c796a0800b2c5f213e77a7632349247c98e0da304291756a960184a2f2d9071b", 16);
		PUBLIC_EXPONENT = new BigInteger("10001", 16);
		PRIVATE_EXPONENT = new BigInteger("428c98ed34b025543dfbc867ac1380628473fc662608c0b9dc0821fa0363d37f61f78aeff897e970642b868ee1f9736256caa289416d853bc0e848d84711eb0206f1ec635c24d2e1dfcc027ee2953f510d3ec821f0620ad56c3bec560592b1d58170eef9c7be45177db1d6807697261368f73368e99f088fe8418b2e70e75929", 16);
	}
	
	//pkcs8 methods
	private static final String PRIVATE_KEY_FILE = "/pkcs8_priv.pem";
	private static final String PUBLIC_KEY_FILE = "/public.key";
	public static PrivateKey privKey;
	public static String gBase64PublicKey;
	
	static{
		try{
	    	File privateKeyFile = new File(RsaUtils.class.getResource(PRIVATE_KEY_FILE).toURI());
	        BufferedReader privateKey = new BufferedReader(new FileReader( privateKeyFile));
	        String strPrivKey = "";
	        String line = "";
	        while((line = privateKey.readLine()) != null){
	        	strPrivKey += line;
	        }
	        privateKey.close();
	        // 私钥需要使用pkcs8格式的，公钥使用x509格式的
	        strPrivKey = strPrivKey.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");  
            Decoder Base64Decoder = Base64.getDecoder();
			byte [] privKeyByte = Base64Decoder.decode(strPrivKey);
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyByte);
			KeyFactory kf = KeyFactory.getInstance(RsaUtils.KEY_ALGORITHM);
			privKey = kf.generatePrivate(privKeySpec);
			
			File publicKeyFile = new File(RsaUtils.class.getResource(PUBLIC_KEY_FILE).toURI());
            BufferedReader publicKey = new BufferedReader(new FileReader( publicKeyFile));    
            gBase64PublicKey = "";
            while((line = publicKey.readLine()) != null){
            	gBase64PublicKey += line;
            }
            gBase64PublicKey = gBase64PublicKey.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
			publicKey.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (InvalidKeySpecException e) {
	        e.printStackTrace();
	    } catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	 
    public static synchronized byte[] encryptWithX509(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
 
    public static synchronized String decryptWithPkcs8(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
 
            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
        return new String(dectyptedText);
    }
	
	public static void main(String[] args) throws Exception {
		String source = "da39a3ee5e6b4b0d3255bfef95601890afd80709";//"01234567890abcdefghijklmnopqrstuvwxyz";
		System.out.println("source:\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = encryptByPublicKey(data, MODULUS, PUBLIC_EXPONENT);
		System.out.printf("encodedData:%s\r\n",bytesToString(encodedData));
		
		//byte[] encryptedData = DatatypeConverter.parseHexBinary("81b429d0e074dd6fd875ee7c20d1f16a9882248ce15859a693abdb49a7cf2dced6aef5c1d5acb839ead6ac518ad9fb3a3df4b80ebd94c7292cf8781d6927a85842c6929723605ba30288959bbd9d398cd29f9ea06ebb20190fff54cedbe6abc41b1089a00739ded6aac611009db31c0c3a604cc426820fb84b0e867b502f0afe");

		byte[] decodedData = decryptByPrivateKey(encodedData, MODULUS, PRIVATE_EXPONENT);
		String target = new String(decodedData);
		System.out.println("decodedData: \r\n" + target);
		
		
		//test openssl
        try {
        	File privateKeyFile = new File(source.getClass().getResource(PRIVATE_KEY_FILE).toURI());
        	File publicKeyFile = new File(source.getClass().getResource(PUBLIC_KEY_FILE).toURI());
            BufferedReader privateKey = new BufferedReader(new FileReader( privateKeyFile));
            BufferedReader publicKey = new BufferedReader(new FileReader( publicKeyFile));    
            String strPrivateKey = "";
            String strPublicKey = "";
            String line = "";
            while((line = privateKey.readLine()) != null){
                strPrivateKey += line;
            }
            while((line = publicKey.readLine()) != null){
                strPublicKey += line;
            }
            privateKey.close();
            publicKey.close();
 
            // 私钥需要使用pkcs8格式的，公钥使用x509格式的
            String strPrivKey = strPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");                
            String strPubKey = strPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            //System.out.print(strPrivKey);
            //System.out.println(strPubKey);    
 
            Decoder Base64Decoder = Base64.getDecoder();
            byte [] privKeyByte = Base64Decoder.decode(strPrivKey);
            byte [] pubKeyByte = Base64Decoder.decode(strPubKey);
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyByte);
            //PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(pubKeyByte);
 
            //X509EncodedKeySpec     privKeySpec = new X509EncodedKeySpec(privKeyByte);
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyByte);
 
            KeyFactory kf = KeyFactory.getInstance(RsaUtils.KEY_ALGORITHM);
 
            PrivateKey privKey = kf.generatePrivate(privKeySpec);
            PublicKey pubKey = kf.generatePublic(pubKeySpec);
 
            byte [] encryptByte = encryptWithX509(source, pubKey);
            System.out.printf("encryptWithX509:%s\r\n",bytesToString(encryptByte));
            System.out.println(decryptWithPkcs8(encryptByte, privKey));
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
	}
}
