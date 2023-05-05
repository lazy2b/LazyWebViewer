package com.lazylibs.webviewer.utils;


import java.nio.charset.StandardCharsets;

/**
 * 加解密工具类。【为防止反编译后很容易判断出加解密算法，请勿改名！！！！】
 * 加密->xCode;
 * 解密->getRaw;
 */
public final class Xc {

    private static final int DELTA = 0x9E3779B9;

    private static int MX(int sum, int y, int z, int p, int e, int[] k) {
        return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
    }

    private Xc() {
    }

    public static String akCode(String d, String k) {
        return xCode2B64Str(d, k(k));
    }

    public static String akRaw(String d, String k) {
        return getRaw(d, k(k));
    }

    private static byte[] xCode(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(xCode(toIntArray(data, true), toIntArray(fixKey(key), false)), false);
    }

    private static byte[] xCode(String data, byte[] key) {
        return xCode(data.getBytes(StandardCharsets.UTF_8), key);
    }

    private static byte[] xCode(byte[] data, String key) {
        return xCode(data, key.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] xCode(String data, String key) {
        return xCode(data.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
    }

    private static String xCode2B64Str(byte[] data, byte[] key) {
        byte[] bytes = xCode(data, key);
        if (bytes == null) return null;
        return eb64(bytes);
    }

    private static String xCode2B64Str(String data, byte[] key) {
        byte[] bytes = xCode(data, key);
        if (bytes == null) return null;
        return eb64(bytes);
    }

    private static String xCode2B64Str(byte[] data, String key) {
        byte[] bytes = xCode(data, key);
        if (bytes == null) return null;
        return eb64(bytes);
    }

    private static String xCode2B64Str(String data, String key) {
        byte[] bytes = xCode(data, key);
        if (bytes == null) return null;
        return eb64(bytes);
    }

    private static byte[] getRaw(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(getRaw(toIntArray(data, false), toIntArray(fixKey(key), false)), true);
    }

    private static byte[] getRaw(byte[] data, String key) {
        return getRaw(data, key.getBytes(StandardCharsets.UTF_8));
    }

    // 进行base64编码
    static String eb64(byte[] bytes) {
        return Xb.encodeToString(bytes, Xb.DEFAULT);
    }

    // 进行base64解码
    static byte[] db64(String data) {
        return Xb.decode(data, Xb.DEFAULT);
    }

    private static byte[] getB64StrRaw(String data, byte[] key) {
        return getRaw(db64(data), key);
    }

    private static byte[] getB64StrRaw(String data, String key) {
        return getRaw(db64(data), key);
    }

    private static String getRaw2Str(byte[] data, byte[] key) {
        byte[] bytes = getRaw(data, key);
        if (bytes == null) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String getRaw2Str(byte[] data, String key) {
        byte[] bytes = getRaw(data, key);
        if (bytes == null) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String getRaw(String data, byte[] key) {
        byte[] bytes = getRaw(db64(data), key);
        if (bytes == null) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String getRaw(String data, String key) {
        //////// 解密方法。
        byte[] bytes = getRaw(db64(data), key);
        if (bytes == null) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static int[] xCode(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        int p, q = 6 + 52 / (n + 1);
        int z = v[n], y, sum = 0, e;

        while (q-- > 0) {
            sum = sum + DELTA;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v[p + 1];
                z = v[p] += MX(sum, y, z, p, e, k);
            }
            y = v[0];
            z = v[n] += MX(sum, y, z, p, e, k);
        }
        return v;
    }

    private static int[] getRaw(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        int p, q = 6 + 52 / (n + 1);
        int z, y = v[0], sum = q * DELTA, e;

        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= MX(sum, y, z, p, e, k);
            }
            z = v[n];
            y = v[0] -= MX(sum, y, z, p, e, k);
            sum = sum - DELTA;
        }
        return v;
    }

    private static byte[] fixKey(byte[] key) {
        if (key.length == 16) return key;
        byte[] fixedkey = new byte[16];
        System.arraycopy(key, 0, fixedkey, 0, Math.min(key.length, 16));
        return fixedkey;
    }

    private static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
        int[] result;

        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for (int i = 0; i < n; ++i) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    private static byte[] toByteArray(int[] data, boolean includeLength) {
        int n = data.length << 2;

        if (includeLength) {
            int m = data[data.length - 1];
            n -= 4;
            if ((m < n - 3) || (m > n)) {
                return null;
            }
            n = m;
        }
        byte[] result = new byte[n];

        for (int i = 0; i < n; ++i) {
            result[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3));
        }
        return result;
    }

    // 获取密钥
    private static String k(String ok) {
        // 这里可能存在多线程访问
        //noinspection StringBufferMayBeStringBuilder
        StringBuffer sb = new StringBuffer();
        char[] temps = ok.toCharArray();
        for (int i = 0; i < temps.length; i++) {
            if (p(i)) {
                sb.append(temps[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否是素数
     *
     * @param src 数字
     * @return 是否是素数
     */
    private static boolean p(int src) {
        double sqrt = Math.sqrt(src);
        if (src < 2) {
            return false;
        }
        if (src == 2 || src == 3) {
            return true;
        }
        if (src % 2 == 0) {// 先判断是否为偶数，若偶数就直接结束程序
            return false;
        }
        for (int i = 3; i <= sqrt; i += 2) {
            if (src % i == 0) {
                return false;
            }
        }
        return true;
    }

}
