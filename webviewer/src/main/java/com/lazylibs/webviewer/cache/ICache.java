package com.lazylibs.webviewer.cache;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ICache {

    public static ICache defaults() {
        return create("defaults");
    }

    public static ICache settings() {
        return create("settings");
    }

    public static void onCreate(Application application) {
        MMKV.initialize(application);
    }

    public static void onTerminate(Application application) {
        if (MAPS != null) {
            for (ICache iCache : MAPS.values()) {
                if (iCache != null) {
                    iCache.onTerminate();
                }
            }
            MAPS.clear();
            MAPS = null;
        }
        MMKV.onExit();
    }

    private static Map<String, ICache> MAPS;

    public static ICache create(@NonNull String id) {
        if (MAPS == null) {
            MAPS = new HashMap<>();
        }
        id = toId(id);
        if (!MAPS.containsKey(id)) {
            MAPS.put(id, new ICache(id));
        }
        return MAPS.get(id);
    }

//    public static String toUUID(String phone) {
//        String clientId;
//        if (!TextUtils.isEmpty(phone)) {
//            clientId = defaults().getString(phone);
//            if (!TextUtils.isEmpty(clientId)) {
//                return clientId;
//            } else {
//                clientId = UUID.nameUUIDFromBytes(phone.getBytes()).toString();
//                defaults().put(phone, clientId);
//            }
//        } else {
//            clientId = UUID.randomUUID().toString();
//        }
//        return clientId;
//    }

    private static String toId(String id) {
        return String.format("ht.mmkv.%s", id);
    }

    private MMKV mmkv;

    private ICache(String id) {
        mmkv = MMKV.mmkvWithID(id, MMKV.MULTI_PROCESS_MODE);
    }

    public String get(String key) {
        return getString(key, "");
    }

    public MMKV core() {
        return mmkv;
    }

    /**
     * 写入 String
     *
     * @param key   键
     * @param value 值
     */
    public void put(@NonNull final String key, @NonNull final String value) {
        put(key, value, false);
    }

    /**
     * SP 中写入 String
     *
     * @param key      键
     * @param value    值
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key,
                    @NonNull final String value,
                    final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().putString(key, value).commit();
        } else {
            mmkv.edit().putString(key, value).apply();
        }
    }

    /**
     * SP 中读取 String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code ""}
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    /**
     * SP 中读取 String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(@NonNull final String key, @NonNull final String defaultValue) {
        return mmkv.getString(key, defaultValue);
    }

    /**
     * SP 中写入 int
     *
     * @param key   键
     * @param value 值
     */
    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }

    /**
     * SP 中写入 int
     *
     * @param key      键
     * @param value    值
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().putInt(key, value).commit();
        } else {
            mmkv.edit().putInt(key, value).apply();
        }
    }

    /**
     * SP 中读取 int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    /**
     * SP 中读取 int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(@NonNull final String key, final int defaultValue) {
        return mmkv.getInt(key, defaultValue);
    }

    /**
     * SP 中写入 long
     *
     * @param key   键
     * @param value 值
     */
    public void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }

    /**
     * SP 中写入 long
     *
     * @param key      键
     * @param value    值
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().putLong(key, value).commit();
        } else {
            mmkv.edit().putLong(key, value).apply();
        }
    }

    /**
     * SP 中读取 long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    /**
     * SP 中读取 long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(@NonNull final String key, final long defaultValue) {
        return mmkv.getLong(key, defaultValue);
    }

    /**
     * SP 中写入 float
     *
     * @param key   键
     * @param value 值
     */
    public void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }

    /**
     * SP 中写入 float
     *
     * @param key      键
     * @param value    值
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().putFloat(key, value).apply();
        } else {
            mmkv.edit().putFloat(key, value).apply();
        }
    }

    /**
     * SP 中读取 float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP 中读取 float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(@NonNull final String key, final float defaultValue) {
        return mmkv.getFloat(key, defaultValue);
    }

    /**
     * SP 中写入 boolean
     *
     * @param key   键
     * @param value 值
     */
    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }

    /**
     * SP 中写入 boolean
     *
     * @param key      键
     * @param value    值
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().putBoolean(key, value).commit();
        } else {
            mmkv.edit().putBoolean(key, value).apply();
        }
    }

    /**
     * SP 中读取 boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    /**
     * SP 中读取 boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return mmkv.getBoolean(key, defaultValue);
    }

    /**
     * SP 中写入 String 集合
     *
     * @param key    键
     * @param values 值
     */
    public void put(@NonNull final String key, @NonNull final Set<String> values) {
        put(key, values, false);
    }

    /**
     * SP 中写入 String 集合
     *
     * @param key      键
     * @param values   值
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void put(@NonNull final String key,
                    @NonNull final Set<String> values,
                    final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().putStringSet(key, values).commit();
        } else {
            mmkv.edit().putStringSet(key, values).apply();
        }
    }

    /**
     * SP 中读取 StringSet
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code Collections.<String>emptySet()}
     */
    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * SP 中读取 StringSet
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public Set<String> getStringSet(@NonNull final String key,
                                    @NonNull final Set<String> defaultValue) {
        return mmkv.getStringSet(key, defaultValue);
    }

    /**
     * SP 中获取所有键值对
     *
     * @return Map 对象
     */
    public Map<String, ?> getAll() {
        return mmkv.getAll();
    }

    /**
     * SP 中是否存在该 key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(@NonNull final String key) {
        return mmkv.contains(key);
    }

    /**
     * SP 中移除该 key
     *
     * @param key      键
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().remove(key).commit();
        } else {
            mmkv.edit().remove(key).apply();
        }
    }

    /**
     * SP 中移除该 key
     *
     * @param key 键
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }

    /**
     * SP 中清除所有数据
     *
     * @param isCommit {@code true}: {@link SharedPreferences.Editor#commit()}<br>
     *                 {@code false}: {@link SharedPreferences.Editor#apply()}
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            mmkv.edit().clear().commit();
        } else {
            mmkv.edit().clear().apply();
        }
    }

    /**
     * SP 中清除所有数据
     */
    public void clear() {
        clear(false);
    }

    private void onTerminate() {
        if (mmkv != null) {
            mmkv.sync();
        }
        mmkv = null;
    }

    public static void clearAllMMKVCache() {
        String root = MMKV.getRootDir();
        if (!TextUtils.isEmpty(root)) {
            File rootFile = new File(root);
            if (rootFile.exists()) {
                rootFile.delete();
            }
        }
    }
}
