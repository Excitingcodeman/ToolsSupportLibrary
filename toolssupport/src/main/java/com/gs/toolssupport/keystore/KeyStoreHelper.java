package com.gs.toolssupport.keystore;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import androidx.annotation.RequiresApi;
import com.gs.toolssupport.GlobalInit;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.gs.toolssupport.keystore.SecurityConstants.SAMPLE_ALIAS;

/**
 * @author husky
 * create on 2019-05-16-15:56
 */
public class KeyStoreHelper {
    /**
     * 判断是否创建过秘钥
     *
     * @return 是否有keystore的标识
     */
    public static boolean isHaveKeyStore() {
        try {
            KeyStore ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            ks.load(null);

            // Load the key pair from the Android Key Store
            //从Android加载密钥对密钥存储库中

            if (Build.VERSION.SDK_INT >= 28) {
                Key key = ks.getKey(SAMPLE_ALIAS, null);
                if (key == null) {
                    return false;
                }
            } else {
                KeyStore.Entry entry = ks.getEntry(SAMPLE_ALIAS, null);
                if (entry == null) {
                    return false;
                }
            }

        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * 创建一个公共和私人密钥,并将其存储使用Android密钥存储库中,因此,只有
     * 这个应用程序将能够访问键。
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void createKeys() throws InvalidAlgorithmParameterException,
            NoSuchProviderException, NoSuchAlgorithmException {

        //创建一个开始和结束时间,有效范围内的密钥对才会生成。
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        //往后加一年
        end.add(Calendar.YEAR, 99);
        AlgorithmParameterSpec spec;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //使用别名来检索的key 。这是一个key 的key !
            spec = new KeyPairGeneratorSpec.Builder(GlobalInit.application)
                    //使用别名来检索的关键。这是一个关键的关键!
                    .setAlias(SAMPLE_ALIAS)
                    // 用于生成自签名证书的主题 X500Principal 接受 RFC 1779/2253的专有名词
                    .setSubject(new X500Principal("CN=" + SAMPLE_ALIAS))
                    //用于自签名证书的序列号生成的一对。
                    .setSerialNumber(BigInteger.ONE)
                    // 签名在有效日期范围内
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
        } else {
            spec = new KeyGenParameterSpec.Builder(SAMPLE_ALIAS, KeyProperties.PURPOSE_ENCRYPT |
                    KeyProperties.PURPOSE_DECRYPT)
                    .setCertificateSubject(new X500Principal("CN=" + SAMPLE_ALIAS))
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA1)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM, KeyProperties.BLOCK_MODE_CTR,
                            KeyProperties.BLOCK_MODE_CBC, KeyProperties.BLOCK_MODE_ECB)
                    .setCertificateSerialNumber(BigInteger.ONE)
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build();
        }
        KeyPairGenerator kpGenerator = KeyPairGenerator
                .getInstance(SecurityConstants.TYPE_RSA,
                        SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        kpGenerator.initialize(spec);
        kpGenerator.generateKeyPair();
    }

    /**
     * 加密数据
     *
     * @param needEncryptWord 加密的数据源
     * @return 加密后的数据
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String encryptString(String needEncryptWord) {
        if (!isHaveKeyStore()) {
            try {
                createKeys();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        String encryptStr = "";
        if (TextUtils.isEmpty(needEncryptWord)) {
            return encryptStr;
        }
        byte[] vals = null;
        try {
            //AndroidKeyStore
            KeyStore ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            // 如果你没有InputStream加载,你仍然需要
            //称之为“负载”,或者它会崩溃
            ks.load(null);
            //从Android加载密钥对密钥存储库中
            PublicKey publicKey;
            if (Build.VERSION.SDK_INT >= 28) {
                Certificate certificate = ks.getCertificate(SAMPLE_ALIAS);
                if (null == certificate) {
                    return null;
                }
                publicKey = certificate.getPublicKey();
            } else {
                KeyStore.Entry entry = ks.getEntry(SAMPLE_ALIAS, null);
                /* *
                 *进行判断处理钥匙是不是存储的当前别名下 不存在要遍历别名列表Keystore.aliases()
                 */
                if (entry == null) {
                    return null;
                }
                if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                    return null;
                }
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
                publicKey = privateKeyEntry.getCertificate().getPublicKey();
            }
            if (null == publicKey) {
                return null;
            }
            Cipher inCipher = Cipher.getInstance(SecurityConstants.RSA_PADDING);
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(needEncryptWord.getBytes(SecurityConstants.ENCODE));
            cipherOutputStream.close();
            vals = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(vals, Base64.DEFAULT);


    }

    /**
     * 解密
     *
     * @param needDecryptWord 解密数据
     * @return 解密后的数据
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String decryptString(String needDecryptWord) {
        if (!isHaveKeyStore()) {
            try {
                createKeys();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        String decryptStr = "";
        if (needDecryptWord.isEmpty()) {
            return decryptStr;
        }
        try {
            //AndroidKeyStore
            KeyStore ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
            // 如果你没有InputStream加载,你仍然需要
            //称之为“负载”,或者它会崩溃
            ks.load(null);
            //从Android加载密钥对密钥存储库中
            PrivateKey key;
            if (Build.VERSION.SDK_INT >= 28) {
                key = (PrivateKey) ks.getKey(SAMPLE_ALIAS, null);
            } else {
                KeyStore.Entry entry = ks.getEntry(SAMPLE_ALIAS, null);
                /* *
                 *进行判断处理钥匙是不是存储的当前别名下 不存在要遍历别名列表Keystore.aliases()
                 */
                if (entry == null) {
                    return null;
                }
                if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                    return null;
                }
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
                key = privateKeyEntry.getPrivateKey();
            }
            if (null == key) {
                return null;
            }
            Cipher output = Cipher.getInstance(SecurityConstants.RSA_PADDING);
            output.init(Cipher.DECRYPT_MODE, key);
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(needDecryptWord, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }
            decryptStr = new String(bytes, 0, bytes.length, SecurityConstants.ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;

    }

}
