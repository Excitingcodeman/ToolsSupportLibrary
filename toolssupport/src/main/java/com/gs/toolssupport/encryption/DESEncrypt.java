package com.gs.toolssupport.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.gs.toolssupport.encryption.EncryptionConstant.ECB_MODE_flag;
import static com.gs.toolssupport.encryption.EncryptionConstant.ENCRYPTION_MANNER;

/**
 * @author husky
 * create on 2019-05-16-15:50
 */
public class DESEncrypt {
    /**
     *3DES加密
     *
     * @param data 加密的数据
     * @param key  加密的key
     * @return
     */
    public static String encrypt3DESSpecialBase64(String data, String key) {
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
            byte[] dataBytes = data.getBytes(EncryptionConstant.ENCODING);
            byte[] keyIv = {1, 2, 3, 4, 5, 6, 7, 8};
            return new BASE64Encoder().encode(des3Encode(dataBytes, keyBytes, keyIv, ECB_MODE_flag));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密
     *
     * @param data 解密的数据
     * @param key  解密的key
     * @return
     */
    public static String decrypy3DESSpecialBase64(String data, String key) {
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
            byte[] dataBytes = new BASE64Decoder().decodeBuffer(data);
            byte[] keyIv = {1, 2, 3, 4, 5, 6, 7, 8};
            return new String(des3Decode(dataBytes, keyBytes, keyIv, ECB_MODE_flag));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES加密数据
     *
     * @param data  需要加密的数据
     * @param key   加密需要的key
     * @param keyIv 加密的偏移量
     * @param type  加密的类型
     * @return 加密后的字节数组
     */
    public static byte[] des3Encode(byte[] data, byte[] key, byte[] keyIv, String type) {
        Key deskey = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_MANNER);
            deskey = secretKeyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(type);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(keyIv);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ivParameterSpec);
            return cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密数据
     *
     * @param data  需要解密的数据
     * @param key   解密用的key
     * @param keyIv 解密的偏移量
     * @param type  解密的类型
     * @return 解密后的字节数组
     */
    public static byte[] des3Decode(byte[] data, byte[] key, byte[] keyIv, String type) {
        Key deskey = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_MANNER);
            deskey = secretKeyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(type);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(keyIv);
            cipher.init(Cipher.DECRYPT_MODE, deskey, ivParameterSpec);
            return cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
