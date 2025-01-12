package com.stock.api.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtils {
   private static final String KEY_ALGORITHM = "RSA";
   public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
   private static final String UTF_8 = "UTF-8";
   private static final String H5_PUBLIC_KEY = "add your public key here";
   public static final Provider provider = new BouncyCastleProvider();

   public static String readRSAKeyPem(String keypath) throws Exception {
      BufferedReader br = new BufferedReader(new FileReader(keypath));
      String s = br.readLine();
      String str = "";

      for(s = br.readLine(); s.charAt(0) != '-'; s = br.readLine()) {
         str = str + s + "\r";
      }

      return str;
   }

   public static PrivateKey getPrivateKeyFromPem(String privateKeyPath) throws Exception {
      byte[] b = Base64.getDecoder().decode(readRSAKeyPem(privateKeyPath));
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(b);
      return kf.generatePrivate(keySpec);
   }

   public static PublicKey getPublicKeyFromCer(String cerPath) throws Exception {
      CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
      FileInputStream fis = new FileInputStream(cerPath);
      X509Certificate Cert = (X509Certificate)certificatefactory.generateCertificate(fis);
      return Cert.getPublicKey();
   }

   public static PublicKey getPublicKeyFromPem(String publicKeyPath) throws Exception {
      byte[] b = Base64.getDecoder().decode(readRSAKeyPem(publicKeyPath));
      KeyFactory kf = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(b);
      return kf.generatePublic(keySpec);
   }

   public static String encrypt(String str, String publicKey) throws Exception {
      byte[] decoded = Base64.getDecoder().decode(publicKey);
      RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(1, pubKey);
      return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
   }

   public static String decrypt(String str, String privateKey) throws Exception {
      byte[] inputByte = Base64.getDecoder().decode(str);
      byte[] decoded = Base64.getDecoder().decode(privateKey);
      RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(2, priKey);
      return new String(cipher.doFinal(inputByte));
   }

   public static void genKeyPair() throws Exception {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
      keyPairGen.initialize(1024);
      KeyPair keyPair = keyPairGen.generateKeyPair();
      RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
      System.out.println("public key:" + new String(publicKey.getEncoded(), "UTF-8"));
      System.out.println("private key:" + new String(privateKey.getEncoded(), "UTF-8"));
   }

   public static String sign(byte[] data, String privateKey) throws Exception {
      byte[] keyBytes = Base64.getDecoder().decode(privateKey);
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
      Signature signature = Signature.getInstance("MD5withRSA");
      signature.initSign(privateK);
      signature.update(data);
      return new String(Base64.getEncoder().encode(signature.sign()));
   }

   public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
      byte[] keyBytes = Base64.getDecoder().decode(publicKey);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey publicK = keyFactory.generatePublic(keySpec);
      Signature signature = Signature.getInstance("MD5withRSA");
      signature.initVerify(publicK);
      signature.update(data);
      return signature.verify(Base64.getDecoder().decode(sign));
   }

   public static String getMyPass(String password) {
      try {
         return encrypt(password, H5_PUBLIC_KEY);
      } catch (Exception var1) {
         return "";
      }
   }

   public static void main(String[] args) throws Exception {
   }

   static {
      Security.addProvider(provider);
   }
}
