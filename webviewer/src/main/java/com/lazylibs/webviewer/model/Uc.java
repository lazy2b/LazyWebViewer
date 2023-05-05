package com.lazylibs.webviewer.model;

import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.lazylibs.webviewer.cache.Cache;

@Keep
public class Uc {

    @JSONField(name = "us")
    private U us;

    @Keep
    public Uc(@NonNull U us) {
        this.us = us;
    }

    @JSONField(serialize = false, deserialize = false)
    public String v() {
        return pollUrl(T.V, us.zv());
    }

    @JSONField(serialize = false, deserialize = false)
    public String w() {
        return pollUrl(T.W, us.zw());
    }

    public enum T {
        V("v"),
        W("w"),
        ;

        T(String val) {
            this.val = val;
        }

        private final static String LAST = "last_%s";

        public String key() {
            return String.format(LAST, val);
        }

        String val;
    }

    /**
     * 根据Tag更新URL索引
     *
     * @param t    url,apiurl,ggurl,adurl,mcurl,gourl等
     * @param ints 为空即缓存+1，否则使用里面第一个元素【ints[0]】
     */
    public static void poll(T t, int... ints) {
        String key = t.key();
        if (ints == null || ints.length == 0) {
            int idx = Cache.getInt(key);
            Cache.put(key, ++idx);
        } else {
            Cache.put(key, ints[0]);
        }
    }

    /**
     * 缓存Url索引全部置为0
     */
    public static void pollAll() {
        for (T t : T.values()) {
            Cache.put(t.key(), 0);
        }
    }

    /**
     * 获取当前缓存索引的URL
     *
     * @param t  url,apiurl,ggurl,adurl,mcurl,gourl等
     * @param ou 原始URL
     * @return 索引的URL
     */
    private static String pollUrl(T t, String ou) {
        poll(t);
        if (TextUtils.isEmpty(ou)) return "";
        String url = ou;
        if (ou.contains(",")) {
            String[] urls = ou.split(",");
            String key = t.key();
            int lastIdx = Cache.getInt(key);
            if (lastIdx >= urls.length) {
                Cache.put(key, lastIdx = 0);
            }
            url = urls[lastIdx];
        }
        return TextUtils.isEmpty(url) ? "" : url;
    }
}
