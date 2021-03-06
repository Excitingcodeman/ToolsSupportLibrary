package com.gs.toolssupport.storage;

import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.gs.toolssupport.keystore.KeyStoreHelper;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author husky
 * create on 2019-05-16-16:53
 */
public class SpWithKeyStoreUtils {

    private SpWithKeyStoreUtils() {
        if (!KeyStoreHelper.isHaveKeyStore()) {
            try {
                KeyStoreHelper.createKeys();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Holder {
        private static SpWithKeyStoreUtils instance = new SpWithKeyStoreUtils();
    }

    public static SpWithKeyStoreUtils getInstance() {
        return Holder.instance;
    }

    /**
     * 加密存储
     *
     * @param key   key
     * @param value value
     */
    public void putString(@NonNull String key, @NonNull String value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String encryptValue = KeyStoreHelper.encryptString(value);
            SharedPreferencesUtil.getInstance().putString(key, encryptValue);
        } else {
            SharedPreferencesUtil.getInstance().putString(key, value);
        }
    }

    /**
     * 解密数据
     *
     * @param key key
     * @return
     */
    public String getString(@NonNull String key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String value = SharedPreferencesUtil.getInstance().getString(key, "");
            if (TextUtils.isEmpty(value)) {
                return "";
            }
            return KeyStoreHelper.decryptString(value);
        } else {
            return SharedPreferencesUtil.getInstance().getString(key, "");
        }
    }

    public String getString(@NonNull String key, String defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String value = SharedPreferencesUtil.getInstance().getString(key, "");
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            }
            String decryptString = KeyStoreHelper.decryptString(value);
            return TextUtils.isEmpty(decryptString) ? defaultValue : decryptString;
        } else {
            return SharedPreferencesUtil.getInstance().getString(key, defaultValue);
        }
    }


    public void putInt(@NonNull String key, @NonNull int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String encryptValue = KeyStoreHelper.encryptString(String.valueOf(value));
            SharedPreferencesUtil.getInstance().putString(key, encryptValue);
        } else {
            SharedPreferencesUtil.getInstance().putInt(key, value);
        }
    }

    public int getInt(@NonNull String key, int defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String value = SharedPreferencesUtil.getInstance().getString(key, "");
            String decryptString = KeyStoreHelper.decryptString(value);
            if (TextUtils.isEmpty(decryptString)) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(decryptString);
            } catch (Exception e) {
                return defaultValue;
            }

        } else {
            return SharedPreferencesUtil.getInstance().getInt(key, defaultValue);
        }
    }


    public void putBoolean(@NonNull String key, @NonNull boolean value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String encryptValue = KeyStoreHelper.encryptString(String.valueOf(value));
            SharedPreferencesUtil.getInstance().putString(key, encryptValue);
        } else {
            SharedPreferencesUtil.getInstance().putBoolean(key, value);
        }
    }


    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String value = SharedPreferencesUtil.getInstance().getString(key, "");
            String decryptString = KeyStoreHelper.decryptString(value);
            if (TextUtils.isEmpty(decryptString)) {
                return defaultValue;
            }
            try {
                return Boolean.parseBoolean(decryptString);
            } catch (Exception e) {
                return defaultValue;
            }

        } else {
            return SharedPreferencesUtil.getInstance().getBoolean(key, defaultValue);
        }
    }


    public void putFloat(@NonNull String key, @NonNull float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String encryptValue = KeyStoreHelper.encryptString(String.valueOf(value));
            SharedPreferencesUtil.getInstance().putString(key, encryptValue);
        } else {
            SharedPreferencesUtil.getInstance().putFloat(key, value);
        }
    }


    public float getFloat(@NonNull String key, float defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String value = SharedPreferencesUtil.getInstance().getString(key, "");
            String decryptString = KeyStoreHelper.decryptString(value);
            try {
                return Float.parseFloat(decryptString);
            } catch (Exception e) {
                return defaultValue;
            }

        } else {
            return SharedPreferencesUtil.getInstance().getFloat(key, defaultValue);
        }
    }

    public void putLong(@NonNull String key, @NonNull long value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String encryptValue = KeyStoreHelper.encryptString(String.valueOf(value));
            SharedPreferencesUtil.getInstance().putString(key, encryptValue);
        } else {
            SharedPreferencesUtil.getInstance().putLong(key, value);
        }
    }


    public long getFloat(@NonNull String key, long defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            String value = SharedPreferencesUtil.getInstance().getString(key, "");
            String decryptString = KeyStoreHelper.decryptString(value);
            try {
                return Long.parseLong(decryptString);
            } catch (Exception e) {
                return defaultValue;
            }

        } else {
            return SharedPreferencesUtil.getInstance().getLong(key, defaultValue);
        }
    }

    public void remove(@NonNull String key) {
        SharedPreferencesUtil.getInstance().remove(key);
    }
}
