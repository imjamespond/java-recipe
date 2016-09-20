package test;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

public class RsaTest2 {
	/**
	 * RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";
	/**
	 * 
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";
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
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
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
	static BigInteger PUB_EXPONENT;
	static BigInteger PRIVATE_EXPONENT;
	static{
		MODULUS = new BigInteger("00cf70c160647e1e7812372fafca5dee5e91a1801aea08884e2dd48e634039f1b21dac05667485d07e3622267b2783dec80776abcd4004c1b6053c7362edc6625d2ce1e90f9adb55c0e845b1728d3ca0f1bf859546f7680d50a405b6a491188b57c796a0800b2c5f213e77a7632349247c98e0da304291756a960184a2f2d9071b", 16);
		PUB_EXPONENT = new BigInteger("10001", 16);
		PRIVATE_EXPONENT = new BigInteger("428c98ed34b025543dfbc867ac1380628473fc662608c0b9dc0821fa0363d37f61f78aeff897e970642b868ee1f9736256caa289416d853bc0e848d84711eb0206f1ec635c24d2e1dfcc027ee2953f510d3ec821f0620ad56c3bec560592b1d58170eef9c7be45177db1d6807697261368f73368e99f088fe8418b2e70e75929", 16);
	}
	public static void main(String[] args) throws Exception {
		String source = "01234567890abcdefghijklmnopqrstuvwxyz";
		System.out.println("source:\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = encryptByPublicKey(data, MODULUS, PUB_EXPONENT);
		System.out.printf("encodedData:%s\r\n",bytesToString(encodedData));
		
		byte[] encryptedData = DatatypeConverter.parseHexBinary("81b429d0e074dd6fd875ee7c20d1f16a9882248ce15859a693abdb49a7cf2dced6aef5c1d5acb839ead6ac518ad9fb3a3df4b80ebd94c7292cf8781d6927a85842c6929723605ba30288959bbd9d398cd29f9ea06ebb20190fff54cedbe6abc41b1089a00739ded6aac611009db31c0c3a604cc426820fb84b0e867b502f0afe");

		byte[] decodedData = decryptByPrivateKey(encryptedData, MODULUS, PRIVATE_EXPONENT);
		String target = new String(decodedData);
		System.out.println("decodedData: \r\n" + target);
	}
}
