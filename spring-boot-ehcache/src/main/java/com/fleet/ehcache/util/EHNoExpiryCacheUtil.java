package com.fleet.ehcache.util;

public class EHNoExpiryCacheUtil extends EHCacheUtil {

    static {
        cache = getCache("noExpiryCache", String.class, Object.class);
    }
}
