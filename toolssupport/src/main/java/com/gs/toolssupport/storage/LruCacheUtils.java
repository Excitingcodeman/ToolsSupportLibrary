package com.gs.toolssupport.storage;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * @author husky
 * create on 2019-05-16-16:06
 */
public class LruCacheUtils<T> {

    private LruCache<String, CacheItem<T>> mLruCache;

    private LruCacheUtils() {
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置缓存的大小
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<>(cacheSize);
    }

    private static class Holder {
        private static LruCacheUtils instance = new LruCacheUtils();
    }

    public static LruCacheUtils getInstance() {
        return Holder.instance;
    }

    public void put(String key, T value, long expiredTime) {
        if (null == value || TextUtils.isEmpty(key)) {
            return;
        }
        if (expiredTime <= 0) {
            expiredTime = StorageConfig.DEFAULT_DURING;
        }
        long time = System.currentTimeMillis();
        mLruCache.put(key, new CacheItem<T>(value, (time + expiredTime)));
    }

    public void put(String key, T value) {
        put(key, value, StorageConfig.DEFAULT_DURING);
    }

    public T get(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        CacheItem<T> tCacheItem = mLruCache.get(key);
        if (null != tCacheItem) {
            if (tCacheItem.deleteTime - System.currentTimeMillis() > 0) {
                return tCacheItem.value;
            } else {
                mLruCache.remove(key);
            }
        }
        return null;
    }

    public void remove(String key) {
        if (!TextUtils.isEmpty(key)) {
            mLruCache.remove(key);
        }
    }

}
