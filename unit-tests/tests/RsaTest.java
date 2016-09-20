package test;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RsaTest {
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
	public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);

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
	public static byte[] decryptByPrivateKey(byte[] encryptedData, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
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

	/**
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Key> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Key> keyMap = new HashMap<String, Key>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
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

	static byte[] publicKey;
	static byte[] privateKey;

	public static void main(String[] args) throws Exception {
		try {
			Map<String, Key> keyMap = genKeyPair();
			publicKey = keyMap.get(PUBLIC_KEY).getEncoded();
			privateKey = keyMap.get(PRIVATE_KEY).getEncoded();

			System.err.println("publicKey: \n\r" + bytesToString(publicKey));
			System.err.println("privateKey: \n\r" + bytesToString(privateKey));
		} catch (Exception e) {
			e.printStackTrace();
		}


		String source = "01234567890abcdefghijklmnopqrstuvwxyz";
		System.out.println("source:\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = encryptByPublicKey(data, publicKey);
		System.out.println("encodedData:\r\n" + new String(encodedData));
		byte[] decodedData = decryptByPrivateKey(encodedData, privateKey);
		String target = new String(decodedData);
		System.out.println("decodedData: \r\n" + target);
	}
}
