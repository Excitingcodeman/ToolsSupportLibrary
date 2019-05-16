package com.gs.toolssupport.storage;

/**
 * @author husky
 * create on 2019-05-16-16:04
 */
public class CacheItem<V> {
    public V value;
    /**
     * 过期时间
     */
    public long deleteTime;

    public CacheItem(V value, long deleteTime) {
        this.value = value;
        this.deleteTime = deleteTime;
    }
}

