package test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AesTest {

	public static byte[] desCrypto(byte[] datasource, String password) {
		try {
			KeyGenerator KeyGen=KeyGenerator.getInstance("AES");
            KeyGen.init(128);

            SecretKey SecKey=KeyGen.generateKey();

            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE,SecKey);

			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] decrypt(byte[] src, String password) throws Exception {
		KeyGenerator KeyGen=KeyGenerator.getInstance("AES");
        KeyGen.init(128);

        SecretKey SecKey=KeyGen.generateKey();

        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,SecKey);

		return cipher.doFinal(src);
	}

	public static void main(String[] args) {

		String str = "01234567890abcdefghijklmnopqrstuvwxyz";

		String password = "hehe1212";
		byte[] result = desCrypto(str.getBytes(), password);
		System.out.println("encrypt:" + new String(result));

		try {
			byte[] decryResult = decrypt(result, password);
			System.out.println("dencrypt:" + new String(decryResult));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
