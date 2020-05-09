package com.fleet.ehcache.util;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

public class EHCacheUtil {

    /**
     * 缓存管理器
     */
    private static CacheManager cacheManager = null;

    /**
     * 缓存域
     */
    public static Cache<String, Object> cache = null;

    static {
        URL url = EHCacheUtil.class.getResource("/ehcache.xml");
        Configuration xmlConfig = new XmlConfiguration(url);
        cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
    }

    /**
     * 获取缓存域
     *
     * @param alias     缓存域名称
     * @param keyType   键类型
     * @param valueType 值类型
     * @return
     */
    public static <K, V> Cache<K, V> getCache(String alias, Class<K> keyType, Class<V> valueType) {
        return cacheManager.getCache(alias, keyType, valueType);
    }

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static Object get(String key) {
        return cache.get(key);
    }

    public static Boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public static void remove(String key) {
        cache.remove(key);
    }
}
