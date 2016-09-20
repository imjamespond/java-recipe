package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.crypto.Cipher;

public class CertTest {
	
	public static final String ALGORITHM = "RSA";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CertTest ct = new CertTest();
		try {
			ct.readCertificate(new File("./crypt/cert.pem"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// import java.security.cert.*;
	public void readCertificate(File f) throws Exception {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		// Use BufferedInputStream (which supports mark and reset) so that each
		// generateCertificate call consumes one certificate.
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		while (in.available() > 0) {
			// Certificate cert = cf.generateCertificate(in);
			// System.out.println("Cert:\n===================\n" +
			// cert.toString() + "\n");

			try {
				X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
				System.out.println("Read in the following certificate:");
				System.out.println("\tCertificate for: " + cert.getSubjectDN());
				System.out.println("\tCertificate issued by: "
						+ cert.getIssuerDN());
				System.out.println("\tThe certificate is valid from "
						+ cert.getNotBefore() + " to " + cert.getNotAfter());
				System.out.println("\tCertificate SN# "
						+ cert.getSerialNumber());
				System.out.println("\tGenerated with " + cert.getSigAlgName());
				
				String encrpytStr = new String(CertTest.encrypt("hello guys", cert.getPublicKey()));
				System.out.println("encrpytStr:"+encrpytStr);
				
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		in.close();
	}

	public void readCertificates(File f) throws Exception {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		InputStream in = new FileInputStream(f);
		Collection<? extends Certificate> certs = cf.generateCertificates(in);
		in.close();

		for (Certificate cert : certs) {
			System.out.println("Cert:\n===================\n" + cert.toString()
					+ "\n");
		}
	}

	/**
     * Encrypt the plain text using public key.
     *
     * @param text
     *            : original plain text
     * @param key
     *            :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
 
    /**
     * Decrypt text using private key.
     *
     * @param text
     *            :encrypted text
     * @param key
     *            :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
 
            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
        return new String(dectyptedText);
    }
}
