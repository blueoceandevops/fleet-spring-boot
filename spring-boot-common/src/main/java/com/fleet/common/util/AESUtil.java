package com.fleet.common.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES工具类
 */
public class AESUtil {

    /**
     * 密钥
     */
    public static String AES_PASSWORD = "AESKEY1234567890";

    // public static void main(String[] args) throws Exception {
    // String content = "admin";
    // String password = "12345";
    // String encrypt = encrypt(content, password);
    // System.out.println("加密后：" + encrypt);
    // String decrypt = decrypt(encrypt, password);
    // System.out.println("解密后：" + decrypt);
    // }

    /**
     * AES加密
     */
    public static String encrypt(String content) throws Exception {
        return encrypt(content, AES_PASSWORD);
    }

    /**
     * AES加密
     */
    public static String encrypt(String content, String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(password.getBytes("utf-8")));
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey sKey = keyGenerator.generateKey();
        byte[] encoded = sKey.getEncoded();
        SecretKeySpec sKeySpec = new SecretKeySpec(encoded, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
        byte[] bytes = cipher.doFinal(content.getBytes("utf-8"));
        // 将二进制转换成16进制
        return Hex.encodeHexString(bytes);
    }

    /**
     * AES解密
     */
    public static String decrypt(String content) throws Exception {
        return decrypt(content, AES_PASSWORD);
    }

    /**
     * AES解密
     */
    public static String decrypt(String content, String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(password.getBytes("utf-8")));
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey sKey = keyGenerator.generateKey();
        byte[] encoded = sKey.getEncoded();
        SecretKeySpec sKeySpec = new SecretKeySpec(encoded, "AES");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
        // 将16进制转换为二进制
        byte[] bytes = Hex.decodeHex(content);
        return new String(cipher.doFinal(bytes));
    }
}
