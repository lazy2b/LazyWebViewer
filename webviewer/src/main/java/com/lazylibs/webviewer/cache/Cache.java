package com.lazylibs.webviewer.cache;

public class Cache {

    public static String cacheKey(String key) {
        return String.format("_%s", key);
    }

    public static void put(String key, String str) {
        ICache.defaults().put(cacheKey(key), str);
    }

    public static String get(String key) {
        return ICache.defaults().getString(cacheKey(key), "");
    }

    public static String get(String key, String defaultValue) {
        return ICache.defaults().getString(cacheKey(key), defaultValue);
    }

    public static void remove(String key) {
        ICache.defaults().remove(cacheKey(key));
    }

    public static void put(String key, int val) {
        ICache.defaults().put(cacheKey(key), val);
    }

    public static int getInt(String key) {
        return ICache.defaults().getInt(cacheKey(key), 0);
    }
}