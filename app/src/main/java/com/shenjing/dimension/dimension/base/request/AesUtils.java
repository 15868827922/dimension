package com.shenjing.dimension.dimension.base.request;

import android.util.Base64;

import com.shenjing.dimension.MainActivity;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.Security;

/**
 * Created by apple_half on 15-1-20.
 *
 */
public class AesUtils {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * AES/CBC/PKCS7Padding 分别对应
     * 加密||解密算法、工作模式、填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";


    private static byte[] getKey() throws Exception {
        return MainActivity.Encryption_SecretKey.getBytes(Charset.forName("UTF-8"));
    }

    /**
     *
     * 加密数据, 返回字节数组
     *
     */
    public static byte[] encrypt1(String toEncrypt) throws Exception {
        Key secretKey = new SecretKeySpec(getKey(), KEY_ALGORITHM);

        // libs 中 bcprov 的支持, bouncycastle 支持 64 位密钥
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 获取位移，并初始化
        final byte[] ivData = MainActivity.Encryption_IV.getBytes();
        IvParameterSpec iv = new IvParameterSpec(ivData);

        // 用 iv 初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        final byte[] encryptedMessage = cipher.doFinal(toEncrypt.getBytes(Charset.forName("UTF-8")));

        final byte[] ivAndEncryptedMessage = new byte[ivData.length + encryptedMessage.length];
        System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, ivData.length);
        System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage, ivData.length, encryptedMessage.length);

        return ivAndEncryptedMessage;
    }

    /**
     *
     * 加密数据，返回 String 对象
     *
     */
    public static String encrypt2(String toEncrypt) throws Exception {
        byte[] ivAndEncryptedMessage = encrypt1(toEncrypt);
        String strResult = new String(Base64.encode(ivAndEncryptedMessage, 0), "UTF-8");
        return strResult;
    }

    /*
     *
     * 解密数据
     *
     */
    public static String decrypt(String toDecrypt) throws Exception {
        byte[] data = Base64.decode(toDecrypt, 0);

//        Key secretKey = new SecretKeySpec(getKey(), KEY_ALGORITHM);
        Key secretKey = setKey(MainActivity.Encryption_SecretKey);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        final byte[] ivData = MainActivity.Encryption_IV.getBytes();
        IvParameterSpec iv = new IvParameterSpec(ivData);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        final byte[] encryptedMessage = cipher.doFinal(data);

        final byte[] result = new byte[encryptedMessage.length - ivData.length];
        System.arraycopy(encryptedMessage, ivData.length, result, 0, result.length);
        return  new String(result,"UTF-8");
    }

    private static Key setKey(String strKey) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            DESKeySpec keySpec = new DESKeySpec(strKey.getBytes("utf-8"));
            keyFactory.generateSecret(keySpec);
            return keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}