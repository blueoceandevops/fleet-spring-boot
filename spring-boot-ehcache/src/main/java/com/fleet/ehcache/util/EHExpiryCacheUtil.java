package com.fleet.ehcache.util;

public class EHExpiryCacheUtil extends EHCacheUtil {

    static {
        cache = getCache("expiryCache", String.class, Object.class);
    }
}
